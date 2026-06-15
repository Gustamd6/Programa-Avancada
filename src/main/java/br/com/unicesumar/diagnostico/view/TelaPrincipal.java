package br.com.unicesumar.diagnostico.view;

import br.com.unicesumar.diagnostico.dao.DoencaDAO;
import br.com.unicesumar.diagnostico.dao.SintomaDAO;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
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
        setSize(620, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void montarComponentes() {
        JLabel titulo = new JLabel("Diagnóstico Virtual Orientado a Objetos", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel subtitulo = new JLabel("Atividade Integradora - Java, Swing, POO, DAO e Persistência", JLabel.CENTER);

        JButton botaoDiagnostico = new JButton("Realizar diagnóstico");
        botaoDiagnostico.addActionListener(event -> new TelaDiagnostico(sintomaDAO, doencaDAO).setVisible(true));

        JButton botaoCadastro = new JButton("Cadastrar doença");
        botaoCadastro.addActionListener(event -> new TelaCadastroDoenca(sintomaDAO, doencaDAO).setVisible(true));

        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 16, 16));
        painelBotoes.add(botaoDiagnostico);
        painelBotoes.add(botaoCadastro);

        JPanel painelCentral = new JPanel(new BorderLayout(16, 16));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
        painelCentral.add(titulo, BorderLayout.NORTH);
        painelCentral.add(subtitulo, BorderLayout.CENTER);
        painelCentral.add(painelBotoes, BorderLayout.SOUTH);

        add(painelCentral);
    }
}
