package plic.repint;

public class AccesTableau extends Acces {

    Expression index;

    public AccesTableau(String uniteCourante) {
        super(uniteCourante, "tableau");
    }

    public void setIndex(Expression index) {
        this.index = index;
    }

    public String toString() {
        return nom + "[" + index + "]";
    }

    public String toMIPS() {
        //Calcul de l'adresse de la variable dans $a0
        //Calcul de l'adresse de la variable dans $v0
        //$a0 <- $a0 + $v0
        return "\t# Accès à la case " + index + " du tableau " + nom + "\n"
                + index.toMIPS() +
                "\tmove $a0, $v0\n" +
                "\tli $v0, " + TDS.getInstance().identifier(new Entree(nom)).getDeplacement() + "\n" +
                "\tadd $a0, $a0, $v0\n" +
                "\tmove $v0, $a0\n\n";
    }

}
