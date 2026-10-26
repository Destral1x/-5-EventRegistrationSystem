/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.model;

import eventregistrationsystem.util.PasswordUtil;

/**
 * Sistēmas lietotājs (tabula LIETOTAJI) - bāzes klase
 * klasēm {@link Administrators} un {@link Dalibnieks}.
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class Lietotajs {

    /** Lietotāja identifikators (pieejams arī apakšklasēm). */
    protected int id;
    private String vards;
    private String uzvards;
    private String lietotajvards;
    private String talrunis;
    private String paroleHash;

    /**
     * Izveido lietotāju.
     *
     * @param id lietotāja identifikators
     * @param vards vārds
     * @param uzvards uzvārds
     * @param lietotajvards lietotājvārds (e-pasts)
     * @param talrunis tālrunis vai null
     * @param paroleHash paroles SHA-256 jaucējkods
     */
    public Lietotajs(int id, String vards, String uzvards, String lietotajvards, String talrunis, String paroleHash) {
        this.id = id;
        this.vards = vards;
        this.uzvards = uzvards;
        this.lietotajvards = lietotajvards;
        this.talrunis = talrunis;
        this.paroleHash = paroleHash;
    }

    /**
     * Salīdzina ievadīto paroli ar jaucējkodu no datu bāzes.
     *
     * @param p ievadītā parole
     * @return true, ja parole ir pareiza
     */
    public boolean parbauditParoli(String p) {
        return paroleHash != null && paroleHash.equals(PasswordUtil.hash(p));
    }

    /**
     * Lietotāja identifikators.
     *
     * @return lietotāja identifikators
     */
    public int getId() {
        return id;
    }

    /**
     * Vārds.
     *
     * @return vārds
     */
    public String getVards() {
        return vards;
    }

    /**
     * Uzvārds.
     *
     * @return uzvārds
     */
    public String getUzvards() {
        return uzvards;
    }

    /**
     * Lietotājvārds (e-pasts).
     *
     * @return lietotājvārds (e-pasts)
     */
    public String getLietotajvardu() {
        return lietotajvards;
    }

    /**
     * Tālrunis vai null, ja nav norādīts.
     *
     * @return tālrunis vai null, ja nav norādīts
     */
    public String getTalrunis() {
        return talrunis;
    }

    /**
     * Uzstāda tālruni (kontaktinformāciju).
     *
     * @param talrunis tālruņa numurs
     */
    public void setTalrunis(String talrunis) {
        this.talrunis = talrunis;
    }
}
