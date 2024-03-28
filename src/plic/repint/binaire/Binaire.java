package plic.repint.binaire;

import plic.exceptions.DeclarManquanteException;
import plic.repint.Expression;

public abstract class Binaire extends Expression {

    protected Expression gauche;
    protected Expression droite;

    protected Binaire(Expression gauche, Expression droite) {
        this.gauche = gauche;
        this.droite = droite;
    }

    @Override
    public String getType() {
        return "boolean";
    }

    @Override
    public void verifier() throws DeclarManquanteException {
        gauche.verifier();
        droite.verifier();
    }

    @Override
    public String toMIPS() {
        return """
                 \t# Evaluation de l'opérande gauche
                """ + gauche.toMIPS() + """
                                
                \t# Empilement de l'opérande gauche
                """ + empiler() + """
                                
                \t# Evaluation de l'opérande droite
                """ + droite.toMIPS() + """
                                
                \t# Dépilement de l'opérande gauche
                """ + depiler() + "\n";
    }
}
