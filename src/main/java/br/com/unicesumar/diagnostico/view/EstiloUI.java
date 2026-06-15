package br.com.unicesumar.diagnostico.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

final class EstiloUI {
    static final Color FUNDO = new Color(241, 246, 248);
    static final Color CARD = Color.WHITE;
    static final Color PRIMARIA = new Color(12, 105, 130);
    static final Color PRIMARIA_ESCURA = new Color(8, 78, 99);
    static final Color SECUNDARIA = new Color(232, 247, 248);
    static final Color DESTAQUE = new Color(41, 157, 143);
    static final Color TEXTO = new Color(30, 41, 59);
    static final Color TEXTO_SUAVE = new Color(100, 116, 139);
    static final Color BORDA = new Color(207, 220, 226);
    static final Color CAMPO = new Color(248, 251, 252);

    static final Font FONTE_BASE = new Font("SansSerif", Font.PLAIN, 14);
    static final Font FONTE_TITULO = new Font("SansSerif", Font.BOLD, 26);
    static final Font FONTE_SUBTITULO = new Font("SansSerif", Font.PLAIN, 14);

    private EstiloUI() {
    }

    static JPanel painelRaiz(int topo, int esquerda, int baixo, int direita) {
        JPanel painel = new JPanel();
        painel.setBackground(FUNDO);
        painel.setBorder(new EmptyBorder(topo, esquerda, baixo, direita));
        return painel;
    }

    static JPanel card() {
        JPanel painel = new PainelArredondado(CARD, 18);
        painel.setBorder(new EmptyBorder(20, 22, 20, 22));
        return painel;
    }

    static JPanel cardMenor() {
        JPanel painel = new PainelArredondado(CARD, 16);
        painel.setBorder(new EmptyBorder(16, 18, 16, 18));
        return painel;
    }

    static JLabel titulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_TITULO);
        label.setForeground(TEXTO);
        return label;
    }

    static JLabel subtitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_SUBTITULO);
        label.setForeground(TEXTO_SUAVE);
        return label;
    }

    static JLabel rotulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_BASE.deriveFont(Font.BOLD));
        label.setForeground(TEXTO);
        return label;
    }

    static JButton botaoPrimario(String texto) {
        JButton botao = new JButton(texto);
        configurarBotao(botao, PRIMARIA, Color.WHITE, PRIMARIA_ESCURA);
        return botao;
    }

    static JButton botaoSecundario(String texto) {
        JButton botao = new JButton(texto);
        configurarBotao(botao, DESTAQUE, Color.WHITE, new Color(35, 137, 126));
        return botao;
    }

    static void configurarBotao(JButton botao, Color fundo, Color texto, Color borda) {
        botao.setFocusPainted(false);
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setFont(FONTE_BASE.deriveFont(Font.BOLD));
        botao.setBackground(fundo);
        botao.setForeground(texto);
        botao.setBorder(new CompoundBorder(new BordaArredondada(borda, 14), new EmptyBorder(12, 18, 12, 18)));
    }

    static TitledBorder bordaTitulo(String titulo) {
        TitledBorder borda = BorderFactory.createTitledBorder(new BordaArredondada(BORDA, 16), titulo);
        borda.setTitleFont(FONTE_BASE.deriveFont(Font.BOLD));
        borda.setTitleColor(PRIMARIA_ESCURA);
        return borda;
    }

    static Border bordaSecao(String titulo) {
        return new CompoundBorder(bordaTitulo(titulo), new EmptyBorder(10, 12, 12, 12));
    }

    static JScrollPane scroll(Component componente) {
        JScrollPane scrollPane = new JScrollPane(componente);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        if (componente instanceof JTextComponent textComponent) {
            scrollPane.getViewport().setBackground(textComponent.getBackground());
        } else {
            scrollPane.getViewport().setBackground(CARD);
        }
        return scrollPane;
    }

    static void configurarCampo(JTextField campo) {
        campo.setUI(new BasicTextFieldUI());
        campo.setFont(FONTE_BASE);
        campo.setOpaque(true);
        campo.setForeground(TEXTO);
        campo.setCaretColor(PRIMARIA_ESCURA);
        campo.setSelectedTextColor(Color.WHITE);
        campo.setSelectionColor(DESTAQUE);
        campo.setBackground(CAMPO);
        campo.setDisabledTextColor(TEXTO_SUAVE);
        campo.setBorder(new CompoundBorder(new BordaArredondada(BORDA, 10), new EmptyBorder(8, 10, 8, 10)));
    }

    static void configurarAreaTexto(JTextArea area) {
        area.setUI(new BasicTextAreaUI());
        area.setFont(FONTE_BASE);
        area.setOpaque(true);
        area.setForeground(TEXTO);
        area.setCaretColor(PRIMARIA_ESCURA);
        area.setSelectedTextColor(Color.WHITE);
        area.setSelectionColor(DESTAQUE);
        area.setBackground(CAMPO);
        area.setDisabledTextColor(TEXTO_SUAVE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setMargin(new Insets(10, 10, 10, 10));
        area.setBorder(new CompoundBorder(new BordaArredondada(BORDA, 10), new EmptyBorder(8, 10, 8, 10)));
    }

    static void configurarCheckBox(JCheckBox checkBox) {
        checkBox.setFont(FONTE_BASE);
        checkBox.setOpaque(false);
        checkBox.setForeground(TEXTO);
        checkBox.setBorder(new EmptyBorder(7, 6, 7, 6));
        checkBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static final class PainelArredondado extends JPanel {
        private final Color cor;
        private final int raio;

        private PainelArredondado(Color cor, int raio) {
            this.cor = cor;
            this.raio = raio;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(214, 226, 232, 120));
            g2.fillRoundRect(2, 3, getWidth() - 5, getHeight() - 5, raio, raio);
            g2.setColor(cor);
            g2.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, raio, raio);
            g2.setColor(BORDA);
            g2.drawRoundRect(0, 0, getWidth() - 6, getHeight() - 6, raio, raio);
            g2.dispose();
            super.paintComponent(graphics);
        }
    }

    private static final class BordaArredondada extends AbstractBorder {
        private final Color cor;
        private final int raio;

        private BordaArredondada(Color cor, int raio) {
            this.cor = cor;
            this.raio = raio;
        }

        @Override
        public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(cor);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(x, y, width - 1, height - 1, raio, raio);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component component) {
            return new Insets(5, 8, 5, 8);
        }

        @Override
        public Insets getBorderInsets(Component component, Insets insets) {
            insets.top = 5;
            insets.left = 8;
            insets.bottom = 5;
            insets.right = 8;
            return insets;
        }
    }
}
