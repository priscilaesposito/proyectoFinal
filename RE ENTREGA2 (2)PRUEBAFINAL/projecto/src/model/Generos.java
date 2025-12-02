package model;

/**
 * Lista de los generos vistos por {@link Cliente}
 * 
 * @author Juana Sabbione
 * @author Priscila Esposito
 * @version 1.0
 * @since 2025
 * @see Cliente
 */

public class Generos {

	public String genero;
    public int cant;

    public Generos() {
    }

    /**
     * Constructor con parametros para inicializar un genero.
     *
     * @param genero nombre del genero (ejemplo: "Accion")
     * @param cant   cantidad de titulos de ese genero
     */
    public Generos(String genero, int cant) {
        this.genero = genero;
        this.cant = cant;
    }

    /**
     * Obtiene el nombre del genero.
     *
     * @return nombre del genero
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Obtiene la cantidad de titulos del genero.
     *
     * @return cantidad de titulos
     */
    public int getCant() {
        return cant;
    }

    /**
     * Asigna el nombre del genero.
     *
     * @param genero nombre del genero
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Asigna la cantidad de titulos del genero.
     *
     * @param cant cantidad de titulos
     */
    public void setCant(int cant) {
        this.cant = cant;
    }

}
