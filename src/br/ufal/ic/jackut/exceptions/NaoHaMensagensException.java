package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando o usuário tenta ler mensagens e sua fila está vazia.
 */
public class NaoHaMensagensException extends Exception {
    /** Mensagem padrão: "Não há mensagens." */
    public NaoHaMensagensException() {
        super("Não há mensagens.");
    }
}

