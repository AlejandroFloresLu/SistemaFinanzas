package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Conexion {

    private String archivo = "db.properties";
    private static Connection connection;

    private Conexion() {
        // Constructor privado según el diagrama: -Conexion()
    }

    private Properties cargarPropiedades() {
        Properties props = new Properties();
        // Leemos desde src/db.properties porque el archivo está ahí en tu proyecto
        try (InputStream in = new FileInputStream("src/" + this.archivo)) {
            props.load(in);
        } catch (Exception e) {
            System.out.println("Error al cargar propiedades: " + e.getMessage());
        }
        return props;
    }

    public static Connection getConexion() {
        if (connection == null) {
            try {
                Conexion instancia = new Conexion();
                Properties props = instancia.cargarPropiedades();
                
                connection = DriverManager.getConnection(
                        props.getProperty("db.url"), 
                        props.getProperty("db.user"), 
                        props.getProperty("db.password")
                );
            } catch (Exception e) {
                System.out.println("Error de conexión: " + e.getMessage());
            }
        }
        return connection;
    }
}
