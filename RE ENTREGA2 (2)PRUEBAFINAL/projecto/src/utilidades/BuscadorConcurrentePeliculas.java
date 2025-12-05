package utilidades;

import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Buscador concurrente de peliculas usando Thread Pool.
 * Permite realizar multiples busquedas en paralelo mejorando el rendimiento.
 */
public class BuscadorConcurrentePeliculas {

    private ExecutorService executorService;
    private static final int NUM_THREADS = 3; // Pool de 3 threads

    /**
     * Constructor que inicializa el thread pool
     */
    public BuscadorConcurrentePeliculas() {
        // Crear un thread pool de tamanio fijo
        executorService = Executors.newFixedThreadPool(NUM_THREADS);
        System.out.println("[BuscadorConcurrente] Thread Pool creado con " + NUM_THREADS + " threads");
    }

    /**
     * Clase interna Runnable para busqueda de pelicula
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
                // Simular busqueda (en implementacion real, llamarias a la API)
                JSONObject resultado = db.ConsultaPeliculasOMDb.buscarPelicula(termino);

                if (resultado != null && !resultado.optString("Response", "False").equals("False")) {
                    System.out.println("[" + threadName + "] ✓ Encontrada: " +
                            resultado.optString("Title", termino));
                    return new ResultadoBusqueda(termino, resultado, null);
                } else {
                    System.out.println("[" + threadName + "] ✗ No encontrada: " + termino);
                    return new ResultadoBusqueda(termino, null,
                            new Exception("Pelicula no encontrada"));
                }

            } catch (Exception e) {
                System.out.println("[" + threadName + "] ✗ Error: " + termino + " - " + e.getMessage());
                return new ResultadoBusqueda(termino, null, e);
            }
        }
    }

    /**
     * Clase para encapsular el resultado de una busqueda
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
     * Buscar multiples peliculas en paralelo
     * 
     * @param terminos Lista de terminos de busqueda
     * @return Lista de resultados (en el mismo orden que los terminos)
     */
    public List<ResultadoBusqueda> buscarMultiple(List<String> terminos) {
        System.out.println("\n[BuscadorConcurrente] Iniciando busqueda de " +
                terminos.size() + " peliculas en paralelo...");

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
        System.out.println("[BuscadorConcurrente] Busqueda completada: " +
                exitosos + " exitosas de " + terminos.size());

        return resultados;
    }

    /**
     * Buscar peliculas con timeout
     * 
     * @param termino         Termino de busqueda
     * @param timeoutSegundos Tiempo maximo de espera en segundos
     * @return Resultado de la busqueda
     */
    public ResultadoBusqueda buscarConTimeout(String termino, int timeoutSegundos) {
        Callable<ResultadoBusqueda> tarea = new TareaBusquedaPelicula(termino);
        Future<ResultadoBusqueda> future = executorService.submit(tarea);

        try {
            return future.get(timeoutSegundos, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true); // Cancelar la tarea
            return new ResultadoBusqueda(termino, null,
                    new Exception("Busqueda excedio el tiempo limite de " + timeoutSegundos + "s"));
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
     * Verificar si el thread pool esta activo
     */
    public boolean estaActivo() {
        return !executorService.isShutdown();
    }
}
