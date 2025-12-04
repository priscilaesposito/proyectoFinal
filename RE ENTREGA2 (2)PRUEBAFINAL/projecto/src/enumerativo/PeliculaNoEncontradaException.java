package enumerativo;

/**
 * Excepción personalizada para cuando no se encuentra una película solicitada.
 * Esta es una Checked Exception que debe ser manejada explícitamente.
 * 
 * Se lanza cuando:
 * - No se encuentra una película por su ID
 * - No se encuentra una película por su título
 * - La búsqueda en OMDb no retorna resultados
 * - No hay películas disponibles para recomendar
 */
public class PeliculaNoEncontradaException extends Exception {

    private String criterioBusqueda; // Criterio usado para buscar (ID, título, etc.)
    private String valorBuscado; // Valor que se buscó

    /**
     * Constructor básico con mensaje de error
     * 
     * @param mensaje Descripción del error
     */
    public PeliculaNoEncontradaException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa
     * 
     * @param mensaje Descripción del error
     * @param causa   Excepción que causó este error
     */
    public PeliculaNoEncontradaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor completo con información de búsqueda
     * 
     * @param mensaje          Descripción del error
     * @param criterioBusqueda Tipo de búsqueda realizada
     * @param valorBuscado     Valor que se intentó encontrar
     */
    public PeliculaNoEncontradaException(String mensaje, String criterioBusqueda, String valorBuscado) {
        super(mensaje);
        this.criterioBusqueda = criterioBusqueda;
        this.valorBuscado = valorBuscado;
    }

    /**
     * Obtiene el criterio de búsqueda usado
     * 
     * @return Criterio de búsqueda
     */
    public String getCriterioBusqueda() {
        return criterioBusqueda;
    }

    /**
     * Obtiene el valor que se buscó
     * 
     * @return Valor buscado
     */
    public String getValorBuscado() {
        return valorBuscado;
    }

    /**
     * Retorna un mensaje detallado del error
     */
    @Override
    public String toString() {
        if (criterioBusqueda != null && valorBuscado != null) {
            return "PeliculaNoEncontradaException: " + getMessage() +
                    " [Búsqueda por " + criterioBusqueda + ": " + valorBuscado + "]";
        }
        return "PeliculaNoEncontradaException: " + getMessage();
    }
}
