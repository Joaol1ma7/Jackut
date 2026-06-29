package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.LoginSenhaInvalidosException;

/**
 * Serviço que define as operações de negócio relacionadas a sessões de usuário.
 */
public interface ISessaoService {
    /**
     * Abre sessão para um usuário se credenciais estiverem válidas.
     *
     * @param login login do usuário
     * @param senha senha do usuário
     * @return identificador da sessão no formato login-session
     * @throws LoginSenhaInvalidosException se o login/senha forem inválidos
     */
    String abrirSessao(String login, String senha) throws LoginSenhaInvalidosException;

    /**
     * Extrai o login de um ID de sessão.
     *
     * @param idSessao identificador de sessão (login + "-session")
     * @return login do usuário
     */
    String extrairLoginDaSessao(String idSessao);

    /** Remove todos os dados de sessão. */
    void zerarSessoes();
}

