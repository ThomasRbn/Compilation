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

    public String toMIPS() {
        return "\tlw $v0, " + TDS.getInstance().identifier(new Entree(nom)).getDeplacement() + "($s7)\n";
    }
}
