package br.ufal.ic.jackut.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um usuário do sistema. Contém login, senha, nome e coleções de
 * atributos, amigos, pedidos e recados. Possui métodos para manipular esses
 * dados em memória.
 */
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private String senha;
    private String nome;
    private Map<String, String> atributos = new HashMap<>();
    private Set<String> amigos = new LinkedHashSet<>();
    private Set<String> pedidosEnviados = new LinkedHashSet<>();
    private Set<String> pedidosRecebidos = new LinkedHashSet<>();
    private Deque<String> recados = new ArrayDeque<>();

    /**
     * Cria um usuário com login, senha e nome.
     *
     * @param login login do usuário
     * @param senha senha do usuário
     * @param nome  nome completo (pode ser null)
     */
    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome == null ? "" : nome;
    }

    /**
     * Construtor sem-argumentos exigido por serializadores como XMLEncoder.
     */
    public Usuario() {
        this.login = "";
        this.senha = "";
        this.nome = "";
        this.atributos = new HashMap<>();
        this.amigos = new LinkedHashSet<>();
        this.pedidosEnviados = new LinkedHashSet<>();
        this.pedidosRecebidos = new LinkedHashSet<>();
        this.recados = new ArrayDeque<>();
    }

    /** @return login do usuário */
    public String getLogin() { return login; }
    /** @return senha do usuário */
    public String getSenha() { return senha; }
    /** @return nome do usuário */
    public String getNome() { return nome; }

    /**
     * Define um atributo do perfil (chave -> valor). A chave é normalizada para
     * minúsculas.
     *
     * @param chave nome do atributo
     * @param valor valor do atributo
     */
    public void setAtributo(String chave, String valor) {
        if (chave == null) return;
        atributos.put(chave.toLowerCase(), valor == null ? "" : valor);
    }

    /**
     * Retorna o valor do atributo informado.
     *
     * @param chave nome do atributo
     * @return valor do atributo ou null se não existir
     */
    public String getAtributo(String chave) {
        if (chave == null) return null;
        return atributos.get(chave.toLowerCase());
    }

    /**
     * Verifica se o login informado é amigo deste usuário.
     *
     * @param login login a verificar
     * @return true se for amigo, false caso contrário
     */
    public boolean ehAmigo(String login) {
        if (login == null) return false;
        return amigos.contains(login);
    }

    /**
     * Marca outro usuário como amigo e remove pedidos relacionados.
     *
     * @param login login do amigo a adicionar
     */
    public void adicionarAmigo(String login) {
        if (login == null) return;
        amigos.add(login);

        pedidosEnviados.remove(login);
        pedidosRecebidos.remove(login);
    }

    /**
     * Registra um pedido de amizade enviado para outro login.
     *
     * @param login login do destinatário do pedido
     */
    public void enviarPedido(String login) {
        if (login == null) return;
        pedidosEnviados.add(login);
    }

    /**
     * Registra um pedido de amizade recebido de outro login.
     *
     * @param login login do remetente do pedido
     */
    public void receberPedido(String login) {
        if (login == null) return;
        pedidosRecebidos.add(login);
    }

    /**
     * Indica se já existe um pedido enviado para o login informado.
     *
     * @param login login a verificar
     * @return true se houver pedido enviado
     */
    public boolean temPedidoEnviado(String login) {
        if (login == null) return false;
        return pedidosEnviados.contains(login);
    }

    /**
     * Indica se há um pedido recebido do login informado.
     *
     * @param login login a verificar
     * @return true se houver pedido recebido
     */
    public boolean temPedidoRecebido(String login) {
        if (login == null) return false;
        return pedidosRecebidos.contains(login);
    }

    /**
     * Retorna os amigos no formato {a,b} usado pelos testes.
     *
     * @return representação textual dos amigos
     */
    public String getAmigosString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (String a : amigos) {
            if (!first) sb.append(",");
            sb.append(a);
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    public Map<String, String> getAtributos() { return atributos; }
    public void setAtributos(Map<String, String> atributos) { this.atributos = atributos == null ? new HashMap<>() : atributos; }

    public Set<String> getAmigos() { return amigos; }
    public void setAmigos(Set<String> amigos) { this.amigos = amigos == null ? new LinkedHashSet<>() : amigos; }

    public Set<String> getPedidosEnviados() { return pedidosEnviados; }
    public void setPedidosEnviados(Set<String> pedidosEnviados) { this.pedidosEnviados = pedidosEnviados == null ? new LinkedHashSet<>() : pedidosEnviados; }

    public Set<String> getPedidosRecebidos() { return pedidosRecebidos; }
    public void setPedidosRecebidos(Set<String> pedidosRecebidos) { this.pedidosRecebidos = pedidosRecebidos == null ? new LinkedHashSet<>() : pedidosRecebidos; }

    public List<String> getRecados() { return new ArrayList<>(recados); }
    public void setRecados(List<String> recadosList) { this.recados = recadosList == null ? new ArrayDeque<>() : new ArrayDeque<>(recadosList); }

    public void setLogin(String login) { this.login = login == null ? "" : login; }
    public void setSenha(String senha) { this.senha = senha == null ? "" : senha; }
    public void setNome(String nome) { this.nome = nome == null ? "" : nome; }

    /**
     * Recebe um recado e adiciona à fila de recados.
     *
     * @param recado texto do recado
     */
    public void receberRecado(String recado) {
        if (recado == null) recado = "";
        recados.addLast(recado);
    }

    /**
     * Lê (remove) o próximo recado da fila.
     *
     * @return texto do recado ou null se não houver recados
     */
    public String lerRecado() {
        return recados.pollFirst();
    }
}

