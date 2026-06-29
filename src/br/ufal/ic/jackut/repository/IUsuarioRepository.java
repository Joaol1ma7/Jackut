package br.ufal.ic.jackut.repository;

import br.ufal.ic.jackut.model.Usuario;
import br.ufal.ic.jackut.model.Comunidade;

import java.util.Map;

/**
 * Contrato para repositório de usuários e comunidades. Fornece operações
 * básicas de persistência e busca.
 */
public interface IUsuarioRepository {
    /** Persiste os dados atuais para o armazenamento permanente. */
    void salvar();

    /** Limpa dados mantidos em memória e no arquivo persistido. */
    void clear();

    /**
     * Adiciona um usuário ao repositório.
     *
     * @param u usuário a ser adicionado
     */
    void addUsuario(Usuario u);

    /**
     * Recupera um usuário pelo login.
     *
     * @param login login do usuário
     * @return instância de {@link Usuario} ou null se não existir
     */
    Usuario getUsuario(String login);

    /**
     * Verifica se um usuário com o login existe no repositório.
     *
     * @param login login a verificar
     * @return true se existir, false caso contrário
     */
    boolean exists(String login);

    /**
     * Adiciona uma comunidade ao repositório.
     * @param c comunidade a ser adicionada
     */
    void addComunidade(Comunidade c);

    /**
     * Recupera uma comunidade pelo nome.
     * @param nome nome da comunidade
     * @return instância de Comunidade ou null se não existir
     */
    Comunidade getComunidade(String nome);

    /**
     * Verifica existência de comunidade pelo nome.
     * @param nome nome da comunidade
     * @return true se existir
     */
    boolean existsComunidade(String nome);

    /**
     * Remove um usuário do repositório (cascata: limpa relacionamentos e participações).
     * @param login login do usuário a remover
     */
    void removeUsuario(String login);
}
