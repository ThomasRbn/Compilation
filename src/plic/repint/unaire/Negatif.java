package plic.repint.unaire;

import plic.exceptions.DeclarManquanteException;
import plic.repint.Expression;

public class Negatif extends Unaire {
    public Negatif(Expression expression) {
        super(expression);
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        expression.verifier();
        if (!expression.getType().equals("entier")) {
            throw new DeclarManquanteException("ERREUR:" + expression + " n'est pas un entier");
        }
    }

    @Override
    public String toMIPS() {
        return """
                \t# Negatif
                """ + expression.toMIPS() + """
                \t# Negatif
                \tneg $v0, $v0
                """;
    }

    @Override
    public String toString() {
        return "-" + expression;
    }
}
