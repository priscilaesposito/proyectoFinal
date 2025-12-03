# Búsqueda de Contenido con OMDb API

## 1. Obtener tu API Key

1. Visita: https://www.omdbapi.com/apikey.aspx
2. Completa el formulario con tu email
3. Revisa tu email y activa tu API Key
4. Copia tu API Key

## 2. Configurar la API Key en el proyecto

Abre el archivo `src/app/ConsultaPeliculasOMDb.java` y reemplaza:

```java
private static final String API_KEY = "TU_API_KEY";
```

Por tu API Key real:

```java
private static final String API_KEY = "abc12345";  // Tu API Key aquí
```

## 3. Compilar el proyecto

Asegúrate de incluir la librería JSON en el classpath. La librería ya está descargada en `lib/json-20240303.jar`.

## 4. Uso de la clase ConsultaPeliculasOMDb

### Métodos disponibles:

#### a) Buscar película por título exacto
```java
JSONObject pelicula = ConsultaPeliculasOMDb.buscarPelicula("Titanic");

if (pelicula != null) {
    String titulo = pelicula.getString("Title");
    String año = pelicula.getString("Year");
    String sinopsis = pelicula.getString("Plot");
    String rating = pelicula.optString("imdbRating", "N/A");
    String poster = pelicula.optString("Poster", "N/A");
}
```

#### b) Buscar película por IMDb ID
```java
JSONObject pelicula = ConsultaPeliculasOMDb.buscarPorIMDbID("tt0120338");
```

#### c) Buscar múltiples películas
```java
JSONObject resultados = ConsultaPeliculasOMDb.buscarVariasPeliculas("matrix");

if (resultados != null) {
    JSONArray peliculas = resultados.getJSONArray("Search");
    for (int i = 0; i < peliculas.length(); i++) {
        JSONObject p = peliculas.getJSONObject(i);
        System.out.println(p.getString("Title") + " (" + p.getString("Year") + ")");
    }
}
```

## 5. Campos disponibles en la respuesta JSON

- **Title**: Título de la película
- **Year**: Año de lanzamiento
- **Released**: Fecha de estreno completa
- **Runtime**: Duración
- **Genre**: Géneros (separados por comas)
- **Director**: Director
- **Writer**: Guionistas
- **Actors**: Actores principales
- **Plot**: Sinopsis
- **Language**: Idiomas
- **Country**: Países
- **Awards**: Premios
- **Poster**: URL del poster
- **Ratings**: Array con ratings de diferentes fuentes
- **Metascore**: Puntuación de Metacritic
- **imdbRating**: Rating de IMDb
- **imdbVotes**: Votos en IMDb
- **imdbID**: ID único de IMDb
- **Type**: Tipo (movie, series, episode)
- **BoxOffice**: Recaudación en taquilla

## 6. Ejemplo completo de integración

```java
// En tu clase de lógica o servicio
public void buscarYGuardarPelicula(String titulo) {
    try {
        JSONObject datosOMDb = ConsultaPeliculasOMDb.buscarPelicula(titulo);
        
        if (datosOMDb != null) {
            // Crear objeto Pelicula
            Pelicula pelicula = new Pelicula();
            
            // Mapear datos de OMDb a tu modelo
            pelicula.getMetadatos().setTitulo(datosOMDb.getString("Title"));
            pelicula.getMetadatos().setSipnosis(datosOMDb.getString("Plot"));
            pelicula.getMetadatos().setDirector(datosOMDb.getString("Director"));
            
            // Extraer año de "Released" o "Year"
            String year = datosOMDb.getString("Year");
            pelicula.setAnio(Integer.parseInt(year.split("–")[0])); // Por si es serie
            
            // Rating promedio (convertir de string a float)
            String imdbRating = datosOMDb.optString("imdbRating", "0");
            if (!imdbRating.equals("N/A")) {
                pelicula.setRatingPromedio(Float.parseFloat(imdbRating));
            }
            
            // Poster URL
            pelicula.setPoster(datosOMDb.getString("Poster"));
            
            // Géneros (viene como "Action, Adventure, Drama")
            String generos = datosOMDb.getString("Genre");
            for (String genero : generos.split(", ")) {
                pelicula.anadirGeneros(genero);
            }
            
            // Duración (viene como "142 min")
            String runtime = datosOMDb.getString("Runtime");
            if (!runtime.equals("N/A")) {
                int duracion = Integer.parseInt(runtime.split(" ")[0]);
                pelicula.getVideo().setDuracion(duracion);
            }
            
            // Guardar en base de datos
            peliculaDAO.registrar(pelicula);
            
            System.out.println("Película guardada exitosamente!");
        } else {
            System.out.println("No se encontró la película");
        }
        
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
    }
}
```

## 7. Límites de la API

- **Free tier**: 1,000 requests por día
- **Patreon tier**: 100,000 requests por día

## 8. Tips

- Usa `optString()` en lugar de `getString()` para campos opcionales
- Siempre verifica que `Response` sea `"True"` antes de procesar
- Para búsquedas con espacios, se reemplazan automáticamente por `+`
- Guarda el `imdbID` para futuras consultas más rápidas
