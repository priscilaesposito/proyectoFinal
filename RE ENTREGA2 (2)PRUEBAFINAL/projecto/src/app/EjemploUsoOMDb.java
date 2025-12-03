package app;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Clase de ejemplo para demostrar el uso de la API de OMDb
 * 
 * IMPORTANTE: Antes de ejecutar, configura tu API_KEY en ConsultaPeliculasOMDb.java
 * Obtén tu API Key en: https://www.omdbapi.com/apikey.aspx
 */
public class EjemploUsoOMDb {
    
    public static void main(String[] args) {
        System.out.println("=== EJEMPLOS DE USO DE LA API OMDb ===\n");
        
        // Ejemplo 1: Buscar una película específica
        ejemplo1BuscarPeliculaEspecifica();
        
        // Ejemplo 2: Buscar y guardar película en la base de datos
        // ejemplo2BuscarYGuardar();
        
        // Ejemplo 3: Buscar múltiples películas
        // ejemplo3BuscarVariasPeliculas();
        
        // Ejemplo 4: Listar películas por término
        // ejemplo4ListarPeliculas();
    }
    
    /**
     * Ejemplo 1: Buscar información detallada de una película
     */
    private static void ejemplo1BuscarPeliculaEspecifica() {
        System.out.println("--- Ejemplo 1: Buscar película por título ---");
        
        try {
            JSONObject pelicula = Logica.buscarPeliculaOMDb("The Matrix");
            
            if (pelicula != null) {
                System.out.println("✅ Película encontrada:");
                System.out.println("   Título: " + pelicula.getString("Title"));
                System.out.println("   Año: " + pelicula.getString("Year"));
                System.out.println("   Director: " + pelicula.getString("Director"));
                System.out.println("   Género: " + pelicula.getString("Genre"));
                System.out.println("   Rating IMDb: " + pelicula.getString("imdbRating"));
                System.out.println("   Sinopsis: " + pelicula.getString("Plot"));
                System.out.println("   Poster: " + pelicula.getString("Poster"));
                System.out.println("   Duración: " + pelicula.getString("Runtime"));
            } else {
                System.out.println("❌ No se encontró la película");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Ejemplo 2: Buscar película en OMDb y guardarla en la base de datos
     */
    private static void ejemplo2BuscarYGuardar() {
        System.out.println("--- Ejemplo 2: Buscar y guardar en BD ---");
        
        try {
            String titulo = "Inception";
            boolean guardado = Logica.buscarYGuardarPeliculaOMDb(titulo);
            
            if (guardado) {
                System.out.println("✅ Película '" + titulo + "' guardada exitosamente en la BD");
            } else {
                System.out.println("❌ No se pudo guardar la película");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Ejemplo 3: Buscar múltiples películas por término
     */
    private static void ejemplo3BuscarVariasPeliculas() {
        System.out.println("--- Ejemplo 3: Buscar varias películas ---");
        
        try {
            JSONObject resultado = Logica.buscarVariasPeliculasOMDb("batman");
            
            if (resultado != null && resultado.has("Search")) {
                JSONArray peliculas = resultado.getJSONArray("Search");
                System.out.println("✅ Se encontraron " + peliculas.length() + " películas:");
                
                for (int i = 0; i < peliculas.length(); i++) {
                    JSONObject p = peliculas.getJSONObject(i);
                    System.out.println("   " + (i+1) + ". " + p.getString("Title") + 
                                     " (" + p.getString("Year") + ") - " + 
                                     p.getString("Type"));
                }
            } else {
                System.out.println("❌ No se encontraron películas");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Ejemplo 4: Listar películas usando el método helper
     */
    private static void ejemplo4ListarPeliculas() {
        System.out.println("--- Ejemplo 4: Listar con método helper ---");
        
        try {
            JSONArray peliculas = Logica.listarPeliculasOMDb("star wars");
            
            System.out.println("✅ Se encontraron " + peliculas.length() + " películas:");
            
            for (int i = 0; i < peliculas.length(); i++) {
                JSONObject p = peliculas.getJSONObject(i);
                System.out.println("   • " + p.getString("Title") + " (" + p.getString("Year") + ")");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Ejemplo completo: Buscar, mostrar detalles y opción de guardar
     */
    public static void buscarYMostrarConOpcionGuardar(String titulo) {
        try {
            System.out.println("Buscando: " + titulo + "...\n");
            
            // Buscar en OMDb
            JSONObject pelicula = Logica.buscarPeliculaOMDb(titulo);
            
            if (pelicula == null) {
                System.out.println("❌ No se encontró la película");
                return;
            }
            
            // Mostrar información
            System.out.println("═══════════════════════════════════════");
            System.out.println("🎬 " + pelicula.getString("Title"));
            System.out.println("═══════════════════════════════════════");
            System.out.println("📅 Año: " + pelicula.getString("Year"));
            System.out.println("🎭 Género: " + pelicula.getString("Genre"));
            System.out.println("🎬 Director: " + pelicula.getString("Director"));
            System.out.println("⭐ Rating: " + pelicula.getString("imdbRating") + "/10");
            System.out.println("⏱️  Duración: " + pelicula.getString("Runtime"));
            System.out.println("\n📖 Sinopsis:");
            System.out.println(pelicula.getString("Plot"));
            System.out.println("\n🖼️  Poster: " + pelicula.getString("Poster"));
            System.out.println("═══════════════════════════════════════\n");
            
            // Guardar en BD
            System.out.println("¿Desea guardar esta película en la base de datos? (S/N)");
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            String respuesta = scanner.nextLine();
            
            if (respuesta.equalsIgnoreCase("S")) {
                boolean guardado = Logica.buscarYGuardarPeliculaOMDb(titulo);
                if (guardado) {
                    System.out.println("✅ Película guardada exitosamente!");
                } else {
                    System.out.println("❌ Error al guardar la película");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
