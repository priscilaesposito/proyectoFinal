package controlador;

import vista.PeliculasVista;
import vista.CalificarPeliculaVista;
import vista.ResultadoBusquedaVista;
import model.Usuario;
import model.Pelicula;
import app.Logica;
import enumerativo.PeliculaNoEncontradaException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controlador de Peliculas - Maneja la logica de eventos de la interfaz de
 * peliculas.
 */
public class PeliculasControlador {

    private PeliculasVista vista;

    public PeliculasControlador(PeliculasVista vista) {
        this.vista = vista;
        configurarEventos();
    }

    private void configurarEventos() {
        // Configurar evento de busqueda
        vista.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPelicula();
            }
        });

        // Configurar evento de cerrar sesion
        vista.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });

        // Configurar eventos de calificacion para todos los botones
        configurarEventosCalificacion();
    }

    private void configurarEventosCalificacion() {
        // Buscar todos los botones de calificar en la vista
        SwingUtilities.invokeLater(() -> {
            buscarYConfigurarBotonesEnContenedor(vista.getContentPane());
        });
    }
    
    private void buscarYConfigurarBotonesEnContenedor(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if ("Calificar".equals(button.getText())) {
                    // Remover listeners anteriores si existen
                    for (ActionListener al : button.getActionListeners()) {
                        button.removeActionListener(al);
                    }
                    
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Pelicula pelicula = (Pelicula) button.getClientProperty("pelicula");
                            if (pelicula != null) {
                                System.out.println("Abriendo ventana de calificación para: " + pelicula.getMetadatos().getTitulo());
                                // Abrir ventana emergente de calificación
                                boolean calificado = CalificarPeliculaVista.mostrar(
                                    vista, 
                                    pelicula, 
                                    vista.getUsuario()
                                );
                                
                                if (calificado) {
                                    // Deshabilitar el botón y cambiar texto
                                    button.setEnabled(false);
                                    button.setText("Calificada");
                                    button.setBackground(Color.GRAY);
                                }
                            } else {
                                System.err.println("ERROR: No se encontró la película en el botón");
                            }
                        }
                    });
                }
            } else if (comp instanceof Container) {
                buscarYConfigurarBotonesEnContenedor((Container) comp);
            }
        };
    }

    private void buscarPelicula() {
        String termino = vista.getSearchTerm();

        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Por favor ingrese un termino de busqueda",
                    "Busqueda",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mostrar dialogo de busqueda
        JDialog loadingDialog = new JDialog(vista, "Buscando...", true);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(vista);

        JLabel loadingMsg = new JLabel("Buscando en OMDb: " + termino + "...", SwingConstants.CENTER);
        loadingDialog.add(loadingMsg);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private org.json.JSONObject resultado;

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

                    if (resultado != null) {
                        mostrarResultadoBusqueda(resultado);
                    } else {
                        JOptionPane.showMessageDialog(vista,
                                "Película no encontrada",
                                "Sin resultados",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (java.util.concurrent.ExecutionException e) {
                    // Verificar si la causa es PeliculaNoEncontradaException
                    Throwable causa = e.getCause();
                    if (causa instanceof PeliculaNoEncontradaException) {
                        JOptionPane.showMessageDialog(vista,
                                "Película no encontrada",
                                "Sin resultados",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(vista,
                                "Error en la busqueda: " + causa.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(vista,
                            "Error en la busqueda: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        loadingDialog.setVisible(true);
    }

    private void mostrarResultadoBusqueda(org.json.JSONObject pelicula) {
        // Usar ventana emergente de ResultadoBusquedaVista con datos de OMDb
        ResultadoBusquedaVista.mostrar(vista, pelicula);
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(vista,
                "¿Esta seguro que desea cerrar sesion?",
                "Cerrar Sesion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            vista.dispose();
            LoginControlador.iniciarLogin();
        }
    }

    /**
     * Metodo estatico para iniciar la interfaz de peliculas
     */
    public static void iniciarPeliculas(Usuario usuario, List<Pelicula> peliculas, boolean esPrimerLogin) {
        iniciarPeliculasConCallback(usuario, peliculas, esPrimerLogin, null);
    }
    
    /**
     * Metodo estatico para iniciar la interfaz de peliculas con callback
     */
    public static void iniciarPeliculasConCallback(Usuario usuario, List<Pelicula> peliculas, boolean esPrimerLogin, Runnable onReady) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Crear la vista en el hilo de eventos de Swing
                PeliculasVista vista = new PeliculasVista(usuario, peliculas, esPrimerLogin);
                PeliculasControlador controlador = new PeliculasControlador(vista);
                
                // Hacer visible la ventana
                vista.setVisible(true);
                
                // Configurar eventos después de que todo esté renderizado
                SwingUtilities.invokeLater(() -> {
                    controlador.configurarEventosCalificacion();
                    
                    // Ejecutar callback cuando todo esté listo
                    if (onReady != null) {
                        // Pequeño delay adicional para asegurar que la ventana esté completamente renderizada
                        Timer readyTimer = new Timer(200, e -> onReady.run());
                        readyTimer.setRepeats(false);
                        readyTimer.start();
                    }
                });
            }
        });
    }
    
    // Hacer público el método para poder llamarlo desde fuera
    public void reconfigurarEventos() {
        configurarEventosCalificacion();
    }
}
