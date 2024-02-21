package plic;

import plic.analyse.AnalyseurSyntaxique;
import plic.exceptions.DoubleDeclaration;
import plic.exceptions.NotPlicFileException;
import plic.exceptions.SyntaxiqueException;
import plic.repint.Bloc;
import plic.repint.TDS;

import java.io.File;
import java.io.FileNotFoundException;

public class Plic {

    public Plic(String nomFichier) throws FileNotFoundException, NotPlicFileException, SyntaxiqueException, DoubleDeclaration {
        if (!nomFichier.endsWith(".plic")) {
            throw new NotPlicFileException("ERREUR: Suffixe incorrect");
        }

        File file = new File(nomFichier);
        AnalyseurSyntaxique analyseurSyntaxique = new AnalyseurSyntaxique(file);
        Bloc bloc = analyseurSyntaxique.analyse();
        System.out.println(bloc);
        System.out.println(TDS.getInstance());
    }

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                throw new IllegalArgumentException("ERREUR: Fichier source absent");
            }
            new Plic(args[0]);
        } catch (NotPlicFileException | IllegalArgumentException | SyntaxiqueException | DoubleDeclaration e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
        } catch (FileNotFoundException e){
            System.out.println("ERREUR: Fichier introuvable");
//            e.printStackTrace();
        }
    }
}
