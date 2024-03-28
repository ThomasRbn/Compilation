package plic.repint.binaire;

import plic.repint.Expression;

public class Different extends Binaire {
    public Different(Expression gauche, Expression droite) {
        super(gauche, droite);
    }

    @Override
    public String toMIPS() {
        return """
                \t# Different
                """ + super.toMIPS() + """
                \t# Different
                \tsne $v0, $v1, $v0
                """;
    }

    @Override
    public String toString() {
        return "(" + gauche + " != " + droite + ")";
    }
}
