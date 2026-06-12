package datos;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase que gestiona la conexión a la base de datos PostgreSQL.
 * Capa: Datos.
 * Implementa el patrón Singleton para garantizar una única instancia
 * de conexión en toda la aplicación. Lee los parámetros de conexión
 * desde el archivo db.properties ubicado en el classpath.
 *
 * @author Alejandro Flores
 */
public class Conexion {

    private static Connection conexion = null;

    /**
     * Obtiene la instancia única de conexión a la base de datos.
     * Si la conexión no existe o está cerrada, crea una nueva leyendo
     * los parámetros desde db.properties.
     *
     * @return objeto Connection activo hacia PostgreSQL
     */
    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Properties propiedades = new Properties();
                InputStream entrada = Conexion.class.getClassLoader()
                        .getResourceAsStream("db.properties");
                if (entrada == null) {
                    System.err.println("No se encontró el archivo db.properties");
                    return null;
                }
                propiedades.load(entrada);
                String url = propiedades.getProperty("db.url");
                String usuario = propiedades.getProperty("db.user");
                String password = propiedades.getProperty("db.password");
                Class.forName("org.postgresql.Driver");
                conexion = DriverManager.getConnection(url, usuario, password);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL no encontrado: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado al conectar: " + e.getMessage());
            e.printStackTrace();
        }
        return conexion;
    }

    /**
     * Cierra la conexión activa a la base de datos si existe.
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
