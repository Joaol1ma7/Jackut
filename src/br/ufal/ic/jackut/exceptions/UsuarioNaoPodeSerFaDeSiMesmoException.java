package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando um usuário tenta declarar a si mesmo como fã/ídolo.
 */
public class UsuarioNaoPodeSerFaDeSiMesmoException extends Exception {
    public UsuarioNaoPodeSerFaDeSiMesmoException() {
        super("Usuário não pode ser fã de si mesmo.");
    }
}

