package br.com.unicesumar.diagnostico.dao;

import br.com.unicesumar.diagnostico.model.Severidade;
import br.com.unicesumar.diagnostico.model.Sintoma;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SintomaDAO implements DAO<Sintoma> {
    private final Path arquivo;

    public SintomaDAO() {
        this(Path.of("data", "sintomas.csv"));
    }

    public SintomaDAO(Path arquivo) {
        this.arquivo = arquivo;
        inicializarArquivo();
    }

    @Override
    public void salvar(Sintoma sintoma) {
        List<Sintoma> sintomas = new ArrayList<>(listarTodos());
        removerInterno(sintomas, sintoma.getId());
        sintomas.add(sintoma);
        gravarTodos(sintomas);
    }

    @Override
    public List<Sintoma> listarTodos() {
        inicializarArquivo();

        try {
            return Files.readAllLines(arquivo, StandardCharsets.UTF_8)
                    .stream()
                    .filter(linha -> !linha.isBlank())
                    .map(Sintoma::fromCsv)
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler sintomas: " + e.getMessage(), e);
        }
    }

    @Override
    public Sintoma buscarPorId(int id) {
        return listarTodos().stream()
                .filter(sintoma -> sintoma.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void remover(int id) {
        List<Sintoma> sintomas = new ArrayList<>(listarTodos());
        removerInterno(sintomas, id);
        gravarTodos(sintomas);
    }

    private void removerInterno(List<Sintoma> sintomas, int id) {
        sintomas.removeIf(sintoma -> sintoma.getId() == id);
    }

    private void gravarTodos(List<Sintoma> sintomas) {
        try {
            Files.createDirectories(arquivo.getParent());
            List<String> linhas = sintomas.stream()
                    .map(Sintoma::toCsv)
                    .toList();
            Files.write(arquivo, linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao gravar sintomas: " + e.getMessage(), e);
        }
    }

    private void inicializarArquivo() {
        try {
            Files.createDirectories(arquivo.getParent());

            if (!Files.exists(arquivo) || Files.size(arquivo) == 0) {
                List<Sintoma> sintomasIniciais = List.of(
                        new Sintoma(1, "Febre", "Corpo inteiro", Severidade.MEDIA),
                        new Sintoma(2, "Tosse", "Sistema respiratório", Severidade.MEDIA),
                        new Sintoma(3, "Dor de cabeça", "Cabeça", Severidade.BAIXA),
                        new Sintoma(4, "Falta de ar", "Sistema respiratório", Severidade.ALTA),
                        new Sintoma(5, "Dor no corpo", "Músculos", Severidade.MEDIA),
                        new Sintoma(6, "Fadiga", "Corpo inteiro", Severidade.BAIXA),
                        new Sintoma(7, "Dor de garganta", "Garganta", Severidade.MEDIA),
                        new Sintoma(8, "Náusea", "Sistema digestivo", Severidade.BAIXA)
                );

                Files.write(
                        arquivo,
                        sintomasIniciais.stream().map(Sintoma::toCsv).toList(),
                        StandardCharsets.UTF_8
                );
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao inicializar arquivo de sintomas: " + e.getMessage(), e);
        }
    }
}
