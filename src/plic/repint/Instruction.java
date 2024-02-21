package plic.repint;

import plic.exceptions.DeclarManquanteException;

public abstract class Instruction {

    public abstract void verifier() throws DeclarManquanteException;

    public abstract String toMIPS();


}
