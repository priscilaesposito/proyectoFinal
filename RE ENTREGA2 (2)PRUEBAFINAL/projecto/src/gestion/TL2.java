package gestion;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dao.DatosPersonalesDAO;

import dao.UsuarioDAO;
import model.Pelicula;
import model.Titulo;
import model.Usuario;
import dao.PeliculaDAO;

import utilidades.OrdenarPorNombreUsuario;
import utilidades.OrdenarPorMail;

import utilidades.OrdenarPorTitulo;
import utilidades.OrdenarPorDuracion;
import utilidades.OrdenarPorPrimerGenero;

public class TL2 {
	private PeliculaDAO PD = new daoJDBC.PeliculaDAOjdbc();
	private UsuarioDAO UD = new daoJDBC.UsuarioDAOjdbc();
	private DatosPersonalesDAO UDJ = new daoJDBC.DatosPersonalesDAOJdbc();
	private LinkedList<Usuario> listaUsuarios;
	private LinkedList<Titulo> catalogo;

	public String iniciarSesion(String username, String password) {
		return null;
	}

	public boolean autenticacion(String token) {
		return false;
	}

	public LinkedList<Titulo> getCatalogo() {
		return null;
	}

	private void restringirGeolocalizacionCatalogo() {
	}

	public LinkedList<Usuario> getListaUsuarios() throws SQLException {

		return listaUsuarios;
	}

	public List<Usuario> getListaPersonas() throws SQLException {
		return UDJ.listarTodos();
	}

	public List<Usuario> getListaUsuariosDAO() throws SQLException {
		return UD.listarTodos();
	}

	public List<Usuario> listarUsuariosOrdenados(String criterio) throws SQLException {

		List<Usuario> listaUsuarios = UD.listarTodos();

		if ("NOMBRE".equalsIgnoreCase(criterio)) {
			Collections.sort(listaUsuarios, new OrdenarPorNombreUsuario());

		} else if ("EMAIL".equalsIgnoreCase(criterio)) {
			Collections.sort(listaUsuarios, new OrdenarPorMail());

		} else {
			System.out.println("Criterio de ordenacion no valido. Se muestra sin ordenar.");
		}

		return listaUsuarios;
	}

	public List<Pelicula> listarPeliculasOrdenadas(String criterio) throws SQLException {

		List<Pelicula> listaPeliculas = PD.listarTodos();

		if ("TITULO".equalsIgnoreCase(criterio)) {
			Collections.sort(listaPeliculas, new OrdenarPorTitulo());
		} else if ("DURACION".equalsIgnoreCase(criterio)) {
			Collections.sort(listaPeliculas, new OrdenarPorDuracion());
		} else if ("GENERO".equalsIgnoreCase(criterio)) {
			Collections.sort(listaPeliculas, new OrdenarPorPrimerGenero());
		} else {
			System.out.println("Criterio de ordenacion no valido. Se muestra sin ordenar.");
		}

		return listaPeliculas;
	}

}
