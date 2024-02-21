package plic.exceptions;

public class DoubleDeclarationException extends Exception {
    public DoubleDeclarationException(String message) {
        super(message);
    }

    public DoubleDeclarationException() {
        super();
    }
}
