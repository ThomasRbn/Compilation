package plic.repint;

import plic.exceptions.DoubleDeclarationException;

import java.util.Map;

public class TDS {

    private static TDS instance;
    private int cplDecl;
    private Map<Entree, SymboleEntier> table;

    private TDS() {
        cplDecl = 0;
        table = new java.util.HashMap<>();
    }

    public static TDS getInstance() {
        if (instance == null) {
            instance = new TDS();
        }
        return instance;
    }

    public static void reset() {
        instance = new TDS();
    }

    public void ajouter(Entree e, SymboleEntier s) throws DoubleDeclarationException {
        if (table.containsKey(e))
            throw new DoubleDeclarationException("ERREUR: identifiant " + e.getIdf() + " déjà déclaré");

        s.setDeplacement(cplDecl);
        table.put(e, s);
        cplDecl = s.nouveauDep(cplDecl);
//        System.out.println(e + " => " + s);
    }

    public int getCplDecl() {
        return cplDecl;
    }

    public SymboleEntier identifier(Entree e) {
        return table.get(e);
    }

    public String toMIPS() {
        return """
                \tmove $s7, $sp
                \tadd $sp, $sp, -""" + -cplDecl + """
                
                
                """;
    }

    @Override
    public String toString() {
        String res = "TDS {\n";
        for (Map.Entry<Entree, SymboleEntier> e : table.entrySet()) {
            res += e.getKey() + " => " + e.getValue() + "\n";
        }
        res += "}";
        return res;
    }
}
