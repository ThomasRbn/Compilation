package plic.repint;

public class SymboleEntier {

    protected String type;
    protected int deplacement;

    public SymboleEntier(String type, int deplacement) {
        this.type = type;
        this.deplacement = deplacement;
    }

    public String getType() {
        return type;
    }

    public int getDeplacement() {
        return deplacement;
    }

    public SymboleEntier setDeplacement(int deplacement) {
        this.deplacement = deplacement;
        return this;
    }

    public int nouveauDep(int deplacement) {
        deplacement -= 4;
        return deplacement;
    }

    @Override
    public String toString() {
        return "Symbole : (" + type + ") (" + deplacement + ")";
    }
}
