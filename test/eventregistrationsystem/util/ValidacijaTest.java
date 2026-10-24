/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.util;

import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author glebsvasiljevs
 */
public class ValidacijaTest {

    private static final String OBLIGATIE = "Aizpildiet visus obligātos laukus!";
    private static final String PARGARS = "Kāds no laukiem ir pārāk garš!";

    // datums nākotnē formātā dd.mm.gggg
    private String nakotnesDatums() {
        return LocalDate.now().plusDays(7).format(Validacija.DATUMA_FORMATS);
    }

    // ---------- reģistrācijas forma ----------

    @Test
    public void registracijaDerigiDati() {
        assertNull(Validacija.registracija("Anna", "Kalniņa", "anna@example.com",
                "+371 20000000", "parole123", "parole123"));
    }

    @Test
    public void registracijaTalrunisNavObligats() {
        assertNull(Validacija.registracija("Anna", "Kalniņa", "anna@example.com",
                "", "parole123", "parole123"));
    }

    @Test
    public void registracijaTuksVards() {
        assertEquals(OBLIGATIE, Validacija.registracija("", "Kalniņa", "anna@example.com",
                "", "parole123", "parole123"));
    }

    @Test
    public void registracijaNederigsEpasts() {
        assertEquals("Ievadiet derīgu e-pasta adresi!", Validacija.registracija("Anna", "Kalniņa",
                "anna.example.com", "", "parole123", "parole123"));
        assertEquals("Ievadiet derīgu e-pasta adresi!", Validacija.registracija("Anna", "Kalniņa",
                "anna@example", "", "parole123", "parole123"));
    }

    @Test
    public void registracijaParIsaParole() {
        assertEquals("Parolei jābūt vismaz 6 simbolus garai!", Validacija.registracija("Anna", "Kalniņa",
                "anna@example.com", "", "12345", "12345"));
    }

    // robežgadījums: tieši 6 simboli ir derīga parole
    @Test
    public void registracijaParole6Simboli() {
        assertNull(Validacija.registracija("Anna", "Kalniņa", "anna@example.com",
                "", "123456", "123456"));
    }

    @Test
    public void registracijaParolesNesakrit() {
        assertEquals("Paroles nesakrīt!", Validacija.registracija("Anna", "Kalniņa",
                "anna@example.com", "", "parole123", "parole124"));
    }

    // robežgadījums: vārds ar 50 simboliem ir derīgs, ar 51 - pārāk garš
    @Test
    public void registracijaVardaGarums() {
        assertNull(Validacija.registracija("A".repeat(50), "Kalniņa", "anna@example.com",
                "", "parole123", "parole123"));
        assertEquals(PARGARS, Validacija.registracija("A".repeat(51), "Kalniņa", "anna@example.com",
                "", "parole123", "parole123"));
    }

    @Test
    public void registracijaNederigsTalrunis() {
        assertEquals("Ievadiet derīgu tālruņa numuru (piem., +371 20000000)!", Validacija.registracija(
                "Anna", "Kalniņa", "anna@example.com", "abc12345", "parole123", "parole123"));
    }

    // ---------- pasākuma forma ----------

    @Test
    public void pasakumsDerigiDati() {
        assertNull(Validacija.pasakums("Java seminārs", "Ievads", nakotnesDatums(), "10:00", "205. kabinets", 25));
    }

    @Test
    public void pasakumsApraksts_NavObligats() {
        assertNull(Validacija.pasakums("Java seminārs", "", nakotnesDatums(), "10:00", "205. kabinets", 25));
    }

    @Test
    public void pasakumsTuksNosaukums() {
        assertEquals(OBLIGATIE, Validacija.pasakums("", "", nakotnesDatums(), "10:00", "vieta", 10));
    }

    @Test
    public void pasakumsParGarsTeksts() {
        assertEquals(PARGARS, Validacija.pasakums("A".repeat(101), "", nakotnesDatums(), "10:00", "vieta", 10));
        assertEquals(PARGARS, Validacija.pasakums("Nosaukums", "A".repeat(501), nakotnesDatums(), "10:00", "vieta", 10));
    }

    @Test
    public void pasakumsNederigsDatumaFormats() {
        String teksts = "Datums jāievada formātā dd.mm.gggg!";
        assertEquals(teksts, Validacija.pasakums("Nosaukums", "", "2030-12-25", "10:00", "vieta", 10));
        // neeksistējošs datums
        assertEquals(teksts, Validacija.pasakums("Nosaukums", "", "31.02.2030", "10:00", "vieta", 10));
    }

    @Test
    public void pasakumsDatumsPagatne() {
        String vakar = LocalDate.now().minusDays(1).format(Validacija.DATUMA_FORMATS);
        assertEquals("Datums nedrīkst būt pagātnē!",
                Validacija.pasakums("Nosaukums", "", vakar, "10:00", "vieta", 10));
    }

    @Test
    public void pasakumsNederigsLaiks() {
        String teksts = "Laiks jāievada formātā hh:mm!";
        assertEquals(teksts, Validacija.pasakums("Nosaukums", "", nakotnesDatums(), "25:00", "vieta", 10));
        assertEquals(teksts, Validacija.pasakums("Nosaukums", "", nakotnesDatums(), "abc", "vieta", 10));
    }

    @Test
    public void pasakumsLaiksBezPrieksejasNulles() {
        assertNull(Validacija.pasakums("Nosaukums", "", nakotnesDatums(), "9:30", "vieta", 10));
    }

    // robežgadījums: 0 vietu nav derīgi, 1 vieta ir derīga
    @Test
    public void pasakumsMaksDalibniekuSkaits() {
        assertEquals("Dalībnieku skaitam jābūt lielākam par 0!",
                Validacija.pasakums("Nosaukums", "", nakotnesDatums(), "10:00", "vieta", 0));
        assertNull(Validacija.pasakums("Nosaukums", "", nakotnesDatums(), "10:00", "vieta", 1));
    }

    // ---------- datuma un laika pārveide ----------

    @Test
    public void parsetDatumuDerigs() {
        assertEquals(LocalDate.of(2030, 12, 25), Validacija.parsetDatumu("25.12.2030"));
    }

    @Test
    public void parsetDatumuNederigs() {
        assertNull(Validacija.parsetDatumu("31.02.2030"));
        assertNull(Validacija.parsetDatumu("abc"));
    }

    @Test
    public void parsetLaikuDerigs() {
        assertEquals(LocalTime.of(18, 30), Validacija.parsetLaiku("18:30"));
        assertEquals(LocalTime.of(9, 5), Validacija.parsetLaiku("9:05"));
    }

    @Test
    public void parsetLaikuNederigs() {
        assertNull(Validacija.parsetLaiku("25:00"));
        assertNull(Validacija.parsetLaiku("18-30"));
    }
}
