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

/**
 * Datu piekļuve tabulai LIETOTAJI: reģistrācija, ielogošanās,
 * kontaktinformācija un konta dzēšana.
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
     * @param lietotajvards lietotājvārds (e-pasts)
     * @param talrunis tālrunis vai null
     * @param parole parole
     * @return true, ja saglabāts; false, ja tāds lietotājvārds jau ir vai radās kļūda
     */
    public boolean registretLietotaju(String vards, String uzvards, String lietotajvards,
            String talrunis, String parole) {
        String sql = "INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, talrunis, parole_hash, loma) "
                + "VALUES (?, ?, ?, ?, ?, 'DALIBNIEKS')";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vards);
            ps.setString(2, uzvards);
            ps.setString(3, lietotajvards);
            ps.setString(4, talrunis);
            ps.setString(5, PasswordUtil.hash(parole));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Atrod lietotāju pēc lietotājvārda.
     *
     * @param lietotajvards lietotājvārds (e-pasts)
     * @return {@link Administrators} vai {@link Dalibnieks}; null, ja nav atrasts
     */
    public Lietotajs atrastLietotaju(String lietotajvards) {
        String sql = "SELECT * FROM LIETOTAJI WHERE lietotajvards = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lietotajvards);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String vards = rs.getString("vards");
                String uzvards = rs.getString("uzvards");
                String talrunis = rs.getString("talrunis");
                String hash = rs.getString("parole_hash");

                if ("ADMIN".equals(rs.getString("loma"))) {
                    return new Administrators(id, vards, uzvards, lietotajvards, talrunis, hash);
                }
                return new Dalibnieks(id, vards, uzvards, lietotajvards, talrunis, hash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
}
