package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.LoginSenhaInvalidosException;
import br.ufal.ic.jackut.model.Usuario;
import br.ufal.ic.jackut.repository.IUsuarioRepository;
import br.ufal.ic.jackut.repository.UsuarioRepository;

/**
 * Implementação das regras de negócio relacionadas a sessões de usuário.
 */
public class SessaoService implements ISessaoService {
    private final IUsuarioRepository repo;

    /** Construtor padrão que instancia o repositório concreto. */
    public SessaoService() {
        this(new UsuarioRepository());
    }

    /**
     * Construtor com injeção de dependência do repositório.
     *
     * @param repo repositório que será usado para persistência
     */
    public SessaoService(IUsuarioRepository repo) {
        this.repo = repo;
    }

    /**
     * Abre sessão para o usuário se credenciais estiverem válidas.
     *
     * @throws LoginSenhaInvalidosException se o login/senha forem inválidos
     */
    @Override
    public String abrirSessao(String login, String senha) throws LoginSenhaInvalidosException {
        Usuario u = repo.getUsuario(login);
        if (u == null || senha == null || !senha.equals(u.getSenha())) {
            throw new LoginSenhaInvalidosException();
        }
        return login + "-session";
    }

    /**
     * Extrai o login de um ID de sessão.
     */
    @Override
    public String extrairLoginDaSessao(String idSessao) {
        return idSessao.endsWith("-session") ? idSessao.substring(0, idSessao.length() - 8) : idSessao;
    }

    /** Remove todos os dados de sessão (neste caso, nenhuma persistência). */
    @Override
    public void zerarSessoes() {
        // Neste modelo, sessões são voláteis e não precisam ser limpas
    }
}

