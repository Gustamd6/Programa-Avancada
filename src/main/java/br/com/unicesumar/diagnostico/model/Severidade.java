package br.com.unicesumar.diagnostico.model;

public enum Severidade {
    BAIXA(1),
    MEDIA(2),
    ALTA(3);

    private final int peso;

    Severidade(int peso) {
        this.peso = peso;
    }

    public int getPeso() {
        return peso;
    }

    public static Severidade fromString(String valor) {
        if (valor == null || valor.isBlank()) {
            return MEDIA;
        }

        return Severidade.valueOf(valor.trim().toUpperCase());
    }
}
