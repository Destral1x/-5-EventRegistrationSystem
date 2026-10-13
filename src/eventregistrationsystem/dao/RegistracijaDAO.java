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
        // true, ja lietotājs jau ir pieteicies šim pasākumam
    public boolean jauPieteicies(int pasakumaId, int lietotajaId) {
        String sql = "SELECT COUNT(*) FROM REGISTRACIJAS WHERE pasakuma_id = ? AND lietotaja_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pasakumaId);
            ps.setInt(2, lietotajaId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // apstiprina pieteikumu tikai ja ir brīvas vietas
    // atgriež: 1 = apstiprināts, -1 = vietu nav, 0 = kļūda
    public int apstiprinatArKontroli(int registracijasId) {
        String sqlInfo = "SELECT r.pasakuma_id, r.statuss, p.maks_dalibnieku_skaits "
                + "FROM REGISTRACIJAS r JOIN PASAKUMI p ON p.id = r.pasakuma_id WHERE r.id = ?";
        String sqlCount = "SELECT COUNT(*) FROM REGISTRACIJAS WHERE pasakuma_id = ? AND statuss = 'APSTIPRINATS'";
        String sqlUpdate = "UPDATE REGISTRACIJAS SET statuss = 'APSTIPRINATS' WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int pasakumaId;
                int maks;
                try (PreparedStatement ps = conn.prepareStatement(sqlInfo)) {
                    ps.setInt(1, registracijasId);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        conn.rollback();
                        return 0;
                    }
                    pasakumaId = rs.getInt(1);
                    if ("APSTIPRINATS".equals(rs.getString(2))) {
                        conn.rollback();
                        return 1; // jau apstiprināts
                    }
                    maks = rs.getInt(3);
                }
                int apstiprinati;
                try (PreparedStatement ps = conn.prepareStatement(sqlCount)) {
                    ps.setInt(1, pasakumaId);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    apstiprinati = rs.getInt(1);
                }
                if (apstiprinati >= maks) {
                    conn.rollback();
                    return -1;
                }
                try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                    ps.setInt(1, registracijasId);
                    ps.executeUpdate();
                }
                conn.commit();
                return 1;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
