package controladorServer;


import logico.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoServicios {

    public ArrayList<Producto> listaProducto(){
        ArrayList<Producto> list = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {
            //
            String query = "select * from PRODUCTO ";
            con = DataBaseServices.getInstancia().getConexion(); //referencia a la conexion.
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Producto produ = new Producto(rs.getInt("ID"), rs.getString("NOMBRE"), rs.getBigDecimal("PRECIO"));

                list.add(produ);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }
    public ArrayList<Producto> listaProductoCarroCompra(long ID_carrocompra){
        ArrayList<Producto> list = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {
            //
            String query = "select pr.ID, pr.NOMBRE ,pr.PRECIO, cpr.CANTIDAD from PRODUCTO pr, CARROCOMPRAPRODUCTO cpr, CARROCOMPRA c where ? = cpr.IDCARROCOMPRA and cpr.IDPRODUCTO = pr.ID group by pr.ID, pr.NOMBRE, pr.PRECIO, cpr.CANTIDAD;";
            con = DataBaseServices.getInstancia().getConexion(); //referencia a la conexion.
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setLong(1, ID_carrocompra);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Producto produ = new Producto(rs.getInt(1), rs.getString(2), rs.getBigDecimal(3));
                produ.setCantidad(rs.getInt(4));
                list.add(produ);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }
    public ArrayList<Producto> listaProductoVentasProductos(long ID_ventasproductos){
        ArrayList<Producto> list = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {
            //
            String query = "select vpc.IdProducto, vpc.Nombre, vpc.Precio, vpc.Cantidad " +
                    "from VENTASPRODUCTO vp, VENTASPRODUCTOPRODUCTOCOMPRADO vpc, PRODUCTOCOMPRADO pc " +
                    "where ? = vpc.IDVENTASPRODUCTO and vpc.IDPRODUCTO = pc.ID group by vpc.IdProducto, vpc.Nombre, vpc.Precio, vpc.Cantidad;";
            con = DataBaseServices.getInstancia().getConexion(); //referencia a la conexion.
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setLong(1, ID_ventasproductos);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Producto produ = new Producto(rs.getInt(1), rs.getString(2), rs.getBigDecimal(3));
                produ.setCantidad(rs.getInt(4));
                list.add(produ);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }

    public boolean crearProducto(Producto produ){
        boolean subio =false;

        Connection con = null;
        try {

            String query = "INSERT INTO PRODUCTO(ID,NOMBRE, PRECIO) VALUES (?,?,?);";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setInt(1, produ.getId());
            prepareStatement.setString(2, produ.getNombre());
            prepareStatement.setBigDecimal(3, produ.getPrecio());
            //
            int fila = prepareStatement.executeUpdate();
            subio = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return subio;
    }
    public boolean updateProducto(Producto produ){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "update PRODUCTO SET NOMBRE = ?, PRECIO = ? where ID = ?";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, produ.getNombre());
            prepareStatement.setBigDecimal(2, produ.getPrecio());
            //Indica el where...
            prepareStatement.setInt(3, produ.getId());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean borrarProducto(int ID){
        boolean ok =false;

        Connection con = null;
        try {
            String query = "delete FROM CARROCOMPRAPRODUCTO where IDPRODUCTO = ?;";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setInt(1, ID);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

            query = "delete from PRODUCTO where ID = ?";
            con = DataBaseServices.getInstancia().getConexion();
            //
            prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setInt(1, ID);
            //
            fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public Producto getProducto(int id) {
        Producto produ = null;
        Connection con = null;
        try {
            //utilizando los comodines (?)...
            String query = "select * from PRODUCTO where ID = ?";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setInt(1, id);
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                produ = new Producto(rs.getInt("ID"), rs.getString("NOMBRE"), rs.getBigDecimal("PRECIO"));

            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return produ;
    }
    public int getIdentityMax(){
        int max = -1;
        Connection con = null;
        try {
            //utilizando los comodines (?)...
            String query = "select max(ID) from PRODUCTO";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                max = rs.getInt(1);
            }
            return max;

        } catch (SQLException ex) {
            Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }
}

