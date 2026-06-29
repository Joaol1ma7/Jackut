package br.ufal.ic.jackut.service;

import br.ufal.ic.jackut.exceptions.UsuarioNaoCadastradoException;
import br.ufal.ic.jackut.exceptions.UsuarioNaoPodeEnviarRecadoException;
import br.ufal.ic.jackut.exceptions.NaoHaRecadosException;
import br.ufal.ic.jackut.model.Usuario;
import br.ufal.ic.jackut.repository.IUsuarioRepository;
import br.ufal.ic.jackut.repository.UsuarioRepository;

/**
 * Implementação das regras de negócio relacionadas a recados.
 */
public class RecadoService implements IRecadoService {
    private final IUsuarioRepository repo;
    private final ISessaoService sessaoService;

    /** Construtor padrão que instancia o repositório e sessão concretos. */
    public RecadoService() {
        this(new UsuarioRepository(), new SessaoService());
    }

    /**
     * Construtor com injeção de dependências.
     *
     * @param repo repositório que será usado para persistência
     * @param sessaoService serviço de sessão
     */
    public RecadoService(IUsuarioRepository repo, ISessaoService sessaoService) {
        this.repo = repo;
        this.sessaoService = sessaoService;
    }

    /**
     * Envia um recado de um usuário para outro.
     *
     * @throws UsuarioNaoCadastradoException se algum usuário não existir
     * @throws UsuarioNaoPodeEnviarRecadoException se tentar enviar para si mesmo
     */
    @Override
    public void enviarRecado(String idSessao, String destinatario, String recado) throws UsuarioNaoCadastradoException, UsuarioNaoPodeEnviarRecadoException, br.ufal.ic.jackut.exceptions.FuncaoInvalidaException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();

        String login = sessaoService.extrairLoginDaSessao(idSessao);
        Usuario remetente = repo.getUsuario(login);
        if (remetente == null) throw new UsuarioNaoCadastradoException();

        if (destinatario == null || destinatario.isEmpty()) throw new UsuarioNaoCadastradoException();
        if (login.equals(destinatario)) throw new UsuarioNaoPodeEnviarRecadoException();

        Usuario dest = repo.getUsuario(destinatario);
        if (dest == null) throw new UsuarioNaoCadastradoException();

        // se o destinatário declarou o remetente como inimigo, ação inválida
        if (dest.getInimigos().contains(login)) {
            throw new br.ufal.ic.jackut.exceptions.FuncaoInvalidaException("Função inválida: " + dest.getNome() + " é seu inimigo.");
        }

        dest.receberRecado(recado);
    }

    /**
     * Lê o próximo recado do usuário identificado pela sessão.
     *
     * @return texto do recado
     * @throws UsuarioNaoCadastradoException se o usuário não existir
     * @throws NaoHaRecadosException se não houver recados
     */
    @Override
    public String lerRecado(String idSessao) throws UsuarioNaoCadastradoException, NaoHaRecadosException {
        if (idSessao == null) throw new UsuarioNaoCadastradoException();

        String login = sessaoService.extrairLoginDaSessao(idSessao);
        Usuario u = repo.getUsuario(login);
        if (u == null) throw new UsuarioNaoCadastradoException();

        String r = u.lerRecado();
        if (r == null) throw new NaoHaRecadosException();
        return r;
    }

    /** Remove todos os dados de recados. */
    @Override
    public void zerarRecados() {
        // Os recados são removidos quando o sistema é zerado via repositório
    }
}

