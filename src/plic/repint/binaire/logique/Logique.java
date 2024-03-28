package plic.repint.binaire.logique;

import plic.exceptions.DeclarManquanteException;
import plic.repint.Expression;
import plic.repint.binaire.Binaire;

public class Logique extends Binaire {
    public Logique(Expression gauche, Expression droite) {
        super(gauche, droite);
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        gauche.verifier();
        droite.verifier();

        if (!gauche.getType().equals("boolean") || !droite.getType().equals("boolean")) {
            throw new DeclarManquanteException("ERREUR: Les opérandes de l'opération logique doivent être de type boolean");
        }
    }

    @Override
    public String toString() {
        return gauche + " " + this.getClass().getSimpleName() + " " + droite;
    }
}
