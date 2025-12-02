package daoJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import dao.PeliculaDAO;
import db.BaseDeDatos;
import model.Pelicula;

public class PeliculaDAOjdbc implements PeliculaDAO {

    @Override
    public void registrar(Pelicula pelicula) throws SQLException {
        String sql = "INSERT INTO PELICULA (GENERO, TITULO, RESUMEN, DIRECTOR, DURACION) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = BaseDeDatos.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            LinkedList<String> generos = pelicula.getGeneros();
            int k;
            String genero = "";
            for (k = 0; k < generos.size() - 1; k++) {
                genero += generos.get(k) + ", ";

            }
            pstmt.setString(1, genero);
            pstmt.setString(2, pelicula.getMetadatos().getTitulo());
            pstmt.setString(3, pelicula.getMetadatos().getSipnosis());
            pstmt.setString(4, pelicula.getMetadatos().getDirector());
            pstmt.setDouble(5, pelicula.getVideo().getDuracion());

            pstmt.executeUpdate();

        }
    }

    @Override
    public Pelicula buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM PELICULA WHERE ID = ?";
        try (Connection conn = BaseDeDatos.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPelicula(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Pelicula> listarTodos() throws SQLException{
        List<Pelicula> peliculas = new ArrayList<>();
        String sql = "SELECT * FROM PELICULA";

        try (Connection conn = BaseDeDatos.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                peliculas.add(mapResultSetToPelicula(rs));
            }
        }
        return peliculas;
    }

    private Pelicula mapResultSetToPelicula(ResultSet rs) throws SQLException {
        Pelicula p = new Pelicula();
        p.setID(rs.getInt("ID"));
        p.getMetadatos().setTitulo(rs.getString("TITULO"));

        String generos = rs.getString("GENERO");
        if (generos != null) {
            for (String genero : generos.split(",")) {
                p.anadirGeneros(genero.trim());
            }
        }

        p.getMetadatos().setSipnosis(rs.getString("RESUMEN"));
        p.getMetadatos().setDirector(rs.getString("DIRECTOR"));
        
        return p;
    }
}