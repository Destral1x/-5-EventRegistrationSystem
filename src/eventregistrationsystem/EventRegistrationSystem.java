/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package eventregistrationsystem;

import eventregistrationsystem.dao.DatabaseUtil;
import java.sql.SQLException;

/**
 *
 * @author artjomsdoktorovs
 */
public class EventRegistrationSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            DatabaseUtil.init();
        } catch (SQLException e) {
            System.err.println("Neizdevās pieslēgties datu bāzei: " + e.getMessage());
        }

        EventRegistrationSystemGUI.main(args);
    }

}
