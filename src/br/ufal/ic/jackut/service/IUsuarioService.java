package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.*;

/**
 * Serviço que define as operações de negócio relacionadas a usuários.
 */
public interface IUsuarioService {
    /** Remove todos os dados do sistema. */
    void zerarSistema();

    /**
     * Cria um novo usuário.
     *
     * @param login login desejado
     * @param senha senha do usuário
     * @param nome  nome completo (pode ser vazio)
     * @throws Exception em caso de validação
     */
    void criarUsuario(String login, String senha, String nome) throws Exception;

    /**
     * Obtém um atributo do usuário.
     *
     * @param login    login do usuário
     * @param atributo nome do atributo
     * @return valor do atributo
     * @throws Exception se houver erro/validação
     */
    String getAtributoUsuario(String login, String atributo) throws Exception;

    /**
     * Edita um atributo do perfil usando id de sessão.
     */
    void editarPerfil(String idSessao, String atributo, String valor) throws Exception;

    /**
     * Cria ou aceita pedidos de amizade.
     */
    void adicionarAmigo(String idSessao, String amigo) throws Exception;

    /** Verifica se dois usuários são amigos. */
    boolean ehAmigo(String login, String amigo) throws Exception;

    /** Retorna amigos no formato esperado pelos testes. */
    String getAmigos(String login) throws Exception;

    /** Envia um recado para outro usuário. */
    void enviarRecado(String idSessao, String destinatario, String recado) throws Exception;

    /** Lê o próximo recado do usuário. */
    String lerRecado(String idSessao) throws Exception;

    /**
     * Abre sessão para login/senha.
     *
     * @return identificador de sessão
     */
    String abrirSessao(String login, String senha) throws Exception;

    /** Persiste os dados e encerra o sistema. */
    void encerrarSistema();
}
