package plic.repint;

import plic.exceptions.DeclarManquanteException;
import plic.repint.primaire.Instruction;
import plic.repint.primaire.TDS;

import java.util.ArrayList;
import java.util.List;

public class Affectation extends Instruction {

    private Acces acces;
    private Expression expression;

    public Affectation(Acces idf, Expression expression) {
        this.acces = idf;
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public String toString() {
        return acces + " := " + expression + " ;";
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        acces.verifier();
        expression.verifier();
    }

    @Override
    public String toMIPS() {
        String mips = "\n# Affectation de " + expression + " à " + acces + "\n";
        if (acces.getType().equals("tableau")) {
            AccesTableau accesTableau = (AccesTableau) acces;
            List<Expression> expressions = new ArrayList<>();
            expressions.add(accesTableau);
            expressions.add(accesTableau.getIndex());

//            while (accesTableau.getIndex().getType().equals("tableau")) {
//                expressions.add(accesTableau.getIndex());
//                mips += "\t# Empilement de l'index\n" +
//                        empiler() +
//                        "\t# Evaluation de l'index\n" +
//                        accesTableau.getIndex().toMIPS() +
//                        "\t# Chargement de l'adresse du tableau " + accesTableau.getNom() + " dans $a0\n" +
//                        "\tla $a0, " + TDS.getInstance().identifier(new Entree(accesTableau.getNom())).getDeplacement() + "($s7)\n" +
//                        "\t# Dépilement de l'index\n" +
//                        depiler() +
//                        "\t# Calcul de l'adresse de la case " + accesTableau.getIndex() + "\n" +
//                        "\tmul $v0, $v0, 4\n" +
//                        "\tadd $a0, $a0, $v0\n";
//                accesTableau = (AccesTableau) accesTableau.getIndex();
//            }
            mips += "\n\t# Chargement de la nouvelle valeur " + accesTableau.getIndex() + " dans $v0\n" +
                    expression.toMIPS() +
                    "\t# Sauvegarde de " + expression + " dans " + accesTableau.getNom() + "[" + accesTableau.getIndex() + "]\n" +
                    "\tsw $v0, " + (TDS.getInstance().identifier(new Entree(acces.getNom())).getDeplacement() - Integer.parseInt(expressions.getLast().toString()) * 4) + "($s7)\n";
        } else {
            mips += expression.toMIPS();
            mips += "\tsw $v0, " + TDS.getInstance().identifier(new Entree(acces.getNom())).getDeplacement() + "($s7)\n";
        }
        return mips;
    }
}
