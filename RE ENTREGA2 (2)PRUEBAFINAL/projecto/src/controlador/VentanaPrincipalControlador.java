package controlador;

import vista.VentanaPrincipalVista;
import model.Usuario;
import model.Pelicula;
import app.Logica;

import javax.swing.*;
import java.util.List;

/**
 * Controlador de la Ventana Principal - Maneja la logica de carga de peliculas.
 */
public class VentanaPrincipalControlador {

    private VentanaPrincipalVista vista;
    private boolean esPrimerLogin;
    private List<Pelicula> peliculasActuales;

    public VentanaPrincipalControlador(VentanaPrincipalVista vista) {
        this.vista = vista;
    }

    public void cargarPeliculasEnBackground() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Pequenia pausa para asegurar que la pantalla de carga sea visible
                Thread.sleep(500);

                // Verificar si es primer login
                esPrimerLogin = Logica.esPrimerLogin(vista.getUsuario().getID_USUARIO());

                // Cargar peliculas segun sea primer login o no
                if (esPrimerLogin) {
                    peliculasActuales = Logica.obtenerTop10Peliculas();
                } else {
                    peliculasActuales = Logica.obtener10PeliculasRandom(vista.getUsuario().getID_USUARIO());
                }

                // Pequenia pausa adicional para que el usuario vea la pantalla de carga
                Thread.sleep(500);

                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Verificar si hubo errores

                    // Marcar como no primer login si era el primero
                    if (esPrimerLogin) {
                        Logica.registrarPrimerLogin(vista.getUsuario().getID_USUARIO());
                    }

                    // Cerrar ventana de carga y abrir ventana de peliculas
                    vista.dispose();
                    PeliculasControlador.iniciarPeliculas(vista.getUsuario(), peliculasActuales, esPrimerLogin);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(vista,
                            "Error al cargar peliculas: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    vista.dispose();
                    LoginControlador.iniciarLogin();
                }
            }
        };

        worker.execute();
    }

    /**
     * Metodo estatico para iniciar la ventana principal
     */
    public static void iniciarVentanaPrincipal(Usuario usuario) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipalVista vista = new VentanaPrincipalVista(usuario);
                VentanaPrincipalControlador controlador = new VentanaPrincipalControlador(vista);
                vista.setVisible(true);

                // Cargar peliculas en segundo plano despues de un pequenio delay
                Timer timer = new Timer(100, e -> controlador.cargarPeliculasEnBackground());
                timer.setRepeats(false);
                timer.start();
            }
        });
    }
}
