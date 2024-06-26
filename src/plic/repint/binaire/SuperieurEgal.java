package plic.repint.binaire;

import plic.repint.Expression;

public class SuperieurEgal extends Binaire {

    public SuperieurEgal(Expression exp1, Expression exp2) {
        super(exp1, exp2);
    }

    @Override
    public String toMIPS() {
        return """
                \t# SUPEGAL
                """ + super.toMIPS() + """
                \t# SUPEGAL
                \tsge $v0, $v1, $v0
                """;
    }

    @Override
    public String getType() {
        return "boolean";
    }

    @Override
    public String toString() {
        return gauche + ">=" + droite;
    }
}
