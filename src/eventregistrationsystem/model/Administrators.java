/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.model;

/**
 * Administrators - lietotājs, kas izveido pasākumus un apstiprina
 * vai noraida dalības pieteikumus.
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class Administrators extends Lietotajs {

    /**
     * Izveido administratoru.
     *
     * @param id lietotāja identifikators
     * @param vards vārds
     * @param uzvards uzvārds
     * @param lietotajvards lietotājvārds (e-pasts)
     * @param talrunis tālrunis vai null
     * @param paroleHash paroles SHA-256 jaucējkods
     */
    public Administrators(int id, String vards, String uzvards, String lietotajvards, String talrunis, String paroleHash) {
        super(id, vards, uzvards, lietotajvards, talrunis, paroleHash);
    }

    /**
     * Izveido jaunu pasākumu, kura izveidotājs ir šis administrators.
     *
     * @param nosaukums pasākuma nosaukums
     * @param apraksts pasākuma apraksts vai null
     * @param datums norises datums
     * @param laiks norises laiks formātā hh:mm
     * @param vieta norises vieta
     * @param maksDalibniekuSkaits maksimālais dalībnieku skaits
     * @return jauns pasākums (vēl nav saglabāts datu bāzē)
     */
    public Pasakums izveidotPasakumu(String nosaukums, String apraksts, java.sql.Date datums,
            String laiks, String vieta, int maksDalibniekuSkaits) {
        return new Pasakums(0, nosaukums, apraksts, datums, laiks, vieta, maksDalibniekuSkaits, id);
    }

    /**
     * Apstiprina dalības pieteikumu.
     *
     * @param r pieteikums
     */
    public void apstiprinatDalibu(Registracija r) {
        r.setStatuss(Registracija.APSTIPRINATS);
    }

    /**
     * Noraida dalības pieteikumu.
     *
     * @param r pieteikums
     */
    public void noraiditDalibu(Registracija r) {
        r.setStatuss(Registracija.NORAIDITS);
    }
}
