
package br.ufal.ic.jackut.exceptions;

/** Exceção lançada quando um atributo de usuário não está preenchido. */
public class AtributoNaoPreenchidoException extends Exception {
    /** Mensagem padrão: "Atributo não preenchido." */
    public AtributoNaoPreenchidoException() {
        super("Atributo não preenchido.");
    }
}


