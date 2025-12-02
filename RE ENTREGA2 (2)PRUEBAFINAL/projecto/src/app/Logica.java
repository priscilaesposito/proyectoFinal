package app;

import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;

import dao.DatosPersonalesDAO;
import dao.UsuarioDAO;

import daoJDBC.DatosPersonalesDAOJdbc;
import daoJDBC.UsuarioDAOjdbc;
import model.Usuario;
import gestion.Administrador;
import gestion.GestionUsuario;
import gestion.ListasyResenias;
import gestion.TL2;
import model.Resenia;

import java.time.LocalDateTime;

public class Logica{

    private static UsuarioDAO usuarioDAO = new UsuarioDAOjdbc();
    private static DatosPersonalesDAO datosPersonalesDAO = new DatosPersonalesDAOJdbc();
    private static Scanner scanner = new Scanner(System.in);
    private static GestionUsuario gestionUsuario = new GestionUsuario();
    private static TL2 TL2 = new TL2();
    private static Administrador Administrador = new Administrador();
    private static ListasyResenias listasyResenias = new ListasyResenias();

    public static void registrarDatosPersonales() throws Exception {
        
        //SOLICITAR DATOS 
        Usuario DatosPersonales = new Usuario();
        solicitarDatosPersonales(DatosPersonales);
        try {

            //VALIDACION
            try{
            gestionUsuario.validacionDatosPersonales(DatosPersonales);
            }
            catch (IllegalArgumentException e) {
            System.err.println("\n[ERROR DE VALIDACION] " + e.getMessage());
            return;  }


            //MOSTRAR Y CONFIRMAR
            mostrarDatosIngresados(DatosPersonales);
            System.out.println("\n¿Son estos datos correctos? (S/N): ");
            String confirmacion = scanner.nextLine();

            if ("S".equals(confirmacion)) {
                //GUARDAR EN LA BASE DE DATOS
                gestionUsuario.registrarDatosPersonales(DatosPersonales);
                System.out.println("\n¡REGISTRO EXITOSO! Los datos se han guardado correctamente.");
            } else {
                System.out.println("\nRegistro cancelado por el usuario. No se guardó en la Base de Datos.");
            }

        } 
        catch (SQLException e) {
            System.err.println("\n[ERROR DE BD] Falló la operación de la base de datos: " + e.getMessage());
        }
    }

    public static void registrarUsuario() throws Exception {
        
    
        Usuario u = new Usuario();
       
        try {
            //LISTA DATOS PERSONALES
            List<Usuario> DP= TL2.getListaPersonas();

            for (Usuario d : DP) {
                System.out.println("ID: " + d.getID_DATOS_PERSONALES() + " - Nombre: " + d.getNombre() + " " + d.getApellido() + " - DNI: " + d.getDNI());
            }
           
            //SELECCIONAR DATOS PERSONALES EXISTENTES
            System.out.println("\nSeleccione el ID de los datos personales que desea asociar al usuario:");
            int idSeleccionado = scanner.nextInt();
            scanner.nextLine(); //SALTO DE LINEA
            
            //SOLICITAR DATOS DEL USUARIO
             solicitarDatosUsuario(u);
             try{
             gestionUsuario.ValidacionUsuario(u);}
             catch (IllegalArgumentException e) {
            System.err.println("\n[ERROR DE VALIDACION] " + e.getMessage());
            return;  }

             u.setID_DATOS_PERSONALES(idSeleccionado);

            //MOSTRAR Y CONFIRMAR 
            mostrarUsuarioIngresados(u);
            System.out.println("\n¿Son estos datos correctos? (S/N): ");
            String confirmacion = scanner.nextLine();

            if ("S".equals(confirmacion)) {
                //GUARDAR EN LA BASE DE DATOS
                gestionUsuario.registrarUsuario(u);
                System.out.println("\n¡REGISTRO EXITOSO! Los datos se han guardado correctamente.");
            } else {
                System.out.println("\nRegistro cancelado por el usuario. No se guardó en la Base de Datos.");
            }

        } 
        catch (SQLException e) {
            System.err.println("\n[ERROR DE BD] Falló la operación de la base de datos: " + e.getMessage());
        }
    }

    public static void registrarPelicula() throws Exception {
        
    
        model.Pelicula p = new model.Pelicula();
       
        try {
            //SOLICITAR DATOS DE LA PELICULA
           solicitarDatosPelicula(p);
           try{
            Administrador.validarRegistroPelicula(p);}
            catch (IllegalArgumentException e) {
            System.err.println("\n[ERROR DE VALIDACION] " + e.getMessage());
            return;  }

            //MOSTRAR Y CONFIRMAR
            mostrarDatosPelicula(p);
            System.out.println("\n¿Son estos datos correctos? (S/N): ");
            String confirmacion = scanner.nextLine();

            if ("S".equals(confirmacion)) {
                //GUARDAR EN LA BASE DE DATOS
                Administrador.almacenarPelicula(p);
                System.out.println("\n¡REGISTRO EXITOSO! Los datos se han guardado correctamente.");
            } else {
                System.out.println("\nRegistro cancelado por el usuario. No se guardó en la Base de Datos.");
            }

        } 
        catch (SQLException e) {
            System.err.println("\n[ERROR DE BD] Falló la operación de la base de datos: " + e.getMessage());
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
                System.out.println("ID: " + u.getID_USUARIO() + ", Username: " + u.getUsername() + ", Correo: " + u.getCorreo() +
                        ", Nombre: " + u.getNombre() + " " + u.getApellido() + ", DNI: " + u.getDNI());
            }
        } 
        catch (SQLException e) {
            System.err.println("\n[ERROR DE BD] Falló la operación de la base de datos: " + e.getMessage());
        }
    }

    public static void listarPeliculas() throws Exception {
        try {
            System.out.println("Seleccione el criterio de ordenamiento:");
            System.out.println("TITULO, DIRECTOR O GENERO");
            String criterio = scanner.nextLine();
            List<model.Pelicula> peliculas = TL2.listarPeliculasOrdenadas(criterio);
            System.out.println("\n--- LISTA DE PELÍCULAS REGISTRADAS ---");
            for (model.Pelicula p : peliculas) {
                System.out.println("ID: " + p.getID() + ", Título: " + p.getMetadatos().getTitulo() + ", Director: " + p.getMetadatos().getDirector() +
                        ", Género(s): " + String.join(", ", p.getGeneros()) + ", Duración: " + p.getVideo().getDuracion() + " minutos");
            }
        } 
        catch (SQLException e) {
            System.err.println("\n[ERROR DE BD] Falló la operación de la base de datos: " + e.getMessage());
        }
    }

public static void registrarResenia() throws Exception {
   

    //SOLICITAR DATOS
    Resenia r = solicitarDatosParaResenia();

    if (r == null) {
        System.out.println("Error al solicitar datos para la reseña.");
        return;
    }

    //LISTAR PELICULAS
    mostrarListaPeliculas();

    //SELECCIONAR PELICULA
    System.out.println("\nSeleccione el ID de la película que desea reseñar:");
    int idPelicula = scanner.nextInt();
    scanner.nextLine(); //SALTO DE LINEA
    r.setID_Pelicula(idPelicula);

    //DATOS DE LA RESENIA
    System.out.println("Ingrese su calificación:");
    int calificacion = scanner.nextInt();
    r.setCalificacion(calificacion);
    scanner.nextLine(); //SALTO DE LINEA
    System.out.println("Ingrese su comentario:");
    String comentario = scanner.nextLine();
    r.setComentario(comentario);
    //MODULO

    // Obtener fecha y hora actual 
    LocalDateTime fechaHoraActual = java.time.LocalDateTime.now();
    r.setFechaHora(fechaHoraActual.toString());


    //CONFIRMAR Y GUARDAR
    System.out.println("\n¿Son estos datos correctos? (S/N): ");
    String confirmacion = scanner.nextLine();
    if (confirmacion.equalsIgnoreCase("S")) {
        listasyResenias.aniadirResenias(r);
        System.out.println("Reseña guardada exitosamente.");
    } else {
        System.out.println("Operación cancelada.");
    }
}

public static void aprobarResenia() throws Exception {
    //RESENIAS NO APROBADAS
    mostrarReseniasNoAprobadas();

    //ID RESENIA
    System.out.println("\nIngrese el ID de la reseña que desea aprobar:");
    int idResenia = scanner.nextInt();
    Resenia r = listasyResenias.buscarReseniaPorId(idResenia);
    if (r == null) {
        System.out.println("Error: Reseña no encontrada. Verifique el ID ingresado.");
        return;
    }

    //VALIDACION
    listasyResenias.validarResenia(idResenia);

    // Se muestra la resena seleccionada.
    mostrardatosresenia(r);

    //APROBAR RESENIA
    System.out.println("¿Desea aprobar esta reseña? (S/N): ");
    scanner.nextLine(); //SALTO DE LINEA
    String confirmacion = scanner.nextLine();
    if (confirmacion.equalsIgnoreCase("S")) {
        listasyResenias.aprobarResenia(idResenia);
        System.out.println("Reseña aprobada exitosamente.");
    } else {
        System.out.println("Operación cancelada.");
    }

}
private static Resenia solicitarDatosParaResenia() throws SQLException {
      Resenia r = new Resenia();
    System.out.println("Ingrese su nombre de usuario:");
    String nombreUsuario = scanner.nextLine();
    System.out.println("Ingrese su contraseña:");
    String contrasenia = scanner.nextLine();
     //VALIDAR DATOS
     
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
        System.out.println("ID: " + p.getID() + ", Título: " + p.getMetadatos().getTitulo() + ", Director: " + p.getMetadatos().getDirector() +
                ", Género(s): " + String.join(", ", p.getGeneros()) + ", Duración: " + p.getVideo().getDuracion() + " minutos");
    }
}
private static void mostrarReseniasNoAprobadas() throws Exception {
     //RESENIAS NO APROBADAS
    List<Resenia> reseniasNoAprobadas = listasyResenias.listarReseniasNoAprobadas();
    for (Resenia r : reseniasNoAprobadas) {
        System.out.println("ID Reseña: " + r.getID_Resenia() + ", ID Película: " + r.getID_Pelicula() +
                ", ID Usuario: " + r.getID_Usuario() + ", Calificación: " + r.getCalificacion() +
                ", Comentario: " + r.getComentario() + ", Fecha y Hora: " + r.getFechaHora());
    }
}

private static void mostrardatosresenia(Resenia r) {
    System.out.println("\n--- DATOS DE LA RESEÑA ---");
    System.out.println("ID Película: " + r.getID_Pelicula());
    System.out.println("ID Usuario: " + r.getID_Usuario());
    System.out.println("Calificación: " + r.getCalificacion());
    System.out.println("Comentario: " + r.getComentario());
    System.out.println("Fecha y Hora: " + r.getFechaHora());
}
    private static void mostrarDatosPelicula(model.Pelicula p) {
        System.out.println("\n--- DATOS INGRESADOS DE LA PELÍCULA ---");
        System.out.println("Género(s): " + String.join(", ", p.getGeneros()));
        System.out.println("Título: " + p.getMetadatos().getTitulo());
        System.out.println("Resumen: " + p.getMetadatos().getSipnosis());
        System.out.println("Director: " + p.getMetadatos().getDirector());
        System.out.println("Duración (minutos): " + p.getVideo().getDuracion());
    }   
     private static void solicitarDatosPelicula(model.Pelicula p){
        System.out.println("Ingrese Género(s) (separados por comas si son varios):");
        String generosInput = scanner.nextLine();
        String[] generosArray = generosInput.split(",");
        for (String genero : generosArray) {
            p.anadirGeneros(genero.trim());
        }
        System.out.println("Ingrese Título:");
        p.getMetadatos().setTitulo(scanner.nextLine());
        System.out.println("Ingrese Resumen:");
        p.getMetadatos().setSipnosis(scanner.nextLine());
        System.out.println("Ingrese Director:");
        p.getMetadatos().setDirector(scanner.nextLine());
        System.out.println("Ingrese Duración (en minutos):");
        p.getVideo().setDuracion(scanner.nextDouble());
        scanner.nextLine(); //  SALTO DE LINEA
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
        System.out.println("Contraseña: " + u.getContrasenia());
    }
    private static void solicitarDatosPersonales(Usuario nuevoUsuario){
        System.out.println("Ingrese DNI:");
        nuevoUsuario.setDNI(scanner.nextInt());
        scanner.nextLine(); // SALTO DE LINEA
        System.out.println("Ingrese Nombre:");
        nuevoUsuario.setNombre(scanner.nextLine());
        System.out.println("Ingrese Apellido:");
        nuevoUsuario.setApellido(scanner.nextLine());

    }
    private static void solicitarDatosUsuario(Usuario nuevoUsuario){
         System.out.println("Ingrese username:");
        nuevoUsuario.setUsername(scanner.nextLine());
        System.out.println("Ingrese Correo:");
        nuevoUsuario.setCorreo(scanner.nextLine());
        System.out.println("Ingrese Contrasenia:");
        nuevoUsuario.setContrasenia(scanner.nextLine());
}
}