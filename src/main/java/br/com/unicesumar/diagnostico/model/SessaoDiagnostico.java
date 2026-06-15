package br.com.unicesumar.diagnostico.model;

import java.util.ArrayList;
import java.util.List;

public class SessaoDiagnostico {
    private final List<Sintoma> sintomasInformados = new ArrayList<>();

    public void adicionarSintoma(Sintoma sintoma) {
        if (sintoma != null && !sintomasInformados.contains(sintoma)) {
            sintomasInformados.add(sintoma);
        }
    }

    public void limpar() {
        sintomasInformados.clear();
    }

    public List<Sintoma> getSintomasInformados() {
        return List.copyOf(sintomasInformados);
    }

    public List<Diagnostico> gerarDiagnosticos(List<Doenca> doencas) {
        return doencas.stream()
                .map(doenca -> {
                    List<Sintoma> correspondentes = doenca.getSintomas().stream()
                            .filter(sintomasInformados::contains)
                            .toList();

                    return new Diagnostico(
                            doenca,
                            doenca.calcularPontuacao(sintomasInformados),
                            correspondentes
                    );
                })
                .filter(diagnostico -> diagnostico.getPontuacao() > 0.0)
                .sorted((a, b) -> Double.compare(b.getPontuacao(), a.getPontuacao()))
                .toList();
    }
}
