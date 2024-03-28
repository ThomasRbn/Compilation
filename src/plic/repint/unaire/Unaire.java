package plic.repint.unaire;

import plic.exceptions.DeclarManquanteException;
import plic.repint.Expression;

public abstract class Unaire extends Expression {

    protected Expression expression;

    public Unaire(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String getType() {
        return "boolean";
    }

    public abstract void verifier() throws DeclarManquanteException;

    @Override
    public String toMIPS() {
        return expression.toMIPS();
    }

    @Override
    public String toString() {
        return expression.toString();
    }

}
