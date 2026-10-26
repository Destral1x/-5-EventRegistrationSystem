/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.dao;

import eventregistrationsystem.model.Pasakums;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Datu piekļuve tabulai PASAKUMI.
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class PasakumuDAO {

    /**
     * Saglabā jaunu pasākumu datu bāzē.
     *
     * @param p pasākums
     * @return true, ja saglabāts
     */
    public boolean saglabatPasakumu(Pasakums p) {
        String sql = "INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, "
                + "maks_dalibnieku_skaits, izveidotajs_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNosaukums());
            ps.setString(2, p.getApraksts());
            ps.setDate(3, p.getDatums());
            ps.setTime(4, Time.valueOf(p.getLaiks() + ":00"));
            ps.setString(5, p.getVieta());
            ps.setInt(6, p.getMaksDalibniekuSkaits());
            ps.setInt(7, p.getIzveidotajsId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Nolasa aktīvos (šodienas un nākotnes) pasākumus kopā ar apstiprināto
     * pieteikumu skaitu, lai varētu aprēķināt brīvās vietas.
     * Kļūdu nodod GUI, lai lietotājs redz paziņojumu, nevis tukšu tabulu.
     *
     * @return pasākumu saraksts, sakārtots pēc datuma un laika
     * @throws SQLException datu bāzes kļūdas gadījumā
     */
    public List<Pasakums> iegutVisusPasakumus() throws SQLException {
        List<Pasakums> saraksts = new ArrayList<>();
        String sql = "SELECT p.*, (SELECT COUNT(*) FROM REGISTRACIJAS r "
                + "WHERE r.pasakuma_id = p.id AND r.statuss = 'APSTIPRINATS') AS apstiprinati "
                + "FROM PASAKUMI p WHERE p.datums >= CURRENT_DATE ORDER BY p.datums, p.laiks";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pasakums p = new Pasakums(
                        rs.getInt("id"),
                        rs.getString("nosaukums"),
                        rs.getString("apraksts"),
                        rs.getDate("datums"),
                        rs.getString("laiks").substring(0, 5),
                        rs.getString("vieta"),
                        rs.getInt("maks_dalibnieku_skaits"),
                        rs.getInt("izveidotajs_id"));
                p.setApstiprinatoSkaits(rs.getInt("apstiprinati"));
                saraksts.add(p);
            }
        }
        return saraksts;
    }

    /**
     * Saglabā labota pasākuma datus.
     *
     * @param p pasākums ar jaunajiem datiem (id norāda, kuru pasākumu labot)
     * @return true, ja atjaunināts
     */
    public boolean atjauninatPasakumu(Pasakums p) {
        String sql = "UPDATE PASAKUMI SET nosaukums = ?, apraksts = ?, datums = ?, laiks = ?, vieta = ?, "
                + "maks_dalibnieku_skaits = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNosaukums());
            ps.setString(2, p.getApraksts());
            ps.setDate(3, p.getDatums());
            ps.setTime(4, Time.valueOf(p.getLaiks() + ":00"));
            ps.setString(5, p.getVieta());
            ps.setInt(6, p.getMaksDalibniekuSkaits());
            ps.setInt(7, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Dzēš pasākumu. Tā pieteikumi izdzēšas ar ON DELETE CASCADE.
     *
     * @param id pasākuma identifikators
     * @return true, ja dzēsts
     */
    public boolean dzestPasakumu(int id) {
        String sql = "DELETE FROM PASAKUMI WHERE id = ?";

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
     * Maina pasākuma maksimālo dalībnieku skaitu.
     *
     * @param id pasākuma identifikators
     * @param skaits jaunais maksimālais dalībnieku skaits
     * @return true, ja atjaunināts
     */
    public boolean atjauninatVietuSkaitu(int id, int skaits) {
        String sql = "UPDATE PASAKUMI SET maks_dalibnieku_skaits = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, skaits);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
