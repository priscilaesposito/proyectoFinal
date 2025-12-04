package utilidades;

import model.Pelicula;
import dao.PeliculaDAO;
import java.util.List;

/**
 * Thread para cargar películas en segundo plano.
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
     * @param peliculaDAO   DAO para acceder a las películas
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
     * Método run() - Contiene el código que se ejecutará concurrentemente
     */
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + threadName + "] Iniciando carga de películas...");

        try {
            // Simular tiempo de carga inicial
            Thread.sleep(500);

            // Cargar películas según el tipo de login
            if (esPrimerLogin) {
                System.out.println("[" + threadName + "] Cargando Top 10 películas...");
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
                    " películas");

        } catch (InterruptedException e) {
            System.out.println("[" + threadName + "] ✗ Thread interrumpido");
            this.error = e;
            Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
        } catch (Exception e) {
            System.out.println("[" + threadName + "] ✗ Error: " + e.getMessage());
            this.error = e;
        }
    }

    /**
     * Obtener las películas cargadas (debe llamarse después de que el thread
     * termine)
     * 
     * @return Lista de películas o null si hubo error
     */
    public List<Pelicula> getPeliculasCargadas() {
        return peliculasCargadas;
    }

    /**
     * Obtener el error si ocurrió alguno
     * 
     * @return Exception o null si no hubo error
     */
    public Exception getError() {
        return error;
    }

    /**
     * Verificar si la carga fue completada exitosamente
     * 
     * @return true si se completó sin errores
     */
    public boolean isCargaCompletada() {
        return cargaCompletada;
    }

    /**
     * Método estático de utilidad para cargar películas con timeout
     * 
     * @param peliculaDAO   DAO de películas
     * @param idUsuario     ID del usuario
     * @param esPrimerLogin Si es primer login
     * @param timeoutMs     Tiempo máximo de espera en milisegundos
     * @return Lista de películas o null si excede el timeout
     */
    public static List<Pelicula> cargarConTimeout(PeliculaDAO peliculaDAO, int idUsuario,
            boolean esPrimerLogin, long timeoutMs) {
        CargadorPeliculasThread cargador = new CargadorPeliculasThread(peliculaDAO, idUsuario, esPrimerLogin);
        cargador.start(); // Iniciar el thread

        try {
            // Esperar a que el thread termine con timeout
            cargador.join(timeoutMs);

            // Verificar si el thread aún está vivo (excedió el timeout)
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
            System.err.println("Error al cargar películas: " + e.getMessage());
            return null;
        }
    }
}
