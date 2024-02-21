package plic.exceptions;

public class SyntaxiqueException extends Exception {
    public SyntaxiqueException(String message) {
        super(message);
    }

    public SyntaxiqueException(String message, Throwable cause) {
        super(message, cause);
    }
}
