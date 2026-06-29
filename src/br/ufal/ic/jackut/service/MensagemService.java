package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.jackut.exceptions.ComunidadeInexistenteException;
import br.ufal.ic.jackut.exceptions.NaoHaMensagensException;
import br.ufal.ic.jackut.model.Comunidade;
import br.ufal.ic.jackut.model.Usuario;
import br.ufal.ic.jackut.repository.IUsuarioRepository;
import br.ufal.ic.jackut.repository.UsuarioRepository;

/**
 * Implementação do serviço de mensagens enviadas a comunidades.
 * <p>
 * Responsável por validar remetente e comunidade e distribuir mensagens
 * para os membros, respeitando regras de relacionamento (ex.: ignorar
 * mensagens para usuários que declararam o remetente como inimigo).
 * </p>
 */
public class MensagemService implements IMensagemService {
    private final IUsuarioRepository repo;
    private final ISessaoService sessaoService;

    public MensagemService() {
        this(new UsuarioRepository(), new SessaoService());
    }

    public MensagemService(IUsuarioRepository repo, ISessaoService sessaoService) {
        this.repo = repo;
        this.sessaoService = sessaoService;
    }

    @Override
    /**
     * Envia uma mensagem para todos os membros da comunidade.
     *
     * @param idSessao  id de sessão do remetente
     * @param comunidade nome da comunidade
     * @param mensagem  texto a ser entregue
     * @throws UsuarioNaoCadastradoException se remetente não existir
     * @throws ComunidadeInexistenteException se a comunidade não existir
     */
    public void enviarMensagem(String idSessao, String comunidade, String mensagem) throws UsuarioNaoCadastradoException, ComunidadeInexistenteException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = sessaoService.extrairLoginDaSessao(idSessao);
        Usuario remetente = repo.getUsuario(login);
        if (remetente == null) throw new UsuarioNaoCadastradoException();

        Comunidade c = repo.getComunidade(comunidade);
        if (c == null) throw new ComunidadeInexistenteException();

        // envia a mensagem para todos os membros da comunidade, exceto
        // para usuários que declararam o remetente como inimigo (mensagem descartada)
        for (String membro : c.getMembros()) {
            Usuario u = repo.getUsuario(membro);
            if (u != null) {
                // se o membro declarou o remetente como inimigo, descarta
                if (u.getInimigos().contains(remetente)) continue;
                u.receberMensagem(mensagem);
            }
        }
    }

    @Override
    /**
     * Lê (remove) a próxima mensagem da fila do usuário identificado pela
     * sessão.
     *
     * @param idSessao id de sessão do usuário
     * @return texto da próxima mensagem
     * @throws UsuarioNaoCadastradoException se a sessão/usuário não existir
     * @throws NaoHaMensagensException se não houver mensagens
     */
    public String lerMensagem(String idSessao) throws UsuarioNaoCadastradoException, NaoHaMensagensException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = sessaoService.extrairLoginDaSessao(idSessao);
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        String m = u.lerMensagem();
        if (m == null) throw new NaoHaMensagensException();
        return m;
    }

    @Override
    /** Remove todos os dados de mensagens (usado ao zerar sistema). */
    public void zerarMensagens() {
        // mensagens são zeradas quando o sistema é zerado via repositório
    }
}

