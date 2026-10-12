/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.model;

import java.sql.Timestamp;

/**
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class Registracija {

    public static final String GAIDA = "GAIDA";
    public static final String APSTIPRINATS = "APSTIPRINATS";
    public static final String NORAIDITS = "NORAIDITS";

    private int id;
    private Pasakums pasakums;
    private Dalibnieks dalibnieks;
    private String statuss;
    private Timestamp datums;

    public Registracija(int id, Pasakums pasakums, Dalibnieks dalibnieks, String statuss, Timestamp datums) {
        this.id = id;
        this.pasakums = pasakums;
        this.dalibnieks = dalibnieks;
        this.statuss = statuss;
        this.datums = datums;
    }

    // statuss tabulai
    public String getStatussTeksts() {
        if (APSTIPRINATS.equals(statuss)) {
            return "Apstiprināts";
        } else if (NORAIDITS.equals(statuss)) {
            return "Noraidīts";
        }
        return "Gaida";
    }

    public int getId() {
        return id;
    }

    public Pasakums getPasakums() {
        return pasakums;
    }

    public Dalibnieks getDalibnieks() {
        return dalibnieks;
    }

    public String getStatuss() {
        return statuss;
    }

    public void setStatuss(String statuss) {
        this.statuss = statuss;
    }

    public Timestamp getDatums() {
        return datums;
    }
}
