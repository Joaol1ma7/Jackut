package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando se tenta criar uma comunidade cujo nome já está em uso.
 */
public class ComunidadeExistenteException extends Exception {
    public ComunidadeExistenteException() {
        super("Comunidade com esse nome já existe.");
    }
}

