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

    private boolean moderacion(Resenia resenia) {
        return true; // puesto de esta forma para no tener errores de compilacion.
    }

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

    public Resenia validarResenia(int IDresenia) throws SQLException { // verifica si la reseni existe, en ese caso devuelve la resenia,
                                                   // caso contrario informa y devuelve null.
        Resenia reseniaEncontrada = RD.buscarPorId(IDresenia);
        if (reseniaEncontrada == null) {
            System.out.println(" Error: La resenia con ID " + IDresenia + " no existe.");
            return null;
        }
        return reseniaEncontrada;
    }

    public void aprobarResenia(int idResenia) throws SQLException {
        RD.aprobarResenia(idResenia);
    }

    public Resenia buscarReseniaPorId(int idResenia) throws SQLException {
        return RD.buscarPorId(idResenia);
    }

}
