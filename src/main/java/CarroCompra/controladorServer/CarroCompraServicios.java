package CarroCompra.controladorServer;

import CarroCompra.logico.CarroCompra;
import CarroCompra.logico.CarroCompra_Producto;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarroCompraServicios extends GestionadDB<CarroCompra> {

    public CarroCompraServicios() {
        super(CarroCompra.class);
    }

    public Set<CarroCompra> listaCarroCompra() {
        /*
        Set<CarroCompra> list = new HashSet<>();
        Connection con = null; //objeto conexion.
        try {
            //
            String query = "select * from CARROCOMPRA";
            con = DataBaseServices.getInstancia().getConexion(); //referencia a la conexion.
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                CarroCompra carroCompra = new CarroCompra(rs.getLong("ID"));
                carroCompra.setListaProductos(new ProductoServicios().listaProductoCarroCompra(carroCompra.getId()));
                list.add(carroCompra);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        return (Set<CarroCompra>) ListadoCompleto();
    }

    public boolean crearCarroCompra(CarroCompra carroCompra) {
        boolean subio = false;

        /*
        Connection con = null;
        try {

            String query = "insert into CARROCOMPRA(ID) values ( ? );";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setLong(1, carroCompra.getId());
            //
            int fila = prepareStatement.executeUpdate();
            subio = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
*/
        return crear(carroCompra);
    }
    /*
    public boolean addProductoCarroCompra(Producto produ, long ID_CarroCompra){

        boolean ok =false;

        Connection con = null;
        try {

            String query = "insert into CARROCOMPRAPRODUCTO(IDCARROCOMPRA, IDPRODUCTO, CANTIDAD) values (?, ?, ?);";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setLong(1, ID_CarroCompra);
            prepareStatement.setLong(2, produ.getId());
            prepareStatement.setInt(3, produ.getCantidad());
            //
            int fila = prepareStatement.executeUpdate();
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
    }*/

    public boolean borrarProductoCarroCompra(long id_carroCompra, long id_producto) {
        CarroCompra aux = getCarroCompra(id_carroCompra);
        for (CarroCompra_Producto ccp : aux.getListaProductos()
        ) {
            if (ccp.getProducto().getId() == id_producto) {
                new CarroCompra_ProductoServicios().getBorrar(ccp);
                aux.getListaProductos().remove(ccp);

                return true;
            }
        }

        /*
        boolean ok =false;

        Connection con = null;
        try {

            String query = "delete FROM CARROCOMPRAPRODUCTO where IDCARROCOMPRA = ? and IDPRODUCTO = ?;";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setLong(1, id_carroCompra);
            prepareStatement.setLong(2, id_producto);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/

        return false;
    }

    public boolean borrarTodoProductoCarroCompra(long id_carroCompra) {
        CarroCompra aux = getCarroCompra(id_carroCompra);
        for (CarroCompra_Producto cpp : aux.getListaProductos()
        ) {
            new CarroCompra_ProductoServicios().getBorrar(cpp);
        }
        aux.getListaProductos().clear();

        /*
        boolean ok =false;

        Connection con = null;
        try {

            String query = "delete FROM CARROCOMPRAPRODUCTO where IDCARROCOMPRA = ?;";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setLong(1, id_carroCompra);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/

        return updateCarroCompraProducto(aux);
    }

    public long getIdentityMax() {



        long valor = 0;
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("select max(cc.id) from CarroCompra cc");
            //query.setParameter("nombre", apellido+"%");
            List list = query.getResultList();
            try {
                valor = (Long) list.get(0);
                System.out.println("imprimi: "+valor+"\n\n\n\n\n");
                return valor ;
            }catch (NullPointerException e){
                return 0;
            }


        } finally {
            em.close();
        }

        /*
        Connection con = null;
        try {
            //utilizando los comodines (?)...
            String query = "select max(ID) from CARROCOMPRA";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while (rs.next()) {
                max = rs.getLong(1);
            }
            return max;

        } catch (SQLException ex) {
            Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/

    }

    public CarroCompra getCarroCompra(long id) {

        /*int carroCompra = 0;
        CarroCompra aux = null;
        Connection con = null;
        try {
            //utilizando los comodines (?)...
            String query = "select count(*) from CARROCOMPRA where ID = ?";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setLong(1, id);
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                carroCompra = rs.getInt(1);
            }
            if (carroCompra!=0){
                aux = new CarroCompra(carroCompra);
                aux.setListaProductos(new ProductoServicios().listaProductoCarroCompra(aux.getId()));
                return aux;
            }else {
                return aux;
            }

        } catch (SQLException ex) {
            Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        CarroCompra aux = find(id);
        Set<CarroCompra_Producto> ss = new CarroCompra_ProductoServicios().getListaCarroCompra_Producto(id);
        try {
            if (ss != null) {
                aux.setListaProductos(ss);
            }
            return aux;
        } catch (NullPointerException S) {
            return aux;
        }


    }

    public boolean updateCarroCompraProducto(CarroCompra aux) {

        /*
        boolean ok =false;

        Connection con = null;
        try {

            String query = "update CARROCOMPRAPRODUCTO set CANTIDAD = ? where IDPRODUCTO = ? and IDCARROCOMPRA = ?;";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setInt(1, produ.getCantidad());
            prepareStatement.setInt(2, produ.getId());
            //Indica el where...
            prepareStatement.setLong(3, id_CarroCompra);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CarroCompraServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/

        return editar(aux);
    }
}
