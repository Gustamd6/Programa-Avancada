package br.com.unicesumar.diagnostico.view;

import br.com.unicesumar.diagnostico.dao.DoencaDAO;
import br.com.unicesumar.diagnostico.dao.SintomaDAO;
import br.com.unicesumar.diagnostico.exception.DiagnosticoException;
import br.com.unicesumar.diagnostico.model.Diagnostico;
import br.com.unicesumar.diagnostico.model.SessaoDiagnostico;
import br.com.unicesumar.diagnostico.model.Sintoma;
import br.com.unicesumar.diagnostico.service.DiagnosticoService;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
        setSize(1060, 680);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
    }

    private void montarComponentes() {
        JPanel painelRaiz = EstiloUI.painelRaiz(22, 26, 24, 26);
        painelRaiz.setLayout(new BorderLayout(0, 18));
        painelRaiz.add(criarCabecalho(), BorderLayout.NORTH);
        painelRaiz.add(criarConteudo(), BorderLayout.CENTER);
        add(painelRaiz);
    }

    private JPanel criarCabecalho() {
        JPanel painel = new JPanel();
        painel.setOpaque(false);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.add(EstiloUI.titulo("Realizar diagnóstico"));
        painel.add(Box.createVerticalStrut(6));
        painel.add(EstiloUI.subtitulo("Marque os sintomas observados e gere as hipóteses com maior compatibilidade."));
        return painel;
    }

    private JPanel criarConteudo() {
        JPanel conteudo = new JPanel(new BorderLayout(18, 0));
        conteudo.setOpaque(false);
        conteudo.add(criarPainelSintomas(), BorderLayout.WEST);
        conteudo.add(criarPainelResultado(), BorderLayout.CENTER);
        return conteudo;
    }

    private JPanel criarPainelSintomas() {
        JPanel card = EstiloUI.card();
        card.setLayout(new BorderLayout(0, 14));
        card.setPreferredSize(new Dimension(390, 0));

        JLabel titulo = EstiloUI.rotulo("Sintomas informados");
        JLabel subtitulo = EstiloUI.subtitulo("Selecione um ou mais sintomas para analisar.");

        JPanel cabecalho = new JPanel();
        cabecalho.setOpaque(false);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.add(titulo);
        cabecalho.add(Box.createVerticalStrut(4));
        cabecalho.add(subtitulo);

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
            checkboxes.add(checkBox);
            lista.add(checkBox);
        }

        JButton botaoGerar = EstiloUI.botaoPrimario("Gerar diagnóstico");
        botaoGerar.addActionListener(event -> gerarDiagnostico());

        JButton botaoLimpar = EstiloUI.botaoSecundario("Limpar seleção");
        botaoLimpar.addActionListener(event -> limpar());

        JPanel botoes = new JPanel(new GridLayout(1, 2, 10, 0));
        botoes.setOpaque(false);
        botoes.add(botaoGerar);
        botoes.add(botaoLimpar);

        card.add(cabecalho, BorderLayout.NORTH);
        card.add(EstiloUI.scroll(lista), BorderLayout.CENTER);
        card.add(botoes, BorderLayout.SOUTH);
        return card;
    }

    private JPanel criarPainelResultado() {
        JPanel card = EstiloUI.card();
        card.setLayout(new BorderLayout(0, 14));

        JPanel cabecalho = new JPanel();
        cabecalho.setOpaque(false);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.add(EstiloUI.rotulo("Resultado do diagnóstico"));
        cabecalho.add(Box.createVerticalStrut(4));
        cabecalho.add(EstiloUI.subtitulo("As hipóteses aparecem ordenadas conforme a pontuação calculada pelo serviço."));

        areaResultado.setEditable(false);
        EstiloUI.configurarAreaTexto(areaResultado);
        areaResultado.setBackground(EstiloUI.CARD);
        areaResultado.setText("Selecione os sintomas observados e clique em Gerar diagnóstico.");

        JScrollPane scrollResultado = EstiloUI.scroll(areaResultado);
        card.add(cabecalho, BorderLayout.NORTH);
        card.add(scrollResultado, BorderLayout.CENTER);
        return card;
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
            texto.append("Diagnósticos encontrados\n\n");

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
                texto.append("\n").append("=".repeat(58)).append("\n\n");
            }

            areaResultado.setText(texto.toString());
            areaResultado.setCaretPosition(0);
        } catch (DiagnosticoException exception) {
            areaResultado.setText("Erro no diagnóstico: " + exception.getMessage());
        }
    }

    private void limpar() {
        checkboxes.forEach(checkBox -> checkBox.setSelected(false));
        sessao.limpar();
        areaResultado.setText("Selecione os sintomas observados e clique em Gerar diagnóstico.");
    }
}
