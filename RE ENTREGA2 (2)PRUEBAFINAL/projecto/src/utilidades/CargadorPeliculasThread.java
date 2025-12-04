package utilidades;

import model.Pelicula;
import dao.PeliculaDAO;
import java.util.List;

/**
 * Thread para cargar peliculas en segundo plano.
 * Extiende la clase Thread e implementa la estrategia de heredar de Thread.
 */
public class CargadorPeliculasThread extends Thread {

    private PeliculaDAO peliculaDAO;
    private int idUsuario;
    private boolean esPrimerLogin;
    private List<Pelicula> peliculasCargadas;
    private Exception error;
    private boolean cargaCompletada;

    /**
     * Constructor del thread
     * 
     * @param peliculaDAO   DAO para acceder a las peliculas
     * @param idUsuario     ID del usuario
     * @param esPrimerLogin Si es el primer login del usuario
     */
    public CargadorPeliculasThread(PeliculaDAO peliculaDAO, int idUsuario, boolean esPrimerLogin) {
        super("CargadorPeliculas-" + idUsuario); // Nombre del thread
        this.peliculaDAO = peliculaDAO;
        this.idUsuario = idUsuario;
        this.esPrimerLogin = esPrimerLogin;
        this.cargaCompletada = false;
    }

    /**
     * Metodo run() - Contiene el codigo que se ejecutara concurrentemente
     */
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + threadName + "] Iniciando carga de peliculas...");

        try {
            // Simular tiempo de carga inicial
            Thread.sleep(500);

            // Cargar peliculas segun el tipo de login
            if (esPrimerLogin) {
                System.out.println("[" + threadName + "] Cargando Top 10 peliculas...");
                peliculasCargadas = peliculaDAO.obtenerTop10PorRating();
            } else {
                System.out.println("[" + threadName + "] Cargando recomendaciones personalizadas...");
                peliculasCargadas = peliculaDAO.obtener10RandomNoCalificadas(idUsuario);
            }

            // Simular procesamiento adicional
            Thread.sleep(300);

            cargaCompletada = true;
            System.out.println("[" + threadName + "] ✓ Carga completada: " +
                    (peliculasCargadas != null ? peliculasCargadas.size() : 0) +
                    " peliculas");

        } catch (InterruptedException e) {
            System.out.println("[" + threadName + "] ✗ Thread interrumpido");
            this.error = e;
            Thread.currentThread().interrupt(); // Restaurar el estado de interrupcion
        } catch (Exception e) {
            System.out.println("[" + threadName + "] ✗ Error: " + e.getMessage());
            this.error = e;
        }
    }

    /**
     * Obtener las peliculas cargadas (debe llamarse despues de que el thread
     * termine)
     * 
     * @return Lista de peliculas o null si hubo error
     */
    public List<Pelicula> getPeliculasCargadas() {
        return peliculasCargadas;
    }

    /**
     * Obtener el error si ocurrio alguno
     * 
     * @return Exception o null si no hubo error
     */
    public Exception getError() {
        return error;
    }

    /**
     * Verificar si la carga fue completada exitosamente
     * 
     * @return true si se completo sin errores
     */
    public boolean isCargaCompletada() {
        return cargaCompletada;
    }

    /**
     * Metodo estatico de utilidad para cargar peliculas con timeout
     * 
     * @param peliculaDAO   DAO de peliculas
     * @param idUsuario     ID del usuario
     * @param esPrimerLogin Si es primer login
     * @param timeoutMs     Tiempo maximo de espera en milisegundos
     * @return Lista de peliculas o null si excede el timeout
     */
    public static List<Pelicula> cargarConTimeout(PeliculaDAO peliculaDAO, int idUsuario,
            boolean esPrimerLogin, long timeoutMs) {
        CargadorPeliculasThread cargador = new CargadorPeliculasThread(peliculaDAO, idUsuario, esPrimerLogin);
        cargador.start(); // Iniciar el thread

        try {
            // Esperar a que el thread termine con timeout
            cargador.join(timeoutMs);

            // Verificar si el thread aun esta vivo (excedio el timeout)
            if (cargador.isAlive()) {
                System.out.println("[CargadorPeliculas] ⚠ Timeout excedido, interrumpiendo...");
                cargador.interrupt();
                return null;
            }

            // Verificar si hubo error
            if (cargador.getError() != null) {
                throw cargador.getError();
            }

            return cargador.getPeliculasCargadas();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (Exception e) {
            System.err.println("Error al cargar peliculas: " + e.getMessage());
            return null;
        }
    }
}
