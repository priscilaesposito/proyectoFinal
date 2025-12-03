package app;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class ConsultaPeliculasOMDb {
    
    // Reemplazá con tu API Key obtenida en https://www.omdbapi.com/apikey.aspx
    private static final String API_KEY = "1dfc3229";
    
    public static void main(String[] args) {
        String titulo = "titanic";  // Reemplazar por el título a buscar
        consultarPelicula(titulo);
    }
    
    public static void consultarPelicula(String titulo) {
        try {
            // Armar la URL de consulta (encodear espacios con '+')
            String url = "https://www.omdbapi.com/?t=" + titulo.replace(" ", "+") + "&apikey=" + API_KEY;
            
            // Crear cliente y solicitud
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            
            // Enviar solicitud
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Procesar respuesta JSON
            JSONObject json = new JSONObject(response.body());
            
            if (json.has("Response") && json.getString("Response").equals("True")) {
                System.out.println("🎬 Título: " + json.getString("Title"));
                System.out.println("📅 Año: " + json.getString("Year"));
                System.out.println("📖 Sinopsis: " + json.getString("Plot"));
                System.out.println("⭐ Rating: " + json.optString("imdbRating", "N/A"));
                System.out.println("🖼️ Poster: " + json.optString("Poster", "N/A"));
            } else {
                System.out.println("❌ Película no encontrada o error en la consulta.");
            }
            
        } catch (Exception e) {
            System.out.println("Error al consultar la API: " + e.getMessage());
        }
    }
    
    /**
     * Buscar película y retornar objeto JSON con los datos
     */
    public static JSONObject buscarPelicula(String titulo) throws Exception {
        // Armar la URL de consulta (encodear espacios con '+')
        String url = "https://www.omdbapi.com/?t=" + titulo.replace(" ", "+") + "&apikey=" + API_KEY;
        
        // Crear cliente y solicitud
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        
        // Enviar solicitud
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Procesar respuesta JSON
        JSONObject json = new JSONObject(response.body());
        
        if (json.has("Response") && json.getString("Response").equals("True")) {
            return json;
        } else {
            return null;
        }
    }
    
    /**
     * Buscar película por ID de IMDb
     */
    public static JSONObject buscarPorIMDbID(String imdbID) throws Exception {
        String url = "https://www.omdbapi.com/?i=" + imdbID + "&apikey=" + API_KEY;
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());
        
        if (json.has("Response") && json.getString("Response").equals("True")) {
            return json;
        } else {
            return null;
        }
    }
    
    /**
     * Buscar múltiples películas por término de búsqueda
     */
    public static JSONObject buscarVariasPeliculas(String searchTerm) throws Exception {
        String url = "https://www.omdbapi.com/?s=" + searchTerm.replace(" ", "+") + "&apikey=" + API_KEY;
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());
        
        if (json.has("Response") && json.getString("Response").equals("True")) {
            return json;
        } else {
            return null;
        }
    }
}
