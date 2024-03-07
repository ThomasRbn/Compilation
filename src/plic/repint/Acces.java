package plic.repint;

import plic.exceptions.DeclarManquanteException;

public class Acces extends Expression {

    protected String nom;

    protected String type;

    public Acces(String nom, String type) {
        this.nom = nom;
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public String getType() {
        return type;
    }

    @Override
    public void verifier() throws DeclarManquanteException {

    }

    @Override
    public String toMIPS() {
        return null;
    }
}
