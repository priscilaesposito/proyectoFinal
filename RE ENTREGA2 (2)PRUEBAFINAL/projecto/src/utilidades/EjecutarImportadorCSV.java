package utilidades;

import javax.swing.*;
import java.io.File;

/**
 * Clase ejecutable para probar el importador de CSV desde línea de comandos.
 * También puede usarse desde la interfaz gráfica.
 */
public class EjecutarImportadorCSV {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Configurar Look and Feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Usar default
            }

            // Crear ventana simple para seleccionar archivo
            JFrame frame = new JFrame("Importador de Películas CSV");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

            JLabel label = new JLabel("Importador de Películas desde CSV");
            label.setFont(label.getFont().deriveFont(16f));
            label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

            JButton btnSeleccionar = new JButton("Seleccionar Archivo CSV");
            btnSeleccionar.setAlignmentX(JButton.CENTER_ALIGNMENT);

            JLabel lblInfo = new JLabel("<html><center>Formato esperado:<br>" +
                    "Titulo,Director,Genero,Año,Rating,Duracion,Sinopsis</center></html>");
            lblInfo.setAlignmentX(JLabel.CENTER_ALIGNMENT);

            panel.add(label);
            panel.add(Box.createVerticalStrut(20));
            panel.add(btnSeleccionar);
            panel.add(Box.createVerticalStrut(15));
            panel.add(lblInfo);

            btnSeleccionar.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                        "Archivos CSV", "csv"));
                fileChooser.setCurrentDirectory(new File("."));

                int result = fileChooser.showOpenDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File archivo = fileChooser.getSelectedFile();

                    // Confirmar importación
                    int confirm = JOptionPane.showConfirmDialog(
                            frame,
                            "¿Desea importar las películas desde:\n" + archivo.getName() + "?",
                            "Confirmar Importación",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        ImportadorCSV importador = new ImportadorCSV();
                        importador.importarConProgreso(archivo, frame, () -> {
                            // Callback al finalizar
                            int continuar = JOptionPane.showConfirmDialog(
                                    frame,
                                    "¿Desea importar otro archivo?",
                                    "Importación Finalizada",
                                    JOptionPane.YES_NO_OPTION);

                            if (continuar != JOptionPane.YES_OPTION) {
                                System.exit(0);
                            }
                        });
                    }
                }
            });

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
