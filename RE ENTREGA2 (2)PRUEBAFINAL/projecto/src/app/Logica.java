package app;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.SQLException;

import dao.DatosPersonalesDAO;
import dao.UsuarioDAO;
import dao.PeliculaDAO;

import daoJDBC.DatosPersonalesDAOJdbc;
import daoJDBC.UsuarioDAOjdbc;
import db.ConsultaPeliculasOMDb;
import daoJDBC.PeliculaDAOjdbc;
import model.Usuario;
import gestion.Administrador;
import gestion.GestionUsuario;
import gestion.ListasyResenias;
import gestion.TL2;
import model.Resenia;
import model.Pelicula;
import enumerativo.UsuarioInvalidoException;
import enumerativo.PeliculaNoEncontradaException;
import enumerativo.ReseniaInvalidaException;

import java.time.LocalDateTime;
import org.json.JSONObject;
import org.json.JSONArray;

public class Logica {

    private static UsuarioDAO usuarioDAO = new UsuarioDAOjdbc();
    private static DatosPersonalesDAO datosPersonalesDAO = new DatosPersonalesDAOJdbc();
    private static PeliculaDAO peliculaDAO = new PeliculaDAOjdbc();
    private static Scanner scanner = new Scanner(System.in);
    private static GestionUsuario gestionUsuario = new GestionUsuario();
    private static TL2 TL2 = new TL2();
    private static Administrador Administrador = new Administrador();
    private static ListasyResenias listasyResenias = new ListasyResenias();

    public static void registrarDatosPersonales() throws Exception {

        // SOLICITAR DATOS
        Usuario DatosPersonales = new Usuario();
        solicitarDatosPersonales(DatosPersonales);
        try {

            // VALIDACION
            try {
                gestionUsuario.validacionDatosPersonales(DatosPersonales);
            } catch (UsuarioInvalidoException e) {
                System.err.println("\n[ERROR DE VALIDACION] " + e.toString());
                return;
            }

            // MOSTRAR Y CONFIRMAR
            mostrarDatosIngresados(DatosPersonales);
            System.out.println("\n¿Son estos datos correctos? (S/N): ");
            String confirmacion = scanner.nextLine();

            if ("S".equals(confirmacion)) {
                // GUARDAR EN LA BASE DE DATOS
                gestionUsuario.registrarDatosPersonales(DatosPersonales);
                System.out.println("\n¡REGISTRO EXITOSO! Los datos se han guardado correctamente.");
            } else {
                System.out.println("\nRegistro cancelado por el usuario. No se guardo en la Base de Datos.");
            }

        } catch (SQLException e) {
            System.err.println("\n[ERROR DE BD] Fallo la operacion de la base de datos: " + e.getMessage());
        }
    }

    public static void registrarUsuario() throws Exception {

        Usuario u = new Usuario();

        try {
            // LISTA DATOS PERSONALES
            List<Usuario> DP = TL2.getListaPersonas();

            for (Usuario d : DP) {
                System.out.println("ID: " + d.getID_DATOS_PERSONALES() + " - Nombre: " + d.getNombre() + " "
                        + d.getApellido() + " - DNI: " + d.getDNI());
            }

            // SELECCIONAR DATOS PERSONALES EXISTENTES
            System.out.println("\nSeleccione el ID de los datos personales que desea asociar al usuario:");
            int idSeleccionado = scanner.nextInt();
            scanner.nextLine(); // SALTO DE LINEA

            // SOLICITAR DATOS DEL USUARIO
            solicitarDatosUsuario(u);
            try {
                gestionUsuario.ValidacionUsuario(u);
            } catch (UsuarioInvalidoException e) {
                System.err.println("\n[ERROR DE VALIDACION] " + e.toString());
                return;
            }

            u.setID_DATOS_PERSONALES(idSeleccionado);

            // MOSTRAR Y CONFIRMAR
            mostrarUsuarioIngresados(u);
            System.out.println("\n¿Son estos datos correctos? (S/N): ");
            String confirmacion = scanner.nextLine();

            if ("S".equals(confirmacion)) {
                // GUARDAR EN LA BASE DE DATOS
                gestionUsuario.registrarUsuario(u);
                System.out.println("\n¡REGISTRO EXITOSO! Los datos se han guardado correctamente.");
            } else {
                System.out.println("\nRegistro cancelado por el usuario. No se guardo en la Base de Datos.");
            }

        } catch (SQLException e) {
            System.err.println("\n[ERROR DE BD] Fallo la operacion de la base de datos: " + e.getMessage());
        }
    }

    public static void registrarPelicula() throws Exception {

        model.Pelicula p = new model.Pelicula();

        try {
            // SOLICITAR DATOS DE LA PELICULA
            solicitarDatosPelicula(p);
            try {
                Administrador.validarRegistroPelicula(p);
            } catch (IllegalArgumentException e) {
                System.err.println("\n[ERROR DE VALIDACION] " + e.getMessage());
                return;
            }

            // MOSTRAR Y CONFIRMAR
            mostrarDatosPelicula(p);
            System.out.println("\n¿Son estos datos correctos? (S/N): ");
            String confirmacion = scanner.nextLine();

            if ("S".equals(confirmacion)) {
                // GUARDAR EN LA BASE DE DATOS
                Administrador.almacenarPelicula(p);
                System.out.println("\n¡REGISTRO EXITOSO! Los datos se han guardado correctamente.");
            } else {
                System.out.println("\nRegistro cancelado por el usuario. No se guardo en la Base de Datos.");
            }

        } catch (SQLException e) {
            System.err.println("\n[ERROR DE BD] Fallo la operacion de la base de datos: " + e.getMessage());
        }
    }

    public static void listarUsuarios() throws Exception {
        try {
            System.out.println("Seleccione el criterio de ordenamiento:");
            System.out.println("NOMBRE O EMAIL");
            String criterio = scanner.nextLine();
            List<Usuario> usuarios = TL2.listarUsuariosOrdenados(criterio);
            System.out.println("\n--- LISTA DE USUARIOS REGISTRADOS ---");
            for (Usuario u : usuarios) {
                System.out.println(
                        "ID: " + u.getID_USUARIO() + ", Username: " + u.getUsername() + ", Correo: " + u.getCorreo() +
                                ", Nombre: " + u.getNombre() + " " + u.getApellido() + ", DNI: " + u.getDNI());
            }
        } catch (SQLException e) {
            System.err.println("\n[ERROR DE BD] Fallo la operacion de la base de datos: " + e.getMessage());
        }
    }

    public static void listarPeliculas() throws Exception {
        try {
            System.out.println("Seleccione el criterio de ordenamiento:");
            System.out.println("TITULO, DIRECTOR O GENERO");
            String criterio = scanner.nextLine();
            List<model.Pelicula> peliculas = TL2.listarPeliculasOrdenadas(criterio);
            System.out.println("\n--- LISTA DE PELICULAS REGISTRADAS ---");
            for (model.Pelicula p : peliculas) {
                System.out.println("ID: " + p.getID() + ", Titulo: " + p.getMetadatos().getTitulo() + ", Director: "
                        + p.getMetadatos().getDirector() +
                        ", Genero(s): " + String.join(", ", p.getGeneros()) + ", Duracion: "
                        + p.getVideo().getDuracion() + " minutos");
            }
        } catch (SQLException e) {
            System.err.println("\n[ERROR DE BD] Fallo la operacion de la base de datos: " + e.getMessage());
        }
    }

    public static void registrarResenia() throws Exception {

        // SOLICITAR DATOS
        Resenia r = solicitarDatosParaResenia();

        if (r == null) {
            System.out.println("Error al solicitar datos para la resenia.");
            return;
        }

        // LISTAR PELICULAS
        mostrarListaPeliculas();

        // SELECCIONAR PELICULA
        System.out.println("\nSeleccione el ID de la pelicula que desea reseniar:");
        int idPelicula = scanner.nextInt();
        scanner.nextLine(); // SALTO DE LINEA
        r.setID_Pelicula(idPelicula);

        // DATOS DE LA RESENIA
        System.out.println("Ingrese su calificacion (1-10):");
        int calificacion = scanner.nextInt();

        // Validar calificacion
        try {
            if (calificacion < 1 || calificacion > 10) {
                throw new ReseniaInvalidaException(
                        "La calificacion debe estar entre 1 y 10",
                        calificacion);
            }
            r.setCalificacion(calificacion);
        } catch (ReseniaInvalidaException e) {
            System.err.println("\n[ERROR DE VALIDACION] " + e.toString());
            return;
        }

        scanner.nextLine(); // SALTO DE LINEA
        System.out.println("Ingrese su comentario:");
        String comentario = scanner.nextLine();

        // Validar comentario
        try {
            if (comentario == null || comentario.trim().isEmpty()) {
                throw new ReseniaInvalidaException(
                        "El comentario no puede estar vacio",
                        "Comentario vacio");
            }
            if (comentario.trim().length() < 10) {
                throw new ReseniaInvalidaException(
                        "El comentario debe tener al menos 10 caracteres",
                        "Comentario demasiado corto (" + comentario.length() + " caracteres)");
            }
            r.setComentario(comentario);
        } catch (ReseniaInvalidaException e) {
            System.err.println("\n[ERROR DE VALIDACION] " + e.toString());
            return;
        }

        // MODULO

        // Obtener fecha y hora actual
        LocalDateTime fechaHoraActual = java.time.LocalDateTime.now();
        r.setFechaHora(fechaHoraActual.toString());

        // CONFIRMAR Y GUARDAR
        System.out.println("\n¿Son estos datos correctos? (S/N): ");
        String confirmacion = scanner.nextLine();
        if (confirmacion.equalsIgnoreCase("S")) {
            listasyResenias.aniadirResenias(r);
            System.out.println("Resenia guardada exitosamente.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    public static void aprobarResenia() throws Exception {
        // RESENIAS NO APROBADAS
        mostrarReseniasNoAprobadas();

        // ID RESENIA
        System.out.println("\nIngrese el ID de la resenia que desea aprobar:");
        int idResenia = scanner.nextInt();

        try {
            Resenia r = listasyResenias.buscarReseniaPorId(idResenia);
            if (r == null) {
                throw new ReseniaInvalidaException(
                        "Resenia no encontrada. Verifique el ID ingresado.",
                        "ID: " + idResenia);
            }

            // VALIDACION
            listasyResenias.validarResenia(idResenia);

            // Se muestra la resena seleccionada.
            mostrardatosresenia(r);

            // APROBAR RESENIA
            System.out.println("¿Desea aprobar esta resenia? (S/N): ");
            scanner.nextLine(); // SALTO DE LINEA
            String confirmacion = scanner.nextLine();
            if (confirmacion.equalsIgnoreCase("S")) {
                listasyResenias.aprobarResenia(idResenia);
                System.out.println("Resenia aprobada exitosamente.");
            } else {
                System.out.println("Operacion cancelada.");
            }
        } catch (ReseniaInvalidaException e) {
            System.err.println("\n[ERROR] " + e.toString());
        }

    }

    private static Resenia solicitarDatosParaResenia() throws SQLException {
        Resenia r = new Resenia();
        System.out.println("Ingrese su nombre de usuario:");
        String nombreUsuario = scanner.nextLine();
        System.out.println("Ingrese su contrasenia:");
        String contrasenia = scanner.nextLine();
        // VALIDAR DATOS

        gestionUsuario.validacionUsuarioContrasenia(nombreUsuario, contrasenia);

        Usuario u = gestionUsuario.buscar(nombreUsuario);
        if (u == null) {
            System.out.println("Error: Usuario no encontrado. Verifique sus credenciales.");
            return null;
        }
        r.setID_Usuario(u.getID_USUARIO());
        return r;
    }

    private static void mostrarListaPeliculas() throws Exception {
        String criterio = "TITULO";
        List<model.Pelicula> peliculas = TL2.listarPeliculasOrdenadas(criterio);
        System.out.println("\n--- LISTA DE PELICULAS REGISTRADAS ---");
        for (model.Pelicula p : peliculas) {
            System.out.println("ID: " + p.getID() + ", Titulo: " + p.getMetadatos().getTitulo() + ", Director: "
                    + p.getMetadatos().getDirector() +
                    ", Genero(s): " + String.join(", ", p.getGeneros()) + ", Duracion: " + p.getVideo().getDuracion()
                    + " minutos");
        }
    }

    private static void mostrarReseniasNoAprobadas() throws Exception {
        // RESENIAS NO APROBADAS
        List<Resenia> reseniasNoAprobadas = listasyResenias.listarReseniasNoAprobadas();
        for (Resenia r : reseniasNoAprobadas) {
            System.out.println("ID Resenia: " + r.getID_Resenia() + ", ID Pelicula: " + r.getID_Pelicula() +
                    ", ID Usuario: " + r.getID_Usuario() + ", Calificacion: " + r.getCalificacion() +
                    ", Comentario: " + r.getComentario() + ", Fecha y Hora: " + r.getFechaHora());
        }
    }

    private static void mostrardatosresenia(Resenia r) {
        System.out.println("\n--- DATOS DE LA RESENIA ---");
        System.out.println("ID Pelicula: " + r.getID_Pelicula());
        System.out.println("ID Usuario: " + r.getID_Usuario());
        System.out.println("Calificacion: " + r.getCalificacion());
        System.out.println("Comentario: " + r.getComentario());
        System.out.println("Fecha y Hora: " + r.getFechaHora());
    }

    private static void mostrarDatosPelicula(model.Pelicula p) {
        System.out.println("\n--- DATOS INGRESADOS DE LA PELICULA ---");
        System.out.println("Genero(s): " + String.join(", ", p.getGeneros()));
        System.out.println("Titulo: " + p.getMetadatos().getTitulo());
        System.out.println("Resumen: " + p.getMetadatos().getSipnosis());
        System.out.println("Director: " + p.getMetadatos().getDirector());
        System.out.println("Duracion (minutos): " + p.getVideo().getDuracion());
    }

    private static void solicitarDatosPelicula(model.Pelicula p) {
        System.out.println("Ingrese Genero(s) (separados por comas si son varios):");
        String generosInput = scanner.nextLine();
        String[] generosArray = generosInput.split(",");
        for (String genero : generosArray) {
            p.anadirGeneros(genero.trim());
        }
        System.out.println("Ingrese Titulo:");
        p.getMetadatos().setTitulo(scanner.nextLine());
        System.out.println("Ingrese Resumen:");
        p.getMetadatos().setSipnosis(scanner.nextLine());
        System.out.println("Ingrese Director:");
        p.getMetadatos().setDirector(scanner.nextLine());
        System.out.println("Ingrese Duracion (en minutos):");
        p.getVideo().setDuracion(scanner.nextDouble());
        scanner.nextLine(); // SALTO DE LINEA
    }

    private static void mostrarDatosIngresados(Usuario u) {
        System.out.println("\n--- DATOS INGRESADOS ---");
        System.out.println("DNI: " + u.getDNI());
        System.out.println("Nombre: " + u.getNombre());
        System.out.println("Apellido: " + u.getApellido());
    }

    private static void mostrarUsuarioIngresados(Usuario u) {
        System.out.println("\n--- DATOS DE USUARIO ---");
        System.out.println("Username: " + u.getUsername());
        System.out.println("Correo: " + u.getCorreo());
        System.out.println("Contrasenia: " + u.getContrasenia());
    }

    private static void solicitarDatosPersonales(Usuario nuevoUsuario) {
        System.out.println("Ingrese DNI:");
        nuevoUsuario.setDNI(scanner.nextInt());
        scanner.nextLine(); // SALTO DE LINEA
        System.out.println("Ingrese Nombre:");
        nuevoUsuario.setNombre(scanner.nextLine());
        System.out.println("Ingrese Apellido:");
        nuevoUsuario.setApellido(scanner.nextLine());

    }

    private static void solicitarDatosUsuario(Usuario nuevoUsuario) {
        System.out.println("Ingrese username:");
        nuevoUsuario.setUsername(scanner.nextLine());
        System.out.println("Ingrese Correo:");
        nuevoUsuario.setCorreo(scanner.nextLine());
        System.out.println("Ingrese Contrasenia:");
        nuevoUsuario.setContrasenia(scanner.nextLine());
    }

    /**
     * Funcion para realizar el login de un usuario (consola)
     * Solicita email y password, valida las credenciales y proporciona feedback
     */
    public static Usuario login() throws Exception {
        System.out.println("\n=== LOGIN - PLATAFORMA DE STREAMING ===");

        try {
            // Solicitar credenciales
            Usuario usuario = new Usuario();
            System.out.println("Ingrese Correo:");
            usuario.setCorreo(scanner.nextLine());
            System.out.println("Ingrese Contrasenia:");
            usuario.setContrasenia(scanner.nextLine());

            // Validacion
            if (usuario.getCorreo().isEmpty() || usuario.getContrasenia().isEmpty()) {
                System.err.println("\n[ERROR] Completar todos los campos.");
                return null;
            }

            // Intentar validar usuario
            Usuario usuarioValidado = usuarioDAO.validar(usuario.getCorreo(), usuario.getContrasenia());

            if (usuarioValidado != null) {
                System.out.println("\n¡Bienvenido/a, " + usuarioValidado.getUsername() + "!");
                return usuarioValidado;
            } else {
                System.err.println("\n[ERROR] E-mail o Password incorrectos.");
                return null;
            }

        } catch (SQLException e) {
            System.err.println("\n[ERROR DE CONEXION] No se pudo verificar las credenciales: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("\n[ERROR] Error inesperado durante el login: " + e.getMessage());
            return null;
        }
    }

    /**
     * Funcion para realizar el login de un usuario (interfaz grafica)
     * Recibe email y password como parametros
     */
    public static Usuario login(String email, String password) throws Exception {
        try {
            // Validaciones basicas
            if (email == null || email.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                return null;
            }

            // Intentar validar usuario usando el DAO con email
            Usuario usuarioValidado = usuarioDAO.validarPorEmail(email.trim(), password);

            return usuarioValidado; // Retorna el usuario si es valido, null si no

        } catch (SQLException e) {
            throw new Exception("Error de conexion a la base de datos: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Error inesperado durante el login: " + e.getMessage());
        }
    }

    /**
     * Verificar si un nombre de usuario ya existe
     */
    public static boolean existeUsuario(String username) throws Exception {
        try {
            Usuario u = usuarioDAO.buscar(username);
            return u != null;
        } catch (SQLException e) {
            throw new Exception("Error al verificar usuario: " + e.getMessage());
        }
    }

    /**
     * Verificar si un email ya existe
     */
    public static boolean existeEmail(String email) throws Exception {
        try {
            return usuarioDAO.existeEmail(email);
        } catch (SQLException e) {
            throw new Exception("Error al verificar email: " + e.getMessage());
        }
    }

    /**
     * Verificar si un DNI ya existe
     */
    public static boolean existeDNI(String dni) throws Exception {
        try {
            int dniInt = Integer.parseInt(dni);
            return datosPersonalesDAO.existeDNI(dniInt);
        } catch (NumberFormatException e) {
            throw new Exception("DNI invalido");
        } catch (SQLException e) {
            throw new Exception("Error al verificar DNI: " + e.getMessage());
        }
    }

    /**
     * Registrar un nuevo usuario con todos sus datos
     */
    public static boolean registrarUsuario(String nombre, String apellido, String dni,
            int edad, String direccion, String telefono,
            String username, String email, String password) throws Exception {
        try {
            // Primero registrar datos personales
            Usuario datosPersonales = new Usuario();
            datosPersonales.setNombre(nombre);
            datosPersonales.setApellido(apellido);
            datosPersonales.setDNI(Integer.parseInt(dni));

            // Validar datos personales
            gestionUsuario.validacionDatosPersonales(datosPersonales);

            // Registrar datos personales en la BD
            datosPersonalesDAO.registrar(datosPersonales);

            // Obtener el ID generado (buscar por DNI)
            Usuario dpRegistrado = datosPersonalesDAO.buscarPorDNI(Integer.parseInt(dni));

            if (dpRegistrado == null) {
                throw new Exception("Error al recuperar los datos personales registrados");
            }

            // Crear usuario
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setCorreo(email);
            usuario.setContrasenia(password);
            usuario.setID_DATOS_PERSONALES(dpRegistrado.getID_DATOS_PERSONALES());

            // Validar datos de usuario
            gestionUsuario.ValidacionUsuario(usuario);

            // Registrar usuario
            usuarioDAO.registrar(usuario);

            return true;

        } catch (IllegalArgumentException e) {
            throw new Exception("Error de validacion: " + e.getMessage());
        } catch (SQLException e) {
            throw new Exception("Error al registrar en la base de datos: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Buscar pelicula en OMDb por titulo
     * 
     * @throws PeliculaNoEncontradaException si no se encuentra la pelicula
     * @throws java.io.IOException si hay problemas de red
     */
    public static JSONObject buscarPeliculaOMDb(String titulo) throws Exception {
        try {
            JSONObject resultado = ConsultaPeliculasOMDb.buscarPelicula(titulo);

            if (resultado == null || resultado.optString("Response", "False").equals("False")) {
                throw new PeliculaNoEncontradaException(
                        "No se encontro la pelicula en OMDb",
                        "Titulo",
                        titulo);
            }

            return resultado;
        } catch (PeliculaNoEncontradaException e) {
            throw e; // Re-lanzar la excepcion personalizada
        } catch (java.net.ConnectException | java.net.UnknownHostException e) {
            throw new java.io.IOException("Sin conexion a Internet. " + e.getMessage());
        } catch (java.io.IOException e) {
            throw e; // Re-lanzar errores de red
        } catch (Exception e) {
            throw new Exception("Error al buscar pelicula: " + e.getMessage());
        }
    }

    /**
     * Buscar multiples peliculas en OMDb
     */
    public static JSONObject buscarVariasPeliculasOMDb(String searchTerm) throws Exception {
        return ConsultaPeliculasOMDb.buscarVariasPeliculas(searchTerm);
    }

    /**
     * Buscar pelicula en OMDb y guardarla en la base de datos
     */
    public static boolean buscarYGuardarPeliculaOMDb(String titulo) throws Exception {
        try {
            JSONObject datosOMDb = ConsultaPeliculasOMDb.buscarPelicula(titulo);

            if (datosOMDb == null) {
                throw new Exception("No se encontro la pelicula en OMDb");
            }

            // Crear objeto Pelicula
            Pelicula pelicula = new Pelicula();

            // Mapear datos basicos
            pelicula.getMetadatos().setTitulo(datosOMDb.getString("Title"));
            pelicula.getMetadatos().setSipnosis(datosOMDb.optString("Plot", "Sin sinopsis"));
            pelicula.getMetadatos().setDirector(datosOMDb.optString("Director", "Desconocido"));

            // Extraer anio
            String year = datosOMDb.optString("Year", "0");
            try {
                // Por si es serie (viene como "2010-2020")
                pelicula.setAnio(Integer.parseInt(year.split("-")[0]));
            } catch (NumberFormatException e) {
                pelicula.setAnio(0);
            }

            // Rating promedio de IMDb
            String imdbRating = datosOMDb.optString("imdbRating", "0");
            if (!imdbRating.equals("N/A") && !imdbRating.isEmpty()) {
                try {
                    pelicula.setRatingPromedio(Float.parseFloat(imdbRating));
                } catch (NumberFormatException e) {
                    pelicula.setRatingPromedio(0.0f);
                }
            }

            // Poster URL
            pelicula.setPoster(datosOMDb.optString("Poster", ""));

            // Generos (viene como "Action, Adventure, Drama")
            String generos = datosOMDb.optString("Genre", "");
            if (!generos.isEmpty() && !generos.equals("N/A")) {
                for (String genero : generos.split(", ")) {
                    pelicula.anadirGeneros(genero.trim());
                }
            } else {
                pelicula.anadirGeneros("Sin genero");
            }

            // Duracion (viene como "142 min")
            String runtime = datosOMDb.optString("Runtime", "0 min");
            if (!runtime.equals("N/A")) {
                try {
                    int duracion = Integer.parseInt(runtime.split(" ")[0]);
                    pelicula.getVideo().setDuracion(duracion);
                } catch (NumberFormatException e) {
                    pelicula.getVideo().setDuracion(0);
                }
            }

            // Validar y guardar
            Administrador.validarRegistroPelicula(pelicula);
            peliculaDAO.registrar(pelicula);

            return true;

        } catch (IllegalArgumentException e) {
            throw new Exception("Error de validacion: " + e.getMessage());
        } catch (SQLException e) {
            throw new Exception("Error al guardar en la base de datos: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Error al buscar/guardar pelicula: " + e.getMessage());
        }
    }

    /**
     * Listar peliculas desde OMDb por termino de busqueda
     * Retorna un array de peliculas encontradas
     */
    public static JSONArray listarPeliculasOMDb(String searchTerm) throws Exception {
        JSONObject resultado = ConsultaPeliculasOMDb.buscarVariasPeliculas(searchTerm);

        if (resultado != null && resultado.has("Search")) {
            return resultado.getJSONArray("Search");
        }

        return new JSONArray(); // Retorna array vacio si no hay resultados
    }

    /**
     * Verificar si es el primer login del usuario
     */
    public static boolean esPrimerLogin(int idUsuario) throws Exception {
        try (java.sql.Connection conn = db.BaseDeDatos.conectar();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT COUNT(*) FROM PRIMER_LOGIN WHERE ID_USUARIO = ?")) {

            pstmt.setInt(1, idUsuario);
            java.sql.ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0; // True si no hay registro
            }
            return true;
        } catch (SQLException e) {
            throw new Exception("Error al verificar primer login: " + e.getMessage());
        }
    }

    /**
     * Registrar que el usuario ya hizo su primer login
     */
    public static void registrarPrimerLogin(int idUsuario) throws Exception {
        try (java.sql.Connection conn = db.BaseDeDatos.conectar();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO PRIMER_LOGIN (ID_USUARIO, FECHA_PRIMER_LOGIN) VALUES (?, ?)")) {

            pstmt.setInt(1, idUsuario);
            pstmt.setString(2, LocalDateTime.now().toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Error al registrar primer login: " + e.getMessage());
        }
    }

    /**
     * Obtener top 10 peliculas mejor rankeadas.
     * Combina películas de BD (con ratings actualizados) y CSV (sin duplicados).
     * 
     * @throws PeliculaNoEncontradaException si no hay peliculas disponibles
     */
    public static List<Pelicula> obtenerTop10Peliculas() throws Exception {
        try {
            // 1. Obtener todas las películas de la BD (con ratings actualizados)
            List<Pelicula> peliculasBD = peliculaDAO.listarTodos();
            
            // 2. Cargar películas del CSV
            List<Pelicula> peliculasCSV = utilidades.CargadorCSV.cargarPeliculasDesdeCSV();
            
            // 3. Crear conjunto de títulos+años de BD para evitar duplicados
            java.util.Set<String> peliculasEnBD = new java.util.HashSet<>();
            for (Pelicula p : peliculasBD) {
                String clave = p.getMetadatos().getTitulo() + "_" + p.getAnio();
                peliculasEnBD.add(clave);
            }
            
            // 4. Combinar: agregar películas del CSV que NO están en BD
            List<Pelicula> todasPeliculas = new ArrayList<>(peliculasBD);
            for (Pelicula p : peliculasCSV) {
                String clave = p.getMetadatos().getTitulo() + "_" + p.getAnio();
                if (!peliculasEnBD.contains(clave)) {
                    todasPeliculas.add(p);
                }
            }
            
            // 5. Ordenar por rating descendente
            todasPeliculas.sort((p1, p2) -> Float.compare(p2.getRatingPromedio(), p1.getRatingPromedio()));
            
            // 6. Tomar las 10 mejores
            List<Pelicula> top10 = todasPeliculas.subList(0, Math.min(10, todasPeliculas.size()));

            if (top10.isEmpty()) {
                throw new PeliculaNoEncontradaException(
                        "No hay peliculas disponibles en el catalogo",
                        "Top 10",
                        "Ninguna");
            }

            return top10;
        } catch (PeliculaNoEncontradaException e) {
            throw e;
        } catch (SQLException e) {
            throw new Exception("Error al obtener top peliculas: " + e.getMessage());
        }
    }

    /**
     * Obtener 10 peliculas random que el usuario no ha calificado
     * 
     * @throws PeliculaNoEncontradaException si no hay peliculas disponibles
     */
    public static List<Pelicula> obtener10PeliculasRandom(int idUsuario) throws Exception {
        try {
            List<Pelicula> peliculas = peliculaDAO.obtener10RandomNoCalificadas(idUsuario);

            if (peliculas == null || peliculas.isEmpty()) {
                throw new PeliculaNoEncontradaException(
                        "No hay peliculas disponibles para recomendar",
                        "Recomendaciones",
                        "Usuario ID: " + idUsuario);
            }

            return peliculas;
        } catch (PeliculaNoEncontradaException e) {
            throw e;
        } catch (SQLException e) {
            throw new Exception("Error al obtener peliculas random: " + e.getMessage());
        }
    }

    /**
     * Guardar calificacion de pelicula
     * 
     * @throws ReseniaInvalidaException si la calificacion o comentario son
     *                                  invalidos
     */
    public static boolean calificarPelicula(int idUsuario, int idPelicula, int calificacion, String comentario)
            throws Exception {
        try {
            // Validar calificacion
            if (calificacion < 1 || calificacion > 10) {
                throw new ReseniaInvalidaException(
                        "La calificacion debe estar entre 1 y 10",
                        calificacion);
            }

            // Validar comentario
            if (comentario == null || comentario.trim().isEmpty()) {
                throw new ReseniaInvalidaException(
                        "El comentario no puede estar vacio",
                        "Comentario vacio");
            }

            if (comentario.trim().length() < 10) {
                throw new ReseniaInvalidaException(
                        "El comentario debe tener al menos 10 caracteres",
                        "Comentario demasiado corto");
            }

            Resenia resenia = new Resenia();
            resenia.setID_Usuario(idUsuario);
            resenia.setID_Pelicula(idPelicula);
            resenia.setCalificacion(calificacion);
            resenia.setComentario(comentario.trim());
            resenia.setAprobado(1); // Auto-aprobar calificaciones simples
            resenia.setFechaHora(LocalDateTime.now().toString());

            listasyResenias.aniadirResenias(resenia);
            return true;

        } catch (ReseniaInvalidaException e) {
            throw e;
        } catch (SQLException e) {
            throw new Exception("Error al guardar calificacion: " + e.getMessage());
        }
    }
}