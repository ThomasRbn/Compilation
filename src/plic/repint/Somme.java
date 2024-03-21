package plic.repint;

public class Somme extends Binaire {
    public Somme(Expression gauche, Expression droite) {
        super(gauche, droite);
    }

    @Override
    public String getType() {
        return "entier";
    }

    @Override
    public String toMIPS() {
        return """
                \t# Somme
                \t# Evaluation de l'opérande gauche
                """ + gauche.toMIPS() + """
                \t# Empilement de l'opérande gauche
                """ + empiler() + """
                \t# Evaluation de l'opérande droite
                """ + droite.toMIPS() + """
                \t# Dépilement de l'opérande gauche
                """ + depiler() + """
                \t# Addition
                \tadd $v0, $v1, $v0
                """;
    }

    @Override
    public String toString() {
        return "(" + gauche + " + " + droite + ")";
    }

}
