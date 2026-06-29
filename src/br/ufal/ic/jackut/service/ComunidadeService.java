package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.ComunidadeExistenteException;
import br.ufal.ic.jackut.exceptions.ComunidadeInexistenteException;
import br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.jackut.model.Comunidade;
import br.ufal.ic.jackut.repository.IUsuarioRepository;

/**
 * Implementação dos casos de uso de comunidades.
 */
public class ComunidadeService implements IComunidadeService {
    private final IUsuarioRepository repo;
    private final ISessaoService sessaoService;

    public ComunidadeService(IUsuarioRepository repo, ISessaoService sessaoService) {
        this.repo = repo;
        this.sessaoService = sessaoService;
    }

    @Override
    public void criarComunidade(String idSessao, String nome, String descricao) throws UsuarioNaoCadastradoException, ComunidadeExistenteException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = sessaoService.extrairLoginDaSessao(idSessao);
        if (!repo.exists(login)) throw new UsuarioNaoCadastradoException();
        if (repo.existsComunidade(nome)) throw new ComunidadeExistenteException();
        Comunidade c = new Comunidade(nome, descricao, login);
        repo.addComunidade(c);
        // garante que o dono também registre a comunidade em sua lista
        br.ufal.ic.jackut.model.Usuario u = repo.getUsuario(login);
        if (u != null) u.adicionarComunidade(nome);
    }

    @Override
    public String getDescricaoComunidade(String nome) throws ComunidadeInexistenteException {
        Comunidade c = repo.getComunidade(nome);
        if (c == null) throw new ComunidadeInexistenteException();
        return c.getDescricao();
    }

    @Override
    public String getDonoComunidade(String nome) throws ComunidadeInexistenteException {
        Comunidade c = repo.getComunidade(nome);
        if (c == null) throw new ComunidadeInexistenteException();
        return c.getDono();
    }

    @Override
    public String getMembrosComunidade(String nome) throws ComunidadeInexistenteException {
        Comunidade c = repo.getComunidade(nome);
        if (c == null) throw new ComunidadeInexistenteException();
        return c.getMembrosString();
    }

    @Override
    public void adicionarComunidade(String idSessao, String nome) throws UsuarioNaoCadastradoException, ComunidadeInexistenteException, br.ufal.ic.jackut.exceptions.UsuarioJaMembroComunidadeException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = sessaoService.extrairLoginDaSessao(idSessao);
        if (!repo.exists(login)) throw new UsuarioNaoCadastradoException();
        Comunidade c = repo.getComunidade(nome);
        if (c == null) throw new ComunidadeInexistenteException();
        if (c.getMembros().contains(login)) throw new br.ufal.ic.jackut.exceptions.UsuarioJaMembroComunidadeException();
        c.getMembros().add(login);
        // atualiza a lista de comunidades no usuário (ordem de ingresso é preservada em LinkedHashSet)
        br.ufal.ic.jackut.model.Usuario u = repo.getUsuario(login);
        if (u != null) u.adicionarComunidade(nome);
    }

    @Override
    public String getComunidades(String idOuSessao) throws UsuarioNaoCadastradoException {
        if (idOuSessao == null) throw new UsuarioNaoCadastradoException();
        String login = idOuSessao.endsWith("-session") ? sessaoService.extrairLoginDaSessao(idOuSessao) : idOuSessao;
        br.ufal.ic.jackut.model.Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        return u.getComunidadesString();
    }

    @Override
    public void zerarComunidades() {
        // nothing to do here: repo.clear() will remove communities as well
    }
}


