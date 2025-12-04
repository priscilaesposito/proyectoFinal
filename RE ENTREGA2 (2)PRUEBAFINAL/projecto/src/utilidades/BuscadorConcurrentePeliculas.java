package utilidades;

import model.Pelicula;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Buscador concurrente de películas usando Thread Pool.
 * Permite realizar múltiples búsquedas en paralelo mejorando el rendimiento.
 */
public class BuscadorConcurrentePeliculas {

    private ExecutorService executorService;
    private static final int NUM_THREADS = 3; // Pool de 3 threads

    /**
     * Constructor que inicializa el thread pool
     */
    public BuscadorConcurrentePeliculas() {
        // Crear un thread pool de tamaño fijo
        executorService = Executors.newFixedThreadPool(NUM_THREADS);
        System.out.println("[BuscadorConcurrente] Thread Pool creado con " + NUM_THREADS + " threads");
    }

    /**
     * Clase interna Runnable para búsqueda de película
     */
    private class TareaBusquedaPelicula implements Callable<ResultadoBusqueda> {
        private String termino;

        public TareaBusquedaPelicula(String termino) {
            this.termino = termino;
        }

        @Override
        public ResultadoBusqueda call() throws Exception {
            String threadName = Thread.currentThread().getName();
            System.out.println("[" + threadName + "] Buscando: " + termino);

            try {
                // Simular búsqueda (en implementación real, llamarías a la API)
                JSONObject resultado = db.ConsultaPeliculasOMDb.buscarPelicula(termino);

                if (resultado != null && !resultado.optString("Response", "False").equals("False")) {
                    System.out.println("[" + threadName + "] ✓ Encontrada: " +
                            resultado.optString("Title", termino));
                    return new ResultadoBusqueda(termino, resultado, null);
                } else {
                    System.out.println("[" + threadName + "] ✗ No encontrada: " + termino);
                    return new ResultadoBusqueda(termino, null,
                            new Exception("Película no encontrada"));
                }

            } catch (Exception e) {
                System.out.println("[" + threadName + "] ✗ Error: " + termino + " - " + e.getMessage());
                return new ResultadoBusqueda(termino, null, e);
            }
        }
    }

    /**
     * Clase para encapsular el resultado de una búsqueda
     */
    public static class ResultadoBusqueda {
        private String termino;
        private JSONObject resultado;
        private Exception error;

        public ResultadoBusqueda(String termino, JSONObject resultado, Exception error) {
            this.termino = termino;
            this.resultado = resultado;
            this.error = error;
        }

        public String getTermino() {
            return termino;
        }

        public JSONObject getResultado() {
            return resultado;
        }

        public Exception getError() {
            return error;
        }

        public boolean esExitoso() {
            return resultado != null && error == null;
        }
    }

    /**
     * Buscar múltiples películas en paralelo
     * 
     * @param terminos Lista de términos de búsqueda
     * @return Lista de resultados (en el mismo orden que los términos)
     */
    public List<ResultadoBusqueda> buscarMultiple(List<String> terminos) {
        System.out.println("\n[BuscadorConcurrente] Iniciando búsqueda de " +
                terminos.size() + " películas en paralelo...");

        List<Future<ResultadoBusqueda>> futures = new ArrayList<>();
        List<ResultadoBusqueda> resultados = new ArrayList<>();

        // Enviar todas las tareas al thread pool
        for (String termino : terminos) {
            Callable<ResultadoBusqueda> tarea = new TareaBusquedaPelicula(termino);
            Future<ResultadoBusqueda> future = executorService.submit(tarea);
            futures.add(future);
        }

        // Recolectar todos los resultados
        for (Future<ResultadoBusqueda> future : futures) {
            try {
                // Esperar hasta 10 segundos por cada resultado
                ResultadoBusqueda resultado = future.get(10, TimeUnit.SECONDS);
                resultados.add(resultado);
            } catch (TimeoutException e) {
                resultados.add(new ResultadoBusqueda("timeout", null,
                        new Exception("Tiempo de espera excedido")));
            } catch (Exception e) {
                resultados.add(new ResultadoBusqueda("error", null, e));
            }
        }

        // Resumen de resultados
        long exitosos = resultados.stream().filter(ResultadoBusqueda::esExitoso).count();
        System.out.println("[BuscadorConcurrente] Búsqueda completada: " +
                exitosos + " exitosas de " + terminos.size());

        return resultados;
    }

    /**
     * Buscar películas con timeout
     * 
     * @param termino         Término de búsqueda
     * @param timeoutSegundos Tiempo máximo de espera
     * @return Resultado de la búsqueda
     */
    public ResultadoBusqueda buscarConTimeout(String termino, int timeoutSegundos) {
        Callable<ResultadoBusqueda> tarea = new TareaBusquedaPelicula(termino);
        Future<ResultadoBusqueda> future = executorService.submit(tarea);

        try {
            return future.get(timeoutSegundos, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true); // Cancelar la tarea
            return new ResultadoBusqueda(termino, null,
                    new Exception("Búsqueda excedió el tiempo límite de " + timeoutSegundos + "s"));
        } catch (Exception e) {
            return new ResultadoBusqueda(termino, null, e);
        }
    }

    /**
     * Apagar el thread pool de forma ordenada
     */
    public void shutdown() {
        System.out.println("[BuscadorConcurrente] Apagando thread pool...");
        executorService.shutdown();

        try {
            // Esperar hasta 5 segundos para que terminen las tareas
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
            System.out.println("[BuscadorConcurrente] Thread pool apagado correctamente");
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Verificar si el thread pool está activo
     */
    public boolean estaActivo() {
        return !executorService.isShutdown();
    }
}
