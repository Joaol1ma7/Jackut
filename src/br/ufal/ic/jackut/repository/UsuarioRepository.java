package br.ufal.ic.jackut.repository;

import br.ufal.ic.jackut.model.Usuario;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import br.ufal.ic.jackut.model.Comunidade;
import br.ufal.ic.jackut.repository.PersistedData;

/**
 * Implementação concreta de {@link IUsuarioRepository} que persiste os dados
 * no arquivo XML usando {@link XMLEncoder}/{@link XMLDecoder}.
 */
public class UsuarioRepository implements IUsuarioRepository {
    private static final String DATA_FILE = "jackut_data.xml";
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Map<String, Comunidade> comunidades = new HashMap<>();

    /** Cria o repositório e carrega dados do arquivo, se existir. */
    public UsuarioRepository() {
        load();
    }

    /**
     * Persiste o mapa de usuários no arquivo XML.
     */
    public synchronized void salvar() {
        try (FileOutputStream fos = new FileOutputStream(DATA_FILE);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             XMLEncoder encoder = new XMLEncoder(bos)) {
            PersistedData pd = new PersistedData();
            pd.setUsuarios(usuarios);
            pd.setComunidades(comunidades);
            encoder.writeObject(pd);
            encoder.flush();
        } catch (IOException e) {
            // Relança como exceção de persistência específica (unchecked)
            throw new br.ufal.ic.jackut.exceptions.DataAccessException("Falha ao salvar dados em " + DATA_FILE, e);
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (FileInputStream fis = new FileInputStream(f);
             BufferedInputStream bis = new BufferedInputStream(fis);
             XMLDecoder decoder = new XMLDecoder(bis)) {
            Object o = decoder.readObject();
            if (o instanceof Map) {
                // formato antigo: apenas mapa de usuários
                usuarios = (Map<String, Usuario>) o;
                comunidades = new HashMap<>();
            } else if (o instanceof PersistedData) {
                PersistedData pd = (PersistedData) o;
                usuarios = pd.getUsuarios() == null ? new HashMap<>() : pd.getUsuarios();
                comunidades = pd.getComunidades() == null ? new HashMap<>() : pd.getComunidades();
            }
        } catch (Exception e) {
            // Se houver qualquer problema ao carregar, considere o repositório vazio
            usuarios = new HashMap<>();
            comunidades = new HashMap<>();
            throw new br.ufal.ic.jackut.exceptions.DataAccessException("Falha ao carregar dados de " + DATA_FILE, e);
        }
    }

    /**
     * Adiciona um usuário ao mapa em memória.
     *
     * @param u usuário a ser adicionado
     */
    public synchronized void addUsuario(Usuario u) {
        usuarios.put(u.getLogin(), u);
    }

    public synchronized void addComunidade(Comunidade c) {
        if (c == null || c.getNome() == null) return;
        comunidades.put(c.getNome(), c);
    }

    /**
     * Remove usuário do repositório e limpa referências a ele.
     * <p>
     * A operação remove o usuário do mapa de usuários, elimina comunidades cujo
     * dono era o usuário removido e limpa todas as referências a esse login nas
     * demais entidades (amizades, pedidos, relacionamentos e participações em
     * comunidades). As filas de recados e mensagens dos demais usuários também
     * são esvaziadas para garantir que mensagens enviadas pelo usuário removido
     * não permaneçam no sistema.
     * </p>
     *
     * @param login login do usuário a remover
     */
    public synchronized void removeUsuario(String login) {
        if (login == null || !usuarios.containsKey(login)) return;

        // remove usuário principal
        usuarios.remove(login);

        // remove comunidades cujo dono é o login
        java.util.Set<String> owned = new java.util.HashSet<>();
        for (java.util.Map.Entry<String, Comunidade> e : comunidades.entrySet()) {
            if (login.equals(e.getValue().getDono())) owned.add(e.getKey());
        }
        for (String cname : owned) {
            comunidades.remove(cname);
        }

        // limpa referências dos demais usuários
        for (Usuario u : usuarios.values()) {
            u.getAmigos().remove(login);
            u.getPedidosEnviados().remove(login);
            u.getPedidosRecebidos().remove(login);
            u.getComunidades().removeAll(owned);
            // remove membership in any community named == login? not applicable
            u.getComunidades().remove(login);
            u.getIdolos().remove(login);
            u.getFas().remove(login);
            u.getPaqueras().remove(login);
            u.getInimigos().remove(login);
            // remove all recados and mensagens (simpler approach: clear message queues so
            // messages sent by the removed user are gone)
            u.setRecados(new java.util.ArrayList<>());
            u.setMensagens(new java.util.ArrayList<>());
        }
    }

    /**
     * Recupera o usuário pelo login.
     *
     * @param login login do usuário
     * @return instância de {@link Usuario} ou null se não existir
     */
    public Usuario getUsuario(String login) {
        return usuarios.get(login);
    }

    public Comunidade getComunidade(String nome) {
        return comunidades.get(nome);
    }

    /**
     * Verifica existência de usuário.
     *
     * @param login login a verificar
     * @return true se existir
     */
    public boolean exists(String login) {
        return usuarios.containsKey(login);
    }

    public boolean existsComunidade(String nome) {
        return comunidades.containsKey(nome);
    }

    /**
     * Limpa a memória e remove o arquivo persistido.
     */
    public synchronized void clear() {
        usuarios.clear();
        comunidades.clear();
        File f = new File(DATA_FILE);
        if (f.exists()) f.delete();
    }
}

