package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista.CalificarPeliculaVista;
import model.Pelicula;
import model.Usuario;
import app.Logica;

/**
 * Controlador para calificar una película - Maneja la lógica de interacción.
 * Coordina las acciones del usuario con la lógica de negocio.
 */
public class CalificarPeliculaControlador {
    
    private CalificarPeliculaVista vista;
    private Pelicula pelicula;
    private Usuario usuario;
    private boolean calificacionGuardada = false;
    
    public CalificarPeliculaControlador(CalificarPeliculaVista vista, Pelicula pelicula, Usuario usuario) {
        this.vista = vista;
        this.pelicula = pelicula;
        this.usuario = usuario;
        
        inicializarEventos();
    }
    
    private void inicializarEventos() {
        vista.getGuardarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCalificacion();
            }
        });
    }
    
    private void guardarCalificacion() {
        String resenia = vista.getResenia();
        int calificacion = vista.getCalificacion();
        
        // Validar que se haya ingresado la reseña
        if (resenia.isEmpty()) {
            vista.mostrarAdvertencia("Campo requerido", "Debe ingresar una reseña para calificar la película.");
            return;
        }
        
        try {
            boolean exito = Logica.calificarPelicula(
                usuario.getID_USUARIO(), 
                pelicula.getID(), 
                calificacion, 
                resenia
            );
            
            if (exito) {
                vista.mostrarExito("Calificación guardada", 
                    "¡Gracias por calificar \"" + pelicula.getMetadatos().getTitulo() + "\"!");
                calificacionGuardada = true;
                vista.dispose();
            }
            
        } catch (Exception ex) {
            vista.mostrarError("Error", "Error al guardar calificación: " + ex.getMessage());
        }
    }
    
    public boolean isCalificacionGuardada() {
        return calificacionGuardada;
    }
}
