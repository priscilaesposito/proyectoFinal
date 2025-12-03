package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Ruta absoluta a la base de datos
    private static final String DB_PATH = System.getProperty("user.home") + 
        "/PriEsposito/proyectoFinal/RE ENTREGA2 (2)PRUEBAFINAL/projecto/plataforma_streaming.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    private static Connection connection = null;

    //DRIVER
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver JDBC de SQLite no encontrado: " + e.getMessage());
            throw new RuntimeException("Driver JDBC no encontrado", e);
        }
    }
    
   
    public static synchronized Connection conectar() throws SQLException {
        if (connection == null || connection.isClosed()) {
            //CREA NUEVA
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    
    public static synchronized void desconectar() {
        try {
             if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null; 
                System.out.println("Conexión a la base de datos cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("Error al desconectar de la base de datos SQLite: " + e.getMessage());
        }
    }
}

