package plic.repint;

import plic.exceptions.DeclarManquanteException;

public class Idf extends Expression {
    private String nom;

    public Idf(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public String toString() {
        return nom;
    }

    public void verifier() throws DeclarManquanteException {
        if (TDS.getInstance().identifier(new Entree(nom)) == null)
            throw new DeclarManquanteException("Variable " + nom + " non déclarée");
    }
}
