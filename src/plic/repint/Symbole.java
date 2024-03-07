package plic.repint;

public class Symbole {

    protected String type;
    protected int deplacement;

    public Symbole(String type, int deplacement) {
        this.type = type;
        this.deplacement = deplacement;
    }

    public String getType() {
        return type;
    }

    public int getDeplacement() {
        return deplacement;
    }

    public Symbole setDeplacement(int deplacement) {
        this.deplacement = deplacement;
        return this;
    }

    @Override
    public String toString() {
        return "Symbole : (" + type + ") (" + deplacement + ")";
    }
}
