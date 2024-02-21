package plic.repint;

import plic.exceptions.DeclarManquanteException;

public class Affectation extends Instruction {

    private Idf idf;
    private Expression expression;

    public Affectation(Idf idf, Expression expression) {
        this.idf = idf;
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public String toString() {
        return idf + " := " + expression + " ;";
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        idf.verifier();
        expression.verifier();
    }

    @Override
    public String toMIPS() {
        return "\t# Affectation de " + expression + " à " + idf.getNom() + "\n"
                + expression.toMIPS()
                + "\tsw $v0, " + TDS.getInstance().identifier(new Entree(idf.getNom())).getDeplacement() + "($s7)\n\n";
    }
}
