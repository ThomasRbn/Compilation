package plic.repint.binaire;

import plic.repint.Expression;

public class Soustraction extends Binaire {
    public Soustraction(Expression gauche, Expression droite) {
        super(gauche, droite);
    }

    @Override
    public String toMIPS() {
        return """
                \t# Soustraction
                """ + super.toMIPS() + """
                \t# Soustraction
                \tsub $v0, $v1, $v0
                """;
    }

    @Override
    public String toString() {
        return "(" + gauche + " - " + droite + ")";
    }

    @Override
    public String getType() {
        return "entier";
    }

}
