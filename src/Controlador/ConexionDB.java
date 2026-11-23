package Controlador;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mariadb://localhost:3306/PasteleriaLaConchita";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "snk2001";

    public static Connection conectar() {
        Connection conexion = null;

        try {
            Class.forName("org.mariadb.jdbc.Driver");

            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);

            System.out.println("Conexión exitosa a MariaDB");

        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver de MariaDB");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos");
            e.printStackTrace();
        }

        return conexion;
    }
}