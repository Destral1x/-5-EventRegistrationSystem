/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.model;

import java.sql.Timestamp;

/**
 * Dalības pieteikums (tabula REGISTRACIJAS) - saista dalībnieku ar pasākumu
 * un glabā pieteikuma statusu.
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class Registracija {

    /** Pieteikums gaida apstiprinājumu. */
    public static final String GAIDA = "GAIDA";
    /** Pieteikums ir apstiprināts. */
    public static final String APSTIPRINATS = "APSTIPRINATS";
    /** Pieteikums ir noraidīts. */
    public static final String NORAIDITS = "NORAIDITS";

    private int id;
    private Pasakums pasakums;
    private Dalibnieks dalibnieks;
    private String statuss;
    private Timestamp datums;

    /**
     * Izveido pieteikumu.
     *
     * @param id pieteikuma identifikators (jaunam pieteikumam 0)
     * @param pasakums pasākums
     * @param dalibnieks dalībnieks
     * @param statuss statuss (GAIDA, APSTIPRINATS vai NORAIDITS)
     * @param datums reģistrācijas laiks vai null
     */
    public Registracija(int id, Pasakums pasakums, Dalibnieks dalibnieks, String statuss, Timestamp datums) {
        this.id = id;
        this.pasakums = pasakums;
        this.dalibnieks = dalibnieks;
        this.statuss = statuss;
        this.datums = datums;
    }

    /**
     * Statuss teksta veidā tabulai.
     *
     * @return statuss teksta veidā tabulai
     */
    public String getStatussTeksts() {
        return statussTeksts(statuss);
    }

    /**
     * Pārveido statusa kodu tekstā, ko rāda tabulā.
     *
     * @param statuss statusa kods (GAIDA, APSTIPRINATS vai NORAIDITS)
     * @return "Apstiprināts", "Noraidīts" vai "Gaida"
     */
    public static String statussTeksts(String statuss) {
        if (APSTIPRINATS.equals(statuss)) {
            return "Apstiprināts";
        } else if (NORAIDITS.equals(statuss)) {
            return "Noraidīts";
        }
        return "Gaida";
    }

    /**
     * Pieteikuma identifikators.
     *
     * @return pieteikuma identifikators
     */
    public int getId() {
        return id;
    }

    /**
     * Pasākums.
     *
     * @return pasākums
     */
    public Pasakums getPasakums() {
        return pasakums;
    }

    /**
     * Dalībnieks.
     *
     * @return dalībnieks
     */
    public Dalibnieks getDalibnieks() {
        return dalibnieks;
    }

    /**
     * Statusa kods.
     *
     * @return statusa kods
     */
    public String getStatuss() {
        return statuss;
    }

    /**
     * Uzstāda pieteikuma statusu.
     *
     * @param statuss jaunais statusa kods
     */
    public void setStatuss(String statuss) {
        this.statuss = statuss;
    }

    /**
     * Reģistrācijas laiks.
     *
     * @return reģistrācijas laiks
     */
    public Timestamp getDatums() {
        return datums;
    }
}
