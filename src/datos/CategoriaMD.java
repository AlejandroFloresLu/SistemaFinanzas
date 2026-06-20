package datos;

import modelo.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Categoria.
 * Capa: Datos (MD).
 * Gestiona las operaciones CRUD sobre la tabla categoria en PostgreSQL.
 *
 * @author Joan Santacruz
 */
public class CategoriaMD {

    /**
     * Inserta una nueva categoría en la base de datos.
     *
     * @param categoria objeto Categoria con los datos a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(Categoria categoria) {
        String sql = "INSERT INTO categoria (nombre, estado) VALUES (?, ?)";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, categoria.getNombre());
            ps.setBoolean(2, categoria.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Modifica los datos de una categoría existente en la base de datos.
     *
     * @param categoria objeto Categoria con los datos actualizados
     * @return true si la modificación fue exitosa, false en caso contrario
     */
    public boolean modificar(Categoria categoria) {
        String sql = "UPDATE categoria SET nombre = ? "
                + "WHERE id_categoria = ?";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getIdCategoria());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Realiza una eliminación lógica de la categoría cambiando su estado a false.
     *
     * @param categoria objeto Categoria con el idCategoria a desactivar
     * @return true si la desactivación fue exitosa, false en caso contrario
     */
    public boolean borrar(Categoria categoria) {
        String sql = "UPDATE categoria SET estado = false WHERE id_categoria = ?";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, categoria.getIdCategoria());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Consulta todas las categorías activas en la base de datos.
     *
     * @return ResultSet con todas las categorías activas
     */
    public List<Categoria> consultar() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, estado "
                + "FROM categoria WHERE estado = true ORDER BY id_categoria";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombre(rs.getString("nombre"));
                c.setEstado(rs.getBoolean("estado"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Consulta categorías activas filtradas por nombre.
     *
     * @param filtro objeto Categoria con los criterios de búsqueda
     * @return List con las categorías que coinciden con el filtro
     */
    public List<Categoria> consultar(Categoria filtro) {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, estado "
                + "FROM categoria WHERE estado = true AND nombre LIKE ? ORDER BY id_categoria";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, "%" + filtro.getNombre() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombre(rs.getString("nombre"));
                c.setEstado(rs.getBoolean("estado"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Verifica si ya existe una categoría con el mismo nombre en la base de datos.
     *
     * @param categoria objeto Categoria con el nombre a verificar
     * @return true si el nombre ya existe, false en caso contrario
     */
    public boolean existeNombre(Categoria categoria) {
        String sql = "SELECT COUNT(*) FROM categoria WHERE LOWER(nombre) = LOWER(?) "
                + "AND estado = true";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, categoria.getNombre());
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
     * Verifica si ya existe una categoría con el mismo nombre excluyendo
     * el registro actual (para modificaciones).
     *
     * @param categoria objeto Categoria con el nombre e idCategoria a verificar
     * @return true si el nombre ya existe en otro registro, false en caso contrario
     */
    public boolean existeNombreExcluyendo(Categoria categoria) {
        String sql = "SELECT COUNT(*) FROM categoria WHERE LOWER(nombre) = LOWER(?) "
                + "AND id_categoria != ? AND estado = true";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getIdCategoria());
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
     * Verifica si la categoría tiene servicios activos asociados en la BD.
     * Se utiliza para bloquear la eliminación de categorías con dependencias.
     *
     * @param categoria objeto Categoria con el idCategoria a verificar
     * @return true si existen servicios asociados, false en caso contrario
     */
    public boolean verificarMovimientos(Categoria categoria) {
        String sql = "SELECT COUNT(*) FROM servicio WHERE id_categoria = ? "
                + "AND estado = true";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, categoria.getIdCategoria());
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
     * Carga una lista de objetos Categoria activos desde la base de datos.
     *
     * @return lista de objetos Categoria activos
     */
    public List<Categoria> cargarCategoriaList() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, estado "
                + "FROM categoria WHERE estado = true ORDER BY id_categoria";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setEstado(rs.getBoolean("estado"));
                lista.add(categoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
