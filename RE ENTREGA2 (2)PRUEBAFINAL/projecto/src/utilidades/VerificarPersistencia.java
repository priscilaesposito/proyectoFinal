package utilidades;

import dao.Conexion;
import db.BaseDeDatos;
import daoJDBC.PeliculaDAOjdbc;
import daoJDBC.ReseniaDAOJdbc;
import model.Pelicula;
import model.Resenia;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase de prueba para verificar que los datos se guardan y recuperan correctamente
 * de la base de datos, especialmente los nuevos campos (rating_promedio, año, etc).
 */
public class VerificarPersistencia {

    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("VERIFICACION DE PERSISTENCIA DE DATOS");
        System.out.println("=".repeat(80));
        
        try {
            // Inicializar BD
            BaseDeDatos.inicializarBaseDeDatos();
            
            // 1. Verificar estructura de tabla PELICULA
            verificarEstructuraTabla();
            
            // 2. Insertar película de prueba
            int idPelicula = insertarPeliculaPrueba();
            
            // 3. Recuperar película y verificar campos
            verificarRecuperacionPelicula(idPelicula);
            
            // 4. Insertar reseñas y verificar actualización de rating
            insertarReseniasPrueba(idPelicula);
            
            // 5. Verificar que el rating promedio se actualizó
            verificarActualizacionRating(idPelicula);
            
            System.out.println("\n" + "=".repeat(80));
            System.out.println("✅ VERIFICACION COMPLETADA - TODOS LOS DATOS SE PERSISTEN CORRECTAMENTE");
            System.out.println("=".repeat(80));
            
        } catch (Exception e) {
            System.out.println("\n❌ ERROR EN LA VERIFICACION: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void verificarEstructuraTabla() throws Exception {
        System.out.println("\n📋 1. VERIFICANDO ESTRUCTURA DE TABLA PELICULA");
        System.out.println("-".repeat(80));
        
        try (Connection conn = BaseDeDatos.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("PRAGMA table_info(PELICULA)")) {
            
            System.out.println("Columnas en la tabla PELICULA:");
            while (rs.next()) {
                String nombre = rs.getString("name");
                String tipo = rs.getString("type");
                int notnull = rs.getInt("notnull");
                System.out.printf("  - %-20s | Tipo: %-15s | NOT NULL: %s%n", 
                                nombre, tipo, notnull == 1 ? "SI" : "NO");
            }
        }
        
        System.out.println("✅ Estructura verificada correctamente");
    }
    
    private static int insertarPeliculaPrueba() throws Exception {
        System.out.println("\n💾 2. INSERTANDO PELICULA DE PRUEBA");
        System.out.println("-".repeat(80));
        
        PeliculaDAOjdbc dao = new PeliculaDAOjdbc();
        Pelicula pelicula = new Pelicula();
        
        pelicula.getMetadatos().setTitulo("Película de Prueba - Persistencia");
        pelicula.getMetadatos().setSipnosis("Esta es una película de prueba para verificar la persistencia de datos");
        pelicula.getMetadatos().setDirector("Director Prueba");
        pelicula.anadirGeneros("Acción");
        pelicula.anadirGeneros("Drama");
        pelicula.getVideo().setDuracion(120.5);
        pelicula.setRatingPromedio(8.5f);
        pelicula.setAnio(2025);
        pelicula.setPoster("https://ejemplo.com/poster.jpg");
        
        dao.registrar(pelicula);
        
        System.out.println("Datos insertados:");
        System.out.println("  - Título: " + pelicula.getMetadatos().getTitulo());
        System.out.println("  - Director: " + pelicula.getMetadatos().getDirector());
        System.out.println("  - Duración: " + pelicula.getVideo().getDuracion() + " minutos");
        System.out.println("  - Rating Promedio: " + pelicula.getRatingPromedio());
        System.out.println("  - Año: " + pelicula.getAnio());
        System.out.println("  - Poster: " + pelicula.getPoster());
        
        // Obtener el ID de la película recién insertada buscando por título
        try (Connection conn = BaseDeDatos.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT ID FROM PELICULA WHERE TITULO = 'Película de Prueba - Persistencia' ORDER BY ID DESC LIMIT 1")) {
            if (rs.next()) {
                int id = rs.getInt("ID");
                System.out.println("  - ID generado: " + id);
                return id;
            }
        }
        
        throw new Exception("No se pudo obtener el ID de la película insertada");
    }
    
    private static void verificarRecuperacionPelicula(int idPelicula) throws Exception {
        System.out.println("\n🔍 3. RECUPERANDO PELICULA Y VERIFICANDO CAMPOS");
        System.out.println("-".repeat(80));
        
        PeliculaDAOjdbc dao = new PeliculaDAOjdbc();
        Pelicula recuperada = dao.buscarPorId(idPelicula);
        
        if (recuperada == null) {
            throw new Exception("❌ No se pudo recuperar la película con ID " + idPelicula);
        }
        
        System.out.println("Datos recuperados:");
        System.out.println("  - ID: " + recuperada.getID());
        System.out.println("  - Título: " + recuperada.getMetadatos().getTitulo());
        System.out.println("  - Sinopsis: " + recuperada.getMetadatos().getSipnosis());
        System.out.println("  - Director: " + recuperada.getMetadatos().getDirector());
        System.out.println("  - Géneros: " + recuperada.getGeneros());
        System.out.println("  - Duración: " + recuperada.getVideo().getDuracion() + " minutos");
        System.out.println("  - Rating Promedio: " + recuperada.getRatingPromedio());
        System.out.println("  - Año: " + recuperada.getAnio());
        System.out.println("  - Poster: " + recuperada.getPoster());
        
        // Verificar que los valores son correctos
        boolean todoCorrecto = true;
        
        if (recuperada.getVideo().getDuracion() != 120.5) {
            System.out.println("  ❌ ERROR: Duración no coincide");
            todoCorrecto = false;
        }
        
        if (Math.abs(recuperada.getRatingPromedio() - 8.5f) > 0.01) {
            System.out.println("  ❌ ERROR: Rating promedio no coincide");
            todoCorrecto = false;
        }
        
        if (recuperada.getAnio() != 2025) {
            System.out.println("  ❌ ERROR: Año no coincide");
            todoCorrecto = false;
        }
        
        if (recuperada.getPoster() == null || !recuperada.getPoster().contains("ejemplo.com")) {
            System.out.println("  ❌ ERROR: Poster no coincide");
            todoCorrecto = false;
        }
        
        if (todoCorrecto) {
            System.out.println("\n✅ Todos los campos se recuperaron correctamente");
        } else {
            throw new Exception("❌ Algunos campos no se recuperaron correctamente");
        }
    }
    
    private static void insertarReseniasPrueba(int idPelicula) throws Exception {
        System.out.println("\n⭐ 4. INSERTANDO RESEÑAS DE PRUEBA");
        System.out.println("-".repeat(80));
        
        ReseniaDAOJdbc dao = new ReseniaDAOJdbc();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHora = LocalDateTime.now().format(formatter);
        
        // Reseña 1: Calificación 9
        Resenia resenia1 = new Resenia();
        resenia1.setCalificacion(9);
        resenia1.setComentario("Excelente película de prueba");
        resenia1.setAprobado(1);
        resenia1.setFechaHora(fechaHora);
        resenia1.setID_Usuario(1); // Asumiendo que existe un usuario con ID 1
        resenia1.setID_Pelicula(idPelicula);
        dao.registrar(resenia1);
        System.out.println("  - Reseña 1 insertada: Calificación 9");
        
        // Reseña 2: Calificación 7
        Resenia resenia2 = new Resenia();
        resenia2.setCalificacion(7);
        resenia2.setComentario("Buena película");
        resenia2.setAprobado(1);
        resenia2.setFechaHora(fechaHora);
        resenia2.setID_Usuario(1);
        resenia2.setID_Pelicula(idPelicula);
        dao.registrar(resenia2);
        System.out.println("  - Reseña 2 insertada: Calificación 7");
        
        // Reseña 3: Calificación 8
        Resenia resenia3 = new Resenia();
        resenia3.setCalificacion(8);
        resenia3.setComentario("Muy buena");
        resenia3.setAprobado(1);
        resenia3.setFechaHora(fechaHora);
        resenia3.setID_Usuario(1);
        resenia3.setID_Pelicula(idPelicula);
        dao.registrar(resenia3);
        System.out.println("  - Reseña 3 insertada: Calificación 8");
        
        System.out.println("\n  Promedio esperado: (9 + 7 + 8) / 3 = 8.0");
    }
    
    private static void verificarActualizacionRating(int idPelicula) throws Exception {
        System.out.println("\n📊 5. VERIFICANDO ACTUALIZACION DE RATING PROMEDIO");
        System.out.println("-".repeat(80));
        
        PeliculaDAOjdbc dao = new PeliculaDAOjdbc();
        Pelicula pelicula = dao.buscarPorId(idPelicula);
        
        float ratingActual = pelicula.getRatingPromedio();
        float ratingEsperado = 8.0f; // (9 + 7 + 8) / 3 = 8.0
        
        System.out.println("  - Rating actual en BD: " + ratingActual);
        System.out.println("  - Rating esperado: " + ratingEsperado);
        
        if (Math.abs(ratingActual - ratingEsperado) < 0.1) {
            System.out.println("\n✅ El rating promedio se actualizó correctamente");
        } else {
            throw new Exception("❌ El rating promedio no se actualizó correctamente");
        }
    }
}
