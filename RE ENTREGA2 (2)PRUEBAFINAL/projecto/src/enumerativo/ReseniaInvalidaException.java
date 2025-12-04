package enumerativo;

/**
 * Excepción personalizada para errores en reseñas de películas.
 * Esta es una Checked Exception que debe ser manejada explícitamente.
 * 
 * Se lanza cuando:
 * - La reseña está vacía o es demasiado corta
 * - La calificación está fuera del rango válido (1-10)
 * - El usuario ya calificó la película
 * - La reseña contiene contenido inapropiado
 * - No se encuentra la reseña solicitada
 */
public class ReseniaInvalidaException extends Exception {

    private int calificacion; // Calificación que causó el error
    private String motivoRechazo; // Motivo específico del rechazo
    private boolean esProblemaCalificacion; // true si el error es por calificación, false si es por texto

    /**
     * Constructor básico con mensaje de error
     * 
     * @param mensaje Descripción del error
     */
    public ReseniaInvalidaException(String mensaje) {
        super(mensaje);
        this.esProblemaCalificacion = false;
    }

    /**
     * Constructor con mensaje y causa
     * 
     * @param mensaje Descripción del error
     * @param causa   Excepción que causó este error
     */
    public ReseniaInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.esProblemaCalificacion = false;
    }

    /**
     * Constructor para errores relacionados con la calificación
     * 
     * @param mensaje      Descripción del error
     * @param calificacion Calificación inválida ingresada
     */
    public ReseniaInvalidaException(String mensaje, int calificacion) {
        super(mensaje);
        this.calificacion = calificacion;
        this.esProblemaCalificacion = true;
    }

    /**
     * Constructor completo con motivo de rechazo
     * 
     * @param mensaje       Descripción del error
     * @param motivoRechazo Razón específica del rechazo
     */
    public ReseniaInvalidaException(String mensaje, String motivoRechazo) {
        super(mensaje);
        this.motivoRechazo = motivoRechazo;
        this.esProblemaCalificacion = false;
    }

    /**
     * Obtiene la calificación que causó el error
     * 
     * @return Calificación inválida
     */
    public int getCalificacion() {
        return calificacion;
    }

    /**
     * Obtiene el motivo del rechazo
     * 
     * @return Motivo del rechazo
     */
    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    /**
     * Indica si el error es por calificación inválida
     * 
     * @return true si es problema de calificación
     */
    public boolean esProblemaCalificacion() {
        return esProblemaCalificacion;
    }

    /**
     * Retorna un mensaje detallado del error
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ReseniaInvalidaException: ");
        sb.append(getMessage());

        if (esProblemaCalificacion) {
            sb.append(" [Calificación inválida: ").append(calificacion).append("]");
        }

        if (motivoRechazo != null) {
            sb.append(" [Motivo: ").append(motivoRechazo).append("]");
        }

        return sb.toString();
    }
}
