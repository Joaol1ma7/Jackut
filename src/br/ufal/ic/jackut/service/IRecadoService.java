package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.jackut.exceptions.UsuarioNaoPodeEnviarRecadoException;
import br.ufal.ic.jackut.exceptions.NaoHaRecadosException;

/**
 * Serviço que define as operações de negócio relacionadas a recados.
 */
public interface IRecadoService {
    /**
     * Envia um recado de um usuário para outro.
     *
     * @param idSessao    identificador de sessão do remetente
     * @param destinatario login do destinatário
     * @param recado       texto do recado
     * @throws UsuarioNaoCadastradoException se algum usuário não existir
     * @throws UsuarioNaoPodeEnviarRecadoException se tentar enviar para si mesmo
     */
    void enviarRecado(String idSessao, String destinatario, String recado) throws UsuarioNaoCadastradoException, UsuarioNaoPodeEnviarRecadoException, br.ufal.ic.jackut.exceptions.FuncaoInvalidaException;

    /**
     * Lê o próximo recado do usuário identificado pela sessão.
     *
     * @param idSessao identificador de sessão
     * @return texto do recado
     * @throws UsuarioNaoCadastradoException se a sessão/usuário não existir
     * @throws NaoHaRecadosException se não houver recados
     */
    String lerRecado(String idSessao) throws UsuarioNaoCadastradoException, NaoHaRecadosException;

    /** Remove todos os dados de recados. */
    void zerarRecados();
}

