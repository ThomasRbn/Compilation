package plic.repint;

public class Tableau extends Symbole{

        private int taille;

        public Tableau(String type, int deplacement, int taille) {
            super(type, deplacement);
            this.taille = taille;
        }

        public int getTaille() {
            return taille;
        }

        @Override
        public String toString() {
            return "Tableau : (" + getType() + ") (" + getDeplacement() + ") (" + taille + ")";
        }
}
