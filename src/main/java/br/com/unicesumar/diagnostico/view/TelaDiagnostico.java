package br.com.unicesumar.diagnostico.view;

import br.com.unicesumar.diagnostico.dao.DoencaDAO;
import br.com.unicesumar.diagnostico.dao.SintomaDAO;
import br.com.unicesumar.diagnostico.exception.DiagnosticoException;
import br.com.unicesumar.diagnostico.model.Diagnostico;
import br.com.unicesumar.diagnostico.model.SessaoDiagnostico;
import br.com.unicesumar.diagnostico.model.Sintoma;
import br.com.unicesumar.diagnostico.service.DiagnosticoService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class TelaDiagnostico extends JFrame {
    private final SintomaDAO sintomaDAO;
    private final DoencaDAO doencaDAO;
    private final SessaoDiagnostico sessao;
    private final DiagnosticoService diagnosticoService;

    private final List<JCheckBox> checkboxes = new ArrayList<>();
    private final JTextArea areaResultado = new JTextArea();

    public TelaDiagnostico(SintomaDAO sintomaDAO, DoencaDAO doencaDAO) {
        this.sintomaDAO = sintomaDAO;
        this.doencaDAO = doencaDAO;
        this.sessao = new SessaoDiagnostico();
        this.diagnosticoService = new DiagnosticoService();

        configurarJanela();
        montarComponentes();
    }

    private void configurarJanela() {
        setTitle("Realizar Diagnóstico");
        setSize(920, 560);
        setLocationRelativeTo(null);
    }

    private void montarComponentes() {
        JPanel painelSintomas = new JPanel(new GridLayout(0, 1, 4, 4));
        painelSintomas.setBorder(BorderFactory.createTitledBorder("Sintomas informados"));

        for (Sintoma sintoma : sintomaDAO.listarTodos()) {
            JCheckBox checkBox = new JCheckBox(
                    sintoma.getNome()
                            + " | Localização: " + sintoma.getLocalizacao()
                            + " | Severidade: " + sintoma.getSeveridade()
            );
            checkBox.putClientProperty("sintoma", sintoma);
            checkboxes.add(checkBox);
            painelSintomas.add(checkBox);
        }

        JButton botaoGerar = new JButton("Gerar diagnóstico");
        botaoGerar.addActionListener(event -> gerarDiagnostico());

        JButton botaoLimpar = new JButton("Limpar seleção");
        botaoLimpar.addActionListener(event -> limpar());

        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 8, 8));
        painelBotoes.add(botaoGerar);
        painelBotoes.add(botaoLimpar);

        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        areaResultado.setBorder(BorderFactory.createTitledBorder("Resultado do diagnóstico"));

        JPanel painelEsquerdo = new JPanel(new BorderLayout(8, 8));
        painelEsquerdo.add(new JScrollPane(painelSintomas), BorderLayout.CENTER);
        painelEsquerdo.add(painelBotoes, BorderLayout.SOUTH);

        add(painelEsquerdo, BorderLayout.WEST);
        add(new JScrollPane(areaResultado), BorderLayout.CENTER);
    }

    private void gerarDiagnostico() {
        sessao.limpar();

        checkboxes.stream()
                .filter(JCheckBox::isSelected)
                .map(checkBox -> (Sintoma) checkBox.getClientProperty("sintoma"))
                .forEach(sessao::adicionarSintoma);

        try {
            List<Diagnostico> diagnosticos = diagnosticoService.diagnosticar(sessao, doencaDAO.listarTodos());

            StringBuilder texto = new StringBuilder();
            texto.append("Diagnósticos encontrados:\n\n");

            for (Diagnostico diagnostico : diagnosticos) {
                texto.append("Doença: ").append(diagnostico.getDoenca().getNome()).append("\n");
                texto.append("Compatibilidade: ")
                        .append(String.format("%.0f%%", diagnostico.getPontuacao() * 100))
                        .append("\n");
                texto.append("Descrição: ").append(diagnostico.getDoenca().getDescricao()).append("\n");
                texto.append("Sintomas compatíveis: ").append(diagnostico.getSintomasCorrespondentes()).append("\n");
                texto.append("Exames sugeridos: ").append(diagnostico.getDoenca().getExames()).append("\n");
                texto.append("Tratamentos sugeridos: ").append(diagnostico.getDoenca().getTratamentos()).append("\n");
                texto.append("Resumo: ").append(diagnostico.getResumo()).append("\n");
                texto.append("\n--------------------------------------------------\n\n");
            }

            areaResultado.setText(texto.toString());
        } catch (DiagnosticoException exception) {
            areaResultado.setText("Erro no diagnóstico: " + exception.getMessage());
        }
    }

    private void limpar() {
        checkboxes.forEach(checkBox -> checkBox.setSelected(false));
        sessao.limpar();
        areaResultado.setText("");
    }
}
