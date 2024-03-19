package plic.repint;

import plic.exceptions.DeclarManquanteException;

public class AccesTableau extends Acces {

    Expression index;

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

    public String toMIPS() {
        //Calcul de l'adresse de la variable dans $a0
        //Calcul de l'adresse de la variable dans $v0
        //$a0 <- $a0 + $v0
        return "\t# Accès à la case " + index + " du tableau " + nom + "\n" +
                "\tli $a0, " + (TDS.getInstance().identifier(new Entree(this.getNom())).getDeplacement() - Integer.parseInt(index.toString()) * 4) + "\n" +
                "\tsub $a0, $s7, $a0\n" +
                "\tsw $v0, 0($a0)\n\n";

    }

}
