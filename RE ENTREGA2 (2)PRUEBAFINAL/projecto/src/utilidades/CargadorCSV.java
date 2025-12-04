package utilidades;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import dao.Conexion;
import db.BaseDeDatos;
import model.Pelicula;
import daoJDBC.PeliculaDAOjdbc;

public class CargadorCSV {
    
    private static final String CSV_PATH = System.getProperty("user.home") + 
        "/PriEsposito/proyectoFinal/RE ENTREGA2 (2)PRUEBAFINAL/projecto/movies_database.csv";
    
    /**
     * Carga peliculas desde movies_database.csv.
     * - Importa una por una guardando en BD
     * - Mantiene objetos en memoria
     * - Retorna lista ordenada por rating_promedio descendente
     */
    public static List<Pelicula> cargarPeliculasDesdeCSV() {
        System.out.println("Cargando peliculas desde movies_database.csv...");
        
        List<Pelicula> peliculasEnMemoria = new ArrayList<>();
        PeliculaDAOjdbc peliculaDAO = new PeliculaDAOjdbc();
        
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH));
             Connection conn = Conexion.conectar()) {
            
            String line;
            boolean primeraLinea = true;
            int contador = 0;
            
            // SQL para insertar pelicula
            String sql = "INSERT INTO PELICULA (GENERO, TITULO, RESUMEN, DIRECTOR, DURACION, RATING_PROMEDIO, ANIO, POSTER) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            while ((line = br.readLine()) != null) {
                // Saltar la primera linea si es cabecera
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                
                // Parsear linea CSV
                String[] datos = parsearLineaCSV(line);
                if (datos == null || datos.length < 8) {
                    continue;
                }
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, datos[0]); // GENERO (primer genero)
                    pstmt.setString(2, datos[1]); // TITULO
                    pstmt.setString(3, datos[2]); // RESUMEN
                    pstmt.setString(4, datos[3]); // DIRECTOR
                    pstmt.setDouble(5, Double.parseDouble(datos[4])); // DURACION
                    pstmt.setFloat(6, Float.parseFloat(datos[5])); // RATING_PROMEDIO
                    pstmt.setInt(7, Integer.parseInt(datos[6])); // ANIO
                    pstmt.setString(8, datos[7]); // POSTER
                    
                    pstmt.executeUpdate();
                    contador++;
                    
                    System.out.println("  [" + contador + "] Cargada: " + datos[1] + " (Rating: " + datos[5] + ")");
                    
                } catch (Exception e) {
                    System.err.println("Error al insertar pelicula: " + datos[1]);
                }
            }
            
            
            // Cargar todas las peliculas en memoria desde la BD
            System.out.println("📥 Cargando peliculas en memoria...");
            peliculasEnMemoria = peliculaDAO.listarTodos();
            
            // Ordenar por rating_promedio descendente usando mecanismo de Java
            peliculasEnMemoria.sort((p1, p2) -> {
                float rating1 = p1.getRatingPromedio();
                float rating2 = p2.getRatingPromedio();
                return Float.compare(rating2, rating1); // Descendente
            });
            
            
        } catch (Exception e) {
            System.err.println("Error al cargar CSV: " + e.getMessage());
            e.printStackTrace();
        }
        
        return peliculasEnMemoria;
    }
    
    private static String[] parsearLineaCSV(String linea) {
        // Parseo simple para CSV - puede necesitar ajustes segun el formato
        String[] partes = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (int i = 0; i < partes.length; i++) {
            partes[i] = partes[i].trim().replaceAll("^\"|\"$", "");
        }
        return partes;
    }
    
    public static boolean existenPeliculasEnBD() {
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM PELICULA")) {
            
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            // Si hay error, asumir que no hay peliculas
        }
        return false;
    }
}
