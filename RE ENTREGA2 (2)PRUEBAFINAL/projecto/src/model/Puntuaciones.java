package model;

/**
 * Lista de las puntuaciones hechas por {@link Cliente}
 * 
 * @author Juana Sabbione
 * @author Priscila Esposito
 * @version 1.0
 * @since 2025
 * @see Cliente
 */

public class Puntuaciones {
	    private String titulo;
	    private  int puntuacion;

	    public Puntuaciones() {
	    }

	    /**
	     * Constructor con parametros para inicializar una puntuacion.
	     *
	     * @param titulo     nombre del titulo evaluado
	     * @param puntuacion calificacion numerica del titulo
	     */
	    public Puntuaciones(String titulo, int puntuacion) {
	        this.titulo = titulo;
	        this.puntuacion = puntuacion;
	    }

	    /**
	     * Obtiene el nombre del titulo.
	     *
	     * @return titulo evaluado
	     */
	    public String getTitulo() {
	        return titulo;
	    }

	    /**
	     * Obtiene la calificacion numerica del título.
	     *
	     * @return puntuacion del titulo
	     */
	    public int getPuntuacion() {
	        return puntuacion;
	    }

}

