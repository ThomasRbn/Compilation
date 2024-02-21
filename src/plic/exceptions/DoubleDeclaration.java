package plic.exceptions;

public class DoubleDeclaration extends Exception {
    public DoubleDeclaration(String message) {
        super(message);
    }

    public DoubleDeclaration() {
        super();
    }
}
