package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando um usuário tenta adicionar a si mesmo como amigo.
 */
public class UsuarioNaoPodeAdicionarAmigoException extends Exception {
    /**
     * Mensagem padrão: "Usuário não pode adicionar a si mesmo como amigo.".
     */
    public UsuarioNaoPodeAdicionarAmigoException() {
        super("Usuário não pode adicionar a si mesmo como amigo.");
    }
}
