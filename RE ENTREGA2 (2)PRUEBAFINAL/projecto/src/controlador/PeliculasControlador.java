package controlador;

import vista.PeliculasVista;
import model.Usuario;
import model.Pelicula;
import app.Logica;
import enumerativo.PeliculaNoEncontradaException;
import enumerativo.ReseniaInvalidaException;
import utilidades.AutoGuardadoResenias;
import utilidades.AutoGuardadoResenias.BorradorResenia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controlador de Películas - Maneja la lógica de eventos de la interfaz de
 * películas.
 */
public class PeliculasControlador {

    private PeliculasVista vista;

    public PeliculasControlador(PeliculasVista vista) {
        this.vista = vista;
        configurarEventos();
    }

    private void configurarEventos() {
        // Configurar evento de búsqueda
        vista.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPelicula();
            }
        });

        // Configurar evento de cerrar sesión
        vista.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });

        // Configurar eventos de calificación para todos los botones
        configurarEventosCalificacion();
    }

    private void configurarEventosCalificacion() {
        // Buscar todos los botones de calificar en la vista
        try {
            Component viewComponent = ((JScrollPane) vista.getContentPane().getComponent(0)
                    .getComponentAt(0, 0))
                    .getViewport().getView();

            if (viewComponent instanceof Container) {
                Component[] components = ((Container) viewComponent).getComponents();
                for (Component comp : components) {
                    if (comp instanceof JPanel) {
                        buscarYConfigurarBotonesCalificar((JPanel) comp);
                    }
                }
            }
        } catch (Exception e) {
            // Si hay error al buscar componentes, ignorar
        }
    }

    private void buscarYConfigurarBotonesCalificar(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if ("Calificar".equals(button.getText())) {
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Pelicula pelicula = (Pelicula) button.getClientProperty("pelicula");
                            @SuppressWarnings("unchecked")
                            JComboBox<Integer> ratingCombo = (JComboBox<Integer>) button
                                    .getClientProperty("ratingCombo");
                            if (pelicula != null && ratingCombo != null) {
                                int calificacion = (Integer) ratingCombo.getSelectedItem();
                                calificarPelicula(pelicula, calificacion);
                            }
                        }
                    });
                }
            } else if (comp instanceof JPanel) {
                buscarYConfigurarBotonesCalificar((JPanel) comp);
            }
        }
    }

    private void calificarPelicula(Pelicula pelicula, int calificacion) {
        // Obtener instancia del auto-guardado (usando concurrencia con Timer)
        AutoGuardadoResenias autoGuardado = AutoGuardadoResenias.getInstance();

        // Verificar si existe un borrador previo
        BorradorResenia borrador = autoGuardado.obtenerBorrador(
                vista.getUsuario().getID_USUARIO(),
                pelicula.getID());

        // Crear diálogo para ingresar la reseña
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel instruccion = new JLabel("Ingrese su calificación y reseña:");
        instruccion.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratingPanel.add(new JLabel("Calificación:"));
        JLabel ratingValue = new JLabel(calificacion + "/10");
        ratingValue.setFont(new Font("Arial", Font.BOLD, 14));
        ratingValue.setForeground(new Color(255, 165, 0));
        ratingPanel.add(ratingValue);

        JLabel reseniaLabel = new JLabel("Reseña:");
        JTextArea reseniaArea = new JTextArea(5, 30);
        reseniaArea.setLineWrap(true);
        reseniaArea.setWrapStyleWord(true);
        reseniaArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Restaurar borrador si existe
        if (borrador != null) {
            reseniaArea.setText(borrador.getComentario());
            calificacion = borrador.getCalificacion();
            ratingValue.setText(calificacion + "/10");
        }

        // Listener para auto-guardar mientras el usuario escribe (concurrencia)
        final int calificacionFinal = calificacion;
        javax.swing.event.DocumentListener autoSaveListener = new javax.swing.event.DocumentListener() {
            private void guardar() {
                String texto = reseniaArea.getText();
                if (texto != null && !texto.trim().isEmpty()) {
                    // Esta llamada se ejecutará en el Timer thread
                    autoGuardado.agregarBorrador(
                            vista.getUsuario().getID_USUARIO(),
                            pelicula.getID(),
                            pelicula.getMetadatos().getTitulo(),
                            calificacionFinal,
                            texto);
                }
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                guardar();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                guardar();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                guardar();
            }
        };
        reseniaArea.getDocument().addDocumentListener(autoSaveListener);

        JScrollPane scrollPane = new JScrollPane(reseniaArea);

        panel.add(instruccion, BorderLayout.NORTH);
        panel.add(ratingPanel, BorderLayout.CENTER);

        JPanel reseniaPanel = new JPanel(new BorderLayout(5, 5));
        reseniaPanel.add(reseniaLabel, BorderLayout.NORTH);
        reseniaPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(reseniaPanel, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(
                vista,
                panel,
                "Calificar: " + pelicula.getMetadatos().getTitulo(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String resenia = reseniaArea.getText().trim();

            if (resenia.isEmpty()) {
                JOptionPane.showMessageDialog(vista,
                        "Debe ingresar una reseña para calificar la película.",
                        "Campo requerido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                boolean exito = Logica.calificarPelicula(
                        vista.getUsuario().getID_USUARIO(),
                        pelicula.getID(),
                        calificacion,
                        resenia);

                if (exito) {
                    // Eliminar borrador al guardar exitosamente
                    autoGuardado.eliminarBorrador(
                            vista.getUsuario().getID_USUARIO(),
                            pelicula.getID());

                    JOptionPane.showMessageDialog(vista,
                            "¡Gracias por calificar \"" + pelicula.getMetadatos().getTitulo() + "\"!",
                            "Calificación guardada",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (ReseniaInvalidaException e) {
                // Manejar específicamente errores de reseña inválida
                String mensaje = e.getMessage();
                if (e.getMotivoRechazo() != null) {
                    mensaje += "\nMotivo: " + e.getMotivoRechazo();
                }
                JOptionPane.showMessageDialog(vista,
                        mensaje,
                        "Reseña Inválida",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista,
                        "Error al guardar calificación: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarPelicula() {
        String termino = vista.getSearchTerm();

        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Por favor ingrese un término de búsqueda",
                    "Búsqueda",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mostrar diálogo de búsqueda
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
                                "No se encontró la película \"" + termino + "\"",
                                "Sin resultados",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (java.util.concurrent.ExecutionException e) {
                    // Verificar si la causa es PeliculaNoEncontradaException
                    Throwable causa = e.getCause();
                    if (causa instanceof PeliculaNoEncontradaException) {
                        PeliculaNoEncontradaException pnee = (PeliculaNoEncontradaException) causa;
                        JOptionPane.showMessageDialog(vista,
                                pnee.getMessage(),
                                "Película No Encontrada",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(vista,
                                "Error en la búsqueda: " + causa.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(vista,
                            "Error en la búsqueda: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        loadingDialog.setVisible(true);
    }

    private void mostrarResultadoBusqueda(org.json.JSONObject pelicula) {
        String info = "🎬 " + pelicula.getString("Title") + "\n\n" +
                "📅 Año: " + pelicula.getString("Year") + "\n" +
                "🎭 Género: " + pelicula.getString("Genre") + "\n" +
                "🎬 Director: " + pelicula.getString("Director") + "\n" +
                "⭐ Rating IMDb: " + pelicula.optString("imdbRating", "N/A") + "/10\n" +
                "⏱️ Duración: " + pelicula.optString("Runtime", "N/A") + "\n\n" +
                "📖 Sinopsis:\n" + pelicula.getString("Plot");

        JOptionPane.showMessageDialog(vista,
                info,
                "Resultado de Búsqueda",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(vista,
                "¿Está seguro que desea cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            vista.dispose();
            LoginControlador.iniciarLogin();
        }
    }

    /**
     * Método estático para iniciar la interfaz de películas
     */
    public static void iniciarPeliculas(Usuario usuario, List<Pelicula> peliculas, boolean esPrimerLogin) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PeliculasVista vista = new PeliculasVista(usuario, peliculas, esPrimerLogin);
                new PeliculasControlador(vista);
                vista.setVisible(true);
            }
        });
    }
}
