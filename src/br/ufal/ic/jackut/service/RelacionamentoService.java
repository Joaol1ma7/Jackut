package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.*;
import br.ufal.ic.jackut.model.Usuario;
import br.ufal.ic.jackut.model.Comunidade;
import br.ufal.ic.jackut.repository.IUsuarioRepository;
import br.ufal.ic.jackut.repository.UsuarioRepository;

/**
 * Serviço que implementa relações sociais adicionais: fã-ídolo, paquera e inimigo.
 * <p>
 * Encapsula validações (usuários existentes, regras de auto-relação e
 * verificações de inimizade) e aplica as mudanças aos modelos.
 * </p>
 */
public class RelacionamentoService implements IRelacionamentoService {
    private final IUsuarioRepository repo;
    private final ISessaoService sessaoService;

    public RelacionamentoService() {
        this(new UsuarioRepository(), new SessaoService());
    }

    public RelacionamentoService(IUsuarioRepository repo, ISessaoService sessaoService) {
        this.repo = repo;
        this.sessaoService = sessaoService;
    }

    /**
     * Declara que o usuario identificado por {@code idSessao} tem como idolo o
     * usuario {@code idolo}. O relacionamento {@code fa->idolo} e publico (o idolo
     * passa a ter o {@code fa} na sua lista de {@code fas}).
     *
     * @throws UsuarioNaoCadastradoException se remetente ou ídolo não existirem
     * @throws UsuarioNaoPodeSerFaDeSiMesmoException se o usuario tentar se tornar fa de si mesmo
     * @throws UsuarioJaIdoloException se o idolo ja estiver declarado
     * @throws FuncaoInvalidaException se o idolo declarou o usuario como inimigo
     */
    public void adicionarIdolo(String idSessao, String idolo) throws UsuarioNaoCadastradoException, UsuarioJaIdoloException, UsuarioNaoPodeSerFaDeSiMesmoException, FuncaoInvalidaException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = sessaoService.extrairLoginDaSessao(idSessao);
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        if (idolo == null || idolo.isEmpty()) throw new UsuarioNaoCadastradoException();
        if (login.equals(idolo)) throw new UsuarioNaoPodeSerFaDeSiMesmoException();
        Usuario idol = repo.getUsuario(idolo);
        if (idol == null) throw new UsuarioNaoCadastradoException();

        // se o idolo declarou o usuário como inimigo, ação inválida
        if (idol.getInimigos().contains(login)) throw new FuncaoInvalidaException("Função inválida: " + idol.getNome() + " é seu inimigo.");

        if (u.getIdolos().contains(idolo)) throw new UsuarioJaIdoloException();

        u.adicionarIdoloLocal(idolo);
        idol.adicionarFaLocal(login);
    }

    /**
     * Verifica se {@code login} declarou {@code idolo} como seu ídolo.
     */
    public boolean ehFa(String login, String idolo) throws UsuarioNaoCadastradoException {
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        return u.getIdolos().contains(idolo);
    }

    /**
     * Retorna a lista de fãs do usuário {@code login} no formato {a,b}.
     */
    public String getFas(String login) throws UsuarioNaoCadastradoException {
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        // return fans of this user
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (String f : u.getFas()) {
            if (!first) sb.append(",");
            sb.append(f);
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    /* ---------- Paquera (privada) ---------- */
    /**
     * Adiciona uma paquera (privada) do usuário identificado por {@code idSessao}
     * em relação a {@code paquera}. Se a paquera for mútua, envia recados
     * automáticos para ambos os envolvidos.
     *
     * @throws UsuarioNaoCadastradoException se remetente ou alvo não existirem
     * @throws UsuarioNaoPodeSerPaqueraDeSiMesmoException se tentar se paquerar a si mesmo
     * @throws UsuarioJaPaqueraException se já houver a paquera
     * @throws FuncaoInvalidaException se o alvo declarou o remetente como inimigo
     */
    public void adicionarPaquera(String idSessao, String paquera) throws UsuarioNaoCadastradoException, UsuarioJaPaqueraException, UsuarioNaoPodeSerPaqueraDeSiMesmoException, FuncaoInvalidaException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = sessaoService.extrairLoginDaSessao(idSessao);
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        if (paquera == null || paquera.isEmpty()) throw new UsuarioNaoCadastradoException();
        if (login.equals(paquera)) throw new UsuarioNaoPodeSerPaqueraDeSiMesmoException();
        Usuario p = repo.getUsuario(paquera);
        if (p == null) throw new UsuarioNaoCadastradoException();

        // se o paquera declarou o usuário como inimigo, ação inválida
        if (p.getInimigos().contains(login)) throw new FuncaoInvalidaException("Função inválida: " + p.getNome() + " é seu inimigo.");

        if (u.getPaqueras().contains(paquera)) throw new UsuarioJaPaqueraException();

        u.adicionarPaqueraLocal(paquera);

        // se for mútuo, envia recados automáticos para ambos
        if (p.getPaqueras().contains(login)) {
            // mensagem para p: "{u.nome} é seu paquera - Recado do Jackut."
            p.receberRecado(u.getNome() + " é seu paquera - Recado do Jackut.");
            // mensagem para u
            u.receberRecado(p.getNome() + " é seu paquera - Recado do Jackut.");
        }
    }

    /** Verifica se o usuário identificado pela sessão adicionou {@code paquera}. */
    public boolean ehPaquera(String idSessao, String paquera) throws UsuarioNaoCadastradoException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = sessaoService.extrairLoginDaSessao(idSessao);
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        return u.getPaqueras().contains(paquera);
    }

    /** Retorna as paqueras do usuário (formato {a,b}). */
    public String getPaqueras(String idSessao) throws UsuarioNaoCadastradoException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = sessaoService.extrairLoginDaSessao(idSessao);
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (String p : u.getPaqueras()) {
            if (!first) sb.append(",");
            sb.append(p);
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    /* ---------- Inimigos ---------- */
    /**
     * Marca {@code inimigo} como inimigo do usuário identificado por {@code idSessao}.
     * Inimizade impede que o alvo use algumas funcionalidades em relação ao remetente.
     */
    public void adicionarInimigo(String idSessao, String inimigo) throws UsuarioNaoCadastradoException, UsuarioJaInimigoException, UsuarioNaoPodeSerInimigoDeSiMesmoException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();
        String login = sessaoService.extrairLoginDaSessao(idSessao);
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();
        if (inimigo == null || inimigo.isEmpty()) throw new UsuarioNaoCadastradoException();
        if (login.equals(inimigo)) throw new UsuarioNaoPodeSerInimigoDeSiMesmoException();
        Usuario inim = repo.getUsuario(inimigo);
        if (inim == null) throw new UsuarioNaoCadastradoException();
        if (u.getInimigos().contains(inimigo)) throw new UsuarioJaInimigoException();

        u.adicionarInimigoLocal(inimigo);
    }
}



