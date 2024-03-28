package plic.repint.binaire.logique;

import plic.repint.Expression;

public class Et extends Logique {

    public Et(Expression exp1, Expression exp2) {
        super(exp1, exp2);
    }

    @Override
    public String toMIPS() {
        return """
                \t# ET
                """ + super.toMIPS() + """
                \tand $v0, $v1, $v0
                """;
    }

    @Override
    public String getType() {
        return "boolean";
    }

    @Override
    public String toString() {
        return gauche + " && " + droite;
    }
}
