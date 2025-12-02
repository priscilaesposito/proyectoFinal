package model;

/**
 * 
 * Cliente se hereda de {@link Usuario}
 * 
 * @author Juana Sabbione
 * @author Priscila Esposito
 * @version 1.0
 * @since 2025
 * @see Usuario
 */

public class Cliente extends Usuario {
	    //private GestionListas gestionListas;
	    private boolean activo;
	    private String ubicacion;
	    private int tiempo;
	    private String plan;

	    /**
	     * Constructor donde inicializo toda la informacion del cliente.
	     * 
	     * @param activo
	     * @param ubicacion
	     * @param tiempo
	     * @param plan
	     * @param gestionListas donde voy a tener todas las listas para recorrer o que
	     *                      guarden informacion de titulos ya vistos
	     */
	    public Cliente(boolean activo, String ubicacion, int tiempo, String plan) {
	        this.activo = activo;
	        this.ubicacion = ubicacion;
	        this.tiempo = tiempo;
	        this.plan = plan;
	     //   this.gestionListas = new GestionListas();
	    }

	//    public Cliente() {
	//        this.gestionListas = new GestionListas();
	//    }

	    /**
	     * Asigna el estado del cliente (en linea o descontectado).
	     * 
	     * @param activo
	     */

	    public void setActivo(boolean activo) {
	        this.activo = activo;
	    }

	    /**
	     * Obtiene el estado del cliente.
	     * 
	     * @return true si el cliente esta activo, false si no.
	     */

	    public boolean getActivo() {
	        return this.activo;
	    }

	    /**
	     * Asigna la ubicacion de donde vive el cliente.
	     * 
	     * @param ubicacion
	     */
	    public void setUbicacion(String ubicacion) {
	        this.ubicacion = ubicacion;
	    }

	    /**
	     * Obtiene la ubicacion de donde vive el cliente.
	     * 
	     * @return ubicacion
	     */
	    public String getUbicacion() {
	        return this.ubicacion;
	    }

	    /**
	     * Asigna el tiempo total visto del cliente.
	     * 
	     * @param tiempo
	     */
	    public void setTiempo(int tiempo) {
	        this.tiempo = tiempo;
	    }

	    /**
	     * Obtiene el tiempo total visto por el cliente.
	     * 
	     * @return tiempo
	     */
	    public int getTiempo() {
	        return this.tiempo;
	    }

	    /**
	     * Asigna el plan.
	     * 
	     * @param plan
	     */
	    public void setPlan(String plan) {
	        this.plan = plan;
	    }

	    /**
	     * Obtiene el plan.
	     * 
	     * @return plan
	     */
	    public String getPlan() {
	        return this.plan;
	    }

	    /**
	     * Aniade un titulo a la lista personal del cliente.
	     * 
	     * @param titulo titulo a aniadir
	     */
	    // public void aniadirATuLista(Titulo titulo) {
	    // this.gestionListas.aniadirAMiLista(titulo);
	    // }

	    /**
	     * Los siguientes tres metodos son para recorrer parte del catalogo con
	     * diferentes condiciones: de peliculas que selecciono anteriormente y las metio
	     * en su lista, de titulos ya vistos o de recomendaciones basadas en el
	     * historial y puntuaciones previas.
	     */
	    public void recorrerMiLista() {
	    }

	    public void recorrerRecomendaciones() {
	    }

	    public void recorrerHistorial() {
	    }
 
}
