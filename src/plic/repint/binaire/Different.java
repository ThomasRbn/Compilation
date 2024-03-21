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
                \t# Evaluation de l'opérande gauche
                """ + gauche.toMIPS() + """
                \t# Empilement de l'opérande gauche
                """ + empiler() + """
                \t# Evaluation de l'opérande droite
                """ + droite.toMIPS() + """
                \t# Dépilement de l'opérande gauche
                """ + depiler() + """
                \t# Different
                \tsne $v0, $v1, $v0
                """;
    }

    @Override
    public String toString() {
        return "(" + gauche + " != " + droite + ")";
    }
}
