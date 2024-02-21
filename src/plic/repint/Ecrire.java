package plic.repint;

import plic.exceptions.DeclarManquanteException;

public class Ecrire extends Instruction {

    private Expression expression;

    public Ecrire(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public String toString() {
        return "ecrire " + expression + " ;";
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        expression.verifier();
    }
}
