package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando já existe amizade entre os usuários.
 */
public class UsuarioJaAmigoException extends Exception {
    /** Mensagem padrão: "Usuário já está adicionado como amigo." */
    public UsuarioJaAmigoException() {
        super("Usuário já está adicionado como amigo.");
    }
}
