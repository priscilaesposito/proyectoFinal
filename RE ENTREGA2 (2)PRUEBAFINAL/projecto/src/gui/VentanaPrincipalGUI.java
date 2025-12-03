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
        
        add(mainPanel);
    }
    
    private void cargarPeliculasEnBackground() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Pequeña pausa para asegurar que la pantalla de carga sea visible
                Thread.sleep(500);
                
                // Verificar si es primer login
                esPrimerLogin = Logica.esPrimerLogin(usuario.getID_USUARIO());
                
                // Cargar películas según sea primer login o no
                if (esPrimerLogin) {
                    peliculasActuales = Logica.obtenerTop10Peliculas();
                } else {
                    peliculasActuales = Logica.obtener10PeliculasRandom(usuario.getID_USUARIO());
                }
                
                // Pequeña pausa adicional para que el usuario vea la pantalla de carga
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
