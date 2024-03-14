package plic.repint;

public class SymboleTableau extends SymboleEntier {

    private int taille;

    public SymboleTableau(String type, int deplacement, int taille) {
        super(type, deplacement);
        this.taille = taille;
    }

    public SymboleEntier setDeplacement(int deplacement) {
        this.deplacement = deplacement;
        return this;
    }

    @Override
    public int nouveauDep(int deplacement) {
        deplacement -= 4 * taille;
        return deplacement;
    }

    public int getTaille() {
        return taille;
    }

    @Override
    public String toString() {
        return "Tableau : (" + getType() + ") (" + getDeplacement() + ") (" + taille + ")";
    }
}
