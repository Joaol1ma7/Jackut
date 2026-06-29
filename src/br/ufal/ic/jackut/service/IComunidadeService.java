package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.ComunidadeExistenteException;
import br.ufal.ic.jackut.exceptions.ComunidadeInexistenteException;
import br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException;

/**
 * Serviços de comunidade (casos de uso relacionados a comunidades).
 */
public interface IComunidadeService {
    void criarComunidade(String idSessao, String nome, String descricao) throws UsuarioNaoCadastradoException, ComunidadeExistenteException;
    String getDescricaoComunidade(String nome) throws ComunidadeInexistenteException;
    String getDonoComunidade(String nome) throws ComunidadeInexistenteException;
    String getMembrosComunidade(String nome) throws ComunidadeInexistenteException;
    void zerarComunidades();

    void adicionarComunidade(String idSessao, String nome) throws UsuarioNaoCadastradoException, ComunidadeInexistenteException, br.ufal.ic.jackut.exceptions.UsuarioJaMembroComunidadeException;

    String getComunidades(String idOuSessao) throws UsuarioNaoCadastradoException;
}


