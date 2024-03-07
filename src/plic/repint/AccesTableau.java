package plic.repint;

public class AccesTableau extends Acces{

    Expression index;

    public AccesTableau(String uniteCourante) {
        super(uniteCourante, "tableau");
    }

    public void setIndex(Expression index) {
        this.index = index;
    }

    public String toString() {
        return nom + "[" + index + "]";
    }

}
