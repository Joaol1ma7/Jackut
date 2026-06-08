package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando um usuário tenta enviar um recado para si mesmo.
 */
public class UsuarioNaoPodeEnviarRecadoException extends Exception {
    /**
     * Mensagem padrão: "Usuário não pode enviar recado para si mesmo.".
     */
    public UsuarioNaoPodeEnviarRecadoException() {
        super("Usuário não pode enviar recado para si mesmo.");
    }
}

