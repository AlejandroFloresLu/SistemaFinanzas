package modelo;

import datos.ServiciosMD;
import java.sql.ResultSet;

/**
 * Clase que representa la entidad Servicio del sistema.
 * Capa: Modelo (PD - Proceso de Datos / Lógica de Negocio).
 * Gestiona la validación de datos y delega las operaciones de
 * persistencia a la clase ServiciosMD de la capa de datos.
 *
 * @author Alejandro Flores
 */
public class Servicio {

    private int idServicio = 0;
    private int idCategoria = 0;
    private String nombre = "";
    private double montoEstimado = 0.0;
    private boolean estado = true;

    // ── Getters ──────────────────────────────────────────────

    /**
     * Obtiene el identificador único del servicio.
     *
     * @return idServicio del servicio
     */
    public int getIdServicio() {
        return idServicio;
    }

    /**
     * Obtiene el identificador de la categoría a la que pertenece.
     *
     * @return idCategoria de la categoría asociada
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * Obtiene el nombre del servicio.
     *
     * @return nombre del servicio
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el monto estimado del servicio.
     *
     * @return montoEstimado del servicio
     */
    public double getMontoEstimado() {
        return montoEstimado;
    }

    /**
     * Obtiene el estado del servicio.
     *
     * @return true si el servicio está activo, false si está inactivo
     */
    public boolean getEstado() {
        return estado;
    }

    // ── Setters (con normalización) ──────────────────────────

    /**
     * Establece el identificador del servicio.
     *
     * @param idServicio identificador a asignar
     */
    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    /**
     * Establece el identificador de la categoría asociada.
     *
     * @param idCategoria identificador de la categoría
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Establece el nombre del servicio aplicando trim() y toLowerCase().
     *
     * @param nombre nombre a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre.trim().toLowerCase();
    }

    /**
     * Establece el monto estimado del servicio.
     *
     * @param montoEstimado monto a asignar
     */
    public void setMontoEstimado(double montoEstimado) {
        this.montoEstimado = montoEstimado;
    }

    /**
     * Establece el estado del servicio.
     *
     * @param estado true para activo, false para inactivo
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // ── Métodos de Lógica de Negocio ─────────────────────────

    /**
     * Verifica que los datos del servicio sean válidos antes de persistir.
     * Valida que el nombre no esté vacío y que la categoría esté asignada.
     *
     * @return true si los datos son válidos, false en caso contrario
     */
    public boolean verificarDP() {
        if (nombre == null || nombre.isEmpty()) {
            return false;
        }
        if (idCategoria <= 0) {
            return false;
        }
        return true;
    }

    /**
     * Reinicia todos los atributos del servicio a sus valores por defecto.
     */
    public void limpiarDatos() {
        idServicio = 0;
        idCategoria = 0;
        nombre = "";
        montoEstimado = 0.0;
        estado = true;
    }

    /**
     * Delega la inserción del servicio a la capa de datos.
     *
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean grabarDP() {
        ServiciosMD serviciosMD = new ServiciosMD();
        return serviciosMD.insertar(this);
    }

    /**
     * Delega la modificación del servicio a la capa de datos.
     *
     * @return true si la modificación fue exitosa, false en caso contrario
     */
    public boolean modificarDP() {
        ServiciosMD serviciosMD = new ServiciosMD();
        return serviciosMD.modificar(this);
    }

    /**
     * Delega la eliminación lógica del servicio a la capa de datos.
     * Cambia el estado a false en lugar de eliminar físicamente.
     *
     * @return true si la desactivación fue exitosa, false en caso contrario
     */
    public boolean borrarDP() {
        ServiciosMD serviciosMD = new ServiciosMD();
        return serviciosMD.borrar(this);
    }

    /**
     * Consulta todos los servicios activos en la base de datos.
     *
     * @return ResultSet con todos los servicios activos
     */
    public ResultSet buscarServicios() {
        ServiciosMD serviciosMD = new ServiciosMD();
        return serviciosMD.consultar();
    }

    /**
     * Consulta servicios activos filtrados por los parámetros del objeto filtro.
     *
     * @param filtro objeto Servicio con los criterios de búsqueda
     * @return ResultSet con los servicios que coinciden con el filtro
     */
    public ResultSet buscarServicios(Servicio filtro) {
        ServiciosMD serviciosMD = new ServiciosMD();
        return serviciosMD.consultar(filtro);
    }

    /**
     * Verifica si el servicio tiene movimientos/pagos asociados en la BD.
     * Se utiliza para bloquear la eliminación de servicios con movimientos.
     *
     * @param servicio objeto Servicio con el idServicio a verificar
     * @return true si existen pagos asociados, false en caso contrario
     */
    public boolean verificarMovimientos(Servicio servicio) {
        ServiciosMD serviciosMD = new ServiciosMD();
        return serviciosMD.verificarMovimientos(servicio);
    }
}
