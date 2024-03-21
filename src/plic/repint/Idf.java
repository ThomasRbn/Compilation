package plic.repint;

public class Idf extends Acces {

    public Idf(String nom) {
        super(nom, "entier");
    }

    public String toString() {
        return nom;
    }

    @Override
    public String toMIPS() {
        //Retrouver la variable
        return "\t# Accès à la variable " + nom + "\n" +
                "\tlw $v0, " + TDS.getInstance().identifier(new Entree(nom)).getDeplacement() + "($s7)\n";
    }
}
