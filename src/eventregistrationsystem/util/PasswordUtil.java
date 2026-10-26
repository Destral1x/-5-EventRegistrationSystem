/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Palīgklase paroļu jaucējkoda aprēķināšanai, lai datu bāzē
 * parole netiktu glabāta atklātā veidā.
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class PasswordUtil {

    /** Klasei ir tikai statiskas metodes, objektu neveido. */
    private PasswordUtil() {
    }

    /**
     * Aprēķina paroles SHA-256 jaucējkodu.
     *
     * @param password parole
     * @return 64 simbolu heksadecimāla virkne
     * @throws RuntimeException ja SHA-256 algoritms nav pieejams
     */
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
