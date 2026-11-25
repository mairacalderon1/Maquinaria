package cl.duoc.maquinaria.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionBD {

    // Ajustar usuario, clave y puerto según la configuración local de XAMPP.
    private static final String URL = "jdbc:mysql://localhost:3306/maquinaria";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    private ConexionBD() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
