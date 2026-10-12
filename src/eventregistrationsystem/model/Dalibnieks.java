/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eventregistrationsystem.model;

/**
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class Dalibnieks extends Lietotajs {

    public Dalibnieks(int id, String vards, String uzvards, String lietotajvards, String talrunis, String paroleHash) {
        super(id, vards, uzvards, lietotajvards, talrunis, paroleHash);
    }

    // jauns pieteikums vienmēr sākas ar GAIDA
    public Registracija registretiesPasakumam(Pasakums p) {
        return new Registracija(0, p, this, Registracija.GAIDA, null);
    }
}
