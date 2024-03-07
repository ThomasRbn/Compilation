package plic.analyse;

import plic.exceptions.DoubleDeclarationException;
import plic.exceptions.SyntaxiqueException;
import plic.repint.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AnalyseurSyntaxique {

    /**
     * Liste des mots clés du langage incluant tous les autres sous-mots clés
     */
    private List<String> motsCles;

    /**
     * Liste des mots clés de déclaration
     */
    private List<String> motsDeclaration;

    /**
     * Liste des mots clés d'entrée/sortie
     */
    private List<String> motsES;

    private AnalyseurLexical analyseurLexical;
    private String uniteCourante;

    public AnalyseurSyntaxique(File fichier) throws FileNotFoundException {
        analyseurLexical = new AnalyseurLexical(fichier);

        motsCles = new ArrayList<>() {{
            add("programme");
            add("EOF");
        }};

        motsDeclaration = new ArrayList<>() {{
            add("entier");
            add("tableau");
        }};

        motsES = new ArrayList<>() {{
            add("ecrire");
        }};

        motsCles.addAll(motsES);
        motsCles.addAll(motsDeclaration);
    }

    /**
     * Traite l'analyse syntaxique du programme
     *
     * @return
     */
    public Bloc analyse() throws SyntaxiqueException, DoubleDeclarationException {
        Bloc bloc = new Bloc();
        uniteCourante = analyseurLexical.next();
        analyseProg(bloc);
        analyseTerminal("EOF");
        return bloc;
    }

    /**
     * Traite l'analyse syntaxique du programme
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseProg(Bloc bloc) throws SyntaxiqueException, DoubleDeclarationException {
        analyseTerminal("programme"); // Mot clé programme

        if (!estIdf()) // Identifiant
            throw new SyntaxiqueException("ERREUR: idf attendu mais " + uniteCourante + " trouvé");
        uniteCourante = analyseurLexical.next();

        analyseBloc(bloc); // Bloc
    }

    /**
     * Traite l'analyse syntaxique du bloc
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseBloc(Bloc bloc) throws SyntaxiqueException, DoubleDeclarationException {
        analyseTerminal("{");

        while (estDeclaration()) { // Déclarations*
            analyseDeclaration();
        }

        bloc.ajouter(analyseInstruction()); // Instruction+
        while (estInstruction()) {
            bloc.ajouter(analyseInstruction());
        }
        analyseTerminal("}");
    }

    /**
     * Traite l'analyse syntaxique d'une déclaration
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseDeclaration() throws SyntaxiqueException, DoubleDeclarationException {
        String type = uniteCourante;
        analyseType();

        if (type.equals("tableau")) {
            analyseTerminal("[");

            if (!uniteCourante.matches("[0-9]+")) // Entier
                throw new SyntaxiqueException("ERREUR: entier attendu mais " + uniteCourante + " trouvé");

            int taille = Integer.parseInt(uniteCourante);

            uniteCourante = analyseurLexical.next();

            analyseTerminal("]");
            System.out.println("uniteCourante : " + uniteCourante + " type : " + type + " taille : " + taille);

            if (!estIdf()) // Identifiant
                throw new SyntaxiqueException("ERREUR: idf attendu mais " + uniteCourante + " trouvé");
            TDS.getInstance().ajouter(new Entree(uniteCourante), new Tableau(type, TDS.getInstance().getCplDecl(), taille));
            uniteCourante = analyseurLexical.next();

        } else {
            if (!estIdf()) // Identifiant
                throw new SyntaxiqueException("ERREUR: idf attendu mais " + uniteCourante + " trouvé");
            TDS.getInstance().ajouter(new Entree(uniteCourante), new Symbole(type, TDS.getInstance().getCplDecl()));
            uniteCourante = analyseurLexical.next();
        }

        analyseTerminal(";");
    }

    /**
     * Traite l'analyse syntaxique du type
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseType() throws SyntaxiqueException {
        if (!motsDeclaration.contains(uniteCourante)) // Mot clé de déclaration
            throw new SyntaxiqueException("ERREUR: mot clé de déclaration attendu mais " + uniteCourante + " trouvé");
        uniteCourante = analyseurLexical.next();
    }

    /**
     * Traite l'analyse syntaxique d'une instruction
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private Instruction analyseInstruction() throws SyntaxiqueException {
        if (estAffectation()) { // Affectation
            return analyseAffectation();
        } else if (estES()) { // ES
            return analyseES();
        }
        throw new SyntaxiqueException("ERREUR: instruction attendue mais " + uniteCourante + " trouvé");
    }

    /**
     * Traite l'analyse syntaxique de l'instruction ES
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private Ecrire analyseES() throws SyntaxiqueException {
        if (!motsES.contains(uniteCourante))
            throw new SyntaxiqueException("ERREUR: mot clé ES attendu mais " + uniteCourante + " trouvé");
        uniteCourante = analyseurLexical.next();

        Expression idf = analyseExpression();

        analyseTerminal(";");

        return new Ecrire(idf);
    }

    /**
     * Traite l'analyse syntaxique de l'affectation
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private Affectation analyseAffectation() throws SyntaxiqueException {

        Acces acces = analyseAcces();

        analyseTerminal(":="); // Affectation

        Expression exp = analyseExpression();

        analyseTerminal(";");

        Affectation affectation = new Affectation(acces, exp);
        System.out.println(affectation);

        return affectation;
    }

    /**
     * Traite l'analyse syntaxique de l'accès
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private Acces analyseAcces() throws SyntaxiqueException {
        if (!estIdf()) // Identifiant
            throw new SyntaxiqueException("ERREUR: idf attendu mais " + uniteCourante + " trouvé");

        if (TDS.getInstance().identifier(new Entree(uniteCourante)).getType().equals("tableau")) {
            AccesTableau aTableau = new AccesTableau(uniteCourante);
            uniteCourante = analyseurLexical.next();
            analyseTerminal("[");

            Expression exp = analyseExpression();
            aTableau.setIndex(exp);

            analyseTerminal("]");
            return aTableau;
        } else {
            Idf idf = new Idf(uniteCourante);
            uniteCourante = analyseurLexical.next();
            return idf;
        }
    }

    /**
     * Traite l'analyse syntaxique d'une expression
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private Expression analyseExpression() throws SyntaxiqueException {
        return analyseOperande();
    }

    /**
     * Traite l'analyse syntaxique d'un opérande
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private Expression analyseOperande() throws SyntaxiqueException {
        if (!estIdf() && !uniteCourante.matches("[0-9]+")) // Identifiant ou entier
            throw new SyntaxiqueException("ERREUR: idf ou entier attendu mais " + uniteCourante + " trouvé");
        try {
            int entier = Integer.parseInt(uniteCourante);
            uniteCourante = analyseurLexical.next();
            return new Nombre(entier);
        } catch (NumberFormatException e) {
            if (TDS.getInstance().identifier(new Entree(uniteCourante)).getType().equals("tableau")) {
                AccesTableau aTableau = new AccesTableau(uniteCourante);
                uniteCourante = analyseurLexical.next();
                analyseTerminal("[");

                Expression exp = analyseExpression();
                aTableau.setIndex(exp);

                analyseTerminal("]");
                return aTableau;
            } else {
                Idf idf = new Idf(uniteCourante);
                uniteCourante = analyseurLexical.next();
                return idf;
            }
        }
    }

    /**
     * Vérifie si l'unite courante est un terminal
     *
     * @param terminal le terminal attendu
     * @throws SyntaxiqueException si l'unite courante n'est pas le terminal attendu
     */
    private void analyseTerminal(String terminal) throws SyntaxiqueException {
        if (!uniteCourante.equals(terminal))
            throw new SyntaxiqueException("ERREUR: " + terminal + " attendu mais " + uniteCourante + " trouvé");
        uniteCourante = analyseurLexical.next();
    }

    /**
     * Vérifie si l'unite courante est un identifiant composé uniquement de lettres
     *
     * @return true si l'unite courante est un identifiant
     */
    private boolean estIdf() {
        return uniteCourante.matches("[a-zA-Z]+") && !motsCles.contains(uniteCourante);
    }

    /**
     * Vérifie si l'unite courante est une déclaration
     *
     * @return true si l'unite courante est un mot clé de déclaration
     */
    private boolean estDeclaration() {
        return motsDeclaration.contains(uniteCourante);
    }

    /**
     * Vérifie si l'unite courante est une instruction
     *
     * @return true si l'unite courante est une affectation ou un ES
     */
    private boolean estInstruction() {
        return estAffectation() || estES();
    }

    /**
     * Vérifie si l'unite courante est une affectation
     *
     * @return true si l'unite courante est un idf
     */
    private boolean estAffectation() {
        return estIdf();
    }

    /**
     * Vérifie si l'unite courante est un mot clé ES
     *
     * @return true si l'unite courante est un mot clé ES
     */
    private boolean estES() {
        return motsES.contains(uniteCourante);
    }
}
