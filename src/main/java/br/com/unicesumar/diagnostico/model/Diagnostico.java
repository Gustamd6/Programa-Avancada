package br.com.unicesumar.diagnostico.model;

import java.time.LocalDateTime;
import java.util.List;

public class Diagnostico {
    private final Doenca doenca;
    private final double pontuacao;
    private final LocalDateTime data;
    private final List<Sintoma> sintomasCorrespondentes;

    public Diagnostico(Doenca doenca, double pontuacao, List<Sintoma> sintomasCorrespondentes) {
        if (doenca == null) {
            throw new IllegalArgumentException("A doença é obrigatória.");
        }

        this.doenca = doenca;
        this.pontuacao = pontuacao;
        this.sintomasCorrespondentes = List.copyOf(sintomasCorrespondentes);
        this.data = LocalDateTime.now();
    }

    public Doenca getDoenca() {
        return doenca;
    }

    public double getPontuacao() {
        return pontuacao;
    }

    public LocalDateTime getData() {
        return data;
    }

    public List<Sintoma> getSintomasCorrespondentes() {
        return sintomasCorrespondentes;
    }

    public String getResumo() {
        return String.format(
                "%s - %.0f%% de compatibilidade",
                doenca.getNome(),
                pontuacao * 100
        );
    }
}
