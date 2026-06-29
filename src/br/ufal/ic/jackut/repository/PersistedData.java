package br.ufal.ic.jackut.repository;

import br.ufal.ic.jackut.model.Usuario;
import br.ufal.ic.jackut.model.Comunidade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Estrutura usada para persistir dados no formato que inclui usuários e
 * comunidades. Mantida separada para compatibilidade com leituras antigas.
 *
 * Esta classe é apenas um DTO usado pelo mecanismo de persistência (XMLEncoder/
 * XMLDecoder) para gravar/ler um único objeto contendo todos os mapas necessários.
 */
public class PersistedData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Map<String, Comunidade> comunidades = new HashMap<>();

    /** Construtor padrão para serialização. */
    public PersistedData() {}

    /** @return mapa de usuários persistidos */
    public Map<String, Usuario> getUsuarios() { return usuarios; }

    /** @param usuarios mapa de usuários a ser restaurado */
    public void setUsuarios(Map<String, Usuario> usuarios) { this.usuarios = usuarios == null ? new HashMap<>() : usuarios; }

    /** @return mapa de comunidades persistidas */
    public Map<String, Comunidade> getComunidades() { return comunidades; }

    /** @param comunidades mapa de comunidades a ser restaurado */
    public void setComunidades(Map<String, Comunidade> comunidades) { this.comunidades = comunidades == null ? new HashMap<>() : comunidades; }
}

