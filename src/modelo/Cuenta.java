package modelo;

import datos.CuentaMD;
import java.sql.ResultSet;

/**
 * Clase que representa la entidad Cuenta del sistema financiero.
 * Capa: Modelo (PD - Proceso de Datos / Lógica de Negocio).
 * Gestiona la validación de datos y delega las operaciones de
 * persistencia a la clase CuentaMD de la capa de datos.
 *
 * @author Alejandro Flores
 */
public class Cuenta {

    private int idCuenta = 0;
    private int idUsuario = 0;
    private String tipoCuenta = "";
    private String nombre = "";
    private double saldoInicial = 0.0;
    private boolean estado = true;

    /** Tipos de cuenta válidos para el sistema. */
    public static final String[] TIPOS_CUENTA = {
            "Dinero Disponible",
            "Tarjetas de Crédito",
            "Deudas/Préstamos",
            "Inversiones/Ahorros"
    };

    // ── Getters ──────────────────────────────────────────────

    /**
     * Obtiene el identificador único de la cuenta.
     *
     * @return idCuenta de la cuenta
     */
    public int getIdCuenta() {
        return idCuenta;
    }

    /**
     * Obtiene el identificador del usuario propietario de la cuenta.
     *
     * @return idUsuario del propietario
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Obtiene el tipo de cuenta.
     *
     * @return tipoCuenta de la cuenta
     */
    public String getTipoCuenta() {
        return tipoCuenta;
    }

    /**
     * Obtiene el nombre de la cuenta.
     *
     * @return nombre de la cuenta
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el saldo inicial de la cuenta.
     *
     * @return saldoInicial de la cuenta
     */
    public double getSaldoInicial() {
        return saldoInicial;
    }

    /**
     * Obtiene el estado de la cuenta.
     *
     * @return true si la cuenta está activa, false si está inactiva
     */
    public boolean getEstado() {
        return estado;
    }

    // ── Setters (con normalización) ──────────────────────────

    /**
     * Establece el identificador de la cuenta.
     *
     * @param idCuenta identificador a asignar
     */
    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    /**
     * Establece el identificador del usuario propietario.
     *
     * @param idUsuario identificador del usuario
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Establece el tipo de cuenta.
     *
     * @param tipoCuenta tipo a asignar
     */
    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta.trim();
    }

    /**
     * Establece el nombre de la cuenta aplicando trim() y toLowerCase().
     *
     * @param nombre nombre a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre.trim().toLowerCase();
    }

    /**
     * Establece el saldo inicial de la cuenta.
     *
     * @param saldoInicial saldo a asignar
     */
    public void setSaldoInicial(double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    /**
     * Establece el estado de la cuenta.
     *
     * @param estado true para activa, false para inactiva
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // ── Métodos de Lógica de Negocio ─────────────────────────

    /**
     * Verifica que los datos de la cuenta sean válidos antes de persistir.
     * Valida que el nombre no esté vacío y que el tipo de cuenta sea uno
     * de los valores permitidos.
     *
     * @return true si los datos son válidos, false en caso contrario
     */
    public boolean verificarDP() {
        if (nombre == null || nombre.isEmpty()) {
            return false;
        }
        if (tipoCuenta == null || tipoCuenta.isEmpty()) {
            return false;
        }
        boolean tipoValido = false;
        for (String tipo : TIPOS_CUENTA) {
            if (tipoCuenta.equals(tipo)) {
                tipoValido = true;
                break;
            }
        }
        if (!tipoValido) {
            return false;
        }
        return true;
    }

    /**
     * Reinicia todos los atributos de la cuenta a sus valores por defecto.
     */
    public void limpiarDatos() {
        idCuenta = 0;
        idUsuario = 0;
        tipoCuenta = "";
        nombre = "";
        saldoInicial = 0.0;
        estado = true;
    }

    /**
     * Delega la inserción de la cuenta a la capa de datos.
     *
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean grabarDP() {
        CuentaMD cuentaMD = new CuentaMD();
        return cuentaMD.insertar(this);
    }

    /**
     * Delega la modificación de la cuenta a la capa de datos.
     *
     * @return true si la modificación fue exitosa, false en caso contrario
     */
    public boolean modificarDP() {
        CuentaMD cuentaMD = new CuentaMD();
        return cuentaMD.modificar(this);
    }

    /**
     * Delega la eliminación de la cuenta a la capa de datos.
     *
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean borrarDP() {
        CuentaMD cuentaMD = new CuentaMD();
        return cuentaMD.borrar(this);
    }
}
