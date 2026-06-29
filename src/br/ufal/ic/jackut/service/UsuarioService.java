package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.*;
import br.ufal.ic.jackut.model.Usuario;
import br.ufal.ic.jackut.repository.IUsuarioRepository;
import br.ufal.ic.jackut.repository.UsuarioRepository;

/**
 * Implementação das regras de negócio relacionadas a usuários. Valida
 * entradas, manipula modelos e delega persistência ao repositório.
 */
public class UsuarioService implements IUsuarioService {
    private final IUsuarioRepository repo;

    /** Construtor padrão que instancia o repositório concreto. */
    public UsuarioService() {
        this(new UsuarioRepository());
    }

    /**
     * Construtor com injeção de dependência do repositório.
     *
     * @param repo repositório que será usado para persistência
     */
    public UsuarioService(IUsuarioRepository repo) {
        this.repo = repo;
    }

    /** Remove todos os dados do sistema. */
    public void zerarSistema() {
        repo.clear();
    }

    /**
     * Cria um novo usuário após validações básicas.
     *
     * @throws LoginInvalidoException se o login for nulo ou vazio
     * @throws SenhaInvalidaException se a senha for nula ou vazia
     * @throws ContaExistenteException se o login já estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome) throws LoginInvalidoException, SenhaInvalidaException, ContaExistenteException {
        if (login == null || login.trim().isEmpty()) throw new LoginInvalidoException();
        if (senha == null || senha.isEmpty()) throw new SenhaInvalidaException();
        if (repo.exists(login)) throw new ContaExistenteException();
        Usuario u = new Usuario(login, senha, nome);
        repo.addUsuario(u);
    }

    /**
     * Obtém um atributo do usuário, com tratamento especial para nome/login/senha.
     *
     * @return valor do atributo solicitado
     * @throws UsuarioNaoCadastradoException se o usuário não existir
     * @throws AtributoNaoPreenchidoException se o atributo personalizado não estiver preenchido
     */
    public String getAtributoUsuario(String login, String atributo) throws UsuarioNaoCadastradoException, AtributoNaoPreenchidoException {
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        switch (atributo.toLowerCase()) {
            case "nome": return u.getNome();
            case "login": return u.getLogin();
            case "senha": return u.getSenha();
            default: {
                String v = u.getAtributo(atributo);
                if (v == null || v.isEmpty()) throw new AtributoNaoPreenchidoException();
                return v;
            }
        }
    }

    /**
     * Edita um atributo do perfil do usuário identificado pela sessão.
     *
     * @throws UsuarioNaoCadastradoException se o usuário não existir
     */
    public void editarPerfil(String idSessao, String atributo, String valor) throws UsuarioNaoCadastradoException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = idSessao.endsWith("-session") ? idSessao.substring(0, idSessao.length() - 8) : idSessao;
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        u.setAtributo(atributo, valor);
    }

    /** Persiste os dados atuais via repositório. */
    public void encerrarSistema() {
        repo.salvar();
    }

    /** Remove a conta do usuário identificado pela sessão. */
    public void removerUsuario(String idSessao) throws UsuarioNaoCadastradoException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = idSessao.endsWith("-session") ? idSessao.substring(0, idSessao.length() - 8) : idSessao;
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        repo.removeUsuario(login);
    }
}
