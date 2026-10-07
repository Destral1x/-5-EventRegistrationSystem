/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package eventregistrationsystem;

import eventregistrationsystem.dao.DatabaseUtil;
import java.sql.SQLException;

/**
 *
 * @author artjomsdoktorovs, glebsvasiljievs
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
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Neizdevās pieslēgties datu bāzei.\nPārbaudiet, vai datu bāze nav atvērta NetBeans Services logā.",
                    "Datu bāzes kļūda", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        EventRegistrationSystemGUI.main(args);
    }

}
