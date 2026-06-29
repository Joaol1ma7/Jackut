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
     * @throws LoginInvalidoException se o login for nulo ou vazio
     * @throws SenhaInvalidaException se a senha for nula ou vazia
     * @throws ContaExistenteException se o login já estiver em uso
     */
    void criarUsuario(String login, String senha, String nome) throws LoginInvalidoException, SenhaInvalidaException, ContaExistenteException;

    /**
     * Obtém um atributo do usuário.
     *
     * @param login    login do usuário
     * @param atributo nome do atributo
     * @return valor do atributo
     * @throws UsuarioNaoCadastradoException se o usuário não existir
     * @throws AtributoNaoPreenchidoException se o atributo personalizado não estiver preenchido
     */
    String getAtributoUsuario(String login, String atributo) throws UsuarioNaoCadastradoException, AtributoNaoPreenchidoException;

    /**
     * Edita um atributo do perfil usando id de sessão.
     *
     * @throws UsuarioNaoCadastradoException se o usuário não existir
     */
    void editarPerfil(String idSessao, String atributo, String valor) throws UsuarioNaoCadastradoException;


    /** Persiste os dados e encerra o sistema. */
    void encerrarSistema();

    /** Remove a conta do usuário identificado pela sessão. */
    void removerUsuario(String idSessao) throws UsuarioNaoCadastradoException;
}
