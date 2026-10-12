/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.dao;

import eventregistrationsystem.model.Dalibnieks;
import eventregistrationsystem.model.Pasakums;
import eventregistrationsystem.model.Registracija;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class RegistracijaDAO {

    // false, ja jau ir pieteicies (UNIQUE pasakuma_id + lietotaja_id)
    public boolean saglabatRegistraciju(Registracija r) {
        String sql = "INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getPasakums().getId());
            ps.setInt(2, r.getDalibnieks().getId());
            ps.setString(3, r.getStatuss());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean mainitStatusu(int id, String statuss) {
        String sql = "UPDATE REGISTRACIJAS SET statuss = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statuss);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // pasākuma dalībnieki priekš "Dalībnieku pārvaldība" loga
    public List<Registracija> iegutPasakumaDalibniekus(Pasakums p) {
        List<Registracija> saraksts = new ArrayList<>();
        String sql = "SELECT r.id, r.statuss, r.registracijas_laiks, l.id AS lid, l.vards, l.uzvards, "
                + "l.lietotajvards, l.talrunis FROM REGISTRACIJAS r "
                + "JOIN LIETOTAJI l ON l.id = r.lietotaja_id "
                + "WHERE r.pasakuma_id = ? ORDER BY r.registracijas_laiks";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Dalibnieks d = new Dalibnieks(rs.getInt("lid"), rs.getString("vards"), rs.getString("uzvards"),
                        rs.getString("lietotajvards"), rs.getString("talrunis"), null);
                saraksts.add(new Registracija(rs.getInt("id"), p, d, rs.getString("statuss"),
                        rs.getTimestamp("registracijas_laiks")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saraksts;
    }
}
