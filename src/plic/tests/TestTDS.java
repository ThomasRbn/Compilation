package plic.tests;

//JUNIT
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plic.exceptions.DoubleDeclaration;
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
    public void testAjouterOK() throws DoubleDeclaration {
        TDS tds = TDS.getInstance();
        tds.ajouter(new Entree("test"), new Symbole("int", 0));
        assertEquals(1, tds.getCplDecl());
    }

    @Test
    public void testAjouterKO() throws DoubleDeclaration {
        TDS tds = TDS.getInstance();
        tds.ajouter(new Entree("test"), new Symbole("int", 0));
        assertThrows(DoubleDeclaration.class, () -> tds.ajouter(new Entree("test"), new Symbole("int", 0)));
    }
}
