package gui;

import javax.swing.*;
import java.awt.*;
import model.Pelicula;
import model.Usuario;
import app.Logica;

public class CalificarPeliculaGUI extends JDialog {
    
    private Pelicula pelicula;
    private Usuario usuario;
    private JComboBox<Integer> ratingCombo;
    private JTextArea reseniaArea;
    private boolean calificacionGuardada = false;
    
    public CalificarPeliculaGUI(JFrame parent, Pelicula pelicula, Usuario usuario) {
        super(parent, "Calificar Película", true);
        this.pelicula = pelicula;
        this.usuario = usuario;
        
        inicializarComponentes();
        establecerPropiedades();
    }
    
    private void inicializarComponentes() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        // Título de la película
        JLabel tituloLabel = new JLabel("Titulo de la Pelicula");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(tituloLabel, BorderLayout.NORTH);
        
        // Panel central con calificación y comentario
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Panel de calificación
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        ratingPanel.setBackground(Color.WHITE);
        ratingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel calificacionLabel = new JLabel("Calificación:");
        calificacionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ratingPanel.add(calificacionLabel);
        
        Integer[] ratings = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ratingCombo = new JComboBox<>(ratings);
        ratingCombo.setSelectedIndex(7); // Default 8/10
        ratingCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        ratingPanel.add(ratingCombo);
        
        centerPanel.add(ratingPanel);
        centerPanel.add(Box.createVerticalStrut(15));
        
        // Panel de comentario
        JLabel comentarioLabel = new JLabel("Comentario:");
        comentarioLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        comentarioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(comentarioLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        
        reseniaArea = new JTextArea(5, 30);
        reseniaArea.setLineWrap(true);
        reseniaArea.setWrapStyleWord(true);
        reseniaArea.setFont(new Font("Arial", Font.PLAIN, 13));
        reseniaArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JScrollPane scrollPane = new JScrollPane(reseniaArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(400, 100));
        centerPanel.add(scrollPane);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Botón Guardar
        JButton guardarBtn = new JButton("Guardar");
        guardarBtn.setFont(new Font("Arial", Font.BOLD, 12));
        guardarBtn.setBackground(new Color(0, 122, 255));
        guardarBtn.setForeground(Color.WHITE);
        guardarBtn.setFocusPainted(false);
        guardarBtn.setOpaque(true);
        guardarBtn.setBorderPainted(false);
        guardarBtn.setPreferredSize(new Dimension(100, 35));
        guardarBtn.addActionListener(e -> guardarCalificacion());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(guardarBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private void guardarCalificacion() {
        String resenia = reseniaArea.getText().trim();
        int calificacion = (Integer) ratingCombo.getSelectedItem();
        
        // Validar que se haya ingresado la reseña
        if (resenia.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe ingresar una reseña para calificar la película.",
                "Campo requerido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            boolean exito = Logica.calificarPelicula(
                usuario.getID_USUARIO(), 
                pelicula.getID(), 
                calificacion, 
                resenia
            );
            
            if (exito) {
                JOptionPane.showMessageDialog(this,
                    "¡Gracias por calificar \"" + pelicula.getMetadatos().getTitulo() + "\"!",
                    "Calificación guardada",
                    JOptionPane.INFORMATION_MESSAGE);
                calificacionGuardada = true;
                dispose();
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar calificación: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void establecerPropiedades() {
        setSize(500, 350);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    public boolean isCalificacionGuardada() {
        return calificacionGuardada;
    }
    
    public static boolean mostrar(JFrame parent, Pelicula pelicula, Usuario usuario) {
        CalificarPeliculaGUI dialog = new CalificarPeliculaGUI(parent, pelicula, usuario);
        dialog.setVisible(true);
        return dialog.isCalificacionGuardada();
    }
}
