package br.com.unicesumar.diagnostico.dao;

import br.com.unicesumar.diagnostico.model.Doenca;
import br.com.unicesumar.diagnostico.model.Exame;
import br.com.unicesumar.diagnostico.model.Sintoma;
import br.com.unicesumar.diagnostico.model.Tratamento;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DoencaDAO implements DAO<Doenca> {
    private final Path arquivo;
    private final SintomaDAO sintomaDAO;

    public DoencaDAO(SintomaDAO sintomaDAO) {
        this(Path.of("data", "doencas.csv"), sintomaDAO);
    }

    public DoencaDAO(Path arquivo, SintomaDAO sintomaDAO) {
        this.arquivo = arquivo;
        this.sintomaDAO = sintomaDAO;
        inicializarArquivo();
    }

    @Override
    public void salvar(Doenca doenca) {
        List<Doenca> doencas = new ArrayList<>(listarTodos());
        removerInterno(doencas, doenca.getId());
        doencas.add(doenca);
        gravarTodos(doencas);
    }

    @Override
    public List<Doenca> listarTodos() {
        inicializarArquivo();

        List<Sintoma> sintomas = sintomaDAO.listarTodos();

        try {
            return Files.readAllLines(arquivo, StandardCharsets.UTF_8)
                    .stream()
                    .filter(linha -> !linha.isBlank())
                    .map(linha -> Doenca.fromCsv(linha, sintomas))
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler doenças: " + e.getMessage(), e);
        }
    }

    @Override
    public Doenca buscarPorId(int id) {
        return listarTodos().stream()
                .filter(doenca -> doenca.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void remover(int id) {
        List<Doenca> doencas = new ArrayList<>(listarTodos());
        removerInterno(doencas, id);
        gravarTodos(doencas);
    }

    private void removerInterno(List<Doenca> doencas, int id) {
        doencas.removeIf(doenca -> doenca.getId() == id);
    }

    private void gravarTodos(List<Doenca> doencas) {
        try {
            Files.createDirectories(arquivo.getParent());
            Files.write(
                    arquivo,
                    doencas.stream().map(Doenca::toCsv).toList(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao gravar doenças: " + e.getMessage(), e);
        }
    }

    private void inicializarArquivo() {
        try {
            Files.createDirectories(arquivo.getParent());

            if (!Files.exists(arquivo) || Files.size(arquivo) == 0) {
                carregarDoencasIniciais();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao inicializar arquivo de doenças: " + e.getMessage(), e);
        }
    }

    private void carregarDoencasIniciais() throws IOException {
        List<Sintoma> sintomas = sintomaDAO.listarTodos();

        Sintoma febre = buscarSintoma(sintomas, 1);
        Sintoma tosse = buscarSintoma(sintomas, 2);
        Sintoma dorCabeca = buscarSintoma(sintomas, 3);
        Sintoma faltaAr = buscarSintoma(sintomas, 4);
        Sintoma dorCorpo = buscarSintoma(sintomas, 5);
        Sintoma fadiga = buscarSintoma(sintomas, 6);
        Sintoma dorGarganta = buscarSintoma(sintomas, 7);
        Sintoma nausea = buscarSintoma(sintomas, 8);

        Doenca gripe = new Doenca(1, "Gripe", "Infecção viral comum com sintomas respiratórios e sistêmicos.");
        gripe.adicionarSintoma(febre);
        gripe.adicionarSintoma(tosse);
        gripe.adicionarSintoma(dorCabeca);
        gripe.adicionarSintoma(dorCorpo);
        gripe.adicionarSintoma(fadiga);
        gripe.adicionarExame(new Exame(1, "Avaliação clínica", "Clínico"));
        gripe.adicionarTratamento(new Tratamento(1, "Repouso e hidratação", "Manter hidratação, repouso e monitoramento dos sintomas."));

        Doenca infeccaoRespiratoria = new Doenca(2, "Infecção respiratória", "Quadro respiratório com possibilidade de comprometimento das vias aéreas.");
        infeccaoRespiratoria.adicionarSintoma(febre);
        infeccaoRespiratoria.adicionarSintoma(tosse);
        infeccaoRespiratoria.adicionarSintoma(faltaAr);
        infeccaoRespiratoria.adicionarSintoma(dorGarganta);
        infeccaoRespiratoria.adicionarExame(new Exame(2, "Raio-X de tórax", "Imagem"));
        infeccaoRespiratoria.adicionarTratamento(new Tratamento(2, "Avaliação médica", "Procurar atendimento médico em caso de falta de ar ou piora do quadro."));

        Doenca enxaqueca = new Doenca(3, "Enxaqueca", "Condição caracterizada por episódios de dor de cabeça recorrente.");
        enxaqueca.adicionarSintoma(dorCabeca);
        enxaqueca.adicionarSintoma(fadiga);
        enxaqueca.adicionarSintoma(nausea);
        enxaqueca.adicionarExame(new Exame(3, "Anamnese", "Clínico"));
        enxaqueca.adicionarTratamento(new Tratamento(3, "Controle de estímulos", "Evitar luz forte, ruídos e observar recorrência dos sintomas."));

        List<String> linhas = List.of(gripe.toCsv(), infeccaoRespiratoria.toCsv(), enxaqueca.toCsv());
        Files.write(arquivo, linhas, StandardCharsets.UTF_8);
    }

    private Sintoma buscarSintoma(List<Sintoma> sintomas, int id) {
        return sintomas.stream()
                .filter(sintoma -> sintoma.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Sintoma inicial não encontrado: " + id));
    }
}
