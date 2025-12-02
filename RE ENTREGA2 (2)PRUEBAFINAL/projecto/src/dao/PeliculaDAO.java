
package dao;

import java.sql.SQLException;
import java.util.List;
import model.Pelicula;

public interface PeliculaDAO {

    void registrar(Pelicula pelicula) throws SQLException;

    Pelicula buscarPorId(int id) throws SQLException;

    List<Pelicula> listarTodos() throws SQLException;
}
