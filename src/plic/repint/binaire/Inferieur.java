package plic.repint.binaire;

import plic.repint.Expression;

public class Inferieur extends Binaire {

    public Inferieur(Expression exp1, Expression exp2) {
        super(exp1, exp2);
    }

    @Override
    public String toMIPS() {
        return """
                \t# INF""" +
                super.toMIPS() +
                """
                \t# INF
                \tslt $v0, $v1, $v0
                """;
    }

    @Override
    public String toString() {
        return gauche + "<" + droite;
    }
}
