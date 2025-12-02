package model;

/**
 * Listas las cuales puede recorrer {@link Cliente} 
 * como listas generadas por el cliente, de recomendaciones hechas por la plataforma y mi historial de visualizacion
 * tambien estan las listas donde se toma la informacion de sus puntuacion y generos mas vistos para actualizar la lista de recomendaciones
 * @author Juana Sabbione
 * @author Priscila Esposito
 * @version 1.0
 * @since 2025
 * @see Cliente
 */

import java.util.LinkedList;

public class GestionListas {
     private LinkedList<Titulo> miLista;
     private LinkedList<Titulo> recomendacion;
     private LinkedList<Titulo> historialVisualizacion;
     private LinkedList<Generos> misGeneros;
     private LinkedList<Puntuaciones> misPuntuaciones;
     public GestionListas() {
     this.miLista = new LinkedList<Titulo>();
     this.recomendacion = new LinkedList<Titulo>();
     this.historialVisualizacion = new LinkedList<Titulo>();
     this.misGeneros = new LinkedList<Generos>();
     this.misPuntuaciones = new LinkedList<Puntuaciones>();
     }

     // public GestionListas(LinkedList<Titulo> miLista, LinkedList<Titulo>
     // recomendacion,
     // LinkedList<Titulo> historialVisualizacion, LinkedList<Generos> misGeneros,
     // LinkedList<Puntuaciones> misPuntuaciones) {
     // this.miLista = miLista;
     // this.recomendacion = recomendacion;
     // this.historialVisualizacion = historialVisualizacion;
     // this.misGeneros = misGeneros;
     // this.misPuntuaciones = misPuntuaciones;
     // }

     /**
      * Obtiene la lista personal de titulos del usuario.
      * 
      * @return lista de titulos guardados
      */
     // public LinkedList<Titulo> getMiLista() {
     // return miLista;
     // }

     /**
      * Obtiene la lista de recomendaciones.
      * 
      * @return lista de recomendaciones
      */
     // public LinkedList<Titulo> getRecomendacion() {
     // return recomendacion;
     // }

     /**
      * Obtiene el historial de visualizacion.
      * 
      * @return lista de titulos vistos
      */
     // public LinkedList<Titulo> getHistorialVisualizacion() {
     // return historialVisualizacion;
     // }

     /**
      * Actualiza la lista de recomendaciones en base a preferencias
      * del usuario y su historial de visualizacion.
      */
  //   private void actualizarRecomendaciones() {
  //   }

     /**
      * Quita un titulo de la lista personal del usuario.
      * 
      * @param titulo titulo a eliminar
      */
     // public void quitarDeMiLista(Titulo titulo) {
     // miLista.remove(titulo);
     // }

     /**
      * Aniaade un titulo a la lista personal del usuario.
      * 
      * @param titulo titulo a aniadir
      */
     // public void aniadirAMiLista(Titulo titulo) {
     // miLista.add(titulo);
     // }

     public void recorrerMiLista() {
     }

     public void recorrerRecomendaciones() {
     }

     public void recorrerHistorial() {
     }

}

