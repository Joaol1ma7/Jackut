package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando se tenta marcar um usuário como inimigo que já
 * figura na lista de inimigos.
 */
public class UsuarioJaInimigoException extends Exception {
    public UsuarioJaInimigoException() {
        super("Usuário já está adicionado como inimigo.");
    }
}

