package plic.repint;

import plic.exceptions.DeclarManquanteException;

public abstract class Expression {

    public abstract String getType();

    public abstract void verifier() throws DeclarManquanteException;

    public static String empiler() {
        return """
                \tsw $v0, 0($sp)
                \taddi $sp, $sp, -4
                """;
    }

    public static String depiler() {
        return """
                \taddi $sp, $sp, 4
                \tlw $v1, 0($sp)
                """;
    }

    public abstract String toMIPS();
}
