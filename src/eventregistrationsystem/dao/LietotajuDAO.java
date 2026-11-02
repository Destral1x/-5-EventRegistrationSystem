/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.dao;

import eventregistrationsystem.model.Administrators;
import eventregistrationsystem.model.Dalibnieks;
import eventregistrationsystem.model.Lietotajs;
import eventregistrationsystem.util.PasswordUtil;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Datu piekļuve tabulai LIETOTAJI: reģistrācija, ielogošanās,
 * kontaktinformācija, konta dzēšana un lietotāju saraksts administratoram.
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class LietotajuDAO {

    /**
     * Reģistrē jaunu lietotāju. Jauns lietotājs vienmēr ir DALIBNIEKS,
     * parole tiek saglabāta kā SHA-256 jaucējkods.
     *
     * @param vards vārds
     * @param uzvards uzvārds
     * @param lietotajvards lietotājvārds
     * @param epasts e-pasta adrese
     * @param talrunis tālrunis vai null
     * @param parole parole
     * @return true, ja saglabāts; false, ja tāds lietotājvārds vai e-pasts jau ir vai radās kļūda
     */
    public boolean registretLietotaju(String vards, String uzvards, String lietotajvards, String epasts,
            String talrunis, String parole) {
        String sql = "INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, epasts, talrunis, parole_hash, loma) "
                + "VALUES (?, ?, ?, ?, ?, ?, 'DALIBNIEKS')";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vards);
            ps.setString(2, uzvards);
            ps.setString(3, lietotajvards);
            ps.setString(4, epasts);
            ps.setString(5, talrunis);
            ps.setString(6, PasswordUtil.hash(parole));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Atrod lietotāju ielogošanās laikā pēc lietotājvārda vai e-pasta
     * (e-pastam lielie un mazie burti netiek šķirti).
     *
     * @param login lietotājvārds vai e-pasts
     * @return {@link Administrators} vai {@link Dalibnieks}; null, ja nav atrasts
     */
    public Lietotajs atrastLietotaju(String login) {
        String sql = "SELECT * FROM LIETOTAJI WHERE lietotajvards = ? OR LOWER(epasts) = LOWER(?)";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, login);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String vards = rs.getString("vards");
                String uzvards = rs.getString("uzvards");
                String lietotajvards = rs.getString("lietotajvards");
                String epasts = rs.getString("epasts");
                String talrunis = rs.getString("talrunis");
                String hash = rs.getString("parole_hash");

                if ("ADMIN".equals(rs.getString("loma"))) {
                    return new Administrators(id, vards, uzvards, lietotajvards, epasts, talrunis, hash);
                }
                return new Dalibnieks(id, vards, uzvards, lietotajvards, epasts, talrunis, hash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Pārbauda, vai lietotājvārds jau ir aizņemts.
     *
     * @param lietotajvards lietotājvārds
     * @return true, ja tāds lietotājs jau ir
     */
    public boolean irLietotajvards(String lietotajvards) {
        return irIeraksts("SELECT COUNT(*) FROM LIETOTAJI WHERE LOWER(lietotajvards) = LOWER(?)", lietotajvards);
    }

    /**
     * Pārbauda, vai ar šo e-pastu jau ir reģistrēts lietotājs.
     *
     * @param epasts e-pasta adrese
     * @return true, ja tāds lietotājs jau ir
     */
    public boolean irEpasts(String epasts) {
        return irIeraksts("SELECT COUNT(*) FROM LIETOTAJI WHERE LOWER(epasts) = LOWER(?)", epasts);
    }

    // izpilda COUNT vaicājumu ar vienu parametru
    private boolean irIeraksts(String sql, String vertiba) {
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vertiba);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saglabā dalībnieka tālruni (kontaktinformāciju), ja tā nebija profilā.
     *
     * @param id lietotāja identifikators
     * @param talrunis tālruņa numurs
     * @return true, ja saglabāts
     */
    public boolean saglabatTalruni(int id, String talrunis) {
        String sql = "UPDATE LIETOTAJI SET talrunis = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, talrunis);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Dzēš dalībnieka kontu. Viņa pieteikumi izdzēšas ar ON DELETE CASCADE.
     * Administratora kontu dzēst nevar.
     *
     * @param id lietotāja identifikators
     * @return true, ja konts dzēsts
     */
    public boolean dzestLietotaju(int id) {
        String sql = "DELETE FROM LIETOTAJI WHERE id = ? AND loma = 'DALIBNIEKS'";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Nolasa visus lietotājus administratora logam kopā ar viņu pieteikumu skaitu.
     *
     * @return rindas tabulai: vārds, uzvārds, lietotājvārds, e-pasts, tālrunis, loma, pieteikumu skaits, reģistrācijas datums
     * @throws SQLException datu bāzes kļūdas gadījumā
     */
    public List<Object[]> iegutVisusLietotajus() throws SQLException {
        List<Object[]> saraksts = new ArrayList<>();
        String sql = "SELECT l.*, (SELECT COUNT(*) FROM REGISTRACIJAS r WHERE r.lietotaja_id = l.id) AS pieteikumi "
                + "FROM LIETOTAJI l ORDER BY l.loma, l.uzvards, l.vards";
        SimpleDateFormat formats = new SimpleDateFormat("dd.MM.yyyy");

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String talrunis = rs.getString("talrunis");
                Timestamp izveidots = rs.getTimestamp("izveidots");
                saraksts.add(new Object[]{
                    rs.getString("vards"),
                    rs.getString("uzvards"),
                    rs.getString("lietotajvards"),
                    rs.getString("epasts"),
                    talrunis == null ? "" : talrunis,
                    "ADMIN".equals(rs.getString("loma")) ? "Administrators" : "Dalībnieks",
                    rs.getInt("pieteikumi"),
                    izveidots == null ? "" : formats.format(izveidots)});
            }
        }
        return saraksts;
    }
}
