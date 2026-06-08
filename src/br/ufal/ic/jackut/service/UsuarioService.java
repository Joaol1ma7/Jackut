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
    public void criarUsuario(String login, String senha, String nome) throws Exception {
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
    public String getAtributoUsuario(String login, String atributo) throws Exception {
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
     */
    public void editarPerfil(String idSessao, String atributo, String valor) throws Exception {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = idSessao.endsWith("-session") ? idSessao.substring(0, idSessao.length() - 8) : idSessao;
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        u.setAtributo(atributo, valor);
    }

    /**
     * Envia, aceita ou valida pedidos de amizade entre usuários.
     */
    public void adicionarAmigo(String idSessao, String amigo) throws Exception {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = idSessao.endsWith("-session") ? idSessao.substring(0, idSessao.length() - 8) : idSessao;
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        if (amigo == null || amigo.isEmpty()) throw new UsuarioNaoCadastradoException();
        if (login.equals(amigo)) throw new UsuarioNaoPodeAdicionarAmigoException();
        Usuario v = repo.getUsuario(amigo);
        if (v == null) throw new UsuarioNaoCadastradoException();

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

    /** Verifica se dois usuários são amigos. */
    public boolean ehAmigo(String login, String amigo) throws Exception {
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        return u.ehAmigo(amigo);
    }

    /** Retorna os amigos no formato {a,b}. */
    public String getAmigos(String login) throws Exception {
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        return u.getAmigosString();
    }

    /**
     * Envia um recado de um usuário para outro.
     */
    public void enviarRecado(String idSessao, String destinatario, String recado) throws Exception {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = idSessao.endsWith("-session") ? idSessao.substring(0, idSessao.length() - 8) : idSessao;
        Usuario remetente = repo.getUsuario(login);
        if (remetente == null) throw new UsuarioNaoCadastradoException();
        if (destinatario == null || destinatario.isEmpty()) throw new UsuarioNaoCadastradoException();
        if (login.equals(destinatario)) throw new UsuarioNaoPodeEnviarRecadoException();
        Usuario dest = repo.getUsuario(destinatario);
        if (dest == null) throw new UsuarioNaoCadastradoException();
        dest.receberRecado(recado);
    }

    /**
     * Lê o próximo recado do usuário identificado pela sessão.
     *
     * @return texto do recado
     * @throws NaoHaRecadosException se não houver recados
     */
    public String lerRecado(String idSessao) throws Exception {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = idSessao.endsWith("-session") ? idSessao.substring(0, idSessao.length() - 8) : idSessao;
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        String r = u.lerRecado();
        if (r == null) throw new NaoHaRecadosException();
        return r;
    }

    /** Abre sessão para o usuário se credenciais estiverem válidas. */
    public String abrirSessao(String login, String senha) throws Exception {
        Usuario u = repo.getUsuario(login);
        if (u == null || senha == null || !senha.equals(u.getSenha())) throw new LoginSenhaInvalidosException();
        return login + "-session";
    }

    /** Persiste os dados atuais via repositório. */
    public void encerrarSistema() {
        repo.salvar();
    }
}

