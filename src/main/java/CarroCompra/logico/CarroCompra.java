package CarroCompra.logico;

import CarroCompra.controladorServer.ProductoCompradoServicios;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CarroCompra implements Serializable {
    @Id
    private long id;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CarroCompra_Producto> carroCompra_productos = new HashSet<CarroCompra_Producto>();


    public CarroCompra() {
    }

    public CarroCompra(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public CarroCompra_Producto add_producto(int id_produ, int cantidad){
        boolean enco = false;
        CarroCompra_Producto rett = null;
        for (CarroCompra_Producto aux: carroCompra_productos
             ) {
            if (aux.getProducto().getId() == id_produ){
                aux.agregar_cantidad(cantidad);
                aux.setId(new CarrocompraProductoId(id,aux.getProducto().getId()));
                //new CarroCompraServicios().updateCarroCompraProducto(aux,id);
                //
                enco = true;
                rett = aux;
                System.out.println("Agrego exitente");
                break;
            }
        }
        /*PostTag postTag = new PostTag(this, tag);
        tags.add(postTag);
        tag.getPosts().add(postTag);*/
        if(enco == false){

            Producto produ = new Producto(Mercado.getInstance().return_Producto(id_produ).getNombre(), Mercado.getInstance().return_Producto(id_produ).getPrecio());
            produ.setId(Mercado.getInstance().return_Producto(id_produ).getId());

            CarroCompra_Producto can = new CarroCompra_Producto(this ,0,produ);
            produ.getCarroCompra_productos().add(can);
            carroCompra_productos.add(can);
            for (CarroCompra_Producto aux: carroCompra_productos
            ) {
                if (aux.getProducto().getId() == id_produ){
                    aux.agregar_cantidad(cantidad);
                    aux.setId(new CarrocompraProductoId(id,aux.getProducto().getId()));
                   // new CarroCompraServicios().updateCarroCompraProducto(aux,id);

                    rett = aux;
                    enco = true;
                    System.out.println("Agrego nuevo");
                    break;
                }
            }
        }
        return rett;
    }

    public void setId(long id) {
        this.id = id;
    }


    public BigDecimal getTotalAcumulado(){
        double total = 0;
        for (CarroCompra_Producto aux: carroCompra_productos
             ) {
            total += aux.getTotal().doubleValue();
        }
        return BigDecimal.valueOf(total).setScale(2);
    }

    public Set<CarroCompra_Producto> getListaProductos() {
        return carroCompra_productos;
    }

    public Set<ProductoComprado> getListaProductos_PreComprado() {
        Set<ProductoComprado> aux = new HashSet<>();
        for (CarroCompra_Producto pro: carroCompra_productos
             ) {
            ProductoComprado pp = new ProductoComprado(pro.getProducto().getNombre(),pro.getProducto().getPrecio(),pro.getCantidad());
            new ProductoCompradoServicios().addProductoComprado(pp);
            aux.add(pp);
        }
        return aux;
    }

    public void setListaProductos(Set<CarroCompra_Producto> listaProductos) {
        this.carroCompra_productos = listaProductos;
    }
}
