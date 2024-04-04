package plic.repint.controle;

import plic.exceptions.DeclarManquanteException;
import plic.repint.primaire.Bloc;
import plic.repint.primaire.Programme;

public class TantQue extends Boucle {

    public TantQue(Bloc contenu) {
        super(contenu);
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        contenu.verifier();
        condition.verifier();
        if (!condition.getType().equals("boolean")) {
            throw new DeclarManquanteException("ERREUR: condition de la boucle tantque doit être de type boolean");
        }
    }

    @Override
    public String toMIPS() {
        String res = """
                tantque""" + Programme.compteur + ":\n";
        res += condition.toMIPS();
        res += """
                \tbeqz $v0, fintantque""" + Programme.compteur + """
                """;
        res += contenu.toMIPS();
        res += "\tj tantque" + Programme.compteur + "\n";
        res += "fintantque" + Programme.compteur + ":\n";
        Programme.compteur++;
        return res;
    }
}
