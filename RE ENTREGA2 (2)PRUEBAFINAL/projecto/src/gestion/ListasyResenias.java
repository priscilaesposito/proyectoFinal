package gestion;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dao.ReseniaDAO;
import model.Resenia;

public class ListasyResenias {
    private ReseniaDAO RD = new daoJDBC.ReseniaDAOJdbc();
    private LinkedList<Resenia> resenias;
    private double puntuacion;
    private int cantPuntuacion;
    private int sumaPuntuacion;

    public LinkedList<Resenia> getResenias() {
        return resenias;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void puntuar(int nuevaPuntuacion) {
        this.sumaPuntuacion += nuevaPuntuacion;
        this.cantPuntuacion++;
        this.puntuacion = (double) this.sumaPuntuacion / this.cantPuntuacion;
    }

    public void aniadirResenias(Resenia resenia) throws SQLException {
        RD.registrar(resenia);
    }

    public List<Resenia> listarReseniasNoAprobadas() throws SQLException {
        return RD.listarNoAprobadas();
    }

	public Resenia validarResenia(int IDresenia) throws SQLException {
		Resenia reseniaEncontrada = RD.buscarPorId(IDresenia);
		if (reseniaEncontrada == null) {
			return null;
		}
		return reseniaEncontrada;
	}    public void aprobarResenia(int idResenia) throws SQLException {
        RD.aprobarResenia(idResenia);
    }

    public Resenia buscarReseniaPorId(int idResenia) throws SQLException {
        return RD.buscarPorId(idResenia);
    }

}
