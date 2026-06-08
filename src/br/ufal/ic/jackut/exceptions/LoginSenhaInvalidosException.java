package br.ufal.ic.jackut.exceptions;


/** Exceção lançada quando login e/ou senha são inválidos. */
public class LoginSenhaInvalidosException extends Exception {
    /** Mensagem padrão compatível com os testes: "Login ou senha invlidos." */
    public LoginSenhaInvalidosException() {
        super("Login ou senha inválidos.");
    }
}

