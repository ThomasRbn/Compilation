package plic.repint;

public class Et extends Binaire{

        public Et(Expression exp1, Expression exp2) {
            super(exp1, exp2);
        }

    @Override
    public String toMIPS() {
        return """
                \t# ET
                \t# Evaluation de l'opérande gauche
                """ + gauche.toMIPS() + """
                \t# Empilement de l'opérande gauche
                """ + empiler() + """
                \t# Evaluation de l'opérande droite
                """ + droite.toMIPS() + """
                \t# Dépilement de l'opérande gauche
                """ + depiler() + """
                \t# ET
                \tand $v0, $v1, $v0
                """;
    }

        @Override
        public String getType() {
            return "boolean";
        }

        @Override
        public String toString() {
            return gauche + " && " + droite;
        }
}