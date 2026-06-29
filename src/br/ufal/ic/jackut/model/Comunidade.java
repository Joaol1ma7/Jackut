package br.ufal.ic.jackut.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Representa uma comunidade do sistema.
 * <p>
 * A comunidade possui nome (chave primária), descrição, dono (login) e um
 * conjunto ordenado de membros. Esta classe é serializável para persistência
 * via XMLEncoder/XMLDecoder.
 * </p>
 */
public class Comunidade implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String descricao;
    private String dono; // login do dono
    private Set<String> membros = new LinkedHashSet<>();

    /** Construtor sem-argumentos requerido por serializadores. */
    public Comunidade() {
        this.nome = "";
        this.descricao = "";
        this.dono = "";
        this.membros = new LinkedHashSet<>();
    }

    /**
     * Cria uma comunidade com nome, descrição e dono. O dono é automaticamente
     * adicionado ao conjunto de membros.
     *
     * @param nome      nome da comunidade (chave)
     * @param descricao descrição textual
     * @param dono      login do dono/criador
     */
    public Comunidade(String nome, String descricao, String dono) {
        this.nome = nome == null ? "" : nome;
        this.descricao = descricao == null ? "" : descricao;
        this.dono = dono == null ? "" : dono;
        this.membros = new LinkedHashSet<>();
        if (!this.dono.isEmpty()) this.membros.add(this.dono);
    }

    /** @return nome da comunidade */
    public String getNome() { return nome; }

    /** @return descrição da comunidade */
    public String getDescricao() { return descricao; }

    /** @return login do dono/criador da comunidade */
    public String getDono() { return dono; }

    /** @return conjunto ordenado de logins dos membros */
    public Set<String> getMembros() { return membros; }

    public void setNome(String nome) { this.nome = nome == null ? "" : nome; }
    public void setDescricao(String descricao) { this.descricao = descricao == null ? "" : descricao; }
    public void setDono(String dono) { this.dono = dono == null ? "" : dono; }
    public void setMembros(Set<String> membros) { this.membros = membros == null ? new LinkedHashSet<>() : membros; }

    public String getMembrosString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (String m : membros) {
            if (!first) sb.append(",");
            sb.append(m);
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
}

