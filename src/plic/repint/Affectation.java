package plic.repint;

import plic.exceptions.DeclarManquanteException;

public class Affectation extends Instruction {

    private Acces acces;
    private Expression expression;

    public Affectation(Acces idf, Expression expression) {
        this.acces = idf;
        this.expression = expression;
    }

    public String empiler() {
        return """
                \t# Empilage de $v0
                \tmove $sp, $v0
                \tadd $sp, $sp, -4

                """;
    }

    public String depiler() {
        return """
                \t# Dépilage
                \tadd $sp, $sp, 4
                \tmove $v0, $sp
                                
                """;
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
        return "\t# Affectation de " + expression + " à " + acces.getNom() + "\n" +
                expression.toMIPS() +
                acces.toMIPS();
    }
}
