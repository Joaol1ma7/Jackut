package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando um pedido de amizade já foi enviado e aguarda aceitação.
 */
public class UsuarioJaAdicionadoException extends Exception {
    /** Mensagem padrão: "Usuário já está adicionado como amigo, esperando aceitação do convite." */
    public UsuarioJaAdicionadoException() {
        super("Usuário já está adicionado como amigo, esperando aceitação do convite.");
    }
}

