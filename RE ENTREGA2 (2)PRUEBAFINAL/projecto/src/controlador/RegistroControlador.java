package controlador;

import vista.RegistroVista;
import app.Logica;
import enumerativo.UsuarioInvalidoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador del Registro - Maneja la lógica de validación y registro de
 * usuarios.
 */
public class RegistroControlador {

    private RegistroVista vista;

    public RegistroControlador(RegistroVista vista) {
        this.vista = vista;
        configurarEventos();
    }

    private void configurarEventos() {
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
            JOptionPane.showMessageDialog(vista,
                    "Todos los campos son obligatorios",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar formato de DNI (solo números)
        if (!dni.matches("\\d+")) {
            JOptionPane.showMessageDialog(vista,
                    "El DNI debe contener solo números",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar formato de email
        if (!esEmailValido(email)) {
            JOptionPane.showMessageDialog(vista,
                    "Formato de email inválido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar longitud de contraseña
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(vista,
                    "La contraseña debe tener al menos 6 caracteres",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar datos únicos en la base de datos
        try {
            // Verificar si el email ya existe
            if (Logica.existeEmail(email)) {
                JOptionPane.showMessageDialog(vista,
                        "El email ya está registrado",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si el DNI ya existe
            if (Logica.existeDNI(dni)) {
                JOptionPane.showMessageDialog(vista,
                        "El DNI ya está registrado",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generar username automáticamente
            String username = generarUsername(nombres, apellidos);

            // Verificar que el username sea único
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
                    usernameFinal, email, password);

            if (registroExitoso) {
                JOptionPane.showMessageDialog(vista,
                        "¡Usuario registrado exitosamente!\nTu nombre de usuario es: " + usernameFinal,
                        "Registro Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
                vista.dispose();
            } else {
                JOptionPane.showMessageDialog(vista,
                        "Error al registrar usuario. Intente nuevamente",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (UsuarioInvalidoException e) {
            // Manejar específicamente excepciones de usuario inválido
            String mensaje = e.getMessage();
            if (e.getCampo() != null) {
                mensaje += "\nCampo: " + e.getCampo();
            }
            JOptionPane.showMessageDialog(vista,
                    mensaje,
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
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

    /**
     * Método estático para iniciar la interfaz de registro
     */
    public static void iniciarRegistro() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RegistroVista vista = new RegistroVista();
                new RegistroControlador(vista);
                vista.setVisible(true);
            }
        });
    }
}
