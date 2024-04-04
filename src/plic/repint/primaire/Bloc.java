package plic.repint.primaire;

import plic.exceptions.DeclarManquanteException;

import java.util.ArrayList;
import java.util.List;

public class Bloc {

    private List<Instruction> instructions;

    public Bloc() {
        this.instructions = new ArrayList<>();
    }

    public void ajouter(Instruction i) {
        this.instructions.add(i);
    }

    @Override
    public String toString() {
        String res = "Bloc {\n";
        for (Instruction i : instructions) {
            res += i + "\n";
        }
        res += "}";
        return res;
    }

    public void verifier() throws DeclarManquanteException {
        for (Instruction i : instructions) {
            i.verifier();
        }
    }

    public String toMIPS() {
        String res = """
                .data
                crlf: .asciiz "\\n"
                
                .text
                main:
                """;
        res += TDS.getInstance().toMIPS();
        for (Instruction i : instructions) {
            res += i.toMIPS();
        }
        res += """
                
                end:
                \tli $v0, 10
                \tsyscall""";
        return res;
    }
}
