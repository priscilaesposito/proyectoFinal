package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista.RegistroVista;
import app.Logica;

/**
 * Controlador del Registro - Maneja la lógica de interacción entre la vista y el modelo.
 * Coordina las acciones del usuario con la lógica de negocio.
 */
public class RegistroControlador {
    
    private RegistroVista vista;
    
    public RegistroControlador(RegistroVista vista) {
        this.vista = vista;
        inicializarEventos();
    }
    
    private void inicializarEventos() {
        vista.getRegistrarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarRegistro();
            }
        });
    }
    
    private void realizarRegistro() {
        String nombres = vista.getNombres();
        String apellidos = vista.getApellidos();
        String dni = vista.getDni();
        String email = vista.getEmail();
        String password = vista.getPassword();
        
        // Validar que todos los campos estén completos
        if (nombres.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || 
            email.isEmpty() || password.isEmpty()) {
            vista.mostrarError("Error", "Todos los campos son obligatorios");
            return;
        }
        
        // Validar formato de DNI (solo números)
        if (!dni.matches("\\d+")) {
            vista.mostrarError("Error", "El DNI debe contener solo números");
            return;
        }
        
        // Validar formato de email
        if (!esEmailValido(email)) {
            vista.mostrarError("Error", "Formato de email inválido");
            return;
        }
        
        // Validar longitud de contraseña
        if (password.length() < 6) {
            vista.mostrarError("Error", "La contraseña debe tener al menos 6 caracteres");
            return;
        }
        
        // Validar datos únicos en la base de datos
        try {
            // Verificar si el email ya existe
            if (Logica.existeEmail(email)) {
                vista.mostrarError("Error", "El email ya está registrado");
                return;
            }
            
            // Verificar si el DNI ya existe
            if (Logica.existeDNI(dni)) {
                vista.mostrarError("Error", "El DNI ya está registrado");
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
            
            // Registrar usuario
            boolean registroExitoso = Logica.registrarUsuario(
                nombres, apellidos, dni, 
                18, // edad por defecto
                "", // dirección vacía
                "", // teléfono vacío
                usernameFinal, email, password
            );
            
            if (registroExitoso) {
                vista.mostrarExito("Registro Exitoso", 
                    "¡Usuario registrado exitosamente!\nTu nombre de usuario es: " + usernameFinal);
                vista.dispose();
            } else {
                vista.mostrarError("Error", "Error al registrar usuario. Intente nuevamente");
            }
            
        } catch (Exception ex) {
            vista.mostrarError("Error", "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private String generarUsername(String nombres, String apellidos) {
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
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
