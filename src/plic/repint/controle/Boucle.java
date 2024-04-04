package plic.repint.controle;

import plic.exceptions.DeclarManquanteException;
import plic.repint.Expression;
import plic.repint.primaire.Bloc;
import plic.repint.primaire.Instruction;

public abstract class Boucle extends Instruction {

    protected Bloc contenu;
    protected Expression condition;

    protected Boucle(Bloc contenu) {
        this.contenu = contenu;
    }

    public Bloc getContenu() {
        return contenu;
    }

    public Boucle setContenu(Bloc contenu) {
        this.contenu = contenu;
        return this;
    }

    public Expression getCondition() {
        return condition;
    }

    public Boucle setCondition(Expression condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public abstract void verifier() throws DeclarManquanteException;

    @Override
    public abstract String toMIPS();
}
