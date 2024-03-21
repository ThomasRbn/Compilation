package plic.repint;

public class Soustraction extends Binaire {
    public Soustraction(Expression gauche, Expression droite) {
        super(gauche, droite);
    }

    @Override
    public String toMIPS() {
        return """
                \t# Soustraction
                \t# Evaluation de l'opérande gauche
                """ + gauche.toMIPS() + """
                \t# Empilement de l'opérande gauche
                """ + empiler() + """
                \t# Evaluation de l'opérande droite
                """ + droite.toMIPS() + """
                \t# Dépilement de l'opérande gauche
                """ + depiler() + """
                \t# Soustraction
                \tsub $v0, $v1, $v0
                """;
    }

    @Override
    public String toString() {
        return "(" + gauche + " - " + droite + ")";
    }

}
