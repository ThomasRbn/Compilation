package plic.analyse;

import plic.exceptions.DoubleDeclarationException;
import plic.exceptions.SyntaxiqueException;
import plic.repint.*;
import plic.repint.binaire.*;
import plic.repint.binaire.logique.Et;
import plic.repint.binaire.logique.Ou;
import plic.repint.controle.Si;
import plic.repint.primaire.*;
import plic.repint.unaire.Negatif;
import plic.repint.unaire.Non;

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

    private List<String> operel;
    private List<String> opad;
    private List<String> opmul;

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

        operel = new ArrayList<>() {{
            add(">");
            add("<");
            add(">=");
            add("<=");
            add("=");
            add("#");
        }};

        opad = new ArrayList<>() {{
            add("+");
            add("-");
            add("ou");
        }};

        opmul = new ArrayList<>() {{
            add("*");
//            add("/");
            add("et");
        }};

        motsCles.addAll(motsES);
        motsCles.addAll(motsDeclaration);
    }

    /**
     * Traite l'analyse syntaxique du programme
     *
     * @return
     */
    public Programme analyse() throws SyntaxiqueException, DoubleDeclarationException {
        Programme prog = new Programme();
        uniteCourante = analyseurLexical.next();
        analyseProg(prog);
        analyseTerminal("EOF");
        return prog;
    }

    /**
     * Traite l'analyse syntaxique du programme
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private void analyseProg(Programme programme) throws SyntaxiqueException, DoubleDeclarationException {
        analyseTerminal("programme"); // Mot clé programme

        if (!estIdf()) // Identifiant
            throw new SyntaxiqueException("ERREUR: idf attendu mais " + uniteCourante + " trouvé");
        uniteCourante = analyseurLexical.next();

        Bloc bloc = new Bloc();
        programme.setBloc(bloc);

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

            if (!estIdf()) // Identifiant
                throw new SyntaxiqueException("ERREUR: idf attendu mais " + uniteCourante + " trouvé");
            TDS.getInstance().ajouter(new Entree(uniteCourante), new SymboleTableau(type, TDS.getInstance().getCplDecl(), taille));
            uniteCourante = analyseurLexical.next();

        } else {
            if (!estIdf()) // Identifiant
                throw new SyntaxiqueException("ERREUR: idf attendu mais " + uniteCourante + " trouvé");
            TDS.getInstance().ajouter(new Entree(uniteCourante), new SymboleEntier(type, TDS.getInstance().getCplDecl()));
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
    private Instruction analyseInstruction() throws SyntaxiqueException, DoubleDeclarationException {
        if (estSi()) {
            return analyseSi();
        } else if (estAffectation()) { // Affectation
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

        return new Affectation(acces, exp);
    }

    /**
     * Traite l'analyse syntaxique de l'accès
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private Acces analyseAcces() throws SyntaxiqueException {
        if (!estIdf()) // Identifiant
            throw new SyntaxiqueException("ERREUR: idf attendu mais " + uniteCourante + " trouvé");

        String nom = uniteCourante;
        uniteCourante = analyseurLexical.next();

        if (uniteCourante.equals("[")) {
            uniteCourante = analyseurLexical.next();
            AccesTableau aTableau = new AccesTableau(nom);
            Expression exp = analyseExpression();
            aTableau.setIndex(exp);
            analyseTerminal("]");
            return aTableau;
        } else {
            return new Idf(nom);
        }
    }

    /**
     * Traite l'analyse syntaxique d'une expression
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private Expression analyseExpression() throws SyntaxiqueException {
        Expression g = analyseTerme();
        Expression e = analyseSuiteExp(g);
        return e == null ? g : e;
    }

    private Expression analyseTerme() throws SyntaxiqueException {
        Expression facteur = analyseFacteur();
        Expression suite = analyseSuiTerme(facteur);
        return suite == null ? facteur : suite;
    }

    private Expression analyseFacteur() throws SyntaxiqueException {
        Expression operande = analyseOperande();
        if (operel.contains(uniteCourante)) {
            Binaire res = analyseOprel();
            Expression droite = analyseExpression();
            res.setGauche(operande);
            res.setDroite(droite);
            return res;
        }
        return operande;
    }

    private Binaire analyseOpad() throws SyntaxiqueException {
        if (opad.contains(uniteCourante)) {
            String op = uniteCourante;
            uniteCourante = analyseurLexical.next();
            return switch (op) {
                case "+" -> new Somme(null, null);
                case "-" -> new Soustraction(null, null);
                case "ou" -> new Ou(null, null);
                default -> throw new SyntaxiqueException("ERREUR: opérateur attendu mais " + uniteCourante + " trouvé");
            };
        }
        return null;
    }

    private Binaire analyseOprel() throws SyntaxiqueException {
        if (operel.contains(uniteCourante)) {
            String op = uniteCourante;
            uniteCourante = analyseurLexical.next();
            return switch (op) {
                case ">" -> new Superieur(null, null);
                case "<" -> new Inferieur(null, null);
                case ">=" -> new SuperieurEgal(null, null);
                case "<=" -> new InferieurEgal(null, null);
                case "=" -> new Egal(null, null);
                case "#" -> new Different(null, null);
                default -> throw new SyntaxiqueException("ERREUR: opérateur attendu mais " + uniteCourante + " trouvé");
            };
        }
        return null;
    }

    private Binaire analyseOpMul() throws SyntaxiqueException {
        if (opmul.contains(uniteCourante)) {
            String op = uniteCourante;
            uniteCourante = analyseurLexical.next();
            return switch (op) {
                case "*" -> new Produit(null, null);
                case "et" -> new Et(null, null);
                default -> throw new SyntaxiqueException("ERREUR: opérateur attendu mais " + uniteCourante + " trouvé");
            };
        }
        return null;
    }


    private Expression analyseSuiTerme(Expression g) throws SyntaxiqueException {
        Binaire res = analyseOpMul();
        if (res == null) return null;
        Expression d = analyseFacteur();
        res.setGauche(g);
        res.setDroite(d);
        Expression e = analyseSuiTerme(res);
        return e == null ? res : e;
    }

    Expression analyseSuiteExp(Expression gauche) throws SyntaxiqueException {
        Binaire res = analyseOpad();
        if (res == null) return null;
        Expression droit = analyseTerme();
        res.setGauche(gauche);
        res.setDroite(droit);
        Expression e = analyseSuiteExp(res);
        return e == null ? res : e;
    }

    /**
     * Traite l'analyse syntaxique d'un opérande
     *
     * @throws SyntaxiqueException si l'analyse syntaxique échoue
     */
    private Expression analyseOperande() throws SyntaxiqueException {
        if (!estIdf() && !uniteCourante.matches("[0-9]+") && !uniteCourante.equals("(") && !uniteCourante.equals("-")) // Identifiant ou entier
            throw new SyntaxiqueException("ERREUR: idf ou entier attendu mais " + uniteCourante + " trouvé");

        try {
            int entier = Integer.parseInt(uniteCourante);
            uniteCourante = analyseurLexical.next();
            return new Nombre(entier);
        } catch (NumberFormatException e) {

            if (uniteCourante.equals("non") || uniteCourante.equals("-")) {
                if (uniteCourante.equals("non")) {
                    uniteCourante = analyseurLexical.next();
                    Expression exp = analyseExpression();
                    return new Non(exp);
                } else {
                    uniteCourante = analyseurLexical.next();
                    Expression exp = analyseExpression();
                    return new Negatif(exp);
                }
            }

            //gestion des parentheses
            if (uniteCourante.equals("(")) {
                uniteCourante = analyseurLexical.next();
                Expression exp = analyseExpression();
                analyseTerminal(")");
                return exp;
            }

            String nom = uniteCourante;
            uniteCourante = analyseurLexical.next();

            if (uniteCourante.equals("[")) {
                uniteCourante = analyseurLexical.next();
                AccesTableau aTableau = new AccesTableau(nom);
                Expression exp = analyseExpression();
                aTableau.setIndex(exp);
                analyseTerminal("]");
                return aTableau;
            } else {
                return new Idf(nom);
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

    private Si analyseSi() throws SyntaxiqueException, DoubleDeclarationException {
        analyseTerminal("si");
        analyseTerminal("(");
        Expression condition = analyseExpression();
        analyseTerminal(")");
        analyseTerminal("alors");
        Bloc alors = new Bloc();
        analyseBloc(alors);
        Bloc sinon = null;
        if (uniteCourante.equals("sinon")) {
            uniteCourante = analyseurLexical.next();
            sinon = new Bloc();
            analyseBloc(sinon);
        }
        return new Si(condition, alors, sinon);
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

    private boolean estSi() {
        return uniteCourante.equals("si");
    }
}
