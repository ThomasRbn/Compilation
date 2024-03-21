package plic.repint.binaire;

import plic.exceptions.DeclarManquanteException;
import plic.repint.Expression;

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
        //Todo vérifier le type
        droite.verifier();
    }

    @Override
    public abstract String toMIPS();
}
