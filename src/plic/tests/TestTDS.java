package plic.tests;

//JUNIT
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plic.exceptions.DoubleDeclarationException;
import plic.repint.Entree;
import plic.repint.Symbole;
import plic.repint.TDS;

import static org.junit.jupiter.api.Assertions.*;


public class TestTDS {

    @BeforeEach
    public void setUp() {
        TDS.reset();
    }

    @Test
    public void testAjouterOK() throws DoubleDeclarationException {
        TDS tds = TDS.getInstance();
        tds.ajouter(new Entree("test"), new Symbole("entier", 0));
        assertEquals(-4, tds.getCplDecl());
    }

    @Test
    public void testAjouterKO() throws DoubleDeclarationException {
        TDS tds = TDS.getInstance();
        tds.ajouter(new Entree("test"), new Symbole("entier", 0));
        assertThrows(DoubleDeclarationException.class, () -> tds.ajouter(new Entree("test"), new Symbole("entier", 0)));
    }
}
