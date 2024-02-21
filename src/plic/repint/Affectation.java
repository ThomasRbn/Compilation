package plic.repint;

public class Affectation extends Instruction {

    private Idf idf;
    private Expression expression;

    public Affectation(Idf idf, Expression expression) {
        this.idf = idf;
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public String toString() {
        return idf + " := " + expression + " ;";
    }

}
