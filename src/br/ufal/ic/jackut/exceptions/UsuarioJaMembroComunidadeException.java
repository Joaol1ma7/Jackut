package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando se tenta adicionar um usuário a uma comunidade da
 * qual ele já é membro.
 */
public class UsuarioJaMembroComunidadeException extends Exception {
    public UsuarioJaMembroComunidadeException() {
        super("Usuario já faz parte dessa comunidade.");
    }
}

