package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.*;

/**
 * Interface que expõe operações para relacionamentos sociais adicionais
 * (fã/ídolo, paquera e inimizade).
 */
public interface IRelacionamentoService {
    /**
     * Adiciona um ídolo para o usuário identificado pela sessão.
     */
    void adicionarIdolo(String idSessao, String idolo) throws UsuarioNaoCadastradoException, UsuarioJaIdoloException, UsuarioNaoPodeSerFaDeSiMesmoException, FuncaoInvalidaException;

    /** Verifica se login é fã de idolo. */
    boolean ehFa(String login, String idolo) throws UsuarioNaoCadastradoException;

    /** Retorna os fãs do usuário informado no formato {a,b}. */
    String getFas(String login) throws UsuarioNaoCadastradoException;

    /**
     * Adiciona uma paquera (privada) para o usuário identificado pela sessão.
     */
    void adicionarPaquera(String idSessao, String paquera) throws UsuarioNaoCadastradoException, UsuarioJaPaqueraException, UsuarioNaoPodeSerPaqueraDeSiMesmoException, FuncaoInvalidaException;

    /** Verifica se paquera foi adicionada (privado para quem adicionou). */
    boolean ehPaquera(String idSessao, String paquera) throws UsuarioNaoCadastradoException;

    /** Retorna paqueras do usuário identificado pela sessão no formato {a,b}. */
    String getPaqueras(String idSessao) throws UsuarioNaoCadastradoException;

    /** Marca outro usuário como inimigo do usuário identificado pela sessão. */
    void adicionarInimigo(String idSessao, String inimigo) throws UsuarioNaoCadastradoException, UsuarioJaInimigoException, UsuarioNaoPodeSerInimigoDeSiMesmoException;
}

