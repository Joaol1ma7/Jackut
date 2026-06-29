package br.ufal.ic.jackut;

import br.ufal.ic.jackut.service.*;
import br.ufal.ic.jackut.repository.UsuarioRepository;
import br.ufal.ic.jackut.exceptions.*;

/**
 * Facade que expõe a API pública da aplicação. A implementação delega para
 * os serviços especializados e mantém contratos simples para os casos de uso.
 */
public class Facade {
	private final IUsuarioService usuarioService;
	private final ISessaoService sessaoService;
	private final IAmizadeService amizadeService;
	private final IRecadoService recadoService;
	private final IComunidadeService comunidadeService;
	private final IMensagemService mensagemService;
	private final IRelacionamentoService relacionamentoService;

	/**
	 * Construtor padrão que cria a stack concreta (serviços + repositório).
	 */
	public Facade() {
		UsuarioRepository repo = new UsuarioRepository();
		this.usuarioService = new UsuarioService(repo);
		this.sessaoService = new SessaoService(repo);
		this.amizadeService = new AmizadeService(repo, sessaoService);
		this.recadoService = new RecadoService(repo, sessaoService);
		this.comunidadeService = new ComunidadeService(repo, sessaoService);
		this.mensagemService = new MensagemService(repo, sessaoService);
		this.relacionamentoService = new RelacionamentoService(repo, sessaoService);
	}

	/**
	 * Construtor que permite injeção de todos os serviços (útil para testes).
	 *
	 * @param usuarioService serviço de usuários
	 * @param sessaoService serviço de sessões
	 * @param amizadeService serviço de amizades
	 * @param recadoService serviço de recados
	 */
	public Facade(IUsuarioService usuarioService, ISessaoService sessaoService,
	             IAmizadeService amizadeService, IRecadoService recadoService) {
		this.usuarioService = usuarioService;
		this.sessaoService = sessaoService;
		this.amizadeService = amizadeService;
		this.recadoService = recadoService;
		// cria um serviço de comunidades simples ligado a um repositório padrão
		this.comunidadeService = new ComunidadeService(new UsuarioRepository(), sessaoService);
		this.mensagemService = new MensagemService(new UsuarioRepository(), sessaoService);
		this.relacionamentoService = new RelacionamentoService(new UsuarioRepository(), sessaoService);
	}

	/**
	 * Construtor que permite injeção de todos os serviços (útil para testes),
	 * incluindo serviço de comunidades.
	 */
	public Facade(IUsuarioService usuarioService, ISessaoService sessaoService,
	             IAmizadeService amizadeService, IRecadoService recadoService, IComunidadeService comunidadeService) {
		this.usuarioService = usuarioService;
		this.sessaoService = sessaoService;
		this.amizadeService = amizadeService;
		this.recadoService = recadoService;
		this.comunidadeService = comunidadeService;
		this.mensagemService = new MensagemService(new UsuarioRepository(), sessaoService);
		this.relacionamentoService = new RelacionamentoService(new UsuarioRepository(), sessaoService);
	}

	/**
	 * Remove todos os usuários e dados persistidos.
	 */
	public void zerarSistema() {
		usuarioService.zerarSistema();
		sessaoService.zerarSessoes();
		amizadeService.zerarAmizades();
		recadoService.zerarRecados();
		// comunidades and messages are cleared via respective services/repository
	}

	/**
	 * Cria um novo usuário.
	 *
	 * @param login login desejado
	 * @param senha senha do usuário
	 * @param nome  nome completo (pode ser vazio)
	 * @throws LoginInvalidoException se o login for nulo ou vazio
	 * @throws SenhaInvalidaException se a senha for nula ou vazia
	 * @throws ContaExistenteException se o login já estiver em uso
	 */
	public void criarUsuario(String login, String senha, String nome) throws LoginInvalidoException, SenhaInvalidaException, ContaExistenteException {
		usuarioService.criarUsuario(login, senha, nome);
	}

	/**
	 * Retorna um atributo do usuário.
	 *
	 * @param login    login do usuário
	 * @param atributo nome do atributo ("nome", "login", "senha" ou atributo personalizado)
	 * @return valor do atributo solicitado
	 * @throws UsuarioNaoCadastradoException se o usuário não existir
	 * @throws AtributoNaoPreenchidoException se o atributo não estiver preenchido
	 */
	public String getAtributoUsuario(String login, String atributo) throws UsuarioNaoCadastradoException, AtributoNaoPreenchidoException {
		return usuarioService.getAtributoUsuario(login, atributo);
	}

	/**
	 * Edita um atributo do perfil do usuário identificado pela sessão.
	 *
	 * @param id      identificador de sessão (login + "-session")
	 * @param atributo nome do atributo a editar
	 * @param valor    novo valor do atributo
	 * @throws UsuarioNaoCadastradoException se a sessão for inválida ou usuário não existir
	 */
	public void editarPerfil(String id, String atributo, String valor) throws UsuarioNaoCadastradoException {
		usuarioService.editarPerfil(id, atributo, valor);
	}

	/**
	 * Envia ou solicita amizade entre usuários.
	 *
	 * @param id    identificador de sessão do remetente
	 * @param amigo login do usuário alvo
	 * @throws UsuarioNaoCadastradoException se o usuário não existir
	 * @throws UsuarioNaoPodeAdicionarAmigoException se tentar adicionar a si mesmo
	 * @throws UsuarioJaAmigoException se já for amigo
	 * @throws UsuarioJaAdicionadoException se já enviou pedido de amizade
	 */
	public void adicionarAmigo(String id, String amigo) throws UsuarioNaoCadastradoException, UsuarioNaoPodeAdicionarAmigoException, UsuarioJaAmigoException, UsuarioJaAdicionadoException, br.ufal.ic.jackut.exceptions.FuncaoInvalidaException {
		amizadeService.adicionarAmigo(id, amigo);
	}

	/**
	 * Verifica se dois usuários são amigos.
	 *
	 * @param login  login do usuário
	 * @param amigo  login do possível amigo
	 * @return true se forem amigos, falso caso contrário
	 * @throws UsuarioNaoCadastradoException se o usuário não existir
	 */
	public boolean ehAmigo(String login, String amigo) throws UsuarioNaoCadastradoException {
		return amizadeService.ehAmigo(login, amigo);
	}

	/**
	 * Retorna a lista de amigos no formato esperado pelos testes.
	 *
	 * @param login login do usuário
	 * @return string com os amigos no formato {a,b}
	 * @throws UsuarioNaoCadastradoException se o usuário não existir
	 */
	public String getAmigos(String login) throws UsuarioNaoCadastradoException {
		return amizadeService.getAmigos(login);
	}

	/**
	 * Envia um recado para outro usuário.
	 *
	 * @param id          identificador de sessão do remetente
	 * @param destinatario login do destinatário
	 * @param recado       texto do recado
	 * @throws UsuarioNaoCadastradoException se o usuário não existir
	 * @throws UsuarioNaoPodeEnviarRecadoException se tentar enviar para si mesmo
	 */
	public void enviarRecado(String id, String destinatario, String recado) throws UsuarioNaoCadastradoException, UsuarioNaoPodeEnviarRecadoException, br.ufal.ic.jackut.exceptions.FuncaoInvalidaException {
		recadoService.enviarRecado(id, destinatario, recado);
	}

	/**
	 * Lê o próximo recado do usuário identificado pela sessão.
	 *
	 * @param id identificador de sessão
	 * @return texto do recado
	 * @throws UsuarioNaoCadastradoException se a sessão for inválida
	 * @throws NaoHaRecadosException se não houver recados
	 */
	public String lerRecado(String id) throws UsuarioNaoCadastradoException, NaoHaRecadosException {
		return recadoService.lerRecado(id);
	}

	/**
	 * Abre uma sessão para o usuário.
	 *
	 * @param login login do usuário
	 * @param senha senha do usuário
	 * @return identificador da sessão no formato login-session
	 * @throws LoginSenhaInvalidosException se login/senha inválidos
	 */
	public String abrirSessao(String login, String senha) throws LoginSenhaInvalidosException {
		return sessaoService.abrirSessao(login, senha);
	}

	/**
	 * Persiste os dados e encerra o sistema.
	 */
	public void encerrarSistema() {
		usuarioService.encerrarSistema();
	}

	/** Remove a conta do usuário identificado pela sessão. */
	public void removerUsuario(String idSessao) throws br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException {
		usuarioService.removerUsuario(idSessao);
	}

	/* ---------- Casos de uso de mensagens a comunidades (US7) ---------- */

	public void enviarMensagem(String idSessao, String comunidade, String mensagem) throws br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException, br.ufal.ic.jackut.exceptions.ComunidadeInexistenteException {
		mensagemService.enviarMensagem(idSessao, comunidade, mensagem);
	}

	public String lerMensagem(String idSessao) throws br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException, br.ufal.ic.jackut.exceptions.NaoHaMensagensException {
		return mensagemService.lerMensagem(idSessao);
	}

	/* ---------- Casos de uso de relacionamento (US8) ---------- */

	public void adicionarIdolo(String idSessao, String idolo) throws br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException, br.ufal.ic.jackut.exceptions.UsuarioJaIdoloException, br.ufal.ic.jackut.exceptions.UsuarioNaoPodeSerFaDeSiMesmoException, br.ufal.ic.jackut.exceptions.FuncaoInvalidaException {
		relacionamentoService.adicionarIdolo(idSessao, idolo);
	}

	public boolean ehFa(String login, String idolo) throws br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException {
		return relacionamentoService.ehFa(login, idolo);
	}

	public String getFas(String login) throws br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException {
		return relacionamentoService.getFas(login);
	}

	public void adicionarPaquera(String idSessao, String paquera) throws br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException, br.ufal.ic.jackut.exceptions.UsuarioJaPaqueraException, br.ufal.ic.jackut.exceptions.UsuarioNaoPodeSerPaqueraDeSiMesmoException, br.ufal.ic.jackut.exceptions.FuncaoInvalidaException {
		relacionamentoService.adicionarPaquera(idSessao, paquera);
	}

	public boolean ehPaquera(String idSessao, String paquera) throws br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException {
		return relacionamentoService.ehPaquera(idSessao, paquera);
	}

	public String getPaqueras(String idSessao) throws br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException {
		return relacionamentoService.getPaqueras(idSessao);
	}

	public void adicionarInimigo(String idSessao, String inimigo) throws br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException, br.ufal.ic.jackut.exceptions.UsuarioJaInimigoException, br.ufal.ic.jackut.exceptions.UsuarioNaoPodeSerInimigoDeSiMesmoException {
		relacionamentoService.adicionarInimigo(idSessao, inimigo);
	}

	/* ---------- Casos de uso de comunidades (US5) ---------- */

	public void criarComunidade(String idSessao, String nome, String descricao) throws br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException, br.ufal.ic.jackut.exceptions.ComunidadeExistenteException {
		comunidadeService.criarComunidade(idSessao, nome, descricao);
	}

	public String getDescricaoComunidade(String nome) throws br.ufal.ic.jackut.exceptions.ComunidadeInexistenteException {
		return comunidadeService.getDescricaoComunidade(nome);
	}

	public String getDonoComunidade(String nome) throws br.ufal.ic.jackut.exceptions.ComunidadeInexistenteException {
		return comunidadeService.getDonoComunidade(nome);
	}

	public String getMembrosComunidade(String nome) throws br.ufal.ic.jackut.exceptions.ComunidadeInexistenteException {
		return comunidadeService.getMembrosComunidade(nome);
	}

	public void adicionarComunidade(String idSessao, String nome) throws br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException, br.ufal.ic.jackut.exceptions.ComunidadeInexistenteException, br.ufal.ic.jackut.exceptions.UsuarioJaMembroComunidadeException {
		comunidadeService.adicionarComunidade(idSessao, nome);
	}

	public String getComunidades(String idOuSessao) throws br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException {
		return comunidadeService.getComunidades(idOuSessao);
	}
}
