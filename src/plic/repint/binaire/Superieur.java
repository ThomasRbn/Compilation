package plic.repint.binaire;

import plic.repint.Expression;

public class Superieur extends Binaire{

        public Superieur(Expression exp1, Expression exp2) {
            super(exp1, exp2);
        }

        @Override
        public String toMIPS() {
            return """
                    \t# SUP
                    """ + super.toMIPS() + """
                    \tsgt $v0, $v1, $v0
                    """;
        }

        @Override
        public String getType() {
            return "boolean";
        }

        @Override
        public String toString() {
            return gauche + ">" + droite;
        }

}