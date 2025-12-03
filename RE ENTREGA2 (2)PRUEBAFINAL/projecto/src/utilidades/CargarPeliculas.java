package utilidades;

import java.util.Scanner;
import org.json.JSONObject;
import db.ConsultaPeliculasOMDb;
import dao.PeliculaDAO;
import daoJDBC.PeliculaDAOjdbc;
import model.Pelicula;
import model.Metadatos;

public class CargarPeliculas {
    
    private static PeliculaDAO peliculaDAO = new PeliculaDAOjdbc();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=".repeat(60));
        System.out.println("CARGADOR DE PELÍCULAS DESDE OMDb");
        System.out.println("=".repeat(60));
        System.out.println();
        
        while (true) {
            System.out.print("Ingrese el título de la película (o 'salir' para terminar): ");
            String titulo = scanner.nextLine().trim();
            
            if (titulo.equalsIgnoreCase("salir")) {
                System.out.println("\n¡Proceso finalizado!");
                break;
            }
            
            if (titulo.isEmpty()) {
                System.out.println("⚠️  El título no puede estar vacío.\n");
                continue;
            }
            
            System.out.println("\n🔍 Buscando en OMDb: " + titulo + "...");
            
            try {
                JSONObject resultado = ConsultaPeliculasOMDb.buscarPelicula(titulo);
                
                if (resultado == null || resultado.optString("Response").equals("False")) {
                    System.out.println("❌ No se encontró la película \"" + titulo + "\"");
                    System.out.println("   Intente con otro título o sea más específico.\n");
                    continue;
                }
                
                // Mostrar información encontrada
                System.out.println("\n✅ Película encontrada:");
                System.out.println("   Título: " + resultado.getString("Title"));
                System.out.println("   Año: " + resultado.optString("Year", "N/A"));
                System.out.println("   Director: " + resultado.getString("Director"));
                System.out.println("   Género: " + resultado.getString("Genre"));
                System.out.println("   Rating IMDb: " + resultado.optString("imdbRating", "N/A"));
                
                System.out.print("\n¿Desea guardar esta película? (s/n): ");
                String respuesta = scanner.nextLine().trim().toLowerCase();
                
                if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("yes")) {
                    guardarPelicula(resultado);
                } else {
                    System.out.println("⏭️  Película no guardada.\n");
                }
                
            } catch (Exception e) {
                System.out.println("❌ Error al buscar/guardar la película: " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println();
        }
        
        scanner.close();
    }
    
    private static void guardarPelicula(JSONObject datos) {
        try {
            // Crear objeto Pelicula
            Pelicula pelicula = new Pelicula();
            
            // Configurar metadatos
            Metadatos metadatos = pelicula.getMetadatos();
            metadatos.setTitulo(datos.getString("Title"));
            metadatos.setDirector(datos.getString("Director"));
            
            // Sinopsis
            String sinopsis = datos.optString("Plot", "");
            if (!sinopsis.equals("N/A") && !sinopsis.isEmpty()) {
                metadatos.setSipnosis(sinopsis);
            }
            
            // Obtener y procesar géneros
            String generosStr = datos.getString("Genre");
            String[] generosArray = generosStr.split(",");
            for (String genero : generosArray) {
                pelicula.anadirGeneros(genero.trim());
            }
            
            // Año
            String yearStr = datos.optString("Year", "2000");
            // Limpiar el año (puede venir como "2020" o "2019-2021")
            yearStr = yearStr.split("-")[0].replaceAll("[^0-9]", "");
            int anio = yearStr.isEmpty() ? 2000 : Integer.parseInt(yearStr);
            pelicula.setAnio(anio);
            
            // Rating promedio (convertir de IMDb rating /10)
            String ratingStr = datos.optString("imdbRating", "7.0");
            float rating = 7.0f;
            try {
                if (!ratingStr.equals("N/A")) {
                    rating = Float.parseFloat(ratingStr);
                }
            } catch (NumberFormatException e) {
                rating = 7.0f;
            }
            pelicula.setRatingPromedio(rating);
            
            // Duración (convertir minutos a formato decimal)
            String runtimeStr = datos.optString("Runtime", "120 min");
            double duracion = 120.0;
            try {
                duracion = Double.parseDouble(runtimeStr.replaceAll("[^0-9]", ""));
            } catch (NumberFormatException e) {
                duracion = 120.0;
            }
            pelicula.getVideo().setDuracion(duracion);
            
            // Poster URL
            String poster = datos.optString("Poster", "");
            if (!poster.equals("N/A")) {
                pelicula.setPoster(poster);
            }
            
            // Guardar en la base de datos
            peliculaDAO.registrar(pelicula);
            
            System.out.println("✅ Película guardada exitosamente en la base de datos!");
            System.out.println("   Título: " + pelicula.getMetadatos().getTitulo());
            
        } catch (Exception e) {
            System.out.println("❌ Error al procesar los datos de la película: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
