package br.com.unicesumar.diagnostico.model;

import java.util.List;

public interface Diagnosticavel {
    double calcularPontuacao(List<Sintoma> sintomas);
}
