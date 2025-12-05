
package daoJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.ReseniaDAO;
import db.BaseDeDatos;
import model.Resenia;

public class ReseniaDAOJdbc implements ReseniaDAO {

    @Override
    public void registrar(Resenia resenia)throws SQLException {

        String sql = "INSERT INTO RESENIA (CALIFICACION, COMENTARIO, APROBADO, FECHA_HORA, ID_USUARIO, ID_PELICULA) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = BaseDeDatos.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, resenia.getCalificacion());
            pstmt.setString(2, resenia.getComentario());
            pstmt.setInt(3, resenia.isAprobado());
            pstmt.setString(4, resenia.getFechaHora());
            pstmt.setInt(5, resenia.getID_Usuario());
            pstmt.setInt(6, resenia.getID_Pelicula());

            pstmt.executeUpdate();
            
            // Actualizar el rating promedio de la película
            actualizarRatingPromedio(conn, resenia.getID_Pelicula());

        }
    }

    @Override
    public Resenia buscarPorId(int id) throws SQLException{
        String sql = "SELECT * FROM RESENIA WHERE ID = ?";
        try (Connection conn = BaseDeDatos.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToResenia(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Resenia> listarNoAprobadas() throws SQLException{
        List<Resenia> lista = new ArrayList<>();
        String sql = "SELECT * FROM RESENIA WHERE APROBADO = 0";

        try (Connection conn = BaseDeDatos.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToResenia(rs));
            }
        }
        return lista;
    }

    @Override
    public void aprobarResenia(int idResenia)throws SQLException {
        String sql = "UPDATE RESENIA SET APROBADO = 1 WHERE ID = ?";
        try (Connection conn = BaseDeDatos.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idResenia);
            pstmt.executeUpdate();
            
            // Obtener el ID de la película para actualizar su rating
            String sqlPelicula = "SELECT ID_PELICULA FROM RESENIA WHERE ID = ?";
            try (PreparedStatement pstmtPelicula = conn.prepareStatement(sqlPelicula)) {
                pstmtPelicula.setInt(1, idResenia);
                try (ResultSet rs = pstmtPelicula.executeQuery()) {
                    if (rs.next()) {
                        int idPelicula = rs.getInt("ID_PELICULA");
                        actualizarRatingPromedio(conn, idPelicula);
                    }
                }
            }

        }
    }
    
    /**
     * Actualiza el rating promedio de una película usando cálculo incremental.
     * Fórmula: nuevo_promedio = (promedio_actual * cantidad_votos + nueva_calificacion) / (cantidad_votos + 1)
     */
    private void actualizarRatingPromedio(Connection conn, int idPelicula) throws SQLException {
        // Obtener la nueva calificación que se acaba de agregar
        String sqlUltimaCalif = "SELECT CALIFICACION FROM RESENIA WHERE ID_PELICULA = ? AND APROBADO = 1 ORDER BY ID DESC LIMIT 1";
        
        // Obtener valores actuales de la película
        String sqlPelicula = "SELECT RATING_PROMEDIO, CANTIDAD_VOTOS FROM PELICULA WHERE ID = ?";
        
        // Actualizar película
        String sqlUpdate = "UPDATE PELICULA SET RATING_PROMEDIO = ?, CANTIDAD_VOTOS = ? WHERE ID = ?";
        
        try (PreparedStatement pstmtCalif = conn.prepareStatement(sqlUltimaCalif);
             PreparedStatement pstmtPelicula = conn.prepareStatement(sqlPelicula)) {
            
            // Obtener nueva calificación
            pstmtCalif.setInt(1, idPelicula);
            float nuevaCalificacion = 0;
            try (ResultSet rsCalif = pstmtCalif.executeQuery()) {
                if (rsCalif.next()) {
                    nuevaCalificacion = rsCalif.getFloat("CALIFICACION");
                }
            }
            
            // Obtener valores actuales
            pstmtPelicula.setInt(1, idPelicula);
            try (ResultSet rsPelicula = pstmtPelicula.executeQuery()) {
                if (rsPelicula.next()) {
                    float promedioActual = rsPelicula.getFloat("RATING_PROMEDIO");
                    int cantidadVotos = rsPelicula.getInt("CANTIDAD_VOTOS");
                    
                    // Cálculo incremental del promedio
                    float nuevoPromedio = (promedioActual * cantidadVotos + nuevaCalificacion) / (cantidadVotos + 1);
                    int nuevaCantidadVotos = cantidadVotos + 1;
                    
                    // Actualizar en la base de datos
                    try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                        pstmtUpdate.setFloat(1, nuevoPromedio);
                        pstmtUpdate.setInt(2, nuevaCantidadVotos);
                        pstmtUpdate.setInt(3, idPelicula);
                        pstmtUpdate.executeUpdate();
                        
                        System.out.println("✅ Rating actualizado para película ID " + idPelicula + 
                                         ": " + nuevoPromedio + " (" + nuevaCantidadVotos + " votos)");
                    }
                }
            }
        }
    }

    private Resenia mapResultSetToResenia(ResultSet rs) throws SQLException {
        Resenia resenia = new Resenia();
        resenia.setID_Resenia(rs.getInt("ID"));
        resenia.setCalificacion(rs.getInt("CALIFICACION"));
        resenia.setComentario(rs.getString("COMENTARIO"));
        resenia.setAprobado(rs.getInt("APROBADO"));
        resenia.setFechaHora(rs.getString("FECHA_HORA"));
        resenia.setID_Usuario(rs.getInt("ID_USUARIO"));
        resenia.setID_Pelicula(rs.getInt("ID_PELICULA"));
        return resenia;
    }

}