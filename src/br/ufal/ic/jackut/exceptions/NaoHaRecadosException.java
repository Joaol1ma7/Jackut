package br.ufal.ic.jackut.exceptions;

/** Exceção lançada quando não há recados a serem lidos. */
public class NaoHaRecadosException extends Exception {
    /** Mensagem padrão: "Não há recados." */
    public NaoHaRecadosException() {
        super("Não há recados.");
    }
}
