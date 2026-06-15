package br.com.unicesumar.diagnostico.view;

import br.com.unicesumar.diagnostico.dao.DoencaDAO;
import br.com.unicesumar.diagnostico.dao.SintomaDAO;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class TelaPrincipal extends JFrame {
    private final SintomaDAO sintomaDAO;
    private final DoencaDAO doencaDAO;

    public TelaPrincipal() {
        this.sintomaDAO = new SintomaDAO();
        this.doencaDAO = new DoencaDAO(sintomaDAO);

        configurarJanela();
        montarComponentes();
    }

    private void configurarJanela() {
        setTitle("Diagnóstico Virtual Orientado a Objetos");
        setSize(820, 520);
        setMinimumSize(new Dimension(760, 480));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void montarComponentes() {
        JPanel painelRaiz = EstiloUI.painelRaiz(28, 34, 28, 34);
        painelRaiz.setLayout(new BorderLayout(0, 22));

        painelRaiz.add(criarCabecalho(), BorderLayout.NORTH);
        painelRaiz.add(criarPainelCentral(), BorderLayout.CENTER);

        add(painelRaiz);
    }

    private JPanel criarCabecalho() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setOpaque(false);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel titulo = EstiloUI.titulo("Diagnóstico Virtual");
        JLabel subtitulo = EstiloUI.subtitulo("Selecione uma opção para iniciar o atendimento ou atualizar a base de doenças.");
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(6));
        textos.add(subtitulo);

        JPanel selo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        selo.setOpaque(false);
        JLabel info = EstiloUI.subtitulo("Base local: " + sintomaDAO.listarTodos().size() + " sintomas | "
                + doencaDAO.listarTodos().size() + " doenças");
        info.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.BORDA),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        selo.add(info);

        painel.add(textos, BorderLayout.WEST);
        painel.add(selo, BorderLayout.EAST);
        return painel;
    }

    private JPanel criarPainelCentral() {
        JPanel painel = EstiloUI.card();
        painel.setLayout(new BorderLayout(0, 24));

        JPanel chamada = new JPanel();
        chamada.setOpaque(false);
        chamada.setLayout(new BoxLayout(chamada, BoxLayout.Y_AXIS));

        JLabel titulo = EstiloUI.rotulo("Painel principal");
        JLabel texto = EstiloUI.subtitulo("O sistema compara sintomas informados com doenças cadastradas e apresenta possíveis compatibilidades.");
        chamada.add(titulo);
        chamada.add(Box.createVerticalStrut(8));
        chamada.add(texto);

        JPanel acoes = new JPanel(new GridLayout(1, 2, 18, 0));
        acoes.setOpaque(false);
        acoes.add(criarCardAcao(
                "Realizar diagnóstico",
                "Marque os sintomas observados e gere uma lista de diagnósticos possíveis.",
                "Abrir diagnóstico",
                true
        ));
        acoes.add(criarCardAcao(
                "Cadastrar doença",
                "Inclua uma nova doença, seus sintomas relacionados, exames e tratamentos.",
                "Abrir cadastro",
                false
        ));

        painel.add(chamada, BorderLayout.NORTH);
        painel.add(acoes, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarCardAcao(String titulo, String descricao, String textoBotao, boolean diagnostico) {
        JPanel card = EstiloUI.cardMenor();
        card.setLayout(new BorderLayout(0, 16));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel tituloLabel = EstiloUI.rotulo(titulo);
        JLabel descricaoLabel = EstiloUI.subtitulo("<html><body style='width:230px'>" + descricao + "</body></html>");
        textos.add(tituloLabel);
        textos.add(Box.createVerticalStrut(8));
        textos.add(descricaoLabel);

        JButton botao = diagnostico ? EstiloUI.botaoPrimario(textoBotao) : EstiloUI.botaoSecundario(textoBotao);
        botao.addActionListener(event -> {
            if (diagnostico) {
                new TelaDiagnostico(sintomaDAO, doencaDAO).setVisible(true);
            } else {
                new TelaCadastroDoenca(sintomaDAO, doencaDAO).setVisible(true);
            }
        });

        card.add(textos, BorderLayout.CENTER);
        card.add(botao, BorderLayout.SOUTH);
        return card;
    }
}
