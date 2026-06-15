package br.com.unicesumar.diagnostico;

import br.com.unicesumar.diagnostico.view.TelaPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Mantém o LookAndFeel padrão caso o sistema não permita alteração.
            }

            configurarAparenciaGlobal();
            new TelaPrincipal().setVisible(true);
        });
    }

    private static void configurarAparenciaGlobal() {
        Font fontePadrao = new Font("SansSerif", Font.PLAIN, 14);
        UIManager.put("Button.font", fontePadrao.deriveFont(Font.BOLD));
        UIManager.put("CheckBox.font", fontePadrao);
        UIManager.put("Label.font", fontePadrao);
        UIManager.put("TextField.font", fontePadrao);
        UIManager.put("TextArea.font", fontePadrao);
        UIManager.put("TitledBorder.font", fontePadrao.deriveFont(Font.BOLD));
        UIManager.put("Panel.background", new Color(241, 246, 248));
        UIManager.put("TextField.background", new Color(248, 251, 252));
        UIManager.put("TextField.foreground", new Color(30, 41, 59));
        UIManager.put("TextField.caretForeground", new Color(8, 78, 99));
        UIManager.put("TextArea.background", new Color(248, 251, 252));
        UIManager.put("TextArea.foreground", new Color(30, 41, 59));
        UIManager.put("TextArea.caretForeground", new Color(8, 78, 99));
        UIManager.put("OptionPane.messageFont", fontePadrao);
        UIManager.put("OptionPane.buttonFont", fontePadrao.deriveFont(Font.BOLD));
        UIManager.put("TextArea.margin", new javax.swing.plaf.InsetsUIResource(8, 10, 8, 10));
    }
}
