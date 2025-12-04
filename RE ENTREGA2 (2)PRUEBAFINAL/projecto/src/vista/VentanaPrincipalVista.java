package vista;

import javax.swing.*;
import java.awt.*;
import model.Usuario;

/**
 * Vista de la Ventana Principal - Muestra pantalla de carga mientras se cargan
 * las películas con barra de progreso animada.
 */
public class VentanaPrincipalVista extends JFrame {

    private Usuario usuario;
    private JPanel mainPanel;
    private JLabel loadingLabel;
    private JProgressBar progressBar;
    private Timer animacionTimer;

    public VentanaPrincipalVista(Usuario usuario) {
        this.usuario = usuario;
        inicializarComponentes();
        establecerPropiedadesVentana();
        iniciarAnimacion();
    }

    private void inicializarComponentes() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        // Icono de carga
        JLabel spinnerLabel = new JLabel("🎬");
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

        // Agregar barra de progreso
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 40, 20, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(300, 25));
        progressBar.setStringPainted(true);
        progressBar.setString("Cargando...");
        progressBar.setForeground(new Color(0, 122, 255));
        mainPanel.add(progressBar, gbc);

        add(mainPanel);
    }

    /**
     * Inicia la animación del texto de carga
     */
    private void iniciarAnimacion() {
        final String[] mensajes = {
                "Cargando contenido...",
                "Preparando películas...",
                "Casi listo...",
                "Cargando contenido..."
        };
        final int[] indice = { 0 };

        animacionTimer = new Timer(800, e -> {
            loadingLabel.setText(mensajes[indice[0]]);
            indice[0] = (indice[0] + 1) % mensajes.length;
        });
        animacionTimer.start();
    }

    /**
     * Detiene la animación
     */
    public void detenerAnimacion() {
        if (animacionTimer != null) {
            animacionTimer.stop();
        }
    }

    @Override
    public void dispose() {
        detenerAnimacion();
        super.dispose();
    }

    private void establecerPropiedadesVentana() {
        setTitle("Cargando...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    // Getters
    public Usuario getUsuario() {
        return usuario;
    }

    public void actualizarMensaje(String mensaje) {
        loadingLabel.setText(mensaje);
    }
}
