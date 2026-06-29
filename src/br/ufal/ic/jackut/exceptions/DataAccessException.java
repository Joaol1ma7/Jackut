package br.ufal.ic.jackut.exceptions;

/**
 * Exceção unchecked lançada quando ocorre falha na camada de persistência
 * (I/O, formato inválido, etc.). Usada para sinalizar erros de infraestrutura
 * sem poluir as assinaturas públicas com checked exceptions.
 */
public class DataAccessException extends RuntimeException {
    /**
     * Cria uma DataAccessException com mensagem e causa original.
     *
     * @param message mensagem contextual
     * @param cause   causa original da exceção
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}

