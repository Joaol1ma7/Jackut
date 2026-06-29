package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.jackut.exceptions.ComunidadeInexistenteException;
import br.ufal.ic.jackut.exceptions.NaoHaMensagensException;

/**
 * Serviço que permite enviar mensagens para comunidades e ler mensagens
 * recebidas pelo usuário.
 */
public interface IMensagemService {
    /**
     * Envia uma mensagem para todos os membros da comunidade.
     *
     * @param idSessao  id de sessão do remetente
     * @param comunidade nome da comunidade
     * @param mensagem  texto da mensagem
     * @throws UsuarioNaoCadastradoException se remetente não existir
     * @throws ComunidadeInexistenteException se a comunidade não existir
     */
    void enviarMensagem(String idSessao, String comunidade, String mensagem) throws UsuarioNaoCadastradoException, ComunidadeInexistenteException;

    /**
     * Lê a próxima mensagem da fila do usuário identificado pela sessão.
     *
     * @param idSessao id de sessão do usuário
     * @return texto da mensagem
     * @throws UsuarioNaoCadastradoException se a sessão/usuário não existir
     * @throws NaoHaMensagensException se não houver mensagens
     */
    String lerMensagem(String idSessao) throws UsuarioNaoCadastradoException, NaoHaMensagensException;

    /** Remove todos os dados de mensagens (usado ao zerar sistema). */
    void zerarMensagens();
}

