package plic.repint.binaire;

import plic.repint.Expression;

public class InferieurEgal extends Binaire {

    public InferieurEgal(Expression exp1, Expression exp2) {
        super(exp1, exp2);
    }

    @Override
    public String toMIPS() {
        return """
                \t# InferieurEgal
                """ + super.toMIPS() + """
                \t# InferieurEgal
                \tsle $v0, $v1, $v0
                """;
    }

    @Override
    public String toString() {
        return gauche + "<=" + droite;
    }
}
