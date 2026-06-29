package br.ufal.ic.jackut.exceptions;

/**
 * Exceção lançada quando uma operação é inválida devido a um relacionamento
 * existente entre usuários (por exemplo: tentar adicionar amigo quando o alvo
 * declarou você como inimigo).
 */
public class FuncaoInvalidaException extends Exception {
    /**
     * @param msg mensagem descritiva do motivo da invalidez da operação
     */
    public FuncaoInvalidaException(String msg) {
        super(msg);
    }
}

