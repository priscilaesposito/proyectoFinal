package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.List;
import model.Usuario;
import model.Pelicula;

/**
 * Vista de Películas - Solo maneja la presentación de la lista de películas.
 * No contiene lógica de negocio.
 */
public class PeliculasVista extends JFrame {
    
    private Usuario usuario;
    private boolean esPrimerLogin;
    private List<Pelicula> peliculasActuales;
    
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton logoutButton;
    
    // Listener para eventos de la vista
    private PeliculasVistaListener listener;
    
    public interface PeliculasVistaListener {
        void onBuscarPelicula(String termino);
        void onCerrarSesion();
        void onOrdenarPorTitulo(boolean ascendente);
        void onOrdenarPorGenero(boolean ascendente);
        void onCalificarPelicula(Pelicula pelicula, JButton boton);
    }
    
    public PeliculasVista(Usuario usuario, List<Pelicula> peliculas, boolean esPrimerLogin) {
        this.usuario = usuario;
        this.peliculasActuales = peliculas;
        this.esPrimerLogin = esPrimerLogin;
        
        inicializarComponentes();
        mostrarPeliculas();
        establecerPropiedadesVentana();
    }
    
    public void setListener(PeliculasVistaListener listener) {
        this.listener = listener;
    }
    
    private void inicializarComponentes() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        JPanel topBarControl = crearBarraSuperiorControl();
        mainPanel.add(topBarControl, BorderLayout.NORTH);
        
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Color.WHITE);
        
        JPanel topBar = crearBarraSuperior();
        containerPanel.add(topBar, BorderLayout.NORTH);
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        containerPanel.add(contentPanel, BorderLayout.CENTER);
        
        mainPanel.add(containerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel crearBarraSuperiorControl() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(0, 50));
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        JLabel titleLabel = new JLabel("Plataforma de Streaming");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topBar.add(titleLabel, BorderLayout.WEST);
        
        JPanel controlButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        controlButtonsPanel.setBackground(Color.WHITE);
        
        JButton minimizeButton = crearBotonControlVentana(new Color(255, 189, 68));
        minimizeButton.setText("−");
        minimizeButton.setFont(new Font("Arial", Font.BOLD, 16));
        minimizeButton.setForeground(Color.WHITE);
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));
        
        JButton maximizeButton = crearBotonControlVentana(new Color(39, 201, 63));
        maximizeButton.setText("□");
        maximizeButton.setFont(new Font("Arial", Font.BOLD, 12));
        maximizeButton.setForeground(Color.WHITE);
        maximizeButton.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        
        JButton closeButton = crearBotonControlVentana(new Color(0, 122, 255));
        closeButton.setText("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> System.exit(0));
        
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
    
    private JPanel crearBarraSuperior() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(245, 245, 245));
        topBar.setPreferredSize(new Dimension(0, 100));
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userInfoPanel.setBackground(new Color(245, 245, 245));
        
        JLabel welcomeLabel = new JLabel("Bienvenido/a, " + usuario.getNombre() + " " + usuario.getApellido());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userInfoPanel.add(welcomeLabel);
        
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.setBackground(new Color(245, 245, 245));
        
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortPanel.setBackground(new Color(245, 245, 245));
        controlsPanel.add(sortPanel, BorderLayout.WEST);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(new Color(245, 245, 245));
        
        searchField = new JTextField(25);
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        searchButton = new JButton("🔍 Buscar");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(new Color(0, 122, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> {
            if (listener != null) {
                listener.onBuscarPelicula(searchField.getText().trim());
            }
        });
        
        searchPanel.add(new JLabel("Buscar película: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        controlsPanel.add(searchPanel, BorderLayout.CENTER);
        
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(new Color(245, 245, 245));
        
        logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            if (listener != null) {
                listener.onCerrarSesion();
            }
        });
        
        logoutPanel.add(logoutButton);
        
        controlsPanel.add(logoutPanel, BorderLayout.EAST);
        
        JPanel combinedPanel = new JPanel(new BorderLayout());
        combinedPanel.setBackground(new Color(245, 245, 245));
        combinedPanel.add(userInfoPanel, BorderLayout.NORTH);
        combinedPanel.add(controlsPanel, BorderLayout.CENTER);
        
        topBar.add(combinedPanel, BorderLayout.CENTER);
        
        return topBar;
    }
    
    public void mostrarPeliculas() {
        contentPanel.removeAll();
        
        JPanel peliculasPanel = new JPanel();
        peliculasPanel.setLayout(new BoxLayout(peliculasPanel, BoxLayout.Y_AXIS));
        peliculasPanel.setBackground(Color.WHITE);
        peliculasPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String tituloSeccion = esPrimerLogin ? 
            "🌟 Top 10 Películas Mejor Rankeadas - ¡Califícalas!" :
            "Seguro viste alguna de estas películas, haznos saber que te pareció dejando una reseña";
        
        JLabel tituloLabel = new JLabel(tituloSeccion);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        tituloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        peliculasPanel.add(tituloLabel);
        peliculasPanel.add(Box.createVerticalStrut(20));
        
        JPanel headerPanel = crearCabeceraTabla();
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        peliculasPanel.add(headerPanel);
        peliculasPanel.add(Box.createVerticalStrut(10));
        
        for (Pelicula pelicula : peliculasActuales) {
            JPanel filaPanel = crearFilaPelicula(pelicula);
            filaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            peliculasPanel.add(filaPanel);
            peliculasPanel.add(Box.createVerticalStrut(5));
        }
        
        JScrollPane scrollPane = new JScrollPane(peliculasPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel crearCabeceraTabla() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JLabel posterHeader = new JLabel("Poster");
        posterHeader.setFont(new Font("Arial", Font.BOLD, 14));
        posterHeader.setPreferredSize(new Dimension(140, 30));
        posterHeader.setMinimumSize(new Dimension(140, 30));
        posterHeader.setMaximumSize(new Dimension(140, 30));
        headerPanel.add(posterHeader);
        
        headerPanel.add(Box.createHorizontalStrut(10));
        
        JPanel tituloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        tituloPanel.setBackground(new Color(240, 240, 240));
        tituloPanel.setPreferredSize(new Dimension(200, 30));
        tituloPanel.setMinimumSize(new Dimension(200, 30));
        tituloPanel.setMaximumSize(new Dimension(200, 30));
        
        JLabel titleHeader = new JLabel("Título");
        titleHeader.setFont(new Font("Arial", Font.BOLD, 14));
        tituloPanel.add(titleHeader);
        
        JPanel flechasTituloPanel = new JPanel();
        flechasTituloPanel.setLayout(new BoxLayout(flechasTituloPanel, BoxLayout.Y_AXIS));
        flechasTituloPanel.setBackground(new Color(240, 240, 240));
        
        JButton tituloAscButton = new JButton("▲");
        tituloAscButton.setFont(new Font("Arial", Font.PLAIN, 8));
        tituloAscButton.setPreferredSize(new Dimension(18, 15));
        tituloAscButton.setMaximumSize(new Dimension(18, 15));
        tituloAscButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        tituloAscButton.setFocusPainted(false);
        tituloAscButton.addActionListener(e -> {
            if (listener != null) listener.onOrdenarPorTitulo(true);
        });
        
        JButton tituloDescButton = new JButton("▼");
        tituloDescButton.setFont(new Font("Arial", Font.PLAIN, 8));
        tituloDescButton.setPreferredSize(new Dimension(18, 15));
        tituloDescButton.setMaximumSize(new Dimension(18, 15));
        tituloDescButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        tituloDescButton.setFocusPainted(false);
        tituloDescButton.addActionListener(e -> {
            if (listener != null) listener.onOrdenarPorTitulo(false);
        });
        
        flechasTituloPanel.add(tituloAscButton);
        flechasTituloPanel.add(tituloDescButton);
        tituloPanel.add(flechasTituloPanel);
        
        headerPanel.add(tituloPanel);
        
        headerPanel.add(Box.createHorizontalStrut(10));
        
        JPanel generoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        generoPanel.setBackground(new Color(240, 240, 240));
        generoPanel.setPreferredSize(new Dimension(150, 30));
        generoPanel.setMinimumSize(new Dimension(150, 30));
        generoPanel.setMaximumSize(new Dimension(150, 30));
        
        JLabel genreHeader = new JLabel("Genero");
        genreHeader.setFont(new Font("Arial", Font.BOLD, 14));
        generoPanel.add(genreHeader);
        
        JPanel flechasGeneroPanel = new JPanel();
        flechasGeneroPanel.setLayout(new BoxLayout(flechasGeneroPanel, BoxLayout.Y_AXIS));
        flechasGeneroPanel.setBackground(new Color(240, 240, 240));
        
        JButton generoAscButton = new JButton("▲");
        generoAscButton.setFont(new Font("Arial", Font.PLAIN, 8));
        generoAscButton.setPreferredSize(new Dimension(18, 15));
        generoAscButton.setMaximumSize(new Dimension(18, 15));
        generoAscButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        generoAscButton.setFocusPainted(false);
        generoAscButton.addActionListener(e -> {
            if (listener != null) listener.onOrdenarPorGenero(true);
        });
        
        JButton generoDescButton = new JButton("▼");
        generoDescButton.setFont(new Font("Arial", Font.PLAIN, 8));
        generoDescButton.setPreferredSize(new Dimension(18, 15));
        generoDescButton.setMaximumSize(new Dimension(18, 15));
        generoDescButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        generoDescButton.setFocusPainted(false);
        generoDescButton.addActionListener(e -> {
            if (listener != null) listener.onOrdenarPorGenero(false);
        });
        
        flechasGeneroPanel.add(generoAscButton);
        flechasGeneroPanel.add(generoDescButton);
        generoPanel.add(flechasGeneroPanel);
        
        headerPanel.add(generoPanel);
        
        headerPanel.add(Box.createHorizontalStrut(10));
        
        JLabel resumeHeader = new JLabel("Resumen");
        resumeHeader.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(resumeHeader);
        
        headerPanel.add(Box.createHorizontalGlue());
        
        JLabel actionHeader = new JLabel("");
        actionHeader.setPreferredSize(new Dimension(100, 30));
        actionHeader.setMinimumSize(new Dimension(100, 30));
        actionHeader.setMaximumSize(new Dimension(100, 30));
        headerPanel.add(actionHeader);
        
        return headerPanel;
    }
    
    private JPanel crearFilaPelicula(Pelicula pelicula) {
        JPanel filaPanel = new JPanel();
        filaPanel.setLayout(new BoxLayout(filaPanel, BoxLayout.X_AXIS));
        filaPanel.setBackground(Color.WHITE);
        filaPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        filaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        JLabel posterLabel = crearPosterLabelTabla(pelicula);
        filaPanel.add(posterLabel);
        
        filaPanel.add(Box.createHorizontalStrut(10));
        
        JLabel tituloLabel = new JLabel("<html>" + pelicula.getMetadatos().getTitulo() + "</html>");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 13));
        tituloLabel.setPreferredSize(new Dimension(200, 100));
        tituloLabel.setMinimumSize(new Dimension(200, 100));
        tituloLabel.setVerticalAlignment(JLabel.TOP);
        filaPanel.add(tituloLabel);
        
        filaPanel.add(Box.createHorizontalStrut(10));
        
        String generos = String.join(", ", pelicula.getGeneros());
        JLabel generoLabel = new JLabel(generos);
        generoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        generoLabel.setForeground(Color.GRAY);
        generoLabel.setPreferredSize(new Dimension(150, 100));
        generoLabel.setMinimumSize(new Dimension(150, 100));
        generoLabel.setVerticalAlignment(JLabel.TOP);
        filaPanel.add(generoLabel);
        
        filaPanel.add(Box.createHorizontalStrut(10));
        
        String resumen = pelicula.getMetadatos().getSipnosis();
        if (resumen == null || resumen.isEmpty()) {
            resumen = "Texto del resumen";
        }
        
        JTextArea resumenArea = new JTextArea(resumen);
        resumenArea.setFont(new Font("Arial", Font.PLAIN, 11));
        resumenArea.setForeground(Color.DARK_GRAY);
        resumenArea.setBackground(Color.WHITE);
        resumenArea.setLineWrap(true);
        resumenArea.setWrapStyleWord(true);
        resumenArea.setEditable(false);
        resumenArea.setFocusable(false);
        resumenArea.setBorder(null);
        resumenArea.setPreferredSize(new Dimension(380, 100));
        resumenArea.setMaximumSize(new Dimension(380, 100));
        filaPanel.add(resumenArea);
        
        filaPanel.add(Box.createHorizontalGlue());
        
        JButton calificarButton = new JButton("Calificar");
        calificarButton.setFont(new Font("Arial", Font.BOLD, 12));
        calificarButton.setBackground(new Color(0, 122, 255));
        calificarButton.setForeground(Color.WHITE);
        calificarButton.setOpaque(true);
        calificarButton.setBorderPainted(false);
        calificarButton.setFocusPainted(false);
        calificarButton.setPreferredSize(new Dimension(100, 35));
        calificarButton.setMinimumSize(new Dimension(100, 35));
        calificarButton.setMaximumSize(new Dimension(100, 35));
        calificarButton.addActionListener(e -> {
            if (listener != null) {
                listener.onCalificarPelicula(pelicula, calificarButton);
            }
        });
        
        filaPanel.add(calificarButton);
        
        return filaPanel;
    }
    
    private JLabel crearPosterLabelTabla(Pelicula pelicula) {
        JLabel posterLabel = new JLabel();
        posterLabel.setPreferredSize(new Dimension(60, 80));
        posterLabel.setMinimumSize(new Dimension(60, 80));
        posterLabel.setMaximumSize(new Dimension(60, 80));
        posterLabel.setHorizontalAlignment(JLabel.CENTER);
        posterLabel.setVerticalAlignment(JLabel.CENTER);
        posterLabel.setOpaque(true);
        posterLabel.setBackground(new Color(240, 240, 240));
        posterLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        String posterUrl = pelicula.getPoster();
        if (posterUrl != null && !posterUrl.isEmpty() && !posterUrl.equals("N/A")) {
            SwingWorker<ImageIcon, Void> worker = new SwingWorker<ImageIcon, Void>() {
                @Override
                protected ImageIcon doInBackground() throws Exception {
                    try {
                        URL url = new URL(posterUrl);
                        BufferedImage img = ImageIO.read(url);
                        if (img != null) {
                            Image scaledImg = img.getScaledInstance(60, 80, Image.SCALE_SMOOTH);
                            return new ImageIcon(scaledImg);
                        }
                    } catch (Exception e) {
                        // Si falla, retornar null
                    }
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        ImageIcon icon = get();
                        if (icon != null) {
                            posterLabel.setIcon(icon);
                            posterLabel.setText("");
                        } else {
                            mostrarImagenNoDisponiblePequenia(posterLabel);
                        }
                    } catch (Exception e) {
                        mostrarImagenNoDisponiblePequenia(posterLabel);
                    }
                }
            };
            
            posterLabel.setText("⏳");
            posterLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            worker.execute();
        } else {
            mostrarImagenNoDisponiblePequenia(posterLabel);
        }
        
        return posterLabel;
    }
    
    private void mostrarImagenNoDisponiblePequenia(JLabel label) {
        label.setText("<html><center><small>Imagen No<br>Disponible</small></center></html>");
        label.setFont(new Font("Arial", Font.PLAIN, 9));
        label.setForeground(Color.GRAY);
    }
    
    private void establecerPropiedadesVentana() {
        setTitle("Plataforma de Streaming - " + usuario.getNombre() + " " + usuario.getApellido());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }
    
    // Getters
    public Usuario getUsuario() {
        return usuario;
    }
    
    public List<Pelicula> getPeliculasActuales() {
        return peliculasActuales;
    }
    
    // Métodos para actualizar la vista
    public void actualizarPeliculas(List<Pelicula> peliculas) {
        this.peliculasActuales = peliculas;
        mostrarPeliculas();
    }
    
    public void mostrarMensaje(String titulo, String mensaje, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
    
    public int mostrarConfirmacion(String titulo, String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, titulo, JOptionPane.YES_NO_OPTION);
    }
    
    public JDialog crearDialogoCargando(String mensaje) {
        JDialog loadingDialog = new JDialog(this, "Buscando...", true);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(this);
        
        JLabel loadingMsg = new JLabel(mensaje, SwingConstants.CENTER);
        loadingDialog.add(loadingMsg);
        
        return loadingDialog;
    }
}
