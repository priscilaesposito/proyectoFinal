package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.Usuario;
import model.Pelicula;
import app.Logica;

public class VentanaPrincipalGUI extends JFrame {
    
    private Usuario usuario;
    private boolean esPrimerLogin;
    private List<Pelicula> peliculasActuales;
    
    // Componentes de la interfaz
    private JPanel mainPanel;
    private JPanel topBar;
    private JPanel contentPanel;
    private JLabel loadingLabel;
    private JTextField searchField;
    
    public VentanaPrincipalGUI(Usuario usuario) {
        this.usuario = usuario;
        
        inicializarComponentes();
        configurarDisenio();
        establecerPropiedadesVentana();
        
        // Cargar películas en segundo plano
        cargarPeliculasEnBackground();
    }
    
    private void inicializarComponentes() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        searchField = new JTextField(30);
        
        // Label de carga
        loadingLabel = new JLabel("Cargando contenido...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loadingLabel.setForeground(new Color(0, 122, 255));
    }
    
    private void configurarDisenio() {
        // Barra superior con datos de usuario, logout y buscador
        topBar = crearBarraSuperior();
        mainPanel.add(topBar, BorderLayout.NORTH);
        
        // Panel de contenido (inicialmente muestra pantalla de carga)
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(crearPantallaCarga(), BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
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
        searchButton.addActionListener(e -> buscarPelicula());
        
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
        
        // Agregar panels a la barra superior
        topBar.add(userInfoPanel, BorderLayout.WEST);
        topBar.add(searchPanel, BorderLayout.CENTER);
        topBar.add(logoutPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel crearPantallaCarga() {
        JPanel loadingPanel = new JPanel(new GridBagLayout());
        loadingPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        
        // Icono de carga (spinner)
        JLabel spinnerLabel = new JLabel("⏳");
        spinnerLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        loadingPanel.add(spinnerLabel, gbc);
        
        gbc.gridy = 1;
        loadingPanel.add(loadingLabel, gbc);
        
        gbc.gridy = 2;
        JLabel messageLabel = new JLabel("Por favor espere mientras cargamos el contenido...");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(Color.GRAY);
        loadingPanel.add(messageLabel, gbc);
        
        return loadingPanel;
    }
    
    private void cargarPeliculasEnBackground() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Verificar si es primer login
                esPrimerLogin = Logica.esPrimerLogin(usuario.getID_USUARIO());
                
                // Cargar películas según sea primer login o no
                if (esPrimerLogin) {
                    peliculasActuales = Logica.obtenerTop10Peliculas();
                } else {
                    peliculasActuales = Logica.obtener10PeliculasRandom(usuario.getID_USUARIO());
                }
                
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get(); // Verificar si hubo errores
                    mostrarPeliculas();
                    
                    // Marcar como no primer login si era el primero
                    if (esPrimerLogin) {
                        Logica.registrarPrimerLogin(usuario.getID_USUARIO());
                    }
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(VentanaPrincipalGUI.this,
                        "Error al cargar películas: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        };
        
        worker.execute();
    }
    
    private void mostrarPeliculas() {
        // Limpiar panel de contenido
        contentPanel.removeAll();
        
        // Crear panel con las películas
        JPanel peliculasPanel = new JPanel();
        peliculasPanel.setLayout(new BoxLayout(peliculasPanel, BoxLayout.Y_AXIS));
        peliculasPanel.setBackground(Color.WHITE);
        peliculasPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        String tituloSeccion = esPrimerLogin ? 
            "🌟 Top 10 Películas Mejor Rankeadas - ¡Califícalas!" :
            "🎬 Películas Recomendadas Para Ti";
        
        JLabel tituloLabel = new JLabel(tituloSeccion);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        tituloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        peliculasPanel.add(tituloLabel);
        peliculasPanel.add(Box.createVerticalStrut(20));
        
        // Agregar cada película
        for (Pelicula pelicula : peliculasActuales) {
            peliculasPanel.add(crearPanelPelicula(pelicula));
            peliculasPanel.add(Box.createVerticalStrut(15));
        }
        
        // Agregar scroll
        JScrollPane scrollPane = new JScrollPane(peliculasPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel crearPanelPelicula(Pelicula pelicula) {
        JPanel peliculaPanel = new JPanel(new BorderLayout(10, 10));
        peliculaPanel.setBackground(new Color(250, 250, 250));
        peliculaPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        peliculaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        // Panel izquierdo: Información de la película
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
    
    private JPanel crearPanelCalificacion(Pelicula pelicula) {
        JPanel ratingPanel = new JPanel();
        ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.Y_AXIS));
        ratingPanel.setBackground(new Color(250, 250, 250));
        
        JLabel instruccion = new JLabel("Tu calificación:");
        instruccion.setFont(new Font("Arial", Font.PLAIN, 12));
        instruccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Panel de estrellas
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
        try {
            boolean exito = Logica.calificarPelicula(
                usuario.getID_USUARIO(), 
                pelicula.getID(), 
                calificacion, 
                ""
            );
            
            if (exito) {
                JOptionPane.showMessageDialog(this,
                    "¡Gracias por calificar \"" + pelicula.getMetadatos().getTitulo() + "\"!",
                    "Calificación guardada",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Recargar películas si ya calificó todas las actuales
                verificarYRecargarPeliculas();
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar calificación: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void verificarYRecargarPeliculas() {
        // Por ahora no recargar automáticamente
        // Podría implementarse un contador de calificaciones
    }
    
    private void buscarPelicula() {
        String termino = searchField.getText().trim();
        
        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese un término de búsqueda",
                "Búsqueda",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Mostrar diálogo de búsqueda en OMDb
        buscarEnOMDb(termino);
    }
    
    private void buscarEnOMDb(String termino) {
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
                        JOptionPane.showMessageDialog(VentanaPrincipalGUI.this,
                            "No se encontró la película \"" + termino + "\"",
                            "Sin resultados",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(VentanaPrincipalGUI.this,
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
    
    public static void abrirVentanaPrincipal(Usuario usuario) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipalGUI(usuario).setVisible(true);
        });
    }
}
