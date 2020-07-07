package controladorServer;

import logico.Producto;
import logico.ProductoComprado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoCompradoServicios extends GestionadDB<ProductoComprado> {
    public ProductoCompradoServicios() {
        super(ProductoComprado.class);
    }

    /*
        public Set<ProductoComprado> listaProductoVentasProductos(long ID_ventasproductos){

            Set<ProductoComprado> list = new HashSet<>();
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
                    ProductoComprado produ = new ProductoComprado(rs.getInt(1), rs.getString(2), rs.getBigDecimal(3),rs.getInt(4));
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
    public boolean addProductoComprado(ProductoComprado productoComprado) {
        return crear(productoComprado);
    }

}
