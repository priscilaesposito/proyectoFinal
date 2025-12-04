package utilidades;

import java.util.Arrays;
import java.util.List;

/**
 * Clase de demostración del uso de concurrencia en el sistema.
 * Muestra ejemplos de uso de Threads, Timer/TimerTask, y Thread Pool.
 */
public class DemostracionConcurrencia {

    public static void main(String[] args) {
        System.out.println("=== DEMOSTRACIÓN DE CONCURRENCIA EN JAVA ===\n");

        // 1. Demostración de Timer y TimerTask (Auto-guardado)
        demostrarAutoGuardado();

        // 2. Demostración de Thread Pool (Búsquedas concurrentes)
        demostrarBusquedaConcurrente();

        // 3. Demostración de Thread extendiendo Thread (Carga de películas)
        demostrarCargaPeliculas();

        System.out.println("\n=== FIN DE LA DEMOSTRACIÓN ===");
    }

    /**
     * Demostración 1: Timer y TimerTask
     * Usa AutoGuardadoResenias que implementa Timer para auto-guardar cada 30
     * segundos
     */
    private static void demostrarAutoGuardado() {
        System.out.println("1️⃣  DEMO: Timer y TimerTask - Auto-guardado de Reseñas");
        System.out.println("   Concepto: Un Timer ejecuta una TimerTask periódicamente en su propio thread\n");

        try {
            // Obtener instancia del auto-guardado (inicia el Timer automáticamente)
            AutoGuardadoResenias autoGuardado = AutoGuardadoResenias.getInstance();

            // Simular que un usuario está escribiendo reseñas
            System.out.println("   Usuario escribe reseñas...");
            autoGuardado.agregarBorrador(1, 101, "The Matrix", 9, "Excelente película de ciencia ficción");
            autoGuardado.agregarBorrador(1, 102, "Inception", 10, "Obra maestra de Christopher Nolan");
            autoGuardado.agregarBorrador(2, 103, "Interstellar", 9, "Impresionante narrativa espacial");

            System.out.println("   ✓ 3 borradores creados");
            System.out.println("   ⏰ El Timer guardará automáticamente cada 30 segundos...");
            System.out.println("   📊 Borradores activos: " + autoGuardado.getCantidadBorradores());

            // Esperar para ver el auto-guardado (opcional en producción)
            // Thread.sleep(35000); // Descomentar para ver el guardado automático

            // Simular que se envía una reseña
            System.out.println("   📤 Usuario envía la reseña de The Matrix...");
            autoGuardado.eliminarBorrador(1, 101);
            System.out.println("   ✓ Borrador eliminado");
            System.out.println("   📊 Borradores restantes: " + autoGuardado.getCantidadBorradores());

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * Demostración 2: ExecutorService y Thread Pool
     * Usa BuscadorConcurrentePeliculas para búsquedas paralelas
     */
    private static void demostrarBusquedaConcurrente() {
        System.out.println("2️⃣  DEMO: ExecutorService y Thread Pool - Búsquedas Concurrentes");
        System.out.println("   Concepto: Un pool de threads reutilizables ejecuta múltiples tareas en paralelo\n");

        BuscadorConcurrentePeliculas buscador = new BuscadorConcurrentePeliculas();

        try {
            // Buscar múltiples películas en paralelo
            List<String> peliculasABuscar = Arrays.asList(
                    "The Matrix",
                    "Inception",
                    "Interstellar",
                    "The Dark Knight",
                    "Fight Club");

            System.out.println("   🔍 Buscando " + peliculasABuscar.size() + " películas en paralelo...");
            long inicio = System.currentTimeMillis();

            List<BuscadorConcurrentePeliculas.ResultadoBusqueda> resultados = buscador.buscarMultiple(peliculasABuscar);

            long fin = System.currentTimeMillis();

            // Mostrar resultados
            System.out.println("\n   📋 RESULTADOS:");
            for (BuscadorConcurrentePeliculas.ResultadoBusqueda resultado : resultados) {
                if (resultado.esExitoso()) {
                    System.out.println("   ✓ " + resultado.getTermino() + " - Encontrada");
                } else {
                    System.out.println("   ✗ " + resultado.getTermino() + " - " +
                            resultado.getError().getMessage());
                }
            }

            System.out.println("\n   ⏱️  Tiempo total: " + (fin - inicio) + " ms");
            System.out.println("   💡 Beneficio: Sin concurrencia tomaría ~" +
                    (peliculasABuscar.size() * 2000) + " ms (estimado)");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Importante: apagar el thread pool
            buscador.shutdown();
        }

        System.out.println();
    }

    /**
     * Demostración 3: Thread extendiendo Thread
     * Usa CargadorPeliculasThread para carga en background
     */
    private static void demostrarCargaPeliculas() {
        System.out.println("3️⃣  DEMO: Thread (extendiendo Thread) - Carga de Películas");
        System.out.println("   Concepto: Un thread personalizado ejecuta una tarea en segundo plano\n");

        try {
            // Simular carga de películas usando el método con timeout
            System.out.println("   📥 Iniciando carga de películas en background...");
            System.out.println("   ⏰ Timeout configurado: 5000 ms");

            // Crear y usar el DAO real (comentado para demo)
            // PeliculaDAO dao = new PeliculaDAOjdbc();
            // List<Pelicula> peliculas = CargadorPeliculasThread.cargarConTimeout(dao, 1,
            // true, 5000);

            // Para la demo, creamos un thread simple
            Thread threadCarga = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("   [" + Thread.currentThread().getName() +
                                "] Cargando datos...");
                        Thread.sleep(1500); // Simular carga
                        System.out.println("   [" + Thread.currentThread().getName() +
                                "] ✓ Datos cargados exitosamente");
                    } catch (InterruptedException e) {
                        System.out.println("   [" + Thread.currentThread().getName() +
                                "] ✗ Carga interrumpida");
                    }
                }
            }, "CargadorDemo");

            threadCarga.start();

            // El thread main continúa ejecutándose
            System.out.println("   [" + Thread.currentThread().getName() +
                    "] Thread main continúa ejecutándose...");

            // Esperar a que termine el thread de carga
            threadCarga.join(); // join() bloquea hasta que el thread termine

            System.out.println("   [" + Thread.currentThread().getName() +
                    "] Thread de carga completado");

            System.out.println("\n   📊 Estados del Thread:");
            System.out.println("   • NEW: Creado pero no iniciado");
            System.out.println("   • RUNNABLE: Ejecutándose o listo para ejecutarse");
            System.out.println("   • BLOCKED: Esperando un monitor lock");
            System.out.println("   • WAITING: Esperando indefinidamente");
            System.out.println("   • TIMED_WAITING: Esperando por un tiempo específico (sleep/join con timeout)");
            System.out.println("   • TERMINATED: Completado");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * Demostración adicional: Métodos clave de Thread
     */
    @SuppressWarnings("unused")
    private static void demostrarMetodosThread() {
        System.out.println("4️⃣  DEMO: Métodos clave de Thread\n");

        Thread thread = new Thread(() -> {
            try {
                System.out.println("   Thread ejecutándose: " + Thread.currentThread().getName());

                // sleep() - Pausa el thread actual
                Thread.sleep(1000);

                // yield() - Sugiere ceder la CPU
                Thread.yield();

                System.out.println("   Thread finalizando");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "ThreadDemo");

        // start() - Inicia el thread
        thread.start();

        try {
            // join() - Espera a que el thread termine
            thread.join();

            // Verificar estado
            System.out.println("   Estado final: " + thread.getState()); // TERMINATED
            System.out.println("   ¿Está vivo? " + thread.isAlive()); // false

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
