package br.com.unicesumar.diagnostico.view;

import br.com.unicesumar.diagnostico.dao.DoencaDAO;
import br.com.unicesumar.diagnostico.dao.SintomaDAO;
import br.com.unicesumar.diagnostico.model.Doenca;
import br.com.unicesumar.diagnostico.model.Exame;
import br.com.unicesumar.diagnostico.model.Sintoma;
import br.com.unicesumar.diagnostico.model.Tratamento;
import br.com.unicesumar.diagnostico.util.IdGenerator;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
        setSize(1040, 720);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);
    }

    private void montarComponentes() {
        JPanel painelRaiz = EstiloUI.painelRaiz(22, 26, 24, 26);
        painelRaiz.setLayout(new BorderLayout(0, 18));
        painelRaiz.add(criarCabecalho(), BorderLayout.NORTH);
        painelRaiz.add(criarConteudo(), BorderLayout.CENTER);
        painelRaiz.add(criarAcoes(), BorderLayout.SOUTH);
        add(painelRaiz);
    }

    private JPanel criarCabecalho() {
        JPanel painel = new JPanel();
        painel.setOpaque(false);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.add(EstiloUI.titulo("Cadastro de doença"));
        painel.add(Box.createVerticalStrut(6));
        painel.add(EstiloUI.subtitulo("Preencha os dados principais, associe sintomas e salve a doença na base local."));
        return painel;
    }

    private JPanel criarConteudo() {
        JPanel conteudo = new JPanel(new GridLayout(1, 2, 18, 0));
        conteudo.setOpaque(false);
        conteudo.add(criarFormulario());
        conteudo.add(criarPainelSintomas());
        return conteudo;
    }

    private JPanel criarFormulario() {
        JPanel card = EstiloUI.card();
        card.setLayout(new BorderLayout(0, 16));

        JPanel cabecalho = new JPanel();
        cabecalho.setOpaque(false);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.add(EstiloUI.rotulo("Dados da doença"));
        cabecalho.add(Box.createVerticalStrut(4));
        cabecalho.add(EstiloUI.subtitulo("Campos principais usados no diagnóstico e na orientação ao usuário."));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);

        EstiloUI.configurarCampo(campoNome);
        EstiloUI.configurarAreaTexto(campoDescricao);
        EstiloUI.configurarCampo(campoExameNome);
        EstiloUI.configurarCampo(campoExameTipo);
        EstiloUI.configurarCampo(campoTratamentoNome);
        EstiloUI.configurarAreaTexto(campoTratamentoOrientacao);

        adicionarCampo(formulario, 0, "Nome da doença", campoNome);
        adicionarCampo(formulario, 1, "Descrição", EstiloUI.scroll(campoDescricao));
        adicionarCampo(formulario, 2, "Nome do exame sugerido", campoExameNome);
        adicionarCampo(formulario, 3, "Tipo do exame", campoExameTipo);
        adicionarCampo(formulario, 4, "Nome do tratamento", campoTratamentoNome);
        adicionarCampo(formulario, 5, "Orientação do tratamento", EstiloUI.scroll(campoTratamentoOrientacao));

        card.add(cabecalho, BorderLayout.NORTH);
        card.add(formulario, BorderLayout.CENTER);
        return card;
    }

    private JPanel criarPainelSintomas() {
        JPanel card = EstiloUI.card();
        card.setLayout(new BorderLayout(0, 14));

        JPanel cabecalho = new JPanel();
        cabecalho.setOpaque(false);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.add(EstiloUI.rotulo("Sintomas relacionados"));
        cabecalho.add(Box.createVerticalStrut(4));
        cabecalho.add(EstiloUI.subtitulo("Selecione os sintomas que fazem parte da doença cadastrada."));

        JPanel lista = new JPanel(new GridLayout(0, 1, 4, 4));
        lista.setBackground(EstiloUI.CARD);

        for (Sintoma sintoma : sintomaDAO.listarTodos()) {
            JCheckBox checkBox = new JCheckBox(
                    sintoma.getNome()
                            + " | " + sintoma.getLocalizacao()
                            + " | " + sintoma.getSeveridade()
            );
            checkBox.putClientProperty("sintoma", sintoma);
            EstiloUI.configurarCheckBox(checkBox);
            checkboxesSintomas.add(checkBox);
            lista.add(checkBox);
        }

        card.add(cabecalho, BorderLayout.NORTH);
        card.add(EstiloUI.scroll(lista), BorderLayout.CENTER);
        return card;
    }

    private JPanel criarAcoes() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setOpaque(false);

        JButton botaoSalvar = EstiloUI.botaoPrimario("Salvar doença");
        botaoSalvar.addActionListener(event -> salvarDoenca());
        painel.add(botaoSalvar, BorderLayout.EAST);
        return painel;
    }

    private void adicionarCampo(JPanel painel, int linha, String rotulo, java.awt.Component campo) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = linha;
        labelConstraints.anchor = GridBagConstraints.NORTHWEST;
        labelConstraints.insets = new Insets(7, 0, 7, 14);

        JLabel label = EstiloUI.rotulo(rotulo);
        painel.add(label, labelConstraints);

        GridBagConstraints campoConstraints = new GridBagConstraints();
        campoConstraints.gridx = 1;
        campoConstraints.gridy = linha;
        campoConstraints.weightx = 1;
        campoConstraints.fill = GridBagConstraints.HORIZONTAL;
        campoConstraints.insets = new Insets(7, 0, 7, 0);
        painel.add(campo, campoConstraints);
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
