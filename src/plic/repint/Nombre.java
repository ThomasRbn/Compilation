package plic.repint;

public class Nombre extends Expression {

    private int val;

    public Nombre(int val) {
        this.val = val;
    }

    public String toString() {
        return "" + val;
    }

    @Override
    public String getType() {
        return "entier";
    }

    public void verifier() {
    }

    public String toMIPS() {
        return "\tli $v0, " + val + "\n\n";
    }

}
