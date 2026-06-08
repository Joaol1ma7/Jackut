package br.ufal.ic.jackut.exceptions;

/** Exceção lançada quando se tenta criar uma conta que já existe. */
public class ContaExistenteException extends Exception {
    /** Mensagem padrão: "Conta com esse nome já existe." */
    public ContaExistenteException() {
        super("Conta com esse nome já existe.");
    }
}

