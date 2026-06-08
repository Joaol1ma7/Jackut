package br.ufal.ic.jackut;

import br.ufal.ic.jackut.service.IUsuarioService;
import br.ufal.ic.jackut.service.UsuarioService;
import br.ufal.ic.jackut.repository.UsuarioRepository;

/**
 * Facade que expõe a API pública da aplicação. A implementação delega para
 * um serviço de usuários e mantém contratos simples para os casos de uso.
 */
public class Facade {
	private final IUsuarioService service;

	/**
	 * Construtor padrão que cria a stack concreta (serviço + repositório).
	 */
	public Facade() {
		this(new UsuarioService(new UsuarioRepository()));
	}

	/**
	 * Construtor que permite injeção do serviço (útil para testes).
	 *
	 * @param service serviço que implementa as regras de usuário
	 */
	public Facade(IUsuarioService service) {
		this.service = service;
	}

	/**
	 * Remove todos os usuários e dados persistidos.
	 */
	public void zerarSistema() {
		service.zerarSistema();
	}

	/**
	 * Cria um novo usuário.
	 *
	 * @param login login desejado
	 * @param senha senha do usuário
	 * @param nome  nome completo (pode ser vazio)
	 * @throws Exception em casos de validação (login inválido, conta existente, etc.)
	 */
	public void criarUsuario(String login, String senha, String nome) throws Exception {
		service.criarUsuario(login, senha, nome);
	}

	/**
	 * Retorna um atributo do usuário.
	 *
	 * @param login    login do usuário
	 * @param atributo nome do atributo ("nome", "login", "senha" ou atributo personalizado)
	 * @return valor do atributo solicitado
	 * @throws Exception se o usuário não existir ou o atributo não estiver preenchido
	 */
	public String getAtributoUsuario(String login, String atributo) throws Exception {
		return service.getAtributoUsuario(login, atributo);
	}

	/**
	 * Edita um atributo do perfil do usuário identificado pela sessão.
	 *
	 * @param id      identificador de sessão (login + "-session")
	 * @param atributo nome do atributo a editar
	 * @param valor    novo valor do atributo
	 * @throws Exception se a sessão for inválida ou usuário não existir
	 */
	public void editarPerfil(String id, String atributo, String valor) throws Exception {
		service.editarPerfil(id, atributo, valor);
	}

	/**
	 * Envia ou solicita amizade entre usuários.
	 *
	 * @param id    identificador de sessão do remetente
	 * @param amigo login do usuário alvo
	 * @throws Exception em casos de validação (usuário inexistente, auto-adicionar, etc.)
	 */
	public void adicionarAmigo(String id, String amigo) throws Exception {
		service.adicionarAmigo(id, amigo);
	}

	/**
	 * Verifica se dois usuários são amigos.
	 *
	 * @param login  login do usuário
	 * @param amigo  login do possível amigo
	 * @return true se forem amigos, falso caso contrário
	 * @throws Exception se o usuário não existir
	 */
	public boolean ehAmigo(String login, String amigo) throws Exception {
		return service.ehAmigo(login, amigo);
	}

	/**
	 * Retorna a lista de amigos no formato esperado pelos testes.
	 *
	 * @param login login do usuário
	 * @return string com os amigos no formato {a,b}
	 * @throws Exception se o usuário não existir
	 */
	public String getAmigos(String login) throws Exception {
		return service.getAmigos(login);
	}

	/**
	 * Envia um recado para outro usuário.
	 *
	 * @param id          identificador de sessão do remetente
	 * @param destinatario login do destinatário
	 * @param recado       texto do recado
	 * @throws Exception em casos de validação (usuário inexistente, envio para si mesmo, etc.)
	 */
	public void enviarRecado(String id, String destinatario, String recado) throws Exception {
		service.enviarRecado(id, destinatario, recado);
	}

	/**
	 * Lê o próximo recado do usuário identificado pela sessão.
	 *
	 * @param id identificador de sessão
	 * @return texto do recado
	 * @throws Exception se não houver recados ou sessão inválida
	 */
	public String lerRecado(String id) throws Exception {
		return service.lerRecado(id);
	}

	/**
	 * Abre uma sessão para o usuário.
	 *
	 * @param login login do usuário
	 * @param senha senha do usuário
	 * @return identificador da sessão no formato login-session
	 * @throws Exception se login/senha inválidos
	 */
	public String abrirSessao(String login, String senha) throws Exception {
		return service.abrirSessao(login, senha);
	}

	/**
	 * Persiste os dados e encerra o sistema.
	 */
	public void encerrarSistema() {
		service.encerrarSistema();
	}
}
