package br.ufal.ic.jackut.repository;

import br.ufal.ic.jackut.model.Usuario;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementação concreta de {@link IUsuarioRepository} que persiste os dados
 * no arquivo XML usando {@link XMLEncoder}/{@link XMLDecoder}.
 */
public class UsuarioRepository implements IUsuarioRepository {
    private static final String DATA_FILE = "jackut_data.xml";
    private Map<String, Usuario> usuarios = new HashMap<>();

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
            encoder.writeObject(usuarios);
            encoder.flush();
        } catch (IOException e) {
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
                usuarios = (Map<String, Usuario>) o;
            }
        } catch (Exception e) {
            usuarios = new HashMap<>();
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

    /**
     * Recupera o usuário pelo login.
     *
     * @param login login do usuário
     * @return instância de {@link Usuario} ou null se não existir
     */
    public Usuario getUsuario(String login) {
        return usuarios.get(login);
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

    /**
     * Limpa a memória e remove o arquivo persistido.
     */
    public synchronized void clear() {
        usuarios.clear();
        File f = new File(DATA_FILE);
        if (f.exists()) f.delete();
    }
}

