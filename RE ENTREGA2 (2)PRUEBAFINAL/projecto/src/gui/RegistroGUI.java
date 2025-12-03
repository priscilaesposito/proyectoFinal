package gui;

import javax.swing.*;

import app.Logica;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistroGUI extends JFrame {
    
    // Campos del formulario
    private JTextField nombresField;
    private JTextField apellidosField;
    private JTextField dniField;
    private JTextField emailField;
    private JPasswordField passwordField;
    
    // Botones
    private JButton registrarButton;
    
    private JPanel mainPanel;
    
    public RegistroGUI() {
        inicializarComponentes();
        configurarDisenio();
        configurarEventos();
        establecerPropiedadesVentana();
    }
    
    private void inicializarComponentes() {
        // Campos del formulario
        nombresField = new JTextField(20);
        apellidosField = new JTextField(20);
        dniField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        
        // Botón
        registrarButton = new JButton("Registrar");
        
        // Panel principal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
    }
    
    private void configurarDisenio() {
        // Barra superior
        JPanel topBar = crearBarraSuperior();
        
        // Panel con todo el contenido
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Panel con formulario
        JPanel formPanel = crearPanelFormulario();
        
        contentPanel.add(formPanel, BorderLayout.CENTER);
        
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
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
        
        // Título a la izquierda
        JLabel title = new JLabel("Plataforma de Streaming - Registración");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        topBar.add(title, BorderLayout.WEST);
        
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
        button.setBorderPainted(false);
        
        return button;
    }
    
    private JPanel crearPanelFormulario() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Nombres
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel nombresLabel = new JLabel("Nombres:");
        nombresLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(nombresLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        nombresField.setPreferredSize(new Dimension(250, 30));
        formPanel.add(nombresField, gbc);
        row++;
        
        // Apellidos
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel apellidosLabel = new JLabel("Apellidos:");
        apellidosLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(apellidosLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        apellidosField.setPreferredSize(new Dimension(250, 30));
        formPanel.add(apellidosField, gbc);
        row++;
        
        // DNI
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel dniLabel = new JLabel("DNI:");
        dniLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(dniLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        dniField.setPreferredSize(new Dimension(250, 30));
        formPanel.add(dniField, gbc);
        row++;
        
        // E-mail
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel emailLabel = new JLabel("E-mail:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        emailField.setPreferredSize(new Dimension(250, 30));
        formPanel.add(emailField, gbc);
        row++;
        
        // Password
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField.setPreferredSize(new Dimension(250, 30));
        formPanel.add(passwordField, gbc);
        row++;
        
        // Espacio
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formPanel.add(Box.createVerticalStrut(20), gbc);
        row++;
        
        // Botón Registrar
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        registrarButton.setBackground(new Color(0, 153, 255));
        registrarButton.setForeground(Color.WHITE);
        registrarButton.setFont(new Font("Arial", Font.BOLD, 14));
        registrarButton.setPreferredSize(new Dimension(150, 40));
        registrarButton.setOpaque(true);
        registrarButton.setBorderPainted(false);
        registrarButton.setFocusPainted(false);
        formPanel.add(registrarButton, gbc);
        
        return formPanel;
    }
    
    private void configurarEventos() {
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarRegistro();
            }
        });
    }
    
    private void realizarRegistro() {
        // Obtener datos de los campos
        String nombres = nombresField.getText().trim();
        String apellidos = apellidosField.getText().trim();
        String dni = dniField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validar que todos los campos estén completos
        if (nombres.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || 
            email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Todos los campos son obligatorios", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar formato de DNI (solo números)
        if (!dni.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, 
                "El DNI debe contener solo números", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar formato de email
        if (!esEmailValido(email)) {
            JOptionPane.showMessageDialog(this, 
                "Formato de email inválido", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar longitud de contraseña
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "La contraseña debe tener al menos 6 caracteres", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar datos únicos en la base de datos
        try {
            // Verificar si el email ya existe
            if (Logica.existeEmail(email)) {
                JOptionPane.showMessageDialog(this, 
                    "El email ya está registrado", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Verificar si el DNI ya existe
            if (Logica.existeDNI(dni)) {
                JOptionPane.showMessageDialog(this, 
                    "El DNI ya está registrado", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Generar username automáticamente a partir del nombre
            String username = generarUsername(nombres, apellidos);
            
            // Verificar que el username sea único, si no, agregar número
            String usernameFinal = username;
            int contador = 1;
            while (Logica.existeUsuario(usernameFinal)) {
                usernameFinal = username + contador;
                contador++;
            }
            
            // Registrar usuario (con valores por defecto para campos opcionales)
            boolean registroExitoso = Logica.registrarUsuario(
                nombres, apellidos, dni, 
                18, // edad por defecto
                "", // dirección vacía
                "", // teléfono vacío
                usernameFinal, email, password
            );
            
            if (registroExitoso) {
                JOptionPane.showMessageDialog(this, 
                    "¡Usuario registrado exitosamente!\nTu nombre de usuario es: " + usernameFinal, 
                    "Registro Exitoso", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al registrar usuario. Intente nuevamente", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private String generarUsername(String nombres, String apellidos) {
        // Generar username: primera letra del nombre + apellido (sin espacios, en minúsculas)
        String username = "";
        if (!nombres.isEmpty()) {
            username = nombres.substring(0, 1).toLowerCase();
        }
        if (!apellidos.isEmpty()) {
            username += apellidos.replace(" ", "").toLowerCase();
        }
        return username;
    }
    
    private boolean esEmailValido(String email) {
        // Validación básica de email
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    private void establecerPropiedadesVentana() {
        setTitle("Plataforma de Streaming - Registración");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }
    
    public static void abrirVentanaRegistro() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegistroGUI().setVisible(true);
            }
        });
    }
}
