package CarroCompra.controladorServer;

import CarroCompra.logico.CarroCompra_Producto;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.Set;

public class CarroCompra_ProductoServicios extends GestionadDB<CarroCompra_Producto> {
    public CarroCompra_ProductoServicios() {
        super(CarroCompra_Producto.class);
    }

    public boolean getAgregar(CarroCompra_Producto carroCompra_producto) {
        return crear(carroCompra_producto);
    }

    public boolean getEditar(CarroCompra_Producto carroCompra_producto) {
        return editar(carroCompra_producto);
    }

    public boolean getdelete(@NotNull CarroCompra_Producto carroCompra_producto) {
        return eliminar(carroCompra_producto.getId());
    }

    public boolean getBorrar(@NotNull CarroCompra_Producto carroCompra_producto) {
        ;
        EntityManager em = getEntityManager();
        try {

            Query query = em.createQuery("delete from CarroCompra_Producto cp where cp.id.carroCompraId = :carroCompraId and cp.id.productoId = :productoId");
            //Long carroCompra_id = new Long(carroCompra_producto.getCarroCompra_id());
            // Long producto_id = new Long(carroCompra_producto.getProducto_id());

            query.setParameter("carroCompraId", carroCompra_producto.getId().getcarroCompraId());
            query.setParameter("productoId", carroCompra_producto.getId().getproductoId());
            em.getTransaction().begin();
            query.executeUpdate();
            em.getTransaction().commit();
            em.close();
            //query.setParameter("producto_id", producto_id);
            //
            return true;
        } finally {
            em.close();
        }

    }
    public boolean getBorrarProductoATodoLosClientes(int ID) {
        ;
        EntityManager em = getEntityManager();
        try {

            Query query = em.createQuery("delete from CarroCompra_Producto cp where cp.id.productoId = :productoId");
            //Long carroCompra_id = new Long(carroCompra_producto.getCarroCompra_id());
            // Long producto_id = new Long(carroCompra_producto.getProducto_id());

            query.setParameter("productoId", ID);
            em.getTransaction().begin();
            query.executeUpdate();
            em.getTransaction().commit();
            em.close();
            //query.setParameter("producto_id", producto_id);
            //
            return true;
        } finally {
            em.close();
        }

    }

    public Set<CarroCompra_Producto> getListaCarroCompra_Producto(Long id_cliente) {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("select cp from CarroCompra_Producto cp where cp.id.carroCompraId = :carroCompraId");
        query.setParameter("carroCompraId", id_cliente);
        //query.setParameter("nombre", apellido+"%");
        Set<CarroCompra_Producto> lista = new HashSet<CarroCompra_Producto>(query.getResultList());
        return lista;


    }

}
