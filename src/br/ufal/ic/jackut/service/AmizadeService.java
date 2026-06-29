package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.*;
import br.ufal.ic.jackut.model.Usuario;
import br.ufal.ic.jackut.repository.IUsuarioRepository;
import br.ufal.ic.jackut.repository.UsuarioRepository;

/**
 * Implementação das regras de negócio relacionadas a amizades.
 */
public class AmizadeService implements IAmizadeService {
    private final IUsuarioRepository repo;
    private final ISessaoService sessaoService;

    /** Construtor padrão que instancia o repositório e sessão concretos. */
    public AmizadeService() {
        this(new UsuarioRepository(), new SessaoService());
    }

    /**
     * Construtor com injeção de dependências.
     *
     * @param repo repositório que será usado para persistência
     * @param sessaoService serviço de sessão
     */
    public AmizadeService(IUsuarioRepository repo, ISessaoService sessaoService) {
        this.repo = repo;
        this.sessaoService = sessaoService;
    }

    /**
     * Envia, aceita ou valida pedidos de amizade entre usuários.
     *
     * @throws UsuarioNaoCadastradoException se o usuário não existir
     * @throws UsuarioNaoPodeAdicionarAmigoException se tentar adicionar a si mesmo
     * @throws UsuarioJaAmigoException se já for amigo
     * @throws UsuarioJaAdicionadoException se já enviou pedido de amizade
     */
    @Override
    public void adicionarAmigo(String idSessao, String amigo) throws UsuarioNaoCadastradoException, UsuarioNaoPodeAdicionarAmigoException, UsuarioJaAmigoException, UsuarioJaAdicionadoException, br.ufal.ic.jackut.exceptions.FuncaoInvalidaException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        
        String login = sessaoService.extrairLoginDaSessao(idSessao);
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        
        if (amigo == null || amigo.isEmpty()) throw new UsuarioNaoCadastradoException();
        if (login.equals(amigo)) throw new UsuarioNaoPodeAdicionarAmigoException();
        
        Usuario v = repo.getUsuario(amigo);
        if (v == null) throw new UsuarioNaoCadastradoException();

        // se o usuário alvo declarou o remetente como inimigo, ação inválida
        if (v.getInimigos().contains(login)) {
            throw new br.ufal.ic.jackut.exceptions.FuncaoInvalidaException("Função inválida: " + v.getNome() + " é seu inimigo.");
        }

        if (u.ehAmigo(amigo)) throw new UsuarioJaAmigoException();

        if (u.temPedidoEnviado(amigo)) throw new UsuarioJaAdicionadoException();

        if (u.temPedidoRecebido(amigo)) {
            u.adicionarAmigo(amigo);
            v.adicionarAmigo(login);
            return;
        }

        u.enviarPedido(amigo);
        v.receberPedido(login);
    }

    /**
     * Verifica se dois usuários são amigos.
     *
     * @throws UsuarioNaoCadastradoException se o usuário não existir
     */
    @Override
    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoCadastradoException {
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        return u.ehAmigo(amigo);
    }

    /**
     * Retorna os amigos no formato {a,b}.
     *
     * @throws UsuarioNaoCadastradoException se o usuário não existir
     */
    @Override
    public String getAmigos(String login) throws UsuarioNaoCadastradoException {
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        return u.getAmigosString();
    }

    /** Remove todos os dados de amizades. */
    @Override
    public void zerarAmizades() {
        // As amizades são removidas quando o sistema é zerado via repositório
    }
}

