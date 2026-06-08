package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando uma operação refere-se a um usuário inexistente.
 */
public class UsuarioNaoCadastradoException extends Exception {
    /** Mensagem padrão: "Usuário não cadastrado." */
    public UsuarioNaoCadastradoException() {
        super("Usuário não cadastrado.");
    }
}
