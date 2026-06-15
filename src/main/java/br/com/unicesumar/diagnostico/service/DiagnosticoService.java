package br.com.unicesumar.diagnostico.service;

import br.com.unicesumar.diagnostico.exception.DiagnosticoException;
import br.com.unicesumar.diagnostico.model.Diagnostico;
import br.com.unicesumar.diagnostico.model.Doenca;
import br.com.unicesumar.diagnostico.model.SessaoDiagnostico;

import java.util.List;

public class DiagnosticoService {
    public List<Diagnostico> diagnosticar(SessaoDiagnostico sessao, List<Doenca> doencas)
            throws DiagnosticoException {

        if (sessao == null) {
            throw new DiagnosticoException("Sessão de diagnóstico não inicializada.");
        }

        if (sessao.getSintomasInformados().isEmpty()) {
            throw new DiagnosticoException("Informe ao menos um sintoma para realizar o diagnóstico.");
        }

        if (doencas == null || doencas.isEmpty()) {
            throw new DiagnosticoException("Nenhuma doença cadastrada para comparação.");
        }

        List<Diagnostico> diagnosticos = sessao.gerarDiagnosticos(doencas);

        if (diagnosticos.isEmpty()) {
            throw new DiagnosticoException("Nenhuma doença apresentou compatibilidade com os sintomas informados.");
        }

        return diagnosticos;
    }
}
