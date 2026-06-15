package br.com.unicesumar.diagnostico.model;

public class Exame extends EntidadeBase {
    private String tipo;

    public Exame(int id, String nome, String tipo) {
        super(id, nome);
        setTipo(tipo);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo == null || tipo.isBlank() ? "Geral" : tipo.trim();
    }

    @Override
    public String toString() {
        return getNome() + " (" + tipo + ")";
    }
}
