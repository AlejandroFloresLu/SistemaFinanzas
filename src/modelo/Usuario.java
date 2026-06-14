package modelo;

import datos.UsuarioMD;
import java.sql.ResultSet;

/**
 * Clase que representa la entidad Usuario del sistema.
 * Capa: Modelo (PD - Proceso de Datos / Lógica de Negocio).
 * Gestiona la validación de datos y delega las operaciones de
 * persistencia a la clase UsuarioMD de la capa de datos.
 *
 * @author Samantha Nuñez
 */
public class Usuario {

    private int idUsuario = 0;
    private String nombre = "";
    private String email = "";
    private String telefono = "";
    private String password = "";
    private boolean estado = true;

    // ── Getters ──────────────────────────────────────────────

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return idUsuario del usuario
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtiene el teléfono del usuario.
     *
     * @return teléfono del usuario
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return password del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Obtiene el estado del usuario.
     *
     * @return true si el usuario está activo, false si está inactivo
     */
    public boolean getEstado() {
        return estado;
    }

    // ── Setters (con normalización) ──────────────────────────

    /**
     * Establece el identificador del usuario.
     *
     * @param idUsuario identificador a asignar
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Establece el nombre del usuario aplicando trim() y toLowerCase().
     *
     * @param nombre nombre a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre.trim().toLowerCase();
    }

    /**
     * Establece el correo electrónico aplicando trim() y toLowerCase().
     *
     * @param email correo a asignar
     */
    public void setEmail(String email) {
        this.email = email.trim().toLowerCase();
    }

    /**
     * Establece el teléfono del usuario aplicando trim().
     *
     * @param telefono teléfono a asignar
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono.trim();
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param password contraseña a asignar
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Establece el estado del usuario.
     *
     * @param estado true para activo, false para inactivo
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // ── Métodos de Lógica de Negocio ─────────────────────────

    /**
     * Verifica que los datos del usuario sean válidos antes de persistir.
     * Valida que el nombre, email y teléfono no estén vacíos.
     *
     * @return true si los datos son válidos, false en caso contrario
     */
    public boolean verificarDP() {
        if (nombre == null || nombre.isEmpty()) {
            return false;
        }
        if (email == null || email.isEmpty()) {
            return false;
        }
        if (telefono == null || telefono.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Reinicia todos los atributos del usuario a sus valores por defecto.
     */
    public void limpiarDatos() {
        idUsuario = 0;
        nombre = "";
        email = "";
        telefono = "";
        password = "";
        estado = true;
    }

    /**
     * Delega la inserción del usuario a la capa de datos.
     * Llama a UsuarioMD.insertar(this).
     *
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean grabarDP() {
        UsuarioMD usuarioMD = new UsuarioMD();
        return usuarioMD.insertar(this);
    }

    /**
     * Delega la modificación del usuario a la capa de datos.
     * Llama a UsuarioMD.modificar(this).
     *
     * @return true si la modificación fue exitosa, false en caso contrario
     */
    public boolean modificarDP() {
        UsuarioMD usuarioMD = new UsuarioMD();
        return usuarioMD.modificar(this);
    }

    /**
     * Delega la eliminación lógica del usuario a la capa de datos.
     * Cambia el estado a false en lugar de eliminar físicamente.
     *
     * @return true si la desactivación fue exitosa, false en caso contrario
     */
    public boolean borrarDP() {
        UsuarioMD usuarioMD = new UsuarioMD();
        return usuarioMD.borrar(this);
    }

    /**
     * Consulta todos los usuarios activos en la base de datos.
     *
     * @return ResultSet con todos los usuarios activos
     */
    public ResultSet buscarUsuarios() {
        UsuarioMD usuarioMD = new UsuarioMD();
        return usuarioMD.consultar();
    }

    /**
     * Consulta usuarios activos filtrados por los parámetros del objeto filtro.
     *
     * @param filtro objeto Usuario con los criterios de búsqueda
     * @return ResultSet con los usuarios que coinciden con el filtro
     */
    public ResultSet buscarUsuarios(Usuario filtro) {
        UsuarioMD usuarioMD = new UsuarioMD();
        return usuarioMD.consultar(filtro);
    }
}
