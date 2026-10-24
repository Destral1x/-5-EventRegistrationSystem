/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author glebsvasiljevs
 */

public class RegistracijaTest {

    private Pasakums pasakums;
    private Dalibnieks dalibnieks;
    private Administrators administrators;

    @Before
    public void setUp() {
        pasakums = new Pasakums(1, "Datu bāzu meistarklase", "Praktiska nodarbība",
                java.sql.Date.valueOf("2030-12-05"), "14:30", "Aktu zāle", 15, 1);
        dalibnieks = new Dalibnieks(2, "Jānis", "Bērziņš", "janis@example.com", null, "hash");
        administrators = new Administrators(1, "Sistēmas", "Administrators", "admin", null, "hash");
    }

    // jauns pieteikums vienmēr sākas ar statusu GAIDA
    @Test
    public void jaunsPieteikumsSakasArGaida() {
        Registracija r = dalibnieks.registretiesPasakumam(pasakums);
        assertEquals(Registracija.GAIDA, r.getStatuss());
        assertEquals(0, r.getId());
        assertSame(pasakums, r.getPasakums());
        assertSame(dalibnieks, r.getDalibnieks());
    }

    @Test
    public void statussaTeksts() {
        Registracija r = dalibnieks.registretiesPasakumam(pasakums);
        assertEquals("Gaida", r.getStatussTeksts());
        r.setStatuss(Registracija.APSTIPRINATS);
        assertEquals("Apstiprināts", r.getStatussTeksts());
        r.setStatuss(Registracija.NORAIDITS);
        assertEquals("Noraidīts", r.getStatussTeksts());
    }

    @Test
    public void administratorsApstiprinaUnNoraida() {
        Registracija r = dalibnieks.registretiesPasakumam(pasakums);
        administrators.apstiprinatDalibu(r);
        assertEquals(Registracija.APSTIPRINATS, r.getStatuss());
        administrators.noraiditDalibu(r);
        assertEquals(Registracija.NORAIDITS, r.getStatuss());
    }

    @Test
    public void administratorsIzveidoPasakumu() {
        Pasakums p = administrators.izveidotPasakumu("Konference", null,
                java.sql.Date.valueOf("2030-05-01"), "09:00", "Aktu zāle", 30);
        assertEquals("Konference", p.getNosaukums());
        assertEquals(1, p.getIzveidotajsId());
        assertEquals(30, p.getBrivoVietuSkaitu());
    }
}
