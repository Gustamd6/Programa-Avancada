package br.com.unicesumar.diagnostico;

import br.com.unicesumar.diagnostico.view.TelaPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Mantém o LookAndFeel padrão caso o sistema não permita alteração.
            }

            new TelaPrincipal().setVisible(true);
        });
    }
}
