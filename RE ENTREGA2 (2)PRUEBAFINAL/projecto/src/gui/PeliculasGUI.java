package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.List;
import model.Usuario;
import model.Pelicula;
import app.Logica;

public class PeliculasGUI extends JFrame {
    
    private Usuario usuario;
    private boolean esPrimerLogin;
    private List<Pelicula> peliculasActuales;
    
    private JPanel mainPanel;
    private JPanel contentPanel;
    
    public PeliculasGUI(Usuario usuario, List<Pelicula> peliculas, boolean esPrimerLogin) {
        this.usuario = usuario;
        this.peliculasActuales = peliculas;
        this.esPrimerLogin = esPrimerLogin;
        
        inicializarComponentes();
        mostrarPeliculas();
        establecerPropiedadesVentana();
    }
    
    private void inicializarComponentes() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Barra superior con botones de control
        JPanel topBarControl = crearBarraSuperiorControl();
        mainPanel.add(topBarControl, BorderLayout.NORTH);
        
        // Panel container para barra de usuario y contenido
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Color.WHITE);
        
        // Barra con datos de usuario y búsqueda
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
        
        // Panel de botones de control
        JPanel controlButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        controlButtonsPanel.setBackground(Color.WHITE);
        
        // Botón minimizar (amarillo)
        JButton minimizeButton = crearBotonControlVentana(new Color(255, 189, 68));
        minimizeButton.setText("−");
        minimizeButton.setFont(new Font("Arial", Font.BOLD, 16));
        minimizeButton.setForeground(Color.WHITE);
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));
        
        // Botón maximizar (verde)
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
        
        // Botón cerrar (azul)
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
        topBar.setPreferredSize(new Dimension(0, 80));
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Panel izquierdo: Datos del usuario
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userInfoPanel.setBackground(new Color(245, 245, 245));
        
        JLabel welcomeLabel = new JLabel("Bienvenido/a, " + usuario.getUsername());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userInfoPanel.add(welcomeLabel);
        
        JLabel emailLabel = new JLabel(" (" + usuario.getCorreo() + ")");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emailLabel.setForeground(Color.GRAY);
        userInfoPanel.add(emailLabel);
        
        // Panel central: Buscador
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(new Color(245, 245, 245));
        
        JTextField searchField = new JTextField(25);
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JButton searchButton = new JButton("🔍 Buscar");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(new Color(0, 122, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> buscarPelicula(searchField.getText().trim()));
        
        searchPanel.add(new JLabel("Buscar película: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // Panel derecho: Botón de cerrar sesión
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(new Color(245, 245, 245));
        
        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> cerrarSesion());
        
        logoutPanel.add(logoutButton);
        
        topBar.add(userInfoPanel, BorderLayout.WEST);
        topBar.add(searchPanel, BorderLayout.CENTER);
        topBar.add(logoutPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private void mostrarPeliculas() {
        contentPanel.removeAll();
        
        JPanel peliculasPanel = new JPanel();
        peliculasPanel.setLayout(new BoxLayout(peliculasPanel, BoxLayout.Y_AXIS));
        peliculasPanel.setBackground(Color.WHITE);
        peliculasPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String tituloSeccion = esPrimerLogin ? 
            "🌟 Top 10 Películas Mejor Rankeadas - ¡Califícalas!" :
            "🎬 Películas Recomendadas Para Ti";
        
        JLabel tituloLabel = new JLabel(tituloSeccion);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        tituloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        peliculasPanel.add(tituloLabel);
        peliculasPanel.add(Box.createVerticalStrut(20));
        
        for (Pelicula pelicula : peliculasActuales) {
            peliculasPanel.add(crearPanelPelicula(pelicula));
            peliculasPanel.add(Box.createVerticalStrut(15));
        }
        
        JScrollPane scrollPane = new JScrollPane(peliculasPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel crearPanelPelicula(Pelicula pelicula) {
        JPanel peliculaPanel = new JPanel(new BorderLayout(15, 10));
        peliculaPanel.setBackground(new Color(250, 250, 250));
        peliculaPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        peliculaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        // Panel izquierdo: Poster
        JLabel posterLabel = crearPosterLabel(pelicula);
        peliculaPanel.add(posterLabel, BorderLayout.WEST);
        
        // Panel central: Información de la película
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(250, 250, 250));
        
        JLabel tituloLabel = new JLabel(pelicula.getMetadatos().getTitulo());
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));
        tituloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel directorLabel = new JLabel("Director: " + pelicula.getMetadatos().getDirector());
        directorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        directorLabel.setForeground(Color.GRAY);
        directorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel generoLabel = new JLabel("Género: " + String.join(", ", pelicula.getGeneros()));
        generoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        generoLabel.setForeground(Color.GRAY);
        generoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel anioLabel = new JLabel("Año: " + pelicula.getAnio());
        anioLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        anioLabel.setForeground(Color.GRAY);
        anioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel ratingLabel = new JLabel("⭐ Rating: " + String.format("%.1f", pelicula.getRatingPromedio()) + "/10");
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        ratingLabel.setForeground(new Color(255, 165, 0));
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(tituloLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(directorLabel);
        infoPanel.add(generoLabel);
        infoPanel.add(anioLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(ratingLabel);
        
        // Panel derecho: Calificación
        JPanel ratingPanel = crearPanelCalificacion(pelicula);
        
        peliculaPanel.add(infoPanel, BorderLayout.CENTER);
        peliculaPanel.add(ratingPanel, BorderLayout.EAST);
        
        return peliculaPanel;
    }
    
    private JLabel crearPosterLabel(Pelicula pelicula) {
        JLabel posterLabel = new JLabel();
        posterLabel.setPreferredSize(new Dimension(120, 180));
        posterLabel.setMinimumSize(new Dimension(120, 180));
        posterLabel.setMaximumSize(new Dimension(120, 180));
        posterLabel.setHorizontalAlignment(JLabel.CENTER);
        posterLabel.setVerticalAlignment(JLabel.CENTER);
        posterLabel.setOpaque(true);
        posterLabel.setBackground(new Color(230, 230, 230));
        posterLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        // Cargar poster en segundo plano
        String posterUrl = pelicula.getPoster();
        if (posterUrl != null && !posterUrl.isEmpty() && !posterUrl.equals("N/A")) {
            SwingWorker<ImageIcon, Void> worker = new SwingWorker<ImageIcon, Void>() {
                @Override
                protected ImageIcon doInBackground() throws Exception {
                    try {
                        URL url = new URL(posterUrl);
                        BufferedImage img = ImageIO.read(url);
                        if (img != null) {
                            Image scaledImg = img.getScaledInstance(120, 180, Image.SCALE_SMOOTH);
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
                            posterLabel.setText("🎬");
                            posterLabel.setFont(new Font("Arial", Font.PLAIN, 48));
                        }
                    } catch (Exception e) {
                        posterLabel.setText("🎬");
                        posterLabel.setFont(new Font("Arial", Font.PLAIN, 48));
                    }
                }
            };
            
            posterLabel.setText("⏳");
            posterLabel.setFont(new Font("Arial", Font.PLAIN, 32));
            worker.execute();
        } else {
            posterLabel.setText("🎬");
            posterLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        }
        
        return posterLabel;
    }
    
    private JPanel crearPanelCalificacion(Pelicula pelicula) {
        JPanel ratingPanel = new JPanel();
        ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.Y_AXIS));
        ratingPanel.setBackground(new Color(250, 250, 250));
        
        JLabel instruccion = new JLabel("Tu calificación:");
        instruccion.setFont(new Font("Arial", Font.PLAIN, 12));
        instruccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel starPanel = new JPanel(new FlowLayout());
        starPanel.setBackground(new Color(250, 250, 250));
        
        JComboBox<Integer> ratingCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        ratingCombo.setSelectedIndex(7); // Default 8/10
        
        JButton calificarButton = new JButton("Calificar");
        calificarButton.setFont(new Font("Arial", Font.BOLD, 12));
        calificarButton.setBackground(new Color(40, 167, 69));
        calificarButton.setForeground(Color.WHITE);
        calificarButton.setOpaque(true);
        calificarButton.setBorderPainted(false);
        calificarButton.setFocusPainted(false);
        
        calificarButton.addActionListener(e -> {
            int calificacion = (Integer) ratingCombo.getSelectedItem();
            calificarPelicula(pelicula, calificacion);
        });
        
        starPanel.add(ratingCombo);
        starPanel.add(calificarButton);
        
        ratingPanel.add(instruccion);
        ratingPanel.add(Box.createVerticalStrut(5));
        ratingPanel.add(starPanel);
        
        return ratingPanel;
    }
    
    private void calificarPelicula(Pelicula pelicula, int calificacion) {
        // Crear diálogo para ingresar la reseña
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel instruccion = new JLabel("Ingrese su calificación y reseña:");
        instruccion.setFont(new Font("Arial", Font.BOLD, 12));
        
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratingPanel.add(new JLabel("Calificación:"));
        JLabel ratingValue = new JLabel(calificacion + "/10");
        ratingValue.setFont(new Font("Arial", Font.BOLD, 14));
        ratingValue.setForeground(new Color(255, 165, 0));
        ratingPanel.add(ratingValue);
        
        JLabel reseniaLabel = new JLabel("Reseña:");
        JTextArea reseniaArea = new JTextArea(5, 30);
        reseniaArea.setLineWrap(true);
        reseniaArea.setWrapStyleWord(true);
        reseniaArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JScrollPane scrollPane = new JScrollPane(reseniaArea);
        
        panel.add(instruccion, BorderLayout.NORTH);
        panel.add(ratingPanel, BorderLayout.CENTER);
        
        JPanel reseniaPanel = new JPanel(new BorderLayout(5, 5));
        reseniaPanel.add(reseniaLabel, BorderLayout.NORTH);
        reseniaPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(reseniaPanel, BorderLayout.SOUTH);
        
        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Calificar: " + pelicula.getMetadatos().getTitulo(),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String resenia = reseniaArea.getText().trim();
            
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
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al guardar calificación: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void buscarPelicula(String termino) {
        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese un término de búsqueda",
                "Búsqueda",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Mostrar diálogo de búsqueda
        JDialog loadingDialog = new JDialog(this, "Buscando...", true);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(this);
        
        JLabel loadingMsg = new JLabel("Buscando en OMDb: " + termino + "...", SwingConstants.CENTER);
        loadingDialog.add(loadingMsg);
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private org.json.JSONObject resultado;
            
            @Override
            protected Void doInBackground() throws Exception {
                resultado = Logica.buscarPeliculaOMDb(termino);
                return null;
            }
            
            @Override
            protected void done() {
                loadingDialog.dispose();
                
                try {
                    get();
                    
                    if (resultado != null) {
                        mostrarResultadoBusqueda(resultado);
                    } else {
                        JOptionPane.showMessageDialog(PeliculasGUI.this,
                            "No se encontró la película \"" + termino + "\"",
                            "Sin resultados",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PeliculasGUI.this,
                        "Error en la búsqueda: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
        loadingDialog.setVisible(true);
    }
    
    private void mostrarResultadoBusqueda(org.json.JSONObject pelicula) {
        String info = "🎬 " + pelicula.getString("Title") + "\n\n" +
                     "📅 Año: " + pelicula.getString("Year") + "\n" +
                     "🎭 Género: " + pelicula.getString("Genre") + "\n" +
                     "🎬 Director: " + pelicula.getString("Director") + "\n" +
                     "⭐ Rating IMDb: " + pelicula.optString("imdbRating", "N/A") + "/10\n" +
                     "⏱️ Duración: " + pelicula.optString("Runtime", "N/A") + "\n\n" +
                     "📖 Sinopsis:\n" + pelicula.getString("Plot");
        
        JOptionPane.showMessageDialog(this,
            info,
            "Resultado de Búsqueda",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            LoginGUI.startGUI();
        }
    }
    
    private void establecerPropiedadesVentana() {
        setTitle("Plataforma de Streaming - " + usuario.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }
    
    public static void abrirVentanaPeliculas(Usuario usuario, List<Pelicula> peliculas, boolean esPrimerLogin) {
        SwingUtilities.invokeLater(() -> {
            new PeliculasGUI(usuario, peliculas, esPrimerLogin).setVisible(true);
        });
    }
}
