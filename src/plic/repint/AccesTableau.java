package plic.repint;

import plic.exceptions.DeclarManquanteException;
import plic.repint.primaire.TDS;

public class AccesTableau extends Acces {

    private Expression index;

    public AccesTableau(String uniteCourante) {
        super(uniteCourante, "tableau");
    }

    public void setIndex(Expression index) {
        this.index = index;
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        super.verifier();
        index.verifier();
    }

    public String toString() {
        return nom + "[" + index + "]";
    }

    public Expression getIndex() {
        return index;
    }

    public String toMIPS() {
        //Calcul de l'adresse de la variable dans $a0
        //Calcul de l'adresse de la variable dans $v0
        //$a0 <- $a0 + $v0
        return "\t# Accès à la case " + index + " du tableau " + nom + "\n" +
                "\t# Evaluation de l'index\n" +
                index.toMIPS() +
                "\t# Empilement de l'index\n" +
                empiler() +
                "\t# Chargement de l'adresse du tableau " + nom + " dans $a0\n" +
                "\tla $a0, " + TDS.getInstance().identifier(new Entree(this.getNom())).getDeplacement() + "($s7)\n" +
                "\t# Dépilement de l'index\n" +
                depiler() +
                "\t# Calcul de l'adresse de la case " + index + "\n" +
                "\tmul $v0, $v0, 4\n" +
                "\tadd $a0, $a0, $v0\n" +
                "\t# Chargement de la valeur de la case " + index + " dans $v0\n" +
                "\tlw $v0, 0($a0)\n";
    }

}
