package plic.repint;

import plic.exceptions.DeclarManquanteException;

public class Idf extends Acces {

    public Idf(String nom) {
        super(nom, "entier");
    }

    public String toString() {
        return nom;
    }

    public void verifier() throws DeclarManquanteException {
        if (TDS.getInstance().identifier(new Entree(nom)) == null)
            throw new DeclarManquanteException("Variable " + nom + " non déclarée");
    }

}
