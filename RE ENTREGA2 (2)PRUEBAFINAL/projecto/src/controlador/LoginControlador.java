package controlador;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista.LoginVista;
import vista.RegistroVista;
import vista.VentanaPrincipalVista;
import model.Usuario;
import app.Logica;

/**
 * Controlador del Login - Maneja la lógica de interacción entre la vista y el modelo.
 * Coordina las acciones del usuario con la lógica de negocio.
 */
public class LoginControlador {
    
    private LoginVista vista;
    
    public LoginControlador(LoginVista vista) {
        this.vista = vista;
        inicializarEventos();
    }
    
    private void inicializarEventos() {
        // Acción del botón de login
        vista.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        // Acción del botón de registro
        vista.getRegisterButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaRegistro();
            }
        });
        
        // Enter en el campo de password ejecuta login
        vista.getPasswordField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
    }
    
    private void realizarLogin() {
        String email = vista.getEmail();
        String password = vista.getPassword();
        
        // Validaciones básicas
        if (email.isEmpty() || password.isEmpty()) {
            vista.mostrarMensajeError("E-mail y Password son campos obligatorios");
            return;
        }
        
        if (!esEmailValido(email)) {
            vista.mostrarMensajeError("Formato de E-mail inválido");
            return;
        }
        
        // Intentar login
        try {
            vista.mostrarMensajeProcesando("Verificando credenciales...");
            
            Usuario usuario = Logica.login(email, password);
            
            if (usuario != null) {
                vista.mostrarMensajeExito("¡Bienvenido/a, " + usuario.getUsername() + "!");
                abrirVentanaPrincipal(usuario);
                vista.dispose();
            } else {
                vista.mostrarMensajeError("E-mail o Password incorrectos");
            }
            
        } catch (Exception ex) {
            vista.mostrarMensajeError("Error de conexión: " + ex.getMessage());
        }
    }
    
    private void abrirVentanaRegistro() {
        RegistroVista registroVista = new RegistroVista();
        new RegistroControlador(registroVista);
        registroVista.setVisible(true);
    }
    
    private void abrirVentanaPrincipal(Usuario usuario) {
        VentanaPrincipalVista ventanaPrincipalVista = new VentanaPrincipalVista(usuario);
        new VentanaPrincipalControlador(ventanaPrincipalVista, usuario);
        ventanaPrincipalVista.setVisible(true);
    }
    
    private boolean esEmailValido(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }
    
    /**
     * Método estático para iniciar la GUI con el patrón MVC
     */
    public static void iniciarAplicacion() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // Usar el Look and Feel por defecto si falla
                }
                
                LoginVista vista = new LoginVista();
                new LoginControlador(vista);
                vista.setVisible(true);
            }
        });
    }
}
