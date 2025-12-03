package controlador;

import javax.swing.*;
import java.util.List;
import vista.VentanaPrincipalVista;
import vista.PeliculasVista;
import vista.LoginVista;
import model.Usuario;
import model.Pelicula;
import app.Logica;
import utilidades.CargadorCSV;

/**
 * Controlador de la Ventana Principal - Maneja la lógica de carga de películas.
 * Coordina la carga asíncrona de datos y la transición a la vista de películas.
 */
public class VentanaPrincipalControlador {
    
    private VentanaPrincipalVista vista;
    private Usuario usuario;
    private boolean esPrimerLogin;
    private List<Pelicula> peliculasActuales;
    
    public VentanaPrincipalControlador(VentanaPrincipalVista vista, Usuario usuario) {
        this.vista = vista;
        this.usuario = usuario;
        
        // Iniciar carga de películas después de un pequeño delay
        Timer timer = new Timer(100, e -> cargarPeliculasEnBackground());
        timer.setRepeats(false);
        timer.start();
    }
    
    private void cargarPeliculasEnBackground() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Verificar si es primer login
                esPrimerLogin = Logica.esPrimerLogin(usuario.getID_USUARIO());
                
                // Si es primer acceso del usuario, verificar si hay que cargar el CSV
                if (esPrimerLogin) {
                    vista.actualizarMensajeCarga("Importando películas desde movies_database.csv...");
                    
                    // Verificar si ya hay películas en la BD
                    if (!CargadorCSV.existenPeliculasEnBD()) {
                        // Cargar películas desde CSV
                        List<Pelicula> todasOrdenadas = CargadorCSV.cargarPeliculasDesdeCSV();
                        
                        vista.actualizarMensajeCarga("Seleccionando top 10 películas mejor rankeadas...");
                        // Tomar las primeras 10
                        peliculasActuales = todasOrdenadas.subList(0, Math.min(10, todasOrdenadas.size()));
                    } else {
                        // Si ya existen películas, obtener top 10 de BD
                        vista.actualizarMensajeCarga("Seleccionando top 10 películas mejor rankeadas...");
                        peliculasActuales = Logica.obtenerTop10Peliculas();
                    }
                } else {
                    vista.actualizarMensajeCarga("Buscando películas para ti...");
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
                    vista.dispose();
                    
                    PeliculasVista peliculasVista = new PeliculasVista(usuario, peliculasActuales, esPrimerLogin);
                    new PeliculasControlador(peliculasVista, usuario, peliculasActuales, esPrimerLogin);
                    peliculasVista.setVisible(true);
                    
                } catch (Exception e) {
                    vista.mostrarError("Error", "Error al cargar películas: " + e.getMessage());
                    e.printStackTrace();
                    vista.dispose();
                    
                    // Volver al login
                    LoginVista loginVista = new LoginVista();
                    new LoginControlador(loginVista);
                    loginVista.setVisible(true);
                }
            }
        };
        
        worker.execute();
    }
}
