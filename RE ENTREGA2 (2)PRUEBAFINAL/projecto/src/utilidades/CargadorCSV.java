package utilidades;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import dao.Conexion;
import db.BaseDeDatos;

public class CargadorCSV {
    
    private static final String CSV_PATH = System.getProperty("user.home") + 
        "/PriEsposito/proyectoFinal/RE ENTREGA2 (2)PRUEBAFINAL/projecto/peliculas.csv";
    
    public static void cargarPeliculasDesdeCSV() {
        System.out.println("Cargando películas desde CSV...");
        
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH));
             Connection conn = Conexion.conectar()) {
            
            String line;
            boolean primeraLinea = true;
            int contador = 0;
            
            // SQL para insertar película
            String sql = "INSERT INTO PELICULA (GENERO, TITULO, RESUMEN, DIRECTOR, DURACION, RATING_PROMEDIO, ANIO, POSTER) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            while ((line = br.readLine()) != null) {
                // Saltar la primera línea si es cabecera
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                
                // Parsear línea CSV
                String[] datos = parsearLineaCSV(line);
                if (datos == null || datos.length < 8) {
                    continue;
                }
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, datos[0]); // GENERO
                    pstmt.setString(2, datos[1]); // TITULO
                    pstmt.setString(3, datos[2]); // RESUMEN
                    pstmt.setString(4, datos[3]); // DIRECTOR
                    pstmt.setDouble(5, Double.parseDouble(datos[4])); // DURACION
                    pstmt.setFloat(6, Float.parseFloat(datos[5])); // RATING_PROMEDIO
                    pstmt.setInt(7, Integer.parseInt(datos[6])); // ANIO
                    pstmt.setString(8, datos[7]); // POSTER
                    
                    pstmt.executeUpdate();
                    contador++;
                } catch (Exception e) {
                    System.err.println("Error al insertar película: " + datos[1]);
                }
            }
            
            System.out.println("✅ " + contador + " películas cargadas desde CSV");
            
        } catch (Exception e) {
            System.err.println("Error al cargar CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String[] parsearLineaCSV(String linea) {
        // Parseo simple para CSV - puede necesitar ajustes según el formato
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
            // Si hay error, asumir que no hay películas
        }
        return false;
    }
}
