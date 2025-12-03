package gui;

import javax.swing.*;
import java.awt.*;
import org.json.JSONObject;

public class ResultadoBusquedaGUI extends JDialog {
    
    private JSONObject pelicula;
    
    public ResultadoBusquedaGUI(JFrame parent, JSONObject pelicula) {
        super(parent, "Plataforma de Streaming - Información", true);
        this.pelicula = pelicula;
        
        inicializarComponentes();
        establecerPropiedades();
    }
    
    private void inicializarComponentes() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        // Título de la película
        JLabel tituloLabel = new JLabel(pelicula.getString("Title"));
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(tituloLabel, BorderLayout.NORTH);
        
        // Panel de información
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Año
        JLabel anioLabel = new JLabel("Año: " + pelicula.optString("Year", "N/A"));
        anioLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        anioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(anioLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        
        // Resumen
        JLabel resumenTituloLabel = new JLabel("Resumen:");
        resumenTituloLabel.setFont(new Font("Arial", Font.BOLD, 13));
        resumenTituloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(resumenTituloLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        
        JTextArea resumenArea = new JTextArea(pelicula.optString("Plot", "No disponible"));
        resumenArea.setFont(new Font("Arial", Font.PLAIN, 13));
        resumenArea.setLineWrap(true);
        resumenArea.setWrapStyleWord(true);
        resumenArea.setEditable(false);
        resumenArea.setFocusable(false);
        resumenArea.setBackground(Color.WHITE);
        resumenArea.setBorder(null);
        resumenArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(resumenArea);
        
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Botón Continuar
        JButton continuarBtn = new JButton("Continuar");
        continuarBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        continuarBtn.setBackground(new Color(0, 122, 255));
        continuarBtn.setForeground(Color.WHITE);
        continuarBtn.setFocusPainted(false);
        continuarBtn.setOpaque(true);
        continuarBtn.setBorderPainted(false);
        continuarBtn.setPreferredSize(new Dimension(100, 30));
        continuarBtn.addActionListener(e -> dispose());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(continuarBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private void establecerPropiedades() {
        setSize(500, 250);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    public static void mostrar(JFrame parent, JSONObject pelicula) {
        SwingUtilities.invokeLater(() -> {
            ResultadoBusquedaGUI dialog = new ResultadoBusquedaGUI(parent, pelicula);
            dialog.setVisible(true);
        });
    }
}
