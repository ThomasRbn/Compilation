package plic.repint.binaire;

import plic.repint.Expression;

public class Produit extends Binaire {
    public Produit(Expression gauche, Expression droite) {
        super(gauche, droite);
    }

    @Override
    public String getType() {
        return "entier";
    }

    @Override
    public String toMIPS() {
        return """
                \t# Produit
                """ + super.toMIPS() + """
                \t# Produit
                \tmul $v0, $v1, $v0
                """;
    }

    @Override
    public String toString() {
        return "(" + gauche + " * " + droite + ")";
    }
}
