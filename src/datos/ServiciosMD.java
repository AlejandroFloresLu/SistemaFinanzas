package datos;

import modelo.Servicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Servicio.
 * Capa: Datos (MD).
 * Gestiona las operaciones CRUD sobre la tabla servicio en PostgreSQL.
 * La validación de nombre duplicado se realiza dentro de la categoría
 * seleccionada.
 *
 * @author Alejandro Flores
 */
public class ServiciosMD {

    /**
     * Inserta un nuevo servicio en la base de datos.
     *
     * @param servicio objeto Servicio con los datos a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(Servicio servicio) {
        String sql = "INSERT INTO servicio (id_categoria, nombre, monto_estimado, estado) "
                + "VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, servicio.getIdCategoria());
            ps.setString(2, servicio.getNombre());
            ps.setDouble(3, servicio.getMontoEstimado());
            ps.setBoolean(4, servicio.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Modifica los datos de un servicio existente en la base de datos.
     *
     * @param servicio objeto Servicio con los datos actualizados
     * @return true si la modificación fue exitosa, false en caso contrario
     */
    public boolean modificar(Servicio servicio) {
        String sql = "UPDATE servicio SET id_categoria = ?, nombre = ?, "
                + "monto_estimado = ? WHERE id_servicio = ?";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, servicio.getIdCategoria());
            ps.setString(2, servicio.getNombre());
            ps.setDouble(3, servicio.getMontoEstimado());
            ps.setInt(4, servicio.getIdServicio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Realiza una eliminación lógica del servicio cambiando su estado a false.
     *
     * @param servicio objeto Servicio con el idServicio a desactivar
     * @return true si la desactivación fue exitosa, false en caso contrario
     */
    public boolean borrar(Servicio servicio) {
        String sql = "UPDATE servicio SET estado = false WHERE id_servicio = ?";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, servicio.getIdServicio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Consulta todos los servicios activos en la base de datos,
     * incluyendo el nombre de la categoría asociada.
     *
     * @return ResultSet con todos los servicios activos
     */
    public ResultSet consultar() {
        String sql = "SELECT s.id_servicio, s.id_categoria, s.nombre, "
                + "s.monto_estimado, s.estado, c.nombre AS nombre_categoria "
                + "FROM servicio s "
                + "INNER JOIN categoria c ON s.id_categoria = c.id_categoria "
                + "WHERE s.estado = true ORDER BY s.id_servicio";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Consulta servicios activos filtrados por categoría y/o nombre.
     *
     * @param filtro objeto Servicio con los criterios de búsqueda
     * @return ResultSet con los servicios que coinciden con el filtro
     */
    public ResultSet consultar(Servicio filtro) {
        StringBuilder sql = new StringBuilder(
                "SELECT s.id_servicio, s.id_categoria, s.nombre, "
                        + "s.monto_estimado, s.estado, c.nombre AS nombre_categoria "
                        + "FROM servicio s "
                        + "INNER JOIN categoria c ON s.id_categoria = c.id_categoria "
                        + "WHERE s.estado = true");
        List<Object> parametros = new ArrayList<>();

        if (filtro.getIdCategoria() > 0) {
            sql.append(" AND s.id_categoria = ?");
            parametros.add(filtro.getIdCategoria());
        }
        if (filtro.getNombre() != null && !filtro.getNombre().isEmpty()) {
            sql.append(" AND s.nombre LIKE ?");
            parametros.add("%" + filtro.getNombre() + "%");
        }
        sql.append(" ORDER BY s.id_servicio");

        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql.toString());
            for (int i = 0; i < parametros.size(); i++) {
                Object param = parametros.get(i);
                if (param instanceof Integer) {
                    ps.setInt(i + 1, (Integer) param);
                } else {
                    ps.setString(i + 1, (String) param);
                }
            }
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verifica si ya existe un servicio con el mismo nombre dentro de
     * la misma categoría.
     *
     * @param servicio objeto Servicio con el nombre e idCategoria a verificar
     * @return true si el nombre ya existe en la categoría, false en caso contrario
     */
    public boolean existeNombre(Servicio servicio) {
        String sql = "SELECT COUNT(*) FROM servicio WHERE LOWER(nombre) = LOWER(?) "
                + "AND id_categoria = ? AND estado = true";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, servicio.getNombre());
            ps.setInt(2, servicio.getIdCategoria());
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
     * Verifica si ya existe un servicio con el mismo nombre dentro de
     * la misma categoría, excluyendo el registro actual (para modificaciones).
     *
     * @param servicio objeto Servicio con el nombre, idCategoria e idServicio
     * @return true si el nombre ya existe en otro registro, false en caso contrario
     */
    public boolean existeNombreExcluyendo(Servicio servicio) {
        String sql = "SELECT COUNT(*) FROM servicio WHERE LOWER(nombre) = LOWER(?) "
                + "AND id_categoria = ? AND id_servicio != ? AND estado = true";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, servicio.getNombre());
            ps.setInt(2, servicio.getIdCategoria());
            ps.setInt(3, servicio.getIdServicio());
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
     * Verifica si el servicio tiene movimientos/pagos asociados en la BD.
     * Se utiliza para bloquear la eliminación de servicios con movimientos.
     *
     * @param servicio objeto Servicio con el idServicio a verificar
     * @return true si existen pagos asociados, false en caso contrario
     */
    public boolean verificarMovimientos(Servicio servicio) {
        String sql = "SELECT COUNT(*) FROM pago WHERE id_servicio = ?";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, servicio.getIdServicio());
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
     * Carga una lista de objetos Servicio activos desde la base de datos.
     *
     * @return lista de objetos Servicio activos
     */
    public List<Servicio> cargarServicioList() {
        List<Servicio> lista = new ArrayList<>();
        String sql = "SELECT id_servicio, id_categoria, nombre, monto_estimado, estado "
                + "FROM servicio WHERE estado = true ORDER BY id_servicio";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setIdServicio(rs.getInt("id_servicio"));
                servicio.setIdCategoria(rs.getInt("id_categoria"));
                servicio.setNombre(rs.getString("nombre"));
                servicio.setMontoEstimado(rs.getDouble("monto_estimado"));
                servicio.setEstado(rs.getBoolean("estado"));
                lista.add(servicio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
