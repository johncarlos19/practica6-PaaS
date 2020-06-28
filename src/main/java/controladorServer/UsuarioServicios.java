package controladorServer;

import logico.Producto;
import logico.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioServicios {

    public ArrayList<Usuario> listaUsuario(){
        ArrayList<Usuario> list = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {
            //
            String query = "select * from USUARIO ";
            con = DataBaseServices.getInstancia().getConexion(); //referencia a la conexion.
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Usuario user = new Usuario(rs.getString("USER"),rs.getString("NOMBRE"),rs.getString("PASSWORD"));

                list.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }
    public boolean crearUsuario(Usuario user){
        boolean subio =false;

        Connection con = null;
        try {

            String query = "insert into USUARIO(NOMBRE, USER, PASSWORD) VALUES (?,?,?);";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, user.getNombre());
            prepareStatement.setString(2, user.getUsuario());
            prepareStatement.setString(3, user.getPassword());
            //
            int fila = prepareStatement.executeUpdate();
            subio = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return subio;
    }
    public boolean updateProducto(Usuario user){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "update USUARIO SET PASSWORD = ? where USER = ?";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, user.getPassword());
            //Indica el where...
            prepareStatement.setString(2, user.getUsuario());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }
        public Usuario getUsuario(String us) {
        Usuario user = null;
        Connection con = null;
        try {
            //utilizando los comodines (?)...
            String query = "select * from USUARIO where USER = ?";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, us);
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                user =  new Usuario(rs.getString("USER"),rs.getString("NOMBRE"),rs.getString("PASSWORD"));

            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return user;
    }
}
