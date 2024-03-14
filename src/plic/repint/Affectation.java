package plic.repint;

import plic.exceptions.DeclarManquanteException;

public class Affectation extends Instruction {

    private Acces acces;
    private Expression expression;

    public Affectation(Acces idf, Expression expression) {
        this.acces = idf;
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public String toString() {
        return acces + " := " + expression + " ;";
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        acces.verifier();
        expression.verifier();
    }

    @Override
    public String toMIPS() {
        return "\t# Affectation de " + expression + " à " + acces.getNom() + "\n"
                + acces.toMIPS() +
                expression.toMIPS() +
                "\tsw $v0, " + TDS.getInstance().identifier(new Entree(acces.getNom())).getDeplacement() + "($s7)\n\n";
    }
}
