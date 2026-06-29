package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando se tenta declarar um usuário como ídolo que já
 * foi declarado anteriormente.
 */
public class UsuarioJaIdoloException extends Exception {
    /** Mensagem padrão: "Usuário já está adicionado como ídolo." */
    public UsuarioJaIdoloException() {
        super("Usuário já está adicionado como ídolo.");
    }
}

