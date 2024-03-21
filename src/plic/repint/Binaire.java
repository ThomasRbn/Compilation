package plic.repint;

import plic.exceptions.DeclarManquanteException;

public abstract class Binaire extends Expression {

    protected Expression gauche;
    protected Expression droite;

    public Binaire(Expression gauche, Expression droite) {
        this.gauche = gauche;
        this.droite = droite;
    }

    @Override
    public String getType() {
        return "operation";
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        gauche.verifier();
        droite.verifier();
    }

    @Override
    public abstract String toMIPS();
}
