package br.com.unicesumar.diagnostico.model;

public class Sintoma extends EntidadeBase {
    private String localizacao;
    private Severidade severidade;

    public Sintoma(int id, String nome, String localizacao, Severidade severidade) {
        super(id, nome);
        setLocalizacao(localizacao);
        setSeveridade(severidade);
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao == null || localizacao.isBlank()
                ? "Não informada"
                : localizacao.trim();
    }

    public Severidade getSeveridade() {
        return severidade;
    }

    public void setSeveridade(Severidade severidade) {
        this.severidade = severidade == null ? Severidade.MEDIA : severidade;
    }

    public String toCsv() {
        return getId() + ";" + escape(getNome()) + ";" + escape(localizacao) + ";" + severidade.name();
    }

    public static Sintoma fromCsv(String linha) {
        String[] partes = linha.split(";", -1);
        if (partes.length < 4) {
            throw new IllegalArgumentException("Linha inválida para Sintoma: " + linha);
        }

        return new Sintoma(
                Integer.parseInt(partes[0]),
                unescape(partes[1]),
                unescape(partes[2]),
                Severidade.fromString(partes[3])
        );
    }

    private static String escape(String valor) {
        return valor == null ? "" : valor.replace(";", ",");
    }

    private static String unescape(String valor) {
        return valor == null ? "" : valor;
    }
}
