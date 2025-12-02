package gestion;


import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

import dao.DatosPersonalesDAO;
import dao.UsuarioDAO;
import model.Usuario;


public class GestionUsuario {
    private DatosPersonalesDAO UDJ = new daoJDBC.DatosPersonalesDAOJdbc();
    private UsuarioDAO UD = new daoJDBC.UsuarioDAOjdbc();

    public void configurarIdioma() {
        // TODO: Implement configurarIdioma
    }

    public void configurarNombre() {
        // TODO: Implement configurarNombr
    }

    public void configurarPreferencias() {
        // TODO: Implement configurarPreferencias
    }

    public void suscribirse() {
        // TODO: Implement suscribirse
    }

    private boolean dniUnico(int dni) throws SQLException {
        return UDJ.existeDNI(dni);
    }

    private boolean stringValido(String linea) {
        LinkedList<Character> caracteresInvalidos = new LinkedList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9', '!', '"', '#', '$', '%', '&', '/', '(', ')', '=', '?', '¡', '¿', '+', '-', '*', '{', '}',
                '[', ']', '^', '`', '´', '¨', ';', ':', '.', ','));
        for (char c : linea.toCharArray()) {
            if (caracteresInvalidos.contains(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Valida todos los campos de un objeto Usuario según los requisitos.
     * 
     * @param usuario El objeto Usuario con los datos a validar de datos personales.
     * @return El mismo objeto Usuario si es valido, o null si hay errores.
     * @throws SQLException
     */
    public void validacionDatosPersonales(Usuario usuario) throws SQLException {
        // Validar unicidad de DNI
         if (dniUnico(usuario.getDNI())) {
            throw new IllegalArgumentException("El DNI ya existe en la base de datos.");
            }

        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        if (!stringValido(usuario.getNombre())) {
            throw new IllegalArgumentException("El nombre no debe contener números ni caracteres especiales.");
        }

        if (usuario.getApellido() == null || usuario.getApellido().isEmpty()) { // Necesitas implementar getApellido()
                                                                                // en Usuario
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }

        if (!stringValido(usuario.getApellido())) {
            throw new IllegalArgumentException("El apellido no debe contener números ni caracteres especiales.");
        }

        return;
    }

    public void registrarDatosPersonales(Usuario nuevoUsuario) throws Exception {
        UDJ.registrar(nuevoUsuario);
    }

    private boolean mailValido(String mail) {

        if (mail == null || mail.trim().isEmpty()) {
            return false;
        }

        int atIndex = mail.indexOf('@');

        if (atIndex <= 0) {
            return false;
        }

        if (mail.lastIndexOf('@') != atIndex) {
            return false;
        }

        if (atIndex == mail.length() - 1) {
            return false;
        }

        return true;

    }

    public void ValidacionUsuario(Usuario usuario) {

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacio.");
        }

        if (usuario.getContrasenia() == null || usuario.getContrasenia().isEmpty()) {
            throw new IllegalArgumentException("La contrasenia no puede estar vacia.");
        }

        if (!mailValido(usuario.getCorreo())) {
            throw new IllegalArgumentException("El formato del mail ingresado debe ser xxx@yyy.");
        }

        return;
    }

    public void registrarUsuario(Usuario nuevoUsuario) throws Exception {
        UD.registrar(nuevoUsuario);
    }

    public boolean validacionUsuarioContrasenia(String usuario, String contrasenia) throws SQLException {
        if (UD.validar(usuario, contrasenia) != null) {
            return true;
        } else {
            return false;
        }
    }

    public Usuario buscar(String nombreUsuario) throws SQLException {
        return UD.buscar(nombreUsuario);
    }

}
