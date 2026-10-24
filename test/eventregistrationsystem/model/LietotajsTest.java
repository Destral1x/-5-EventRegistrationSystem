/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.model;

import eventregistrationsystem.util.PasswordUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author glebsvasiljevs
 **/
public class LietotajsTest {

    private Lietotajs lietotajs(String hash) {
        return new Lietotajs(1, "Anna", "Kalniņa", "anna@example.com", "+371 20000000", hash);
    }

    @Test
    public void pareizaParoleTiekAtpazita() {
        String hash = PasswordUtil.hash("Drosiba123");
        assertTrue(lietotajs(hash).parbauditParoli("Drosiba123"));
    }

    @Test
    public void nepareizaParoleNetiekAtpazita() {
        String hash = PasswordUtil.hash("Drosiba123");
        assertFalse(lietotajs(hash).parbauditParoli("nepareiza"));
    }

    // paroles pārbaude ir reģistrjutīga
    @Test
    public void paroleIrRegistrjutiga() {
        String hash = PasswordUtil.hash("Drosiba123");
        assertFalse(lietotajs(hash).parbauditParoli("drosiba123"));
    }

    // robežgadījums: lietotājam nav saglabāta paroles jaucējkoda
    @Test
    public void bezHashParoleNeder() {
        assertFalse(lietotajs(null).parbauditParoli("jebkas"));
    }

    @Test
    public void jaucejkodsIrSHA256() {
        assertEquals(64, PasswordUtil.hash("x").length());
        // tāds pats jaucējkods ir sākuma datos (data.sql) administratoram
        assertEquals("240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9",
                PasswordUtil.hash("admin123"));
    }

    @Test
    public void getteriAtgriezIevaditosDatus() {
        Lietotajs l = lietotajs("hash");
        assertEquals(1, l.getId());
        assertEquals("Anna", l.getVards());
        assertEquals("Kalniņa", l.getUzvards());
        assertEquals("anna@example.com", l.getLietotajvardu());
        assertEquals("+371 20000000", l.getTalrunis());
    }
}
