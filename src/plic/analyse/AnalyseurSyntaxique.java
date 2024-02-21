package plic.analyse;

import plic.exceptions.DoubleDeclaration;
import plic.exceptions.SyntaxiqueException;
import plic.repint.Entree;
import plic.repint.Symbole;
import plic.repint.TDS;

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
        this.analyseurLexical = new AnalyseurLexical(fichier);

        motsCles = new ArrayList<>() {{
            add("programme");
            add("EOF");
        }};

        motsDeclaration = new ArrayList<>() {{
            add("entier");
        }};

        motsES = new ArrayList<>() {{
            add("ecrire");
        }};

        motsCles.addAll(motsES);
        motsCles.addAll(motsDeclaration);
    }

    /**
     * Traite l'analyse syntaxique du programme
     */
    public void analyse() throws SyntaxiqueException, DoubleDeclaration {
        this.uniteCourante = this.analyseurLexical.next();
        this.analyseProg();
        this.analyseTerminal("EOF");
    }

    /**
     * Traite l'analyse syntaxique du programme
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseProg() throws SyntaxiqueException, DoubleDeclaration {
        this.analyseTerminal("programme"); // Mot clé programme

        if (!this.estIdf()) // Identifiant
            throw new SyntaxiqueException("ERREUR: idf attendu mais " + this.uniteCourante + " trouvé");
        this.uniteCourante = this.analyseurLexical.next();

        this.analyseBloc(); // Bloc
    }

    /**
     * Traite l'analyse syntaxique du bloc
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseBloc() throws SyntaxiqueException, DoubleDeclaration {
        this.analyseTerminal("{");

        while (this.estDeclaration()) { // Déclarations*
            this.analyseDeclaration();
        }

        this.analyseInstruction(); // Instruction+
        while (this.estInstruction()) {
            this.analyseInstruction();
        }
        this.analyseTerminal("}");
    }

    /**
     * Traite l'analyse syntaxique d'une déclaration
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseDeclaration() throws SyntaxiqueException, DoubleDeclaration {
        String type = this.uniteCourante;
        this.analyseType();

        if (!this.estIdf()) // Identifiant
            throw new SyntaxiqueException("ERREUR: idf attendu mais " + this.uniteCourante + " trouvé");
        TDS.getInstance().ajouter(new Entree(this.uniteCourante), new Symbole(type, TDS.getInstance().getCplDecl()));
        this.uniteCourante = this.analyseurLexical.next();

        this.analyseTerminal(";");
    }

    /**
     * Traite l'analyse syntaxique du type
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseType() throws SyntaxiqueException {
        if (!this.motsDeclaration.contains(this.uniteCourante)) // Mot clé de déclaration
            throw new SyntaxiqueException("ERREUR: mot clé de déclaration attendu mais " + this.uniteCourante + " trouvé");
        this.uniteCourante = this.analyseurLexical.next();
    }

    /**
     * Traite l'analyse syntaxique d'une instruction
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseInstruction() throws SyntaxiqueException {
        if (this.estAffectation()) { // Affectation
            this.analyseAffectation();
            return;
        } else if (this.estES()) { // ES
            this.analyseES();
            return;
        }
        throw new SyntaxiqueException("ERREUR: instruction attendue mais " + this.uniteCourante + " trouvé");
    }

    /**
     * Traite l'analyse syntaxique de l'instruction ES
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseES() throws SyntaxiqueException {
        if (!this.motsES.contains(this.uniteCourante))
            throw new SyntaxiqueException("ERREUR: mot clé ES attendu mais " + this.uniteCourante + " trouvé");
        this.uniteCourante = this.analyseurLexical.next();

        if (!this.estIdf()) // Identifiant
            throw new SyntaxiqueException("ERREUR: idf attendu mais " + this.uniteCourante + " trouvé");
        this.uniteCourante = this.analyseurLexical.next();

        this.analyseTerminal(";");
    }

    /**
     * Traite l'analyse syntaxique de l'affectation
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseAffectation() throws SyntaxiqueException {

        this.analyseAcces();

        this.analyseTerminal(":="); // Affectation

        this.analyseExpression();

        this.analyseTerminal(";");
    }

    /**
     * Traite l'analyse syntaxique de l'accès
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseAcces() throws SyntaxiqueException {
        if (!this.estIdf()) // Identifiant
            throw new SyntaxiqueException("ERREUR: idf attendu mais " + this.uniteCourante + " trouvé");
        this.uniteCourante = this.analyseurLexical.next();
    }

    /**
     * Traite l'analyse syntaxique d'une expression
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseExpression() throws SyntaxiqueException {
        this.analyseOperande();
    }

    /**
     * Traite l'analyse syntaxique d'un opérande
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseOperande() throws SyntaxiqueException {
        if (!this.estIdf() && !this.uniteCourante.matches("[0-9]+")) // Identifiant ou entier
            throw new SyntaxiqueException("ERREUR: idf ou entier attendu mais " + this.uniteCourante + " trouvé");
        this.uniteCourante = this.analyseurLexical.next();
    }

    /**
     * Vérifie si l'unite courante est un terminal
     *
     * @param terminal le terminal attendu
     * @throws SyntaxiqueException si l'unite courante n'est pas le terminal attendu
     */
    private void analyseTerminal(String terminal) throws SyntaxiqueException {
        if (!this.uniteCourante.equals(terminal))
            throw new SyntaxiqueException("ERREUR: " + terminal + " attendu mais " + this.uniteCourante + " trouvé");
        this.uniteCourante = this.analyseurLexical.next();
    }

    /**
     * Vérifie si l'unite courante est un identifiant composé uniquement de lettres
     *
     * @return true si l'unite courante est un identifiant
     */
    private boolean estIdf() {
        return this.uniteCourante.matches("[a-zA-Z]+") && !this.motsCles.contains(this.uniteCourante);
    }

    /**
     * Vérifie si l'unite courante est une déclaration
     *
     * @return true si l'unite courante est un mot clé de déclaration
     */
    private boolean estDeclaration() {
        return this.motsDeclaration.contains(this.uniteCourante);
    }

    /**
     * Vérifie si l'unite courante est une instruction
     *
     * @return true si l'unite courante est une affectation ou un ES
     */
    private boolean estInstruction() {
        return this.estAffectation() || this.estES();
    }

    /**
     * Vérifie si l'unite courante est une affectation
     *
     * @return true si l'unite courante est un idf
     */
    private boolean estAffectation() {
        return this.estIdf();
    }

    /**
     * Vérifie si l'unite courante est un mot clé ES
     *
     * @return true si l'unite courante est un mot clé ES
     */
    private boolean estES() {
        return this.motsES.contains(this.uniteCourante);
    }
}
