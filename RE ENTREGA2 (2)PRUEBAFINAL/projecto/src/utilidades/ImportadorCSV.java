package utilidades;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import model.Pelicula;
import dao.PeliculaDAO;
import daoJDBC.PeliculaDAOjdbc;

/**
 * Importador de películas desde archivo CSV usando concurrencia con
 * SwingWorker.
 * Permite cargar películas sin bloquear la interfaz gráfica y muestra el
 * progreso.
 * 
 * Formato CSV esperado:
 * Titulo,Director,Genero,Año,Rating,Duracion,Sinopsis
 */
public class ImportadorCSV {

    private PeliculaDAO peliculaDAO;

    public ImportadorCSV() {
        this.peliculaDAO = new PeliculaDAOjdbc();
    }

    private JProgressBar barraProgreso;
    private JLabel labelEstado;

    /**
     * Importa películas desde un archivo CSV mostrando una barra de progreso.
     * Usa SwingWorker para ejecutar la importación en segundo plano.
     * 
     * @param archivo  Archivo CSV a importar
     * @param parent   Componente padre para mostrar diálogos
     * @param callback Callback opcional que se ejecuta al finalizar
     */
    public void importarConProgreso(File archivo, JFrame parent, Runnable callback) {
        // Crear diálogo de progreso
        JDialog dialogoProgreso = crearDialogoProgreso(parent);

        // SwingWorker para importación en background
        SwingWorker<ResultadoImportacion, ProgresoImportacion> worker = new SwingWorker<ResultadoImportacion, ProgresoImportacion>() {

            @Override
            protected ResultadoImportacion doInBackground() throws Exception {
                return procesarArchivoCSV(archivo, this);
            }

            @Override
            protected void process(List<ProgresoImportacion> chunks) {
                // Actualizar UI con el último progreso
                if (!chunks.isEmpty()) {
                    ProgresoImportacion ultimo = chunks.get(chunks.size() - 1);
                    barraProgreso.setValue(ultimo.getPorcentaje());
                    labelEstado.setText(ultimo.getMensaje());
                }
            }

            @Override
            protected void done() {
                dialogoProgreso.dispose();

                try {
                    ResultadoImportacion resultado = get();

                    // Mostrar resultado
                    String mensaje = String.format(
                            "Importación completada:\n\n" +
                                    "✅ Películas importadas: %d\n" +
                                    "⚠️  Errores: %d\n" +
                                    "⏱️  Tiempo: %.2f segundos",
                            resultado.getExitosas(),
                            resultado.getErrores(),
                            resultado.getTiempoSegundos());

                    if (resultado.getErrores() > 0 && !resultado.getMensajesError().isEmpty()) {
                        mensaje += "\n\nPrimeros errores:\n";
                        int maxErrores = Math.min(3, resultado.getMensajesError().size());
                        for (int i = 0; i < maxErrores; i++) {
                            mensaje += "• " + resultado.getMensajesError().get(i) + "\n";
                        }
                    }

                    JOptionPane.showMessageDialog(
                            parent,
                            mensaje,
                            "Importación Finalizada",
                            resultado.getErrores() == 0 ? JOptionPane.INFORMATION_MESSAGE
                                    : JOptionPane.WARNING_MESSAGE);

                    // Ejecutar callback si existe
                    if (callback != null) {
                        callback.run();
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            parent,
                            "Error durante la importación:\n" + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        // Iniciar importación
        worker.execute();
        dialogoProgreso.setVisible(true);
    }

    /**
     * Procesa el archivo CSV línea por línea
     */
    private ResultadoImportacion procesarArchivoCSV(
            File archivo,
            SwingWorker<ResultadoImportacion, ProgresoImportacion> worker) {
        long inicio = System.currentTimeMillis();
        ResultadoImportacion resultado = new ResultadoImportacion();
        List<String> lineas = new ArrayList<>();

        try {
            // Leer todas las líneas primero
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                boolean primeraLinea = true;

                while ((linea = br.readLine()) != null) {
                    if (primeraLinea) {
                        primeraLinea = false; // Saltar encabezado
                        continue;
                    }
                    lineas.add(linea);
                }
            }

            int total = lineas.size();
            int procesadas = 0;

            // Procesar cada línea
            for (String linea : lineas) {
                try {
                    Pelicula pelicula = parsearLineaCSV(linea);
                    peliculaDAO.registrar(pelicula);
                    resultado.incrementarExitosas();

                } catch (Exception e) {
                    resultado.incrementarErrores();
                    resultado.agregarMensajeError("Línea " + (procesadas + 2) + ": " + e.getMessage());
                }

                procesadas++;

                // Publicar progreso
                int porcentaje = (procesadas * 100) / total;
                String mensaje = String.format(
                        "Procesando película %d de %d...",
                        procesadas,
                        total);
                ProgresoImportacion progreso = new ProgresoImportacion(porcentaje, mensaje);
                SwingUtilities.invokeLater(() -> {
                    barraProgreso.setValue(progreso.getPorcentaje());
                    labelEstado.setText(progreso.getMensaje());
                });

                // Pequeña pausa para que se vea el progreso
                Thread.sleep(10);
            }

        } catch (Exception e) {
            resultado.agregarMensajeError("Error general: " + e.getMessage());
        }

        long fin = System.currentTimeMillis();
        resultado.setTiempoSegundos((fin - inicio) / 1000.0);

        return resultado;
    }

    /**
     * Parsea una línea del CSV y crea un objeto Pelicula
     * Formato: Titulo,Director,Genero,Año,Rating,Duracion,Sinopsis
     */
    private Pelicula parsearLineaCSV(String linea) throws Exception {
        String[] campos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Split respetando comillas

        if (campos.length < 7) {
            throw new Exception("Formato inválido - se esperan 7 campos");
        }

        Pelicula pelicula = new Pelicula();

        // Título
        pelicula.getMetadatos().setTitulo(limpiarCampo(campos[0]));

        // Director
        pelicula.getMetadatos().setDirector(limpiarCampo(campos[1]));

        // Género(s)
        String[] generos = limpiarCampo(campos[2]).split("\\|");
        for (String genero : generos) {
            pelicula.anadirGeneros(genero.trim());
        }

        // Año
        try {
            pelicula.setAnio(Integer.parseInt(limpiarCampo(campos[3])));
        } catch (NumberFormatException e) {
            pelicula.setAnio(2000);
        }

        // Rating
        try {
            pelicula.setRatingPromedio(Float.parseFloat(limpiarCampo(campos[4])));
        } catch (NumberFormatException e) {
            pelicula.setRatingPromedio(7.0f);
        }

        // Duración
        try {
            pelicula.getVideo().setDuracion(Double.parseDouble(limpiarCampo(campos[5])));
        } catch (NumberFormatException e) {
            pelicula.getVideo().setDuracion(120.0);
        }

        // Sinopsis
        pelicula.getMetadatos().setSipnosis(limpiarCampo(campos[6]));

        return pelicula;
    }

    /**
     * Limpia un campo CSV removiendo comillas y espacios
     */
    private String limpiarCampo(String campo) {
        if (campo == null)
            return "";
        return campo.trim().replaceAll("^\"|\"$", "");
    }

    /**
     * Crea el diálogo de progreso
     */
    private JDialog crearDialogoProgreso(JFrame parent) {
        JDialog dialogo = new JDialog(parent, "Importando Películas", true);
        dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialogo.setSize(450, 150);
        dialogo.setLocationRelativeTo(parent);
        dialogo.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        labelEstado = new JLabel("Iniciando importación...", SwingConstants.CENTER);
        labelEstado.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(labelEstado, BorderLayout.NORTH);

        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setStringPainted(true);
        barraProgreso.setPreferredSize(new Dimension(400, 30));
        panel.add(barraProgreso, BorderLayout.CENTER);

        JLabel labelInfo = new JLabel("Por favor espere...", SwingConstants.CENTER);
        labelInfo.setFont(new Font("Arial", Font.ITALIC, 10));
        labelInfo.setForeground(Color.GRAY);
        panel.add(labelInfo, BorderLayout.SOUTH);

        dialogo.add(panel);

        return dialogo;
    }

    /**
     * Clase interna para reportar progreso
     */
    private static class ProgresoImportacion {
        private int porcentaje;
        private String mensaje;

        public ProgresoImportacion(int porcentaje, String mensaje) {
            this.porcentaje = porcentaje;
            this.mensaje = mensaje;
        }

        public int getPorcentaje() {
            return porcentaje;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

    /**
     * Clase interna para resultado de importación
     */
    public static class ResultadoImportacion {
        private int exitosas = 0;
        private int errores = 0;
        private double tiempoSegundos = 0;
        private List<String> mensajesError = new ArrayList<>();

        public void incrementarExitosas() {
            exitosas++;
        }

        public void incrementarErrores() {
            errores++;
        }

        public void setTiempoSegundos(double tiempo) {
            tiempoSegundos = tiempo;
        }

        public void agregarMensajeError(String mensaje) {
            mensajesError.add(mensaje);
        }

        public int getExitosas() {
            return exitosas;
        }

        public int getErrores() {
            return errores;
        }

        public double getTiempoSegundos() {
            return tiempoSegundos;
        }

        public List<String> getMensajesError() {
            return mensajesError;
        }
    }
}
