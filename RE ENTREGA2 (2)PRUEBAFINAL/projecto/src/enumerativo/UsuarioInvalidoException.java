package enumerativo;

/**
 * Excepción personalizada para errores relacionados con datos de usuario
 * inválidos.
 * Esta es una Checked Exception que debe ser manejada explícitamente.
 * 
 * Se lanza cuando:
 * - El DNI ya existe en la base de datos
 * - El nombre o apellido contienen caracteres inválidos
 * - El email tiene un formato incorrecto
 * - La contraseña no cumple con los requisitos mínimos
 * - El nombre de usuario ya está en uso
 */
public class UsuarioInvalidoException extends Exception {

    private String campo; // Campo que causó el error (ej: "DNI", "email", "password")
    private String valorIngresado; // Valor que causó el error

    /**
     * Constructor básico con mensaje de error
     * 
     * @param mensaje Descripción del error
     */
    public UsuarioInvalidoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa
     * 
     * @param mensaje Descripción del error
     * @param causa   Excepción que causó este error
     */
    public UsuarioInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor completo con información detallada del error
     * 
     * @param mensaje        Descripción del error
     * @param campo          Campo que causó el error
     * @param valorIngresado Valor que causó el error
     */
    public UsuarioInvalidoException(String mensaje, String campo, String valorIngresado) {
        super(mensaje);
        this.campo = campo;
        this.valorIngresado = valorIngresado;
    }

    /**
     * Obtiene el campo que causó el error
     * 
     * @return Nombre del campo
     */
    public String getCampo() {
        return campo;
    }

    /**
     * Obtiene el valor que causó el error
     * 
     * @return Valor ingresado
     */
    public String getValorIngresado() {
        return valorIngresado;
    }

    /**
     * Retorna un mensaje detallado del error
     */
    @Override
    public String toString() {
        if (campo != null && valorIngresado != null) {
            return "UsuarioInvalidoException: " + getMessage() +
                    " [Campo: " + campo + ", Valor: " + valorIngresado + "]";
        }
        return "UsuarioInvalidoException: " + getMessage();
    }
}
