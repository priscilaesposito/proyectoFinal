package app;

import javax.swing.*;
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
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setFrameProperties();
    }
    
    private void initializeComponents() {
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
    
    private void setupLayout() {
        // Panel izquierdo con la imagen de bienvenida
        JPanel leftPanel = createWelcomePanel();
        
        // Panel derecho con el formulario de login
        JPanel rightPanel = createLoginPanel();
        
        // Agregar paneles al panel principal
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.setBackground(new Color(255, 223, 186)); // Color beige claro
        welcomePanel.setPreferredSize(new Dimension(400, 500));
        
        // Título de bienvenida
        JLabel welcomeTitle = new JLabel("Bienvenido a la Plataforma de Streaming", JLabel.CENTER);
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        
        // Panel para la imagen del perrito
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(new Color(255, 223, 186));
        imagePanel.setLayout(new BorderLayout());
        
        // Cargar imagen usando ResourceManager con fallback automático
        ImageIcon dogStreamingIcon = GestionRecursos.loadImageWithFallback("dog_streaming.jpg", 300, 250);
        JLabel imageLabel = new JLabel(dogStreamingIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        
        welcomePanel.add(welcomeTitle, BorderLayout.NORTH);
        welcomePanel.add(imagePanel, BorderLayout.CENTER);
         
        
        
        return welcomePanel;
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Mensaje de instrucciones (simulando el tooltip)
        JLabel instructionLabel = new JLabel("<html><center>Solicite los datos de<br/>acuerdo a su modelo,<br/>ajustando las<br/>validaciones<br/>necesarias</center></html>");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        instructionLabel.setForeground(new Color(100, 100, 100));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setBackground(new Color(255, 182, 193));
        instructionLabel.setOpaque(true);
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(instructionLabel, gbc);
        
        // Campo E-mail
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(new JLabel("E-mail:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        emailField.setPreferredSize(new Dimension(200, 30));
        loginPanel.add(emailField, gbc);
        
        // Campo Password
        gbc.gridx = 0; gbc.gridy = 2;
        loginPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        passwordField.setPreferredSize(new Dimension(200, 30));
        loginPanel.add(passwordField, gbc);
        
        // Botón Ingresar
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        loginButton.setBackground(new Color(51, 153, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginPanel.add(loginButton, gbc);
        
        // Mensaje "¿Aún no sos usuario?" y botón Regístrate
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel registerPanel = new JPanel(new FlowLayout());
        registerPanel.setBackground(Color.WHITE);
        registerPanel.add(new JLabel("¿Aún no sos usuario?"));
        
        registerButton.setBackground(new Color(51, 153, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 12));
        registerPanel.add(registerButton);
        
        loginPanel.add(registerPanel, gbc);
        
        // Label para mensajes de estado
        gbc.gridy = 5;
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        loginPanel.add(statusLabel, gbc);
        
        // Nota sobre validaciones (simulando los tooltips amarillos)
        JLabel validationNote = new JLabel("<html><center>Verificar el formato del mail<br/>y que los datos estén completos</center></html>");
        validationNote.setFont(new Font("Arial", Font.ITALIC, 10));
        validationNote.setForeground(new Color(100, 100, 100));
        validationNote.setBackground(new Color(255, 255, 153));
        validationNote.setOpaque(true);
        validationNote.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        gbc.gridy = 6;
        loginPanel.add(validationNote, gbc);
        
        return loginPanel;
    }
    
    private void setupEventHandlers() {
        // Acción del botón de login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        // Acción del botón de registro
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationWindow();
            }
        });
        
        // Enter en el campo de password ejecuta login
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }
    
    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validaciones básicas
        if (email.isEmpty() || password.isEmpty()) {
            showErrorMessage("E-mail y Password son campos obligatorios");
            return;
        }
        
        if (!isValidEmail(email)) {
            showErrorMessage("Formato de E-mail inválido");
            return;
        }
        
        // Intentar login
        try {
            statusLabel.setText("Verificando credenciales...");
            statusLabel.setForeground(Color.BLUE);
            
            Usuario usuario = Logica.login(email, password);
            
            if (usuario != null) {
                showSuccessMessage("¡Bienvenido/a, " + usuario.getUsername() + "!");
                // Aquí puedes abrir la ventana principal
                openMainWindow(usuario);
                this.dispose(); // Cerrar ventana de login
            } else {
                showErrorMessage("E-mail o Password incorrectos");
            }
            
        } catch (Exception ex) {
            showErrorMessage("Error de conexión: " + ex.getMessage());
        }
    }
    
    private void openRegistrationWindow() {
        // TODO: Implementar ventana de registro
        JOptionPane.showMessageDialog(this, 
            "Funcionalidad de registro en desarrollo...\nPor ahora use la consola para registrar usuarios.", 
            "Registro", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openMainWindow(Usuario usuario) {
        // TODO: Implementar ventana principal
        JOptionPane.showMessageDialog(this, 
            "¡Login exitoso!\nBienvenido " + usuario.getUsername() + "!\n\nVentana principal en desarrollo...", 
            "Bienvenido", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Por ahora, cerrar la aplicación después del mensaje
        System.exit(0);
    }
    
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }
    
    private void showErrorMessage(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(Color.RED);
        // Opcional: hacer que el mensaje desaparezca después de unos segundos
        Timer timer = new Timer(5000, e -> statusLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
    
    private void showSuccessMessage(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(new Color(0, 150, 0));
    }
    
    private void setFrameProperties() {
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