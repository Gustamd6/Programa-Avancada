package br.com.unicesumar.diagnostico.dao;

import java.util.List;

public interface DAO<T> {
    void salvar(T objeto);
    List<T> listarTodos();
    T buscarPorId(int id);
    void remover(int id);
}
