package plic.repint.binaire;

import plic.repint.Expression;

public class Somme extends Binaire {
    public Somme(Expression gauche, Expression droite) {
        super(gauche, droite);
    }

    @Override
    public String getType() {
        return "entier";
    }


    @Override
    public String toMIPS() {
        return """
                \t# Somme
                """ + super.toMIPS() + """
                \t# Addition
                \tadd $v0, $v1, $v0
                """;
    }

    @Override
    public String toString() {
        return "(" + gauche + " + " + droite + ")";
    }

}
