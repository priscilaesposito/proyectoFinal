// Archivo: src/db/BaseDeDatos.java
package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import dao.Conexion;

public class BaseDeDatos {

    public static Connection conectar() throws SQLException {
        return Conexion.conectar();
    }

    public static void inicializarBaseDeDatos() {
        try (Connection connection = conectar();
             Statement stmt = connection.createStatement()) {

            String sqlDatosPersonales = "CREATE TABLE IF NOT EXISTS DATOS_PERSONALES (" +
                    "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "NOMBRES TEXT (100) NOT NULL, " +
                    "APELLIDO TEXT (100) NOT NULL, " +
                    "DNI INTEGER NOT NULL" +
                    ");";
            stmt.executeUpdate(sqlDatosPersonales);

            String sqlPelicula = "CREATE TABLE IF NOT EXISTS PELICULA (" +
                    "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "GENERO TEXT (1) NOT NULL, " +
                    "TITULO TEXT (100) NOT NULL, " +
                    "RESUMEN TEXT (500), " +
                    "DIRECTOR TEXT (100) NOT NULL, " +
                    "DURACION REAL NOT NULL" +
                    ");";
            stmt.executeUpdate(sqlPelicula);

            String sqlUsuario = "CREATE TABLE IF NOT EXISTS USUARIO (" +
                    "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "NOMBRE_USUARIO TEXT NOT NULL, " +
                    "EMAIL TEXT NOT NULL, " +
                    "CONTRASENIA TEXT NOT NULL, " +
                    "ID_DATOS_PERSONALES INTEGER NOT NULL, " +
                    "CONSTRAINT USUARIO_DATOS_PERSONALES_FK FOREIGN KEY (ID_DATOS_PERSONALES) " + 
                    "REFERENCES DATOS_PERSONALES (ID)" +
                    ");";
            stmt.executeUpdate(sqlUsuario);

            String sqlResenia = "CREATE TABLE IF NOT EXISTS RESENIA (" +
                    "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "CALIFICACION INTEGER NOT NULL, " +
                    "COMENTARIO TEXT(500), " +
                    "APROBADO INTEGER DEFAULT (1) NOT NULL, " +
                    "FECHA_HORA DATETIME NOT NULL, " +
                    "ID_USUARIO INTEGER NOT NULL, " +
                    "ID_PELICULA INTEGER NOT NULL, " +
                    "CONSTRAINT RESENIA_USUARIO_FK FOREIGN KEY (ID_USUARIO) " +
                    "REFERENCES USUARIO(ID), " +
                    "CONSTRAINT RESENIA_PELICULA_FK FOREIGN KEY (ID_PELICULA) " +
                    "REFERENCES PELICULA (ID)" +
                    ");";
            stmt.executeUpdate(sqlResenia);

        } catch (SQLException e) {
            System.out.println("Error al crear las tablas: " + e.getMessage());
        }
    }
}
