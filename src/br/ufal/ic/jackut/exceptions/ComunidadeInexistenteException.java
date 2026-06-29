package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando uma operação referencia uma comunidade inexistente.
 */
public class ComunidadeInexistenteException extends Exception {
    public ComunidadeInexistenteException() {
        super("Comunidade não existe.");
    }
}

