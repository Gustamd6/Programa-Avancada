package br.com.unicesumar.diagnostico.model;

import java.util.Objects;

public abstract class EntidadeBase {
    private int id;
    private String nome;

    protected EntidadeBase(int id, String nome) {
        setId(id);
        setNome(nome);
    }

    public int getId() {
        return id;
    }

    protected final void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O id deve ser maior que zero.");
        }
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        this.nome = nome.trim();
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EntidadeBase outra)) return false;
        return id == outra.id && getClass().equals(outra.getClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getClass());
    }
}
