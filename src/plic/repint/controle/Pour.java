package plic.repint.controle;

import plic.exceptions.DeclarManquanteException;
import plic.repint.Affectation;
import plic.repint.Entree;
import plic.repint.Expression;
import plic.repint.Idf;
import plic.repint.primaire.Bloc;
import plic.repint.primaire.Programme;
import plic.repint.primaire.TDS;

import static plic.repint.Expression.depiler;
import static plic.repint.Expression.empiler;

public class Pour extends Boucle {

    private Idf idf;
    private Expression gauche;
    private Expression droite;

    public Pour(Bloc contenu, Idf idf, Expression gauche, Expression droite) {
        super(contenu);
        this.idf = idf;
        this.gauche = gauche;
        this.droite = droite;
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        gauche.verifier();
        droite.verifier();
        contenu.verifier();
        if (!gauche.getType().equals("entier") || !droite.getType().equals("entier")) {
            throw new DeclarManquanteException("ERREUR: les bornes de la boucle pour doivent être de type entier");
        }
    }

    @Override
    public String toMIPS() {
        String res = "";
        Affectation a = new Affectation(idf, gauche);
        res += a.toMIPS();
        res += "# Borne supérieure\n";
        res += droite.toMIPS();
        res += empiler();
        res += "pour" + Programme.compteur + ":\n";
        res += idf.toMIPS();
        res += depiler();
        res += "\t# Comparaison de " + idf + " et " + droite + "\n";
        res += "\tbgt $v0, $v1, finpour" + Programme.compteur + "\n";
        res += "\t# Empilage\n";
        res += "\tmove $t1, $v0\n";
        res += "\tmove $v0, $v1\n";
        res += empiler();
        res += "\tmove $v0, $t1\n";
        res += contenu.toMIPS();
        res += idf.toMIPS();
        res += "\taddi $v0, $v0, 1\n";
        res += "\tsw $v0, " + TDS.getInstance().identifier(new Entree(idf.getNom())).getDeplacement() + "($s7)\n";
        res += "\tj pour" + Programme.compteur + "\n";
        res += "\tfinpour" + Programme.compteur + ":\n";
        Programme.compteur++;
        return res;
    }

    public Idf getIdf() {
        return idf;
    }

    public Pour setIdf(Idf idf) {
        this.idf = idf;
        return this;
    }

    public Expression getGauche() {
        return gauche;
    }

    public Pour setGauche(Expression gauche) {
        this.gauche = gauche;
        return this;
    }

    public Expression getDroite() {
        return droite;
    }

    public Pour setDroite(Expression droite) {
        this.droite = droite;
        return this;
    }
}
