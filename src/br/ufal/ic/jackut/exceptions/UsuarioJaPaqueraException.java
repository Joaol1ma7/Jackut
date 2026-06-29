package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando se tenta adicionar um usuário como paquera que já
 * foi previamente adicionado.
 */
public class UsuarioJaPaqueraException extends Exception {
    public UsuarioJaPaqueraException() {
        super("Usuário já está adicionado como paquera.");
    }
}

