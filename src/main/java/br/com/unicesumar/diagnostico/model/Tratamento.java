package br.com.unicesumar.diagnostico.model;

public class Tratamento extends EntidadeBase {
    private String orientacao;

    public Tratamento(int id, String nome, String orientacao) {
        super(id, nome);
        setOrientacao(orientacao);
    }

    public String getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(String orientacao) {
        this.orientacao = orientacao == null || orientacao.isBlank()
                ? "Orientação não informada."
                : orientacao.trim();
    }

    @Override
    public String toString() {
        return getNome() + ": " + orientacao;
    }
}
