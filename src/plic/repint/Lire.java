package plic.repint;

import plic.exceptions.DeclarManquanteException;
import plic.repint.primaire.Instruction;
import plic.repint.primaire.TDS;

public class Lire extends Instruction {

    private Acces idf;

    public Lire(Acces idf) {
        this.idf = idf;
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        idf.verifier();
//        if (!idf.getType().equals("entier")) {
//            throw new DeclarManquanteException("ERREUR: Lire doit être suivi d'une variable de type entier");
//        }
    }

    @Override
    public String toMIPS() {
        return """

                # Lire
                \tli $v0, 5
                \tsyscall
                \tsw $v0,\s""" + TDS.getInstance().identifier(new Entree(idf.getNom())).getDeplacement() + "($s7)";
    }
}
