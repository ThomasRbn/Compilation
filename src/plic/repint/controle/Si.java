package plic.repint.controle;

import plic.exceptions.DeclarManquanteException;
import plic.repint.Expression;
import plic.repint.primaire.Bloc;
import plic.repint.primaire.Instruction;
import plic.repint.primaire.Programme;

public class Si extends Instruction {

    public Expression condition;
    public Bloc alors;
    public Bloc sinon;

    public Si(Expression condition, Bloc alors, Bloc sinon) {
        this.condition = condition;
        this.alors = alors;
        this.sinon = sinon;
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        condition.verifier();

        if (!condition.getType().equals("boolean")) {
            throw new DeclarManquanteException("ERREUR: condition du si doit être de type boolean");
        }

        alors.verifier();
        if (sinon != null) {
            sinon.verifier();
        }
    }

    @Override
    public String toMIPS() {
        String res = "";
        res += condition.toMIPS();

        if (sinon == null) {
            res += """
                    \tbeqz $v0, fin""" + Programme.compteur + """
                    """;
            res += alors.toMIPS();
        } else {
            res += """
                    \tbeqz $v0, sinon""" + Programme.compteur + """
                    """;
            res += alors.toMIPS();
            res += """
                    \tj fin""" + Programme.compteur + "\n";
            res += """
                    sinon""" + Programme.compteur + ":\n";
            res += sinon.toMIPS();
        }
        res += """
                fin""" + Programme.compteur + ":\n";

        Programme.compteur++;
        return res;
    }
}
