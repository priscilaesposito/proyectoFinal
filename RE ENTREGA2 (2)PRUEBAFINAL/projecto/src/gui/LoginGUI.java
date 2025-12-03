package gui;

import javax.swing.*;

import app.Logica;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Usuario;
import recursos.GestionRecursos;

public class LoginGUI extends JFrame {
    
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;
    private JPanel mainPanel;
    
    public LoginGUI() {
        inicializarComponentes();
        configurarDisenio();
        configurarEventos();
        establecerPropiedadesVentana();
    }
    
    private void inicializarComponentes() {
        // Componentes principales
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Ingresar");
        registerButton = new JButton("Regístrate");
        statusLabel = new JLabel(" ");
        
        // Panel principal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
    }
    
    private void configurarDisenio() {
        // Panel 1: Barra superior con título y botones de control
        JPanel topBar = crearBarraSuperior();
        
        // Panel 2: Panel izquierdo con imagen del perrito (3/4 del ancho)
        JPanel leftPanel = crearPanelImagen();
        
        // Panel 3: Panel derecho con formulario de login (1/4 del ancho)
        JPanel rightPanel = crearPanelLogin();
        
        // Panel contenedor para los paneles inferiores
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.add(leftPanel, BorderLayout.CENTER);
        bottomContainer.add(rightPanel, BorderLayout.EAST);
        
        // Agregar paneles al panel principal
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(bottomContainer, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel crearBarraSuperior() {
        // Barra superior delgada que ocupa todo el ancho
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(0, 50)); // Altura fija, ancho automático
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Título de bienvenida a la izquierda
        JLabel welcomeTitle = new JLabel("Bienvenido a la Plataforma de Streaming");
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 16));
        topBar.add(welcomeTitle, BorderLayout.WEST);
        
        // Panel de botones de control a la derecha
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
    
    private JPanel crearPanelImagen() {
        // Panel izquierdo con la imagen del perrito (ocupa 3/4 del espacio)
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(255, 223, 186));
        imagePanel.setPreferredSize(new Dimension(600, 400)); // Altura reducida
        
        // Cargar imagen usando ResourceManager con fallback automático
        ImageIcon dogStreamingIcon = GestionRecursos.loadImageWithFallback("perrito.png", 500, 380);
        JLabel imageLabel = new JLabel(dogStreamingIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        return imagePanel;
    }
    
    private JButton crearBotonControlVentana(Color color) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(12, 12));
        button.setBackground(color);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        
        // Hacer el botón circular
        button.setBorderPainted(false);
        
        return button;
    }

    private JPanel crearPanelLogin() {
        // Panel derecho con formulario de login (ocupa 1/4 del espacio)
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setPreferredSize(new Dimension(300, 400)); // Altura reducida
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Campo E-mail
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(new JLabel("E-mail:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        emailField.setPreferredSize(new Dimension(150, 30));
        loginPanel.add(emailField, gbc);
        
        // Campo Password
        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        passwordField.setPreferredSize(new Dimension(150, 30));
        loginPanel.add(passwordField, gbc);
        
        // Botón Ingresar
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        loginButton.setBackground(new Color(51, 153, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginPanel.add(loginButton, gbc);
        
        // Mensaje "¿Aún no sos usuario?" y botón Regístrate
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel registerPanel = new JPanel(new FlowLayout());
        registerPanel.setBackground(Color.WHITE);
        registerPanel.add(new JLabel("¿Aún no sos usuario?"));
        
        registerButton.setBackground(new Color(51, 153, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 12));
        registerButton.setOpaque(true);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerPanel.add(registerButton);
        
        loginPanel.add(registerPanel, gbc);
        
        // Label para mensajes de estado
        gbc.gridy = 4;
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        loginPanel.add(statusLabel, gbc);
        
        return loginPanel;
    }
    
    private void configurarEventos() {
        // Acción del botón de login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        // Acción del botón de registro
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaRegistro();
            }
        });
        
        // Enter en el campo de password ejecuta login
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
    }
    
    private void realizarLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validaciones básicas
        if (email.isEmpty() || password.isEmpty()) {
            mostrarMensajeError("E-mail y Password son campos obligatorios");
            return;
        }
        
        if (!esEmailValido(email)) {
            mostrarMensajeError("Formato de E-mail inválido");
            return;
        }
        
        // Intentar login
        try {
            statusLabel.setText("Verificando credenciales...");
            statusLabel.setForeground(Color.BLUE);
            
            Usuario usuario = Logica.login(email, password);
            
            if (usuario != null) {
                mostrarMensajeExito("¡Bienvenido/a, " + usuario.getUsername() + "!");
                // Aquí puedes abrir la ventana principal
                abrirVentanaPrincipal(usuario);
                this.dispose(); // Cerrar ventana de login
            } else {
                mostrarMensajeError("E-mail o Password incorrectos");
            }
            
        } catch (Exception ex) {
            mostrarMensajeError("Error de conexión: " + ex.getMessage());
        }
    }
    
    private void abrirVentanaRegistro() {
        RegistroGUI.abrirVentanaRegistro();
    }
    
    private void abrirVentanaPrincipal(Usuario usuario) {
        VentanaPrincipalGUI.abrirVentanaPrincipal(usuario);
    }
    
    private boolean esEmailValido(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }
    
    private void mostrarMensajeError(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(Color.RED);
        // Opcional: hacer que el mensaje desaparezca después de unos segundos
        Timer timer = new Timer(5000, e -> statusLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
    
    private void mostrarMensajeExito(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(new Color(0, 150, 0));
    }
    
    private void establecerPropiedadesVentana() {
        setTitle("Plataforma de Streaming - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null); // Centrar en pantalla
        
        // Icono de la ventana (opcional)
        try {
            // Si tienes un icono, puedes cargarlo aquí
            // setIconImage(...);
        } catch (Exception e) {
            // Ignorar si no hay icono
        }
    }
    
    /**
     * Método principal para iniciar la interfaz gráfica
     */
    public static void startGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Usar el Look and Feel del sistema
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // Usar el Look and Feel por defecto si falla
                }
                
                new LoginGUI().setVisible(true);
            }
        });
    }
}