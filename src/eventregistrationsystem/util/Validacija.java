/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Ievaddatu pārbaude reģistrācijas un pasākuma izveides formām.
 * Metodes atgriež kļūdas tekstu vai null, ja dati ir pareizi.
 *
 * @author glebsvasiljievs
 */
public class Validacija {

    /** Datuma formāts dd.mm.gggg (stingrā pārbaude). */
    public static final DateTimeFormatter DATUMA_FORMATS
            = DateTimeFormatter.ofPattern("dd.MM.uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter LAIKA_FORMATS = DateTimeFormatter.ofPattern("H:mm");

    private Validacija() {
    }

    /**
     * Pārbauda reģistrācijas formas laukus.
     *
     * @param vards vārds
     * @param uzvards uzvārds
     * @param epasts e-pasts (lietotājvārds)
     * @param talrunis tālrunis (nav obligāts, var būt tukšs)
     * @param parole parole
     * @param parole2 parole atkārtoti
     * @return kļūdas teksts vai null, ja visi dati ir pareizi
     */
    public static String registracija(String vards, String uzvards, String epasts,
            String talrunis, String parole, String parole2) {
        if (vards.isEmpty() || uzvards.isEmpty() || epasts.isEmpty() || parole.isEmpty()) {
            return "Aizpildiet visus obligātos laukus!";
        }
        // e-pastam jāsatur @ un punkts
        if (!epasts.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return "Ievadiet derīgu e-pasta adresi!";
        }
        if (parole.length() < 6) {
            return "Parolei jābūt vismaz 6 simbolus garai!";
        }
        if (!parole.equals(parole2)) {
            return "Paroles nesakrīt!";
        }
        // teksts nedrīkst būt garāks, nekā ir vietas datu bāzē
        if (vards.length() > 50 || uzvards.length() > 50 || epasts.length() > 50 || talrunis.length() > 20) {
            return "Kāds no laukiem ir pārāk garš!";
        }
        // tālrunis nav obligāts, bet ja ievadīts - tikai cipari, + un atstarpes
        if (!talrunis.isEmpty() && !derigsTalrunis(talrunis)) {
            return "Ievadiet derīgu tālruņa numuru (piem., +371 20000000)!";
        }
        return null;
    }

    /**
     * Pārbauda tālruņa numura formātu: tikai cipari, + sākumā un atstarpes.
     *
     * @param talrunis tālruņa numurs
     * @return true, ja formāts ir pareizs
     */
    public static boolean derigsTalrunis(String talrunis) {
        return talrunis.matches("^\\+?[0-9 ]{8,19}$");
    }

    /**
     * Pārbauda pasākuma izveides formas laukus.
     *
     * @param nosaukums pasākuma nosaukums
     * @param apraksts apraksts (nav obligāts)
     * @param datumsTeksts datums formātā dd.mm.gggg
     * @param laiksTeksts laiks formātā hh:mm
     * @param vieta norises vieta
     * @param maks maksimālais dalībnieku skaits
     * @return kļūdas teksts vai null, ja visi dati ir pareizi
     */
    public static String pasakums(String nosaukums, String apraksts, String datumsTeksts,
            String laiksTeksts, String vieta, int maks) {
        if (nosaukums.isEmpty() || datumsTeksts.isEmpty() || laiksTeksts.isEmpty() || vieta.isEmpty()) {
            return "Aizpildiet visus obligātos laukus!";
        }
        if (nosaukums.length() > 100 || vieta.length() > 100 || apraksts.length() > 500) {
            return "Kāds no laukiem ir pārāk garš!";
        }
        LocalDate datums = parsetDatumu(datumsTeksts);
        if (datums == null) {
            return "Datums jāievada formātā dd.mm.gggg!";
        }
        if (datums.isBefore(LocalDate.now())) {
            return "Datums nedrīkst būt pagātnē!";
        }
        LocalTime laiks = parsetLaiku(laiksTeksts);
        if (laiks == null) {
            return "Laiks jāievada formātā hh:mm!";
        }
        // ja pasākums ir šodien, laiks nedrīkst būt jau pagājis
        if (datums.isEqual(LocalDate.now()) && laiks.isBefore(LocalTime.now())) {
            return "Pasākuma laiks nedrīkst būt pagātnē!";
        }
        if (maks <= 0) {
            return "Dalībnieku skaitam jābūt lielākam par 0!";
        }
        return null;
    }

    /**
     * Pārveido tekstu datumā.
     *
     * @param teksts datums formātā dd.mm.gggg
     * @return datums vai null, ja formāts ir nepareizs
     */
    public static LocalDate parsetDatumu(String teksts) {
        try {
            return LocalDate.parse(teksts, DATUMA_FORMATS);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Pārveido tekstu laikā.
     *
     * @param teksts laiks formātā hh:mm
     * @return laiks vai null, ja formāts ir nepareizs
     */
    public static LocalTime parsetLaiku(String teksts) {
        try {
            return LocalTime.parse(teksts, LAIKA_FORMATS);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
