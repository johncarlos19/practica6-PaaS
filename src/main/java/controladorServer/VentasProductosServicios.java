package controladorServer;

import logico.CarroCompra;
import logico.Producto;
import logico.VentasProductos;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentasProductosServicios {

    public ArrayList<VentasProductos> listaVentasProductos(){
        ArrayList<VentasProductos> list = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {
            //
            String query = "select * from VENTASPRODUCTO;";
            con = DataBaseServices.getInstancia().getConexion(); //referencia a la conexion.
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                VentasProductos ventasProductos = new VentasProductos(rs.getLong("ID"),rs.getDate("FECHACOMPRA"),rs.getString("NOMBRECLIENTE"));
                ventasProductos.setListaProducto(new ProductoServicios().listaProductoVentasProductos(ventasProductos.getId()));
                list.add(ventasProductos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VentasProductosServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(VentasProductosServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }
    public VentasProductos crearVentasProductos(VentasProductos ventasProductos){
        boolean subio =false;
        VentasProductos ven = null;
        Connection con = null;
        try {

            String query = "insert into VENTASPRODUCTO(FECHACOMPRA, NOMBRECLIENTE) VALUES (CURRENT_TIMESTAMP, ? );";

            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, ventasProductos.getNombreCliente());
            //
            int fila = prepareStatement.executeUpdate();
            query = "select scope_identity(), CURRENT_TIMESTAMP;";
            prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            long id = 0;
            Date dd = null;
            ven = ventasProductos;

            while(rs.next()){
                id = rs.getLong(1);
                dd = rs.getDate(2);

            }
            ven.setId(id);
            ven.setFechaCompra(dd);
            for (Producto aux: ventasProductos.getListaProducto()) {
                addProductoComprado(aux,id);

            }

        } catch (SQLException ex) {
            Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ven;
    }
    private boolean addProductoComprado(Producto produ, long ID_VentasProductos){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "insert into ProductoComprado VALUES ();";

            con = DataBaseServices.getInstancia().getConexion();
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            /*prepareStatement.setLong(1, produ.getId());
            prepareStatement.setLong(2, produ.getId());
            prepareStatement.setInt(3, produ.getCantidad());*/
            //
            int fila = prepareStatement.executeUpdate();
            query = "select scope_identity();";
            prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            long id = 0;
            while(rs.next()){
                id = rs.getLong(1);
            }
            query = "insert into VENTASPRODUCTOPRODUCTOCOMPRADO(IdVentasProducto, IdProducto, Cantidad, Nombre, Precio) VALUES ( ?, ?, ?, ?, ? );";
            con = DataBaseServices.getInstancia().getConexion();
            prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setLong(1, ID_VentasProductos);
            prepareStatement.setLong(2, id);
            prepareStatement.setInt(3, produ.getCantidad());
            prepareStatement.setString(4, produ.getNombre());
            prepareStatement.setBigDecimal(5, produ.getPrecio());
            fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }


    public long getIdentityMax(){
        long max = -1;
        Connection con = null;
        try {
            //utilizando los comodines (?)...
            String query = "select max(ID) from VENTASPRODUCTO";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                max = rs.getLong(1);
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
