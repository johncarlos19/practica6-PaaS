package controladorServer;


import logico.CarroCompra_Producto;
import logico.Foto;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 */
public class FotoServices extends GestionadDB<Foto> {

    private static FotoServices instancia;

    private FotoServices(){
        super(Foto.class);
    }

    public static FotoServices getInstancia(){
        if(instancia==null){
            instancia = new FotoServices();
        }
        return instancia;
    }
/*
    public boolean getBorrar(Foto foto) {
        ;
        EntityManager em = getEntityManager();
        try {


            Query query = em.createQuery("delete from Foto pf where pf.id = :id");
            //Long carroCompra_id = new Long(carroCompra_producto.getCarroCompra_id());
            // Long producto_id = new Long(carroCompra_producto.getProducto_id());

            query.setParameter("id", foto.getId());
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

    }*/

}
