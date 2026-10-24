/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.model;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author glebsvasiljevs
 */

public class PasakumsTest {

    private Pasakums pasakums;

    @Before
    public void setUp() {
        pasakums = new Pasakums(1, "Programmēšanas seminārs", "Ievads Java valodā",
                java.sql.Date.valueOf("2030-12-25"), "10:00", "205. kabinets", 5, 1);
    }

    // jaunam pasākumam visas vietas ir brīvas
    @Test
    public void jaunamPasakumamBrivasVietasIrMaksimums() {
        assertEquals(5, pasakums.getBrivoVietuSkaitu());
    }

    @Test
    public void brivasVietasPecApstiprinasanas() {
        pasakums.setApstiprinatoSkaits(3);
        assertEquals(2, pasakums.getBrivoVietuSkaitu());
    }

    // robežgadījums: visi apstiprināti - brīvu vietu nav
    @Test
    public void pilnamPasakumamNavBrivuVietu() {
        pasakums.setApstiprinatoSkaits(5);
        assertEquals(0, pasakums.getBrivoVietuSkaitu());
    }

    @Test
    public void getteriAtgriezIevaditosDatus() {
        assertEquals("Programmēšanas seminārs", pasakums.getNosaukums());
        assertEquals("10:00", pasakums.getLaiks());
        assertEquals("205. kabinets", pasakums.getVieta());
        assertEquals(5, pasakums.getMaksDalibniekuSkaits());
        assertEquals(1, pasakums.getIzveidotajsId());
    }
}
