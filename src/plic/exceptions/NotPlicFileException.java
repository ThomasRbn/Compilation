package plic.exceptions;

public class NotPlicFileException extends Exception {

    public NotPlicFileException(String message) {
        super(message);
    }

    public NotPlicFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
