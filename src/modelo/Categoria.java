package modelo;

import datos.CategoriaMD;
import java.util.List;

/**
 * Clase que representa la entidad Categoria del sistema.
 * Capa: Modelo (PD - Proceso de Datos / Lógica de Negocio).
 * Gestiona la validación de datos y delega las operaciones de
 * persistencia a la clase CategoriaMD de la capa de datos.
 *
 * @author Joan Santacruz
 */
public class Categoria {

    private int idCategoria = 0;
    private String nombre = "";
    private boolean estado = true;

    // ── Getters ──────────────────────────────────────────────

    /**
     * Obtiene el identificador único de la categoría.
     *
     * @return idCategoria de la categoría
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * Obtiene el nombre de la categoría.
     *
     * @return nombre de la categoría
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el estado de la categoría.
     *
     * @return true si la categoría está activa, false si está inactiva
     */
    public boolean getEstado() {
        return estado;
    }

    // ── Setters (con normalización) ──────────────────────────

    /**
     * Establece el identificador de la categoría.
     *
     * @param idCategoria identificador a asignar
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Establece el nombre de la categoría aplicando trim() y toLowerCase().
     *
     * @param nombre nombre a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre.trim().toLowerCase();
    }

    /**
     * Establece el estado de la categoría.
     *
     * @param estado true para activa, false para inactiva
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // ── Métodos de Lógica de Negocio ─────────────────────────

    /**
     * Verifica que los datos de la categoría sean válidos antes de persistir.
     * Valida que el nombre no esté vacío.
     *
     * @return true si los datos son válidos, false en caso contrario
     */
    public boolean verificarDP() {
        if (nombre == null || nombre.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Reinicia todos los atributos de la categoría a sus valores por defecto.
     */
    public void limpiarDatos() {
        idCategoria = 0;
        nombre = "";
        estado = true;
    }

    /**
     * Delega la inserción de la categoría a la capa de datos.
     *
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean grabarDP() {
        CategoriaMD categoriaMD = new CategoriaMD();
        return categoriaMD.insertar(this);
    }

    /**
     * Delega la modificación de la categoría a la capa de datos.
     *
     * @return true si la modificación fue exitosa, false en caso contrario
     */
    public boolean modificarDP() {
        CategoriaMD categoriaMD = new CategoriaMD();
        return categoriaMD.modificar(this);
    }

    /**
     * Delega la eliminación lógica de la categoría a la capa de datos.
     * Cambia el estado a false en lugar de eliminar físicamente.
     *
     * @return true si la desactivación fue exitosa, false en caso contrario
     */
    public boolean borrarDP() {
        CategoriaMD categoriaMD = new CategoriaMD();
        return categoriaMD.borrar(this);
    }

    /**
     * Consulta todas las categorías activas en la base de datos.
     *
     * @return ResultSet con todas las categorías activas
     */
    public List<Categoria> buscarCategorias() {
        CategoriaMD categoriaMD = new CategoriaMD();
        return categoriaMD.consultar();
    }

    /**
     * Consulta categorías activas filtradas por los parámetros del objeto filtro.
     *
     * @param filtro objeto Categoria con los criterios de búsqueda
     * @return ResultSet con las categorías que coinciden con el filtro
     */
    public List<Categoria> buscarCategorias(Categoria filtro) {
        CategoriaMD categoriaMD = new CategoriaMD();
        return categoriaMD.consultar(filtro);
    }

    public boolean existeNombre() {
        datos.CategoriaMD md = new datos.CategoriaMD();
        return md.existeNombre(this);
    }
    public boolean existeNombreExcluyendo() {
        datos.CategoriaMD md = new datos.CategoriaMD();
        return md.existeNombreExcluyendo(this);
    }
    public boolean verificarMovimientos() {
        datos.CategoriaMD md = new datos.CategoriaMD();
        return md.verificarMovimientos(this);
    }
}
