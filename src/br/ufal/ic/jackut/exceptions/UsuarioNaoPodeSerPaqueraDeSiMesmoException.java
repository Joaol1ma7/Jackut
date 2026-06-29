package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando um usuário tenta adicionar a si próprio como paquera.
 */
public class UsuarioNaoPodeSerPaqueraDeSiMesmoException extends Exception {
    public UsuarioNaoPodeSerPaqueraDeSiMesmoException() {
        super("Usuário não pode ser paquera de si mesmo.");
    }
}

