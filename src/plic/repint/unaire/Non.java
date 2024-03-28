package plic.repint.unaire;

import plic.exceptions.DeclarManquanteException;
import plic.repint.Expression;

public class Non extends Unaire {
    public Non(Expression expression) {
        super(expression);
    }

    @Override
    public String toMIPS() {
        return """
                \t# Non
                """ + expression.toMIPS() + """
                \t# Non
                \txori $v0, $v0, 1
                """;
    }

    @Override
    public String toString() {
        return "!" + expression;
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        expression.verifier();
        if (!expression.getType().equals("boolean")) {
            throw new DeclarManquanteException("ERREUR: " + expression + " n'est pas un booleen");
        }
    }

    @Override
    public String getType() {
        return "boolean";
    }
}
