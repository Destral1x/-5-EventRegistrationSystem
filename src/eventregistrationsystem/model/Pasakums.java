/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.model;

import java.sql.Date;

/**
 * Pasākums, kuram dalībnieki var pieteikties (tabula PASAKUMI).
 * Glabā pasākuma datus un aprēķina brīvo vietu skaitu.
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class Pasakums {

    private int id;
    private String nosaukums;
    private String apraksts;
    private Date datums;
    private String laiks;
    private String vieta;
    private int maksDalibniekuSkaits;
    private int izveidotajsId;
    private int apstiprinatoSkaits; // nav tabulā, saskaita no REGISTRACIJAS

    /**
     * Izveido pasākumu.
     *
     * @param id pasākuma identifikators (jaunam pasākumam 0)
     * @param nosaukums pasākuma nosaukums
     * @param apraksts pasākuma apraksts vai null
     * @param datums norises datums
     * @param laiks norises laiks formātā hh:mm
     * @param vieta norises vieta
     * @param maksDalibniekuSkaits maksimālais dalībnieku skaits
     * @param izveidotajsId administratora id, kas izveidoja pasākumu
     */
    public Pasakums(int id, String nosaukums, String apraksts, Date datums, String laiks,
            String vieta, int maksDalibniekuSkaits, int izveidotajsId) {
        this.id = id;
        this.nosaukums = nosaukums;
        this.apraksts = apraksts;
        this.datums = datums;
        this.laiks = laiks;
        this.vieta = vieta;
        this.maksDalibniekuSkaits = maksDalibniekuSkaits;
        this.izveidotajsId = izveidotajsId;
    }

    /**
     * Aprēķina brīvo vietu skaitu.
     *
     * @return maksimālais dalībnieku skaits mīnus apstiprināto pieteikumu skaits
     */
    public int getBrivoVietuSkaitu() {
        return maksDalibniekuSkaits - apstiprinatoSkaits;
    }

    /**
     * Pasākuma identifikators.
     *
     * @return pasākuma identifikators
     */
    public int getId() {
        return id;
    }

    /**
     * Pasākuma nosaukums.
     *
     * @return pasākuma nosaukums
     */
    public String getNosaukums() {
        return nosaukums;
    }

    /**
     * Pasākuma apraksts vai null.
     *
     * @return pasākuma apraksts vai null
     */
    public String getApraksts() {
        return apraksts;
    }

    /**
     * Norises datums.
     *
     * @return norises datums
     */
    public Date getDatums() {
        return datums;
    }

    /**
     * Norises laiks formātā hh:mm.
     *
     * @return norises laiks formātā hh:mm
     */
    public String getLaiks() {
        return laiks;
    }

    /**
     * Norises vieta.
     *
     * @return norises vieta
     */
    public String getVieta() {
        return vieta;
    }

    /**
     * Maksimālais dalībnieku skaits.
     *
     * @return maksimālais dalībnieku skaits
     */
    public int getMaksDalibniekuSkaits() {
        return maksDalibniekuSkaits;
    }

    /**
     * Administratora id, kas izveidoja pasākumu.
     *
     * @return administratora id, kas izveidoja pasākumu
     */
    public int getIzveidotajsId() {
        return izveidotajsId;
    }

    /**
     * Uzstāda apstiprināto pieteikumu skaitu (nolasīts no tabulas REGISTRACIJAS).
     *
     * @param apstiprinatoSkaits apstiprināto pieteikumu skaits
     */
    public void setApstiprinatoSkaits(int apstiprinatoSkaits) {
        this.apstiprinatoSkaits = apstiprinatoSkaits;
    }
}
