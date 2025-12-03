package vista;

import javax.swing.*;
import java.awt.*;
import model.Pelicula;
import model.Usuario;

/**
 * Vista para calificar una película - Solo maneja la presentación.
 * No contiene lógica de negocio.
 */
public class CalificarPeliculaVista extends JDialog {
    
    private Pelicula pelicula;
    private Usuario usuario;
    private JComboBox<Integer> ratingCombo;
    private JTextArea reseniaArea;
    private JButton guardarButton;
    
    public CalificarPeliculaVista(JFrame parent, Pelicula pelicula, Usuario usuario) {
        super(parent, "Calificar Película", true);
        this.pelicula = pelicula;
        this.usuario = usuario;
        
        inicializarComponentes();
        establecerPropiedades();
    }
    
    private void inicializarComponentes() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Color.WHITE);
        
        JPanel topBar = crearBarraSuperior();
        containerPanel.add(topBar, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        JLabel tituloLabel = new JLabel("Título de la Película");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(tituloLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        ratingPanel.setBackground(Color.WHITE);
        ratingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel calificacionLabel = new JLabel("Calificación:");
        calificacionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ratingPanel.add(calificacionLabel);
        
        Integer[] ratings = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ratingCombo = new JComboBox<>(ratings);
        ratingCombo.setSelectedIndex(7);
        ratingCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        ratingPanel.add(ratingCombo);
        
        centerPanel.add(ratingPanel);
        centerPanel.add(Box.createVerticalStrut(15));
        
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
        
        guardarButton = new JButton("Guardar");
        guardarButton.setFont(new Font("Arial", Font.BOLD, 12));
        guardarButton.setBackground(new Color(0, 122, 255));
        guardarButton.setForeground(Color.WHITE);
        guardarButton.setFocusPainted(false);
        guardarButton.setOpaque(true);
        guardarButton.setBorderPainted(false);
        guardarButton.setPreferredSize(new Dimension(100, 35));
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(guardarButton);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        setContentPane(containerPanel);
    }
    
    private JPanel crearBarraSuperior() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(0, 40));
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        JLabel titleLabel = new JLabel("Calificar Película");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topBar.add(titleLabel, BorderLayout.WEST);
        
        JPanel controlButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        controlButtonsPanel.setBackground(Color.WHITE);
        
        JButton minimizeButton = crearBotonControlVentana(new Color(255, 189, 68));
        minimizeButton.setText("−");
        minimizeButton.setFont(new Font("Arial", Font.BOLD, 16));
        minimizeButton.setForeground(Color.WHITE);
        minimizeButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                ((JFrame) window).setState(JFrame.ICONIFIED);
            }
        });
        
        JButton maximizeButton = crearBotonControlVentana(new Color(39, 201, 63));
        maximizeButton.setText("□");
        maximizeButton.setFont(new Font("Arial", Font.BOLD, 12));
        maximizeButton.setForeground(Color.WHITE);
        maximizeButton.setEnabled(false);
        
        JButton closeButton = crearBotonControlVentana(new Color(0, 122, 255));
        closeButton.setText("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> dispose());
        
        controlButtonsPanel.add(minimizeButton);
        controlButtonsPanel.add(maximizeButton);
        controlButtonsPanel.add(closeButton);
        
        topBar.add(controlButtonsPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JButton crearBotonControlVentana(Color color) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(12, 12));
        button.setBackground(color);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        return button;
    }
    
    private void establecerPropiedades() {
        setSize(500, 390);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    // Getters
    public JButton getGuardarButton() {
        return guardarButton;
    }
    
    public int getCalificacion() {
        return (Integer) ratingCombo.getSelectedItem();
    }
    
    public String getResenia() {
        return reseniaArea.getText().trim();
    }
    
    public Pelicula getPelicula() {
        return pelicula;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    // Métodos para mostrar mensajes
    public void mostrarAdvertencia(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }
    
    public void mostrarExito(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarError(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }
}
