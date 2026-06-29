package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.*;

/**
 * Serviço que define as operações de negócio relacionadas a amizades.
 */
public interface IAmizadeService {
    /**
     * Cria ou aceita pedidos de amizade entre usuários.
     *
     * @param idSessao identificador de sessão do usuário
     * @param amigo    login do usuário alvo
     * @throws UsuarioNaoCadastradoException se o usuário não existir
     * @throws UsuarioNaoPodeAdicionarAmigoException se tentar adicionar a si mesmo
     * @throws UsuarioJaAmigoException se já for amigo
     * @throws UsuarioJaAdicionadoException se já enviou pedido de amizade
     */
    void adicionarAmigo(String idSessao, String amigo) throws UsuarioNaoCadastradoException, UsuarioNaoPodeAdicionarAmigoException, UsuarioJaAmigoException, UsuarioJaAdicionadoException, br.ufal.ic.jackut.exceptions.FuncaoInvalidaException;

    /**
     * Verifica se dois usuários são amigos.
     *
     * @param login login do primeiro usuário
     * @param amigo login do possível amigo
     * @return true se forem amigos, falso caso contrário
     * @throws UsuarioNaoCadastradoException se algum usuário não existir
     */
    boolean ehAmigo(String login, String amigo) throws UsuarioNaoCadastradoException;

    /**
     * Retorna a lista de amigos de um usuário.
     *
     * @param login login do usuário
     * @return string com os amigos no formato {a,b}
     * @throws UsuarioNaoCadastradoException se o usuário não existir
     */
    String getAmigos(String login) throws UsuarioNaoCadastradoException;

    /** Remove todos os dados de amizades. */
    void zerarAmizades();
}

