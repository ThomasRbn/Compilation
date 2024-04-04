package plic.repint;

import plic.exceptions.DeclarManquanteException;
import plic.repint.primaire.Instruction;

public class Ecrire extends Instruction {

    private Expression expression;

    public Ecrire(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public String toString() {
        return "ecrire " + expression + " ;";
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        expression.verifier();
    }

    @Override
    public String toMIPS() {
        return "\n# Ecrire " + expression + "\n"
                + expression.toMIPS()
                + "\tmove $a0, $v0\n"
                + "\tli $v0, 1\n"
                + "\tsyscall\n"
                + "\t# Saut de ligne\n"
                + "\tla $a0, crlf\n"
                + "\tli $v0, 4\n"
                + "\tsyscall\n";
    }
}
