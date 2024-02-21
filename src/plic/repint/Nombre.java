package plic.repint;

public class Nombre extends Expression {

    private int val;

    public Nombre(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public String toString() {
        return "" + val;
    }

    public void verifier() {
    }

}
