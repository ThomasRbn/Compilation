package plic.repint.binaire.logique;

import plic.repint.Expression;

public class Ou extends Logique {

    public Ou(Expression exp1, Expression exp2) {
        super(exp1, exp2);
    }

    @Override
    public String toMIPS() {
        return """
                \t# OU
                """ + super.toMIPS() + """
                \tor $v0, $v1, $v0
                """;
    }

    @Override
    public String toString() {
        return gauche + "||" + droite;
    }
}
