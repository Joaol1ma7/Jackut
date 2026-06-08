package br.ufal.ic.jackut.exceptions;

/** Exceção para senha inválida. */
public class SenhaInvalidaException extends Exception {
    /** Mensagem padrão: "Senha inválida." */
    public SenhaInvalidaException() {
        super("Senha inválida.");
    }
}
