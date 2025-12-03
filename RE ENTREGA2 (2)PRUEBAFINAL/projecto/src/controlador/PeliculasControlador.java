package controlador;

import javax.swing.*;
import java.util.List;
import vista.PeliculasVista;
import vista.LoginVista;
import vista.CalificarPeliculaVista;
import vista.ResultadoBusquedaVista;
import model.Usuario;
import model.Pelicula;
import app.Logica;
import org.json.JSONObject;

/**
 * Controlador de Películas - Maneja la lógica de interacción de la lista de películas.
 * Coordina las acciones del usuario con la lógica de negocio.
 */
public class PeliculasControlador implements PeliculasVista.PeliculasVistaListener {
    
    private PeliculasVista vista;
    private Usuario usuario;
    private List<Pelicula> peliculasActuales;
    private boolean esPrimerLogin;
    
    public PeliculasControlador(PeliculasVista vista, Usuario usuario, List<Pelicula> peliculas, boolean esPrimerLogin) {
        this.vista = vista;
        this.usuario = usuario;
        this.peliculasActuales = peliculas;
        this.esPrimerLogin = esPrimerLogin;
        
        // Registrar este controlador como listener de la vista
        vista.setListener(this);
    }
    
    @Override
    public void onBuscarPelicula(String termino) {
        if (termino.isEmpty()) {
            vista.mostrarMensaje("Búsqueda", "Por favor ingrese un término de búsqueda", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog loadingDialog = vista.crearDialogoCargando("Buscando en OMDb: " + termino + "...");
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private JSONObject resultado;
            
            @Override
            protected Void doInBackground() throws Exception {
                resultado = Logica.buscarPeliculaOMDb(termino);
                return null;
            }
            
            @Override
            protected void done() {
                loadingDialog.dispose();
                
                try {
                    get();
                    
                    if (resultado != null && !resultado.optString("Response", "False").equals("False")) {
                        ResultadoBusquedaVista resultadoVista = new ResultadoBusquedaVista(vista, resultado);
                        resultadoVista.setVisible(true);
                    } else {
                        vista.mostrarMensaje("Sin resultados", 
                            "No se encontró la película \"" + termino + "\".\nNo se encuentra disponible.",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                } catch (Exception e) {
                    vista.mostrarMensaje("Error", "Error en la búsqueda: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
        loadingDialog.setVisible(true);
    }
    
    @Override
    public void onCerrarSesion() {
        int confirm = vista.mostrarConfirmacion("Cerrar Sesión", "¿Está seguro que desea cerrar sesión?");
        
        if (confirm == JOptionPane.YES_OPTION) {
            vista.dispose();
            
            LoginVista loginVista = new LoginVista();
            new LoginControlador(loginVista);
            loginVista.setVisible(true);
        }
    }
    
    @Override
    public void onOrdenarPorTitulo(boolean ascendente) {
        if (ascendente) {
            peliculasActuales.sort((p1, p2) -> 
                p1.getMetadatos().getTitulo().compareToIgnoreCase(p2.getMetadatos().getTitulo())
            );
        } else {
            peliculasActuales.sort((p1, p2) -> 
                p2.getMetadatos().getTitulo().compareToIgnoreCase(p1.getMetadatos().getTitulo())
            );
        }
        vista.actualizarPeliculas(peliculasActuales);
    }
    
    @Override
    public void onOrdenarPorGenero(boolean ascendente) {
        if (ascendente) {
            peliculasActuales.sort((p1, p2) -> {
                String genero1 = p1.getGeneros().isEmpty() ? "" : p1.getGeneros().get(0);
                String genero2 = p2.getGeneros().isEmpty() ? "" : p2.getGeneros().get(0);
                return genero1.compareToIgnoreCase(genero2);
            });
        } else {
            peliculasActuales.sort((p1, p2) -> {
                String genero1 = p1.getGeneros().isEmpty() ? "" : p1.getGeneros().get(0);
                String genero2 = p2.getGeneros().isEmpty() ? "" : p2.getGeneros().get(0);
                return genero2.compareToIgnoreCase(genero1);
            });
        }
        vista.actualizarPeliculas(peliculasActuales);
    }
    
    @Override
    public void onCalificarPelicula(Pelicula pelicula, JButton boton) {
        CalificarPeliculaVista calificarVista = new CalificarPeliculaVista(vista, pelicula, usuario);
        CalificarPeliculaControlador calificarControlador = new CalificarPeliculaControlador(calificarVista, pelicula, usuario);
        calificarVista.setVisible(true);
        
        if (calificarControlador.isCalificacionGuardada()) {
            boton.setEnabled(false);
            boton.setText("Calificada");
            boton.setBackground(java.awt.Color.GRAY);
        }
    }
}
