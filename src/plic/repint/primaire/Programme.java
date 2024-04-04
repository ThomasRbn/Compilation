package plic.repint.primaire;

import plic.exceptions.DeclarManquanteException;

public class Programme {

    private Bloc bloc;
    public static int compteurSi = 0;

    @Override
    public String toString() {
        return "Programme {\n" + bloc + "\n}";
    }

    public void verifier() throws DeclarManquanteException {
        bloc.verifier();
    }

    public String toMIPS() {
        String res = """
                .data
                crlf: .asciiz "\\n"
                                
                .text
                main:
                """;
        res += TDS.getInstance().toMIPS();
        res += bloc.toMIPS();
        res += """
                                
                end:
                \tli $v0, 10
                \tsyscall""";
        return res;
    }

    public Bloc getBloc() {
        return bloc;
    }

    public Programme setBloc(Bloc bloc) {
        this.bloc = bloc;
        return this;
    }
}
