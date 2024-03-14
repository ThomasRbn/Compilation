package plic.repint;

import plic.exceptions.DeclarManquanteException;

public class Idf extends Acces {

    public Idf(String nom) {
        super(nom, "entier");
    }

    public String toString() {
        return nom;
    }
}
