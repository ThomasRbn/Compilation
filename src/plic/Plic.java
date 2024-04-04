package plic;

import plic.analyse.AnalyseurSyntaxique;
import plic.exceptions.DeclarManquanteException;
import plic.exceptions.DoubleDeclarationException;
import plic.exceptions.NotPlicFileException;
import plic.exceptions.SyntaxiqueException;
import plic.repint.primaire.Programme;

import java.io.File;
import java.io.FileNotFoundException;

public class Plic {

    public Plic(String nomFichier) throws FileNotFoundException, NotPlicFileException, SyntaxiqueException, DoubleDeclarationException, DeclarManquanteException {
        if (!nomFichier.endsWith(".plic")) {
            throw new NotPlicFileException("ERREUR: Suffixe incorrect");
        }

        File file = new File(nomFichier);
        AnalyseurSyntaxique analyseurSyntaxique = new AnalyseurSyntaxique(file);
        Programme programme = analyseurSyntaxique.analyse();
        programme.verifier();
        System.out.println(programme.getBloc());
//        System.out.println(bloc);
//        System.out.println(TDS.getInstance());

        System.out.println(programme.toMIPS());
    }

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                throw new IllegalArgumentException("ERREUR: Fichier source absent");
            }
            if (args.length > 1) {
                throw new IllegalArgumentException("ERREUR: Trop d'arguments");
            }
            new Plic(args[0]);
        } catch (NotPlicFileException | IllegalArgumentException | SyntaxiqueException | DoubleDeclarationException |
                 DeclarManquanteException e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("ERREUR: Fichier introuvable");
//            e.printStackTrace();
        }
    }
}
