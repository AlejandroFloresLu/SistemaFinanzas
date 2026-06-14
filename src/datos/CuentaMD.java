package datos;

import modelo.Cuenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Cuenta.
 * Capa: Datos (MD).
 * Gestiona las operaciones CRUD sobre la tabla cuenta en PostgreSQL.
 * Todas las consultas filtran por idUsuario del usuario autenticado.
 *
 * @author Alejandro Flores
 */
public class CuentaMD {

    /**
     * Inserta una nueva cuenta en la base de datos.
     *
     * @param cuenta objeto Cuenta con los datos a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(Cuenta cuenta) {
        String sql = "INSERT INTO cuenta (id_usuario, tipo_cuenta, nombre, saldo_inicial, estado) "
                + "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, cuenta.getIdUsuario());
            ps.setString(2, cuenta.getTipoCuenta());
            ps.setString(3, cuenta.getNombre());
            ps.setDouble(4, cuenta.getSaldoInicial());
            ps.setBoolean(5, cuenta.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Modifica los datos de una cuenta existente en la base de datos.
     *
     * @param cuenta objeto Cuenta con los datos actualizados
     * @return true si la modificación fue exitosa, false en caso contrario
     */
    public boolean modificar(Cuenta cuenta) {
        String sql = "UPDATE cuenta SET tipo_cuenta = ?, nombre = ?, saldo_inicial = ? "
                + "WHERE id_cuenta = ?";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, cuenta.getTipoCuenta());
            ps.setString(2, cuenta.getNombre());
            ps.setDouble(3, cuenta.getSaldoInicial());
            ps.setInt(4, cuenta.getIdCuenta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una cuenta de la base de datos (eliminación física).
     * Solo se permite si la cuenta no tiene movimientos asociados.
     *
     * @param cuenta objeto Cuenta con el idCuenta a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean borrar(Cuenta cuenta) {
        String sql = "DELETE FROM cuenta WHERE id_cuenta = ?";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, cuenta.getIdCuenta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Consulta todas las cuentas activas de un usuario específico.
     *
     * @param idUsuario identificador del usuario autenticado
     * @return ResultSet con todas las cuentas del usuario
     */
    public ResultSet consultar(int idUsuario) {
        String sql = "SELECT id_cuenta, id_usuario, tipo_cuenta, nombre, saldo_inicial, estado "
                + "FROM cuenta WHERE id_usuario = ? AND estado = true ORDER BY id_cuenta";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Consulta todas las cuentas activas (sin filtro de usuario).
     *
     * @return ResultSet con todas las cuentas activas
     */
    public ResultSet consultar() {
        String sql = "SELECT id_cuenta, id_usuario, tipo_cuenta, nombre, saldo_inicial, estado "
                + "FROM cuenta WHERE estado = true ORDER BY id_cuenta";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Consulta cuentas activas del usuario filtradas por tipo de cuenta y/o nombre.
     *
     * @param filtro objeto Cuenta con los criterios de búsqueda (incluye idUsuario)
     * @return ResultSet con las cuentas que coinciden con el filtro
     */
    public ResultSet consultar(Cuenta filtro) {
        StringBuilder sql = new StringBuilder(
                "SELECT id_cuenta, id_usuario, tipo_cuenta, nombre, saldo_inicial, estado "
                        + "FROM cuenta WHERE id_usuario = ? AND estado = true");
        List<Object> parametros = new ArrayList<>();
        parametros.add(filtro.getIdUsuario());

        if (filtro.getTipoCuenta() != null && !filtro.getTipoCuenta().isEmpty()) {
            sql.append(" AND tipo_cuenta = ?");
            parametros.add(filtro.getTipoCuenta());
        }
        if (filtro.getNombre() != null && !filtro.getNombre().isEmpty()) {
            sql.append(" AND nombre LIKE ?");
            parametros.add("%" + filtro.getNombre() + "%");
        }
        sql.append(" ORDER BY id_cuenta");

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
     * Verifica si ya existe una cuenta con el mismo nombre para el usuario dado.
     *
     * @param cuenta objeto Cuenta con el nombre e idUsuario a verificar
     * @return true si el nombre ya existe, false en caso contrario
     */
    public boolean existeNombre(Cuenta cuenta) {
        String sql = "SELECT COUNT(*) FROM cuenta WHERE LOWER(nombre) = LOWER(?) "
                + "AND id_usuario = ? AND estado = true";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, cuenta.getNombre());
            ps.setInt(2, cuenta.getIdUsuario());
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
     * Verifica si ya existe una cuenta con el mismo nombre para el usuario dado,
     * excluyendo el registro actual (para modificaciones).
     *
     * @param cuenta objeto Cuenta con el nombre, idCuenta e idUsuario a verificar
     * @return true si el nombre ya existe en otro registro, false en caso contrario
     */
    public boolean existeNombreExcluyendo(Cuenta cuenta) {
        String sql = "SELECT COUNT(*) FROM cuenta WHERE LOWER(nombre) = LOWER(?) "
                + "AND id_usuario = ? AND id_cuenta != ? AND estado = true";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setString(1, cuenta.getNombre());
            ps.setInt(2, cuenta.getIdUsuario());
            ps.setInt(3, cuenta.getIdCuenta());
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
     * Verifica si la cuenta tiene movimientos/pagos asociados en la BD.
     * Se utiliza para bloquear la eliminación de cuentas con movimientos.
     *
     * @param cuenta objeto Cuenta con el idCuenta a verificar
     * @return true si existen pagos asociados, false en caso contrario
     */
    public boolean verificarMovimientos(Cuenta cuenta) {
        String sql = "SELECT COUNT(*) FROM pago WHERE id_cuenta = ?";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, cuenta.getIdCuenta());
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
     * Carga una lista de objetos Cuenta activos para un usuario específico.
     *
     * @param idUsuario identificador del usuario autenticado
     * @return lista de objetos Cuenta activos del usuario
     */
    public List<Cuenta> cargarCuentaList(int idUsuario) {
        List<Cuenta> lista = new ArrayList<>();
        String sql = "SELECT id_cuenta, id_usuario, tipo_cuenta, nombre, saldo_inicial, estado "
                + "FROM cuenta WHERE id_usuario = ? AND estado = true ORDER BY id_cuenta";
        try {
            PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setIdCuenta(rs.getInt("id_cuenta"));
                cuenta.setIdUsuario(rs.getInt("id_usuario"));
                cuenta.setTipoCuenta(rs.getString("tipo_cuenta"));
                cuenta.setNombre(rs.getString("nombre"));
                cuenta.setSaldoInicial(rs.getDouble("saldo_inicial"));
                cuenta.setEstado(rs.getBoolean("estado"));
                lista.add(cuenta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
