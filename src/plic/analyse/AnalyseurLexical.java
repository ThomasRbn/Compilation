package plic.analyse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AnalyseurLexical {

    private Scanner scanner;

    public AnalyseurLexical(File fichier) throws FileNotFoundException {
        this.scanner = new Scanner(fichier);
    }

    public String next() {
        if (this.scanner.hasNext()) {
            String mot = this.scanner.next();
            if (mot.matches("//.*")) {
                this.scanner.nextLine();
                return next();
            }
            return mot;
        } else {
            return "EOF";
        }
    }

}
