package app;

import java.util.Scanner;
import java.sql.SQLException;

import dao.Conexion;
import db.BaseDeDatos;
import controlador.LoginControlador;

public class Main {

    private static void menuLogin(){
        System.out.println("Seleccione una opción:");
        System.out.println("1. Login / Iniciar Sesión");
        System.out.println("2. Registrar Usuario");
        System.out.println("3. Salir");
    }

    private static void menuPrincipal(){
        System.out.println("Seleccione una opción:");
        System.out.println("1. Registrar Datos Personales");
        System.out.println("2. Registrar Película");
        System.out.println("3. Listar Usuarios");
        System.out.println("4. Listar Películas");
        System.out.println("5. Registrar Reseña");
        System.out.println("6. Aprobar Reseña");
        System.out.println("7. Salir");
    }
     private static boolean opcionesLogin(int op) throws Exception {
        switch (op) {
            case 1:
                return Logica.login() != null; // Retorna true si login exitoso
            case 2:
                Logica.registrarUsuario();
                return false; 
            case 3:
                return false; 
            default:
                System.out.println("Opción no válida.");
                return false;
        }
    }

    private static void opcionesPrincipal(int op) throws Exception {
        switch (op) {
            case 1:
                Logica.registrarDatosPersonales();
                break;
            case 2:
                Logica.registrarPelicula();
                break;
            case 3:
                Logica.listarUsuarios();
                break;
            case 4:
                Logica.listarPeliculas();
                break;
            case 5:
                Logica.registrarResenia();
                break;
            case 6:
                Logica.aprobarResenia();
                break;
            case 7:
                break; // Cerrar sesión
            default:
                System.out.println("Opción no válida.");
                break;
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            // Inicializar base de datos
            Conexion.conectar();
            BaseDeDatos.inicializarBaseDeDatos();
            
            // Verificar si se debe usar interfaz gráfica
            boolean useGUI = true; // Por defecto usar GUI
            
            // Si se pasa argumento "--console", usar consola
            if (args.length > 0 && "--console".equals(args[0])) {
                useGUI = false;
            }
            
            if (useGUI) {
                System.out.println("Iniciando interfaz gráfica con patrón MVC...");
                // Iniciar interfaz gráfica usando el patrón MVC
                LoginControlador.iniciarAplicacion();
            } else {
                // Modo consola (código original)
                runConsoleMode();
            }
            
        } catch (SQLException e) {
            System.err.println("Error al iniciar la aplicación: La conexión a la BD falló.");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Modo consola (funcionalidad original)
     */
    private static void runConsoleMode() throws Exception {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("=== MODO CONSOLA ===");
            menuLogin();
            int op = scanner.nextInt();
            scanner.nextLine(); // Consumir nueva línea
            opcionesLogin(op);
        } finally {
            Conexion.desconectar();
            scanner.close();
        }
    }


}
