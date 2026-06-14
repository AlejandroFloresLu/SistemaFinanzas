package datos;

import modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Usuario.
 * Capa: Datos (MD).
 * Gestiona las operaciones CRUD sobre la tabla usuario en PostgreSQL.
 *
 * @author Samantha Nuñez
 */
public class UsuarioMD {

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param usuario objeto Usuario con los datos a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre, email, telefono, password, estado) "
                + "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getTelefono());
            ps.setString(4, usuario.getPassword());
            ps.setBoolean(5, usuario.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Modifica los datos de un usuario existente en la base de datos.
     *
     * @param usuario objeto Usuario con los datos actualizados
     * @return true si la modificación fue exitosa, false en caso contrario
     */
    public boolean modificar(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre = ?, email = ?, telefono = ?, "
                + "password = ? WHERE id_usuario = ?";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getTelefono());
            ps.setString(4, usuario.getPassword());
            ps.setInt(5, usuario.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Realiza una eliminación lógica del usuario cambiando su estado a false.
     *
     * @param usuario objeto Usuario con el idUsuario a desactivar
     * @return true si la desactivación fue exitosa, false en caso contrario
     */
    public boolean borrar(Usuario usuario) {
        String sql = "UPDATE usuario SET estado = false WHERE id_usuario = ?";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, usuario.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Consulta todos los usuarios activos en la base de datos.
     *
     * @return ResultSet con todos los usuarios activos
     */
    public ResultSet consultar() {
        String sql = "SELECT id_usuario, nombre, email, telefono, password, estado "
                + "FROM usuario WHERE estado = true ORDER BY id_usuario";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Consulta usuarios activos filtrados por nombre.
     *
     * @param filtro objeto Usuario con los criterios de búsqueda
     * @return ResultSet con los usuarios que coinciden con el filtro
     */
    public ResultSet consultar(Usuario filtro) {
        String sql = "SELECT id_usuario, nombre, email, telefono, password, estado "
                + "FROM usuario WHERE estado = true AND nombre LIKE ? "
                + "ORDER BY id_usuario";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, "%" + filtro.getNombre() + "%");
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verifica si ya existe un usuario con el mismo nombre en la base de datos.
     *
     * @param usuario objeto Usuario con el nombre a verificar
     * @return true si el nombre ya existe, false en caso contrario
     */
    public boolean existeNombre(Usuario usuario) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE LOWER(nombre) = LOWER(?) "
                + "AND estado = true";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Verifica si ya existe un usuario con el mismo nombre excluyendo
     * el registro actual (para modificaciones).
     *
     * @param usuario objeto Usuario con el nombre y idUsuario a verificar
     * @return true si el nombre ya existe en otro registro, false en caso contrario
     */
    public boolean existeNombreExcluyendo(Usuario usuario) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE LOWER(nombre) = LOWER(?) "
                + "AND id_usuario != ? AND estado = true";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setInt(2, usuario.getIdUsuario());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Verifica si el usuario tiene registros asociados (cuentas) en la BD.
     * Se utiliza para bloquear la eliminación de usuarios con dependencias.
     *
     * @param usuario objeto Usuario con el idUsuario a verificar
     * @return true si existen registros asociados, false en caso contrario
     */
    public boolean verificarMovimientos(Usuario usuario) {
        String sql = "SELECT COUNT(*) FROM cuenta WHERE id_usuario = ? AND estado = true";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, usuario.getIdUsuario());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Carga una lista de objetos Usuario activos desde la base de datos.
     *
     * @return lista de objetos Usuario activos
     */
    public List<Usuario> cargarUsuarioList() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre, email, telefono, password, estado "
                + "FROM usuario WHERE estado = true ORDER BY id_usuario";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setPassword(rs.getString("password"));
                usuario.setEstado(rs.getBoolean("estado"));
                lista.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
