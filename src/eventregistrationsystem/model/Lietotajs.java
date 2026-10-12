/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.model;

import eventregistrationsystem.util.PasswordUtil;

/**
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class Lietotajs {

    protected int id;
    private String vards;
    private String uzvards;
    private String lietotajvards;
    private String talrunis;
    private String paroleHash;

    public Lietotajs(int id, String vards, String uzvards, String lietotajvards, String talrunis, String paroleHash) {
        this.id = id;
        this.vards = vards;
        this.uzvards = uzvards;
        this.lietotajvards = lietotajvards;
        this.talrunis = talrunis;
        this.paroleHash = paroleHash;
    }

    // salīdzina ievadīto paroli ar hash no DB
    public boolean parbauditParoli(String p) {
        return paroleHash != null && paroleHash.equals(PasswordUtil.hash(p));
    }

    public int getId() {
        return id;
    }

    public String getVards() {
        return vards;
    }

    public String getUzvards() {
        return uzvards;
    }

    public String getLietotajvardu() {
        return lietotajvards;
    }

    public String getTalrunis() {
        return talrunis;
    }
}
