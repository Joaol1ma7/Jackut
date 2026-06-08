package br.ufal.ic.jackut.exceptions;

/** Exceção lançada quando o login informado é inválido. */
public class LoginInvalidoException extends Exception {
    /** Mensagem padrão: "Login inválido." */
    public LoginInvalidoException() {
        super("Login inválido.");
    }
}
