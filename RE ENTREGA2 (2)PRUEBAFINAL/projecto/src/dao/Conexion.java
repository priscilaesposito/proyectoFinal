package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;

public class Conexion {
    // Nombre del archivo de base de datos
    private static final String DB_NAME = "plataforma_streaming.db";
    
    // Obtiene la ruta de la base de datos de forma dinámica
    private static String getDbPath() {
        // Primero intentar encontrar la base de datos en el directorio actual
        File dbFile = new File(DB_NAME);
        if (dbFile.exists()) {
            return dbFile.getAbsolutePath();
        }
        
        // Intentar en el directorio donde se ejecuta el JAR
        try {
            String jarPath = Conexion.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI().getPath();
            File jarDir = new File(jarPath).getParentFile();
            
            // Si se ejecuta desde un JAR, buscar en el mismo directorio
            if (jarDir != null) {
                dbFile = new File(jarDir, DB_NAME);
                if (dbFile.exists()) {
                    return dbFile.getAbsolutePath();
                }
            }
        } catch (Exception e) {
            // Ignorar errores al obtener la ruta del JAR
        }
        
        // Por defecto, usar el directorio actual (creará la BD aquí si no existe)
        return DB_NAME;
    }
    
    private static final String DB_URL = "jdbc:sqlite:" + getDbPath();

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

