package utilidades;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Gestor de auto-guardado de reseñas en borrador usando Timer y TimerTask.
 * Implementa concurrencia para guardar automáticamente el progreso de las
 * reseñas.
 */
public class AutoGuardadoResenias {

    private static AutoGuardadoResenias instancia;
    private Timer timer;
    private Map<String, BorradorResenia> borradores;

    // Intervalo de auto-guardado: cada 30 segundos
    private static final long INTERVALO_GUARDADO = 30000; // 30 segundos

    /**
     * Clase interna para representar un borrador de reseña
     */
    public static class BorradorResenia {
        private int idUsuario;
        private int idPelicula;
        private String tituloPelicula;
        private int calificacion;
        private String comentario;
        private long ultimaModificacion;

        public BorradorResenia(int idUsuario, int idPelicula, String tituloPelicula) {
            this.idUsuario = idUsuario;
            this.idPelicula = idPelicula;
            this.tituloPelicula = tituloPelicula;
            this.ultimaModificacion = System.currentTimeMillis();
        }

        public void actualizar(int calificacion, String comentario) {
            this.calificacion = calificacion;
            this.comentario = comentario;
            this.ultimaModificacion = System.currentTimeMillis();
        }

        public int getIdUsuario() {
            return idUsuario;
        }

        public int getIdPelicula() {
            return idPelicula;
        }

        public String getTituloPelicula() {
            return tituloPelicula;
        }

        public int getCalificacion() {
            return calificacion;
        }

        public String getComentario() {
            return comentario;
        }

        public long getUltimaModificacion() {
            return ultimaModificacion;
        }
    }

    /**
     * Constructor privado (Patrón Singleton)
     */
    private AutoGuardadoResenias() {
        // Usar ConcurrentHashMap para thread-safety
        borradores = new ConcurrentHashMap<>();
        timer = new Timer("AutoGuardado-Timer", true); // Daemon thread
        iniciarAutoGuardado();
    }

    /**
     * Obtener instancia única (Singleton)
     */
    public static synchronized AutoGuardadoResenias getInstance() {
        if (instancia == null) {
            instancia = new AutoGuardadoResenias();
        }
        return instancia;
    }

    /**
     * Iniciar el timer de auto-guardado
     */
    private void iniciarAutoGuardado() {
        TimerTask tareaGuardado = new TimerTask() {
            @Override
            public void run() {
                guardarBorradores();
            }
        };

        // Programar la tarea para ejecutarse cada INTERVALO_GUARDADO milisegundos
        timer.scheduleAtFixedRate(tareaGuardado, INTERVALO_GUARDADO, INTERVALO_GUARDADO);

        System.out.println("[AutoGuardado] Timer iniciado. Guardado cada " +
                (INTERVALO_GUARDADO / 1000) + " segundos.");
    }

    /**
     * Guardar todos los borradores (ejecutado por el Timer)
     */
    private void guardarBorradores() {
        if (borradores.isEmpty()) {
            return;
        }

        System.out.println("[AutoGuardado] Guardando " + borradores.size() +
                " borrador(es) - Thread: " + Thread.currentThread().getName());

        for (Map.Entry<String, BorradorResenia> entry : borradores.entrySet()) {
            BorradorResenia borrador = entry.getValue();

            // Simular guardado (en una implementación real, guardarías en BD o archivo)
            System.out.println("  → Borrador guardado: Usuario " + borrador.getIdUsuario() +
                    ", Película: " + borrador.getTituloPelicula() +
                    ", Calificación: " + borrador.getCalificacion() +
                    ", Comentario: " +
                    (borrador.getComentario() != null
                            ? borrador.getComentario().substring(0, Math.min(20, borrador.getComentario().length()))
                                    + "..."
                            : "vacío"));
        }
    }

    /**
     * Agregar o actualizar un borrador
     */
    public void agregarBorrador(int idUsuario, int idPelicula, String tituloPelicula,
            int calificacion, String comentario) {
        String clave = idUsuario + "_" + idPelicula;

        BorradorResenia borrador = borradores.get(clave);
        if (borrador == null) {
            borrador = new BorradorResenia(idUsuario, idPelicula, tituloPelicula);
            borradores.put(clave, borrador);
            System.out.println("[AutoGuardado] Nuevo borrador creado para: " + tituloPelicula);
        }

        borrador.actualizar(calificacion, comentario);
    }

    /**
     * Eliminar un borrador (cuando se envía la reseña final)
     */
    public void eliminarBorrador(int idUsuario, int idPelicula) {
        String clave = idUsuario + "_" + idPelicula;
        BorradorResenia eliminado = borradores.remove(clave);

        if (eliminado != null) {
            System.out.println("[AutoGuardado] Borrador eliminado: " + eliminado.getTituloPelicula());
        }
    }

    /**
     * Obtener un borrador específico
     */
    public BorradorResenia obtenerBorrador(int idUsuario, int idPelicula) {
        String clave = idUsuario + "_" + idPelicula;
        return borradores.get(clave);
    }

    /**
     * Limpiar todos los borradores
     */
    public void limpiarBorradores() {
        borradores.clear();
        System.out.println("[AutoGuardado] Todos los borradores limpiados");
    }

    /**
     * Detener el timer (llamar al cerrar la aplicación)
     */
    public void detener() {
        if (timer != null) {
            guardarBorradores(); // Guardar una última vez antes de detener
            timer.cancel();
            System.out.println("[AutoGuardado] Timer detenido");
        }
    }

    /**
     * Obtener cantidad de borradores activos
     */
    public int getCantidadBorradores() {
        return borradores.size();
    }
}
