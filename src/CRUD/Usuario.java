package CRUD;
import static CRUD.ConexionDB.abrirConexion;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Usuario {
  
        public static boolean validarUsuario(String nombre, String contraseña) {
        String sql = "SELECT * FROM usuarios WHERE nombre = ? AND contraseña = ?";
        try (Connection conn = abrirConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, contraseña);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Devuelve true si encuentra un registro

        } catch (Exception e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }

        
 public void tablaUsuarios(JTable tbUsuarios)
    {
// Crear la tabla para visualizar los datos en tbUsuarios
       ConexionDB con = new ConexionDB();
       DefaultTableModel modelo = new DefaultTableModel();
       modelo.addColumn("ID");
       modelo.addColumn("Nombre");
       modelo.addColumn("Apellido");
       modelo.addColumn("Sueldo");
       modelo.addColumn("Contraseña");
              
//Incorporamos los elementos creados a tabla
        tbUsuarios.setModel(modelo);
        String sql = "Select * from usuarios";
        String[] datos = new String[5];
        Statement st;

//agregamos el contenido a las filas    
        try {
            st = ConexionDB.abrirConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);

                modelo.addRow(datos);
            }

            tbUsuarios.setModel(modelo);
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al cargar informacion");
        } finally {
            con.cerrarConexion();
        }
    }
       
     public void nuevoUsuario(JTextField txtNombre,JTextField txtApellido,JTextField txtSueldo,JTextField txtPassword){
     String sql = "INSERT INTO usuarios (nombre, apellido, sueldo, contraseña) VALUES (?, ?, ?, ?)";
     
         
    String nombrematches= "^[a-zA-Z\\s]+$";
    String apellidomatches = "^[a-zA-Z\\s]+$";
    String sueldomatches = "^\\d+(\\.\\d+)?$";
       
    try ( PreparedStatement pst = ConexionDB.abrirConexion().prepareStatement(sql)) {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String sueldo = txtSueldo.getText().trim();
        String password = txtPassword.getText().trim();

        //Validamos que no haya campos vacios 
        if (nombre.isEmpty() || apellido.isEmpty() || sueldo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Favor de llenar todos los campos");
            return;
            
         //Utilisamos matche para validar los datos que se ingresan sean correctos   
         }else if(!nombre.matches(nombrematches)||!apellido.matches(apellidomatches)||!sueldo.matches(sueldomatches)){
            JOptionPane.showMessageDialog(null, "Favor de ingresar datos validos en los campos");
            return;
         }
         
        pst.setString(1, nombre);
        pst.setString(2, apellido);
        pst.setString(3, sueldo);
        pst.setString(4, password);
        pst.execute();
        
        JOptionPane.showMessageDialog(null, "Usuario guardado");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al ingresar datos");
        // Registra el error completo para fines de depuración
        System.err.println(e.getMessage());
    }
}
        
public void seleccionarUsuario(JTable tbUsuarios,JTextField txtID,JTextField txtNombre,JTextField txtApellido,JTextField txtSueldo,JTextField txtPassword){
 
     try {
         int fila = tbUsuarios.getSelectedRow();
         if(fila >=0){
        
             txtID.setText(tbUsuarios.getValueAt(fila, 0).toString());
             txtNombre.setText(tbUsuarios.getValueAt(fila, 1).toString());
             txtApellido.setText(tbUsuarios.getValueAt(fila, 2).toString());
             txtSueldo.setText(tbUsuarios.getValueAt(fila, 3).toString());
             txtPassword.setText(tbUsuarios.getValueAt(fila, 4).toString()); 
         }
         
     }catch (Exception e) {
         JOptionPane.showMessageDialog(null, "Error al seleccionar datos"+e.toString());
     }
}

    public void editarUsuario(JTextField txtID,JTextField txtNombre, JTextField txtApellido, JTextField txtSueldo, JTextField txtPassword){

        ConexionDB con = new ConexionDB();
        String sql = "update usuarios set nombre=?,apellido=?,sueldo=?,contraseña=? where id=?";

        try {
            PreparedStatement pst = ConexionDB.abrirConexion().prepareStatement(sql);
            
            
            pst.setString(1, txtNombre.getText());
            pst.setString(2, txtApellido.getText());
            pst.setString(3, txtSueldo.getText());
            pst.setString(4, txtPassword.getText());
            pst.setString(5, txtID.getText());

            pst.execute();
            JOptionPane.showMessageDialog(null, "Usuario actualizado");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "No se guardo el resgristo");
        } finally {
            con.cerrarConexion();
        }}

    
    public void buscarUsuario(JTextField txtID,JTextField txtNombre, JTextField txtApellido,
    JTextField txtSueldo, JTextField txtPassword, JTextField txtBuscar) throws SQLException{
   
      String sql = "SELECT * FROM usuarios WHERE nombre LIKE ? COLLATE NOCASE";
        try (Connection conn = abrirConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
         ps.setString(1, txtBuscar.getText());
         ResultSet rs = ps.executeQuery();
        
         if(rs.next()){
             txtID.setText(rs.getString("ID"));
             txtNombre.setText(rs.getString("nombre"));
             txtApellido.setText(rs.getString("apellido"));
             txtSueldo.setText(rs.getString("sueldo"));
             txtPassword.setText(rs.getString("contraseña"));
            }
        
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "No se encontro registro");
        }
     
    }
    
 public void borrarUsuario(JTextField txtID){
         ConexionDB con = new ConexionDB();
         String sql = "delete from usuarios where id=?";
         
           try(PreparedStatement pst = ConexionDB.abrirConexion().prepareStatement(sql);) {
            pst.setString(1, txtID.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Usuario Borrado");
        
           } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se borro el usuario");
        } finally {
            con.cerrarConexion();
        }}

   public void limpiarFormulario(JTextField txtID,JTextField txtNombre, JTextField txtApellido, JTextField txtSueldo, JTextField txtPassword){
    txtID.setText("");
    txtNombre.setText("");
    txtApellido.setText("");
    txtSueldo.setText("");
    txtPassword.setText("");
}

   
   
}





