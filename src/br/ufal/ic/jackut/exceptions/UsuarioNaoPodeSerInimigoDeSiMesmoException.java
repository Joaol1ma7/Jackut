package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando um usuário tenta marcar a si mesmo como inimigo.
 */
public class UsuarioNaoPodeSerInimigoDeSiMesmoException extends Exception {
    public UsuarioNaoPodeSerInimigoDeSiMesmoException() {
        super("Usuário não pode ser inimigo de si mesmo.");
    }
}

