package CarroCompra.controladorServer;


import CarroCompra.logico.Foto;
import CarroCompra.logico.Producto;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoServicios extends GestionadDB<Producto> {

    public ProductoServicios() {
        super(Producto.class);
    }


    public ArrayList<Producto> listaProductoCompleto () {

        return (ArrayList<Producto>) ListadoCompleto();
    }
    public ArrayList<Producto> listaProducto(int page) {

        /*Session session = sessionFactory.openSession();
Query query = sess.createQuery("From Foo");
query.setFirstResult(0);
query.setMaxResults(10);
List<Foo> fooList = fooList = query.list();*/
        /*
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
        }*/
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select cp from Producto cp");
        query.setFirstResult(0+10*(page-1));
        query.setMaxResults(10);
        //query.setParameter("nombre", apellido+"%");
        List<Producto> lista = query.getResultList();
        return (ArrayList<Producto>) lista;


    }
    /*
    public Set<Producto> listaProductoCarroCompra(long ID_carrocompra){

        Set<Producto> list = new HashSet<>();
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
    }*/


    public boolean crearProducto(Producto produ) {
        return crear(produ);
    }

    public boolean updateProducto(Producto produ) {

        /*
        boolean ok = false;

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
            ok = fila > 0;

        } catch (SQLException ex) {
            Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/

        return editar(produ);
    }

    public boolean borrarProducto(int ID) {/*
        boolean ok = false;

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
            ok = fila > 0;

            query = "delete from PRODUCTO where ID = ?";
            con = DataBaseServices.getInstancia().getConexion();
            //
            prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setInt(1, ID);
            //
            fila = prepareStatement.executeUpdate();
            ok = fila > 0;

        } catch (SQLException ex) {
            Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/

        return eliminar(ID);
    }

    public Producto getProducto(int id) {
        /*
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
                produ = new Producto(rs.getString("NOMBRE"), rs.getBigDecimal("PRECIO"));
                produ.setId(rs.getInt("ID"));

            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductoServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/

        return find(id);
    }
    public boolean deleteFoto(Producto producto, Foto foto){
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        Producto b = em.find(Producto.class, producto.getId());
        b.getFotos().remove(foto);

        em.getTransaction().commit();
        em.close();
        return true;
    }

    public int getIdentityMax() {
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
            while (rs.next()) {
                max = rs.getInt(1);
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
        }
        return -1;
    }
}

