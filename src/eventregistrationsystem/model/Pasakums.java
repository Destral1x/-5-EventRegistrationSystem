/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.model;

import java.sql.Date;

/**
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

    public int getBrivoVietuSkaitu() {
        return maksDalibniekuSkaits - apstiprinatoSkaits;
    }

    public int getId() {
        return id;
    }

    public String getNosaukums() {
        return nosaukums;
    }

    public String getApraksts() {
        return apraksts;
    }

    public Date getDatums() {
        return datums;
    }

    public String getLaiks() {
        return laiks;
    }

    public String getVieta() {
        return vieta;
    }

    public int getMaksDalibniekuSkaits() {
        return maksDalibniekuSkaits;
    }

    public int getIzveidotajsId() {
        return izveidotajsId;
    }

    public void setApstiprinatoSkaits(int apstiprinatoSkaits) {
        this.apstiprinatoSkaits = apstiprinatoSkaits;
    }
}
