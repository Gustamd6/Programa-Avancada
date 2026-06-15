package br.com.unicesumar.diagnostico.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Doenca extends EntidadeBase implements Diagnosticavel {
    private String descricao;
    private final List<Sintoma> sintomas = new ArrayList<>();
    private final List<Tratamento> tratamentos = new ArrayList<>();
    private final List<Exame> exames = new ArrayList<>();

    public Doenca(int id, String nome, String descricao) {
        super(id, nome);
        setDescricao(descricao);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao == null || descricao.isBlank()
                ? "Sem descrição."
                : descricao.trim();
    }

    public void adicionarSintoma(Sintoma sintoma) {
        if (sintoma != null && !sintomas.contains(sintoma)) {
            sintomas.add(sintoma);
        }
    }

    public void adicionarTratamento(Tratamento tratamento) {
        if (tratamento != null && !tratamentos.contains(tratamento)) {
            tratamentos.add(tratamento);
        }
    }

    public void adicionarExame(Exame exame) {
        if (exame != null && !exames.contains(exame)) {
            exames.add(exame);
        }
    }

    public List<Sintoma> getSintomas() {
        return Collections.unmodifiableList(sintomas);
    }

    public List<Tratamento> getTratamentos() {
        return Collections.unmodifiableList(tratamentos);
    }

    public List<Exame> getExames() {
        return Collections.unmodifiableList(exames);
    }

    @Override
    public double calcularPontuacao(List<Sintoma> sintomasInformados) {
        if (sintomasInformados == null || sintomasInformados.isEmpty() || sintomas.isEmpty()) {
            return 0.0;
        }

        int pesoTotalDoenca = sintomas.stream()
                .mapToInt(sintoma -> sintoma.getSeveridade().getPeso())
                .sum();

        int pesoCorrespondente = sintomas.stream()
                .filter(sintomasInformados::contains)
                .mapToInt(sintoma -> sintoma.getSeveridade().getPeso())
                .sum();

        return (double) pesoCorrespondente / pesoTotalDoenca;
    }

    public String toCsv() {
        String sintomasIds = sintomas.stream()
                .map(sintoma -> String.valueOf(sintoma.getId()))
                .reduce((a, b) -> a + "," + b)
                .orElse("");

        String examesTexto = exames.stream()
                .map(exame -> exame.getNome() + "|" + exame.getTipo())
                .reduce((a, b) -> a + "||" + b)
                .orElse("");

        String tratamentosTexto = tratamentos.stream()
                .map(tratamento -> tratamento.getNome() + "|" + tratamento.getOrientacao())
                .reduce((a, b) -> a + "||" + b)
                .orElse("");

        return getId() + ";"
                + escape(getNome()) + ";"
                + escape(descricao) + ";"
                + escape(sintomasIds) + ";"
                + escape(examesTexto) + ";"
                + escape(tratamentosTexto);
    }

    public static Doenca fromCsv(String linha, List<Sintoma> sintomasDisponiveis) {
        String[] partes = linha.split(";", -1);
        if (partes.length < 6) {
            throw new IllegalArgumentException("Linha inválida para Doença: " + linha);
        }

        Doenca doenca = new Doenca(
                Integer.parseInt(partes[0]),
                unescape(partes[1]),
                unescape(partes[2])
        );

        String[] sintomasIds = unescape(partes[3]).split(",");
        for (String idTexto : sintomasIds) {
            if (!idTexto.isBlank()) {
                int id = Integer.parseInt(idTexto.trim());
                sintomasDisponiveis.stream()
                        .filter(sintoma -> sintoma.getId() == id)
                        .findFirst()
                        .ifPresent(doenca::adicionarSintoma);
            }
        }

        String examesTexto = unescape(partes[4]);
        if (!examesTexto.isBlank()) {
            String[] exames = examesTexto.split("\\|\\|");
            int contador = 1;
            for (String exameTexto : exames) {
                String[] campos = exameTexto.split("\\|", -1);
                String nome = campos.length > 0 ? campos[0] : "Exame";
                String tipo = campos.length > 1 ? campos[1] : "Geral";
                doenca.adicionarExame(new Exame(contador++, nome, tipo));
            }
        }

        String tratamentosTexto = unescape(partes[5]);
        if (!tratamentosTexto.isBlank()) {
            String[] tratamentos = tratamentosTexto.split("\\|\\|");
            int contador = 1;
            for (String tratamentoTexto : tratamentos) {
                String[] campos = tratamentoTexto.split("\\|", -1);
                String nome = campos.length > 0 ? campos[0] : "Tratamento";
                String orientacao = campos.length > 1 ? campos[1] : "Orientação não informada.";
                doenca.adicionarTratamento(new Tratamento(contador++, nome, orientacao));
            }
        }

        return doenca;
    }

    private static String escape(String valor) {
        return valor == null ? "" : valor.replace(";", ",");
    }

    private static String unescape(String valor) {
        return valor == null ? "" : valor;
    }
}
