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
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class LietotajuDAO {

    // jauns lietotājs vienmēr ir DALIBNIEKS, false ja tāds lietotājvārds jau ir
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

    // atrod pēc lietotājvārda, ja nav - null
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
}
