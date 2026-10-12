/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.model;

/**
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class Administrators extends Lietotajs {

    public Administrators(int id, String vards, String uzvards, String lietotajvards, String talrunis, String paroleHash) {
        super(id, vards, uzvards, lietotajvards, talrunis, paroleHash);
    }

    public Pasakums izveidotPasakumu(String nosaukums, String apraksts, java.sql.Date datums,
            String laiks, String vieta, int maksDalibniekuSkaits) {
        return new Pasakums(0, nosaukums, apraksts, datums, laiks, vieta, maksDalibniekuSkaits, id);
    }

    public void apstiprinatDalibu(Registracija r) {
        r.setStatuss(Registracija.APSTIPRINATS);
    }

    public void noraiditDalibu(Registracija r) {
        r.setStatuss(Registracija.NORAIDITS);
    }
}
