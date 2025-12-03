package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.Usuario;
import model.Pelicula;
import app.Logica;
import utilidades.CargadorCSV;

public class VentanaPrincipalGUI extends JFrame {
    
    private Usuario usuario;
    private boolean esPrimerLogin;
    private List<Pelicula> peliculasActuales;
    
    private JPanel mainPanel;
    private JLabel loadingLabel;
    
    public VentanaPrincipalGUI(Usuario usuario) {
        this.usuario = usuario;
        
        inicializarComponentes();
        establecerPropiedadesVentana();
        
        // Hacer visible la ventana primero
        setVisible(true);
        
        // Cargar películas en segundo plano después de un pequeño delay
        Timer timer = new Timer(100, e -> cargarPeliculasEnBackground());
        timer.setRepeats(false);
        timer.start();
    }
    
    private void inicializarComponentes() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        
        // Icono de carga
        JLabel spinnerLabel = new JLabel("⏳");
        spinnerLabel.setFont(new Font("Arial", Font.PLAIN, 64));
        mainPanel.add(spinnerLabel, gbc);
        
        gbc.gridy = 1;
        loadingLabel = new JLabel("Cargando contenido...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loadingLabel.setForeground(new Color(0, 122, 255));
        mainPanel.add(loadingLabel, gbc);
        
        gbc.gridy = 2;
        JLabel messageLabel = new JLabel("Por favor espere mientras cargamos las películas...");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(Color.GRAY);
        mainPanel.add(messageLabel, gbc);
        
        // Panel principal con barra superior
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Color.WHITE);
        
        // Agregar barra superior con botones de control
        JPanel topBar = crearBarraSuperior();
        containerPanel.add(topBar, BorderLayout.NORTH);
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        
        add(containerPanel);
    }
    
    private JPanel crearBarraSuperior() {
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
    
    private void cargarPeliculasEnBackground() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Verificar si es primer login
                esPrimerLogin = Logica.esPrimerLogin(usuario.getID_USUARIO());
                
                // Si es primer acceso del usuario, verificar si hay que cargar el CSV
                if (esPrimerLogin) {
                    loadingLabel.setText("Importando películas desde movies_database.csv...");
                    
                    // Verificar si ya hay películas en la BD
                    if (!CargadorCSV.existenPeliculasEnBD()) {
                        // Cargar películas desde CSV (importa una por una, guarda en memoria y ordena)
                        List<Pelicula> todasOrdenadas = CargadorCSV.cargarPeliculasDesdeCSV();
                        
                        loadingLabel.setText("Seleccionando top 10 películas mejor rankeadas...");
                        // Tomar las primeras 10 (ya están ordenadas descendente por rating)
                        peliculasActuales = todasOrdenadas.subList(0, Math.min(10, todasOrdenadas.size()));
                    } else {
                        // Si ya existen películas, obtener top 10 de BD
                        loadingLabel.setText("Seleccionando top 10 películas mejor rankeadas...");
                        peliculasActuales = Logica.obtenerTop10Peliculas();
                    }
                } else {
                    loadingLabel.setText("Buscando películas para ti...");
                    peliculasActuales = Logica.obtener10PeliculasRandom(usuario.getID_USUARIO());
                }
                
                // Pequeña pausa para que el usuario vea el mensaje
                Thread.sleep(500);
                
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get(); // Verificar si hubo errores
                    
                    // Marcar como no primer login si era el primero
                    if (esPrimerLogin) {
                        Logica.registrarPrimerLogin(usuario.getID_USUARIO());
                    }
                    
                    // Cerrar ventana de carga y abrir ventana de películas
                    dispose();
                    PeliculasGUI.abrirVentanaPeliculas(usuario, peliculasActuales, esPrimerLogin);
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(VentanaPrincipalGUI.this,
                        "Error al cargar películas: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    dispose();
                    LoginGUI.startGUI();
                }
            }
        };
        
        worker.execute();
    }
    
    private void establecerPropiedadesVentana() {
        setTitle("Cargando...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    public static void abrirVentanaPrincipal(Usuario usuario) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipalGUI(usuario);
        });
    }
}
