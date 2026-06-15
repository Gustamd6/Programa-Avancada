package br.com.unicesumar.diagnostico.view;

import br.com.unicesumar.diagnostico.dao.DoencaDAO;
import br.com.unicesumar.diagnostico.dao.SintomaDAO;
import br.com.unicesumar.diagnostico.model.Doenca;
import br.com.unicesumar.diagnostico.model.Exame;
import br.com.unicesumar.diagnostico.model.Sintoma;
import br.com.unicesumar.diagnostico.model.Tratamento;
import br.com.unicesumar.diagnostico.util.IdGenerator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class TelaCadastroDoenca extends JFrame {
    private final SintomaDAO sintomaDAO;
    private final DoencaDAO doencaDAO;

    private final JTextField campoNome = new JTextField();
    private final JTextArea campoDescricao = new JTextArea(4, 30);
    private final JTextField campoExameNome = new JTextField();
    private final JTextField campoExameTipo = new JTextField();
    private final JTextField campoTratamentoNome = new JTextField();
    private final JTextArea campoTratamentoOrientacao = new JTextArea(3, 30);
    private final List<JCheckBox> checkboxesSintomas = new ArrayList<>();

    public TelaCadastroDoenca(SintomaDAO sintomaDAO, DoencaDAO doencaDAO) {
        this.sintomaDAO = sintomaDAO;
        this.doencaDAO = doencaDAO;

        configurarJanela();
        montarComponentes();
    }

    private void configurarJanela() {
        setTitle("Cadastro de Doença");
        setSize(860, 650);
        setLocationRelativeTo(null);
    }

    private void montarComponentes() {
        JPanel painelFormulario = new JPanel(new GridLayout(0, 2, 8, 8));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados da doença"));

        painelFormulario.add(new JLabel("Nome da doença:"));
        painelFormulario.add(campoNome);

        painelFormulario.add(new JLabel("Descrição:"));
        painelFormulario.add(new JScrollPane(campoDescricao));

        painelFormulario.add(new JLabel("Nome do exame sugerido:"));
        painelFormulario.add(campoExameNome);

        painelFormulario.add(new JLabel("Tipo do exame:"));
        painelFormulario.add(campoExameTipo);

        painelFormulario.add(new JLabel("Nome do tratamento:"));
        painelFormulario.add(campoTratamentoNome);

        painelFormulario.add(new JLabel("Orientação do tratamento:"));
        painelFormulario.add(new JScrollPane(campoTratamentoOrientacao));

        JPanel painelSintomas = new JPanel(new GridLayout(0, 1, 4, 4));
        painelSintomas.setBorder(BorderFactory.createTitledBorder("Sintomas relacionados"));

        for (Sintoma sintoma : sintomaDAO.listarTodos()) {
            JCheckBox checkBox = new JCheckBox(
                    sintoma.getNome()
                            + " | " + sintoma.getLocalizacao()
                            + " | " + sintoma.getSeveridade()
            );
            checkBox.putClientProperty("sintoma", sintoma);
            checkboxesSintomas.add(checkBox);
            painelSintomas.add(checkBox);
        }

        JButton botaoSalvar = new JButton("Salvar doença");
        botaoSalvar.addActionListener(event -> salvarDoenca());

        add(painelFormulario, BorderLayout.NORTH);
        add(new JScrollPane(painelSintomas), BorderLayout.CENTER);
        add(botaoSalvar, BorderLayout.SOUTH);
    }

    private void salvarDoenca() {
        try {
            String nome = campoNome.getText();
            String descricao = campoDescricao.getText();

            if (nome == null || nome.isBlank()) {
                throw new IllegalArgumentException("Informe o nome da doença.");
            }

            List<Sintoma> sintomasSelecionados = checkboxesSintomas.stream()
                    .filter(JCheckBox::isSelected)
                    .map(checkBox -> (Sintoma) checkBox.getClientProperty("sintoma"))
                    .toList();

            if (sintomasSelecionados.isEmpty()) {
                throw new IllegalArgumentException("Selecione ao menos um sintoma para a doença.");
            }

            int id = IdGenerator.proximoId(doencaDAO.listarTodos());

            Doenca doenca = new Doenca(id, nome, descricao);

            sintomasSelecionados.forEach(doenca::adicionarSintoma);

            if (!campoExameNome.getText().isBlank()) {
                doenca.adicionarExame(new Exame(1, campoExameNome.getText(), campoExameTipo.getText()));
            }

            if (!campoTratamentoNome.getText().isBlank()) {
                doenca.adicionarTratamento(new Tratamento(
                        1,
                        campoTratamentoNome.getText(),
                        campoTratamentoOrientacao.getText()
                ));
            }

            doencaDAO.salvar(doenca);

            JOptionPane.showMessageDialog(this, "Doença cadastrada com sucesso.");
            limparFormulario();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erro ao cadastrar doença",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limparFormulario() {
        campoNome.setText("");
        campoDescricao.setText("");
        campoExameNome.setText("");
        campoExameTipo.setText("");
        campoTratamentoNome.setText("");
        campoTratamentoOrientacao.setText("");
        checkboxesSintomas.forEach(checkBox -> checkBox.setSelected(false));
    }
}
