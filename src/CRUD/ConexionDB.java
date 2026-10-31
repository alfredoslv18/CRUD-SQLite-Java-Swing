package CRUD;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionDB {

    public static Connection abrirConexion() {
        Connection conn = null;
        try {

            String url = "jdbc:sqlite:abc.db";// Conectar a la base de datos SQLite
            conn = DriverManager.getConnection(url);
          

        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conn;
    }
    
    
public void cerrarConexion() {
        Connection conn = null;
        try {
            if (conn != null) {
                conn.close();
                JOptionPane.showMessageDialog(null, "Conexion cerrada correctamente");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se cerro la conexion");
        }
    }

}


