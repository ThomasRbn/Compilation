package plic.repint.binaire;

import plic.repint.Expression;

public class Egal extends Binaire {
    public Egal(Expression gauche, Expression droite) {
        super(gauche, droite);
    }

    @Override
    public String toMIPS() {
        return """
                \t# Egal
                """ + super.toMIPS() + """
                \t# Egal
                \tseq $v0, $v1, $v0
                """;
    }

    @Override
    public String toString() {
        return "(" + gauche + " == " + droite + ")";
    }
}
