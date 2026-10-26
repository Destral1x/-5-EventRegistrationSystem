/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.model;

/**
 * Dalībnieks - lietotājs, kas piesakās dalībai pasākumos.
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class Dalibnieks extends Lietotajs {

    /**
     * Izveido dalībnieku.
     *
     * @param id lietotāja identifikators
     * @param vards vārds
     * @param uzvards uzvārds
     * @param lietotajvards lietotājvārds (e-pasts)
     * @param talrunis tālrunis vai null
     * @param paroleHash paroles SHA-256 jaucējkods
     */
    public Dalibnieks(int id, String vards, String uzvards, String lietotajvards, String talrunis, String paroleHash) {
        super(id, vards, uzvards, lietotajvards, talrunis, paroleHash);
    }

    /**
     * Izveido pieteikumu pasākumam. Jauns pieteikums vienmēr sākas ar statusu GAIDA.
     *
     * @param p pasākums, kuram piesakās
     * @return jauns pieteikums (vēl nav saglabāts datu bāzē)
     */
    public Registracija registretiesPasakumam(Pasakums p) {
        return new Registracija(0, p, this, Registracija.GAIDA, null);
    }
}
