package utilidades;

import java.util.Arrays;
import java.util.List;

/**
 * Clase de demostracion del uso de concurrencia en el sistema.
 * Muestra ejemplos de uso de Threads, Timer/TimerTask, y Thread Pool.
 */
public class DemostracionConcurrencia {

    public static void main(String[] args) {
        System.out.println("=== DEMOSTRACIoN DE CONCURRENCIA EN JAVA ===\n");

        // 1. Demostracion de Timer y TimerTask (Auto-guardado)
        demostrarAutoGuardado();

        // 2. Demostracion de Thread Pool (Busquedas concurrentes)
        demostrarBusquedaConcurrente();

        // 3. Demostracion de Thread extendiendo Thread (Carga de peliculas)
        demostrarCargaPeliculas();

        System.out.println("\n=== FIN DE LA DEMOSTRACIoN ===");
    }

    /**
     * Demostracion 1: Timer y TimerTask
     * Usa AutoGuardadoResenias que implementa Timer para auto-guardar cada 30
     * segundos
     */
    private static void demostrarAutoGuardado() {
        System.out.println("1️⃣  DEMO: Timer y TimerTask - Auto-guardado de Resenias");
        System.out.println("   Concepto: Un Timer ejecuta una TimerTask periodicamente en su propio thread\n");

        try {
            // Obtener instancia del auto-guardado (inicia el Timer automaticamente)
            AutoGuardadoResenias autoGuardado = AutoGuardadoResenias.getInstance();

            // Simular que un usuario esta escribiendo resenias
            System.out.println("   Usuario escribe resenias...");
            autoGuardado.agregarBorrador(1, 101, "The Matrix", 9, "Excelente pelicula de ciencia ficcion");
            autoGuardado.agregarBorrador(1, 102, "Inception", 10, "Obra maestra de Christopher Nolan");
            autoGuardado.agregarBorrador(2, 103, "Interstellar", 9, "Impresionante narrativa espacial");

            System.out.println("   ✓ 3 borradores creados");
            System.out.println("   ⏰ El Timer guardara automaticamente cada 30 segundos...");
            System.out.println("   📊 Borradores activos: " + autoGuardado.getCantidadBorradores());

            // Esperar para ver el auto-guardado (opcional en produccion)
            // Thread.sleep(35000); // Descomentar para ver el guardado automatico

            // Simular que se envia una resenia
            System.out.println("   📤 Usuario envia la resenia de The Matrix...");
            autoGuardado.eliminarBorrador(1, 101);
            System.out.println("   ✓ Borrador eliminado");
            System.out.println("   📊 Borradores restantes: " + autoGuardado.getCantidadBorradores());

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * Demostracion 2: ExecutorService y Thread Pool
     * Usa BuscadorConcurrentePeliculas para busquedas paralelas
     */
    private static void demostrarBusquedaConcurrente() {
        System.out.println("2️⃣  DEMO: ExecutorService y Thread Pool - Busquedas Concurrentes");
        System.out.println("   Concepto: Un pool de threads reutilizables ejecuta multiples tareas en paralelo\n");

        BuscadorConcurrentePeliculas buscador = new BuscadorConcurrentePeliculas();

        try {
            // Buscar multiples peliculas en paralelo
            List<String> peliculasABuscar = Arrays.asList(
                    "The Matrix",
                    "Inception",
                    "Interstellar",
                    "The Dark Knight",
                    "Fight Club");

            System.out.println("   🔍 Buscando " + peliculasABuscar.size() + " peliculas en paralelo...");
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
            System.out.println("   💡 Beneficio: Sin concurrencia tomaria ~" +
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
     * Demostracion 3: Thread extendiendo Thread
     * Usa CargadorPeliculasThread para carga en background
     */
    private static void demostrarCargaPeliculas() {
        System.out.println("3️⃣  DEMO: Thread (extendiendo Thread) - Carga de Peliculas");
        System.out.println("   Concepto: Un thread personalizado ejecuta una tarea en segundo plano\n");

        try {
            // Simular carga de peliculas usando el metodo con timeout
            System.out.println("   📥 Iniciando carga de peliculas en background...");
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

            // El thread main continua ejecutandose
            System.out.println("   [" + Thread.currentThread().getName() +
                    "] Thread main continua ejecutandose...");

            // Esperar a que termine el thread de carga
            threadCarga.join(); // join() bloquea hasta que el thread termine

            System.out.println("   [" + Thread.currentThread().getName() +
                    "] Thread de carga completado");

            System.out.println("\n   📊 Estados del Thread:");
            System.out.println("   • NEW: Creado pero no iniciado");
            System.out.println("   • RUNNABLE: Ejecutandose o listo para ejecutarse");
            System.out.println("   • BLOCKED: Esperando un monitor lock");
            System.out.println("   • WAITING: Esperando indefinidamente");
            System.out.println("   • TIMED_WAITING: Esperando por un tiempo especifico (sleep/join con timeout)");
            System.out.println("   • TERMINATED: Completado");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * Demostracion adicional: Metodos clave de Thread
     */
    @SuppressWarnings("unused")
    private static void demostrarMetodosThread() {
        System.out.println("4️⃣  DEMO: Metodos clave de Thread\n");

        Thread thread = new Thread(() -> {
            try {
                System.out.println("   Thread ejecutandose: " + Thread.currentThread().getName());

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
            System.out.println("   ¿Esta vivo? " + thread.isAlive()); // false

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
