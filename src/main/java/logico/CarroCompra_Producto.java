package logico;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class CarroCompra_Producto implements Serializable {


    @EmbeddedId
    private CarrocompraProductoId id;

    @ManyToOne
    @MapsId("productoId")
    @JoinColumn(name = "productoId")
    private Producto producto;


    @ManyToOne
    @MapsId("carroCompraId")
    @JoinColumn(name = "parroCompraId")
    private CarroCompra carroCompra;

    @Column()
    private int cantidad;

    public CarroCompra_Producto() {
    }/*PostTag postTag = new PostTag(this, tag);
        tags.add(postTag);
        tag.getPosts().add(postTag);*/

    public CarroCompra_Producto(CarroCompra carroCompra, int cantidad, Producto producto) {
        this.carroCompra = carroCompra;
        this.cantidad = cantidad;
        this.producto = producto;

    }
    public BigDecimal getTotal(){
        double tot = producto.getPrecio().doubleValue() * cantidad;
        BigDecimal total = BigDecimal.valueOf(tot).setScale(2);
        return total;
    }
    public int getCantidad() {

        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }
    public void agregar_cantidad(int i){
        this.cantidad+=i;
    }


    public void setProductoroductos(Producto productoroductos) {
        this.producto = productoroductos;
    }

    public CarrocompraProductoId getId() {
        return id;
    }

    public void setId(CarrocompraProductoId id) {
        this.id = id;
    }

    public CarroCompra getCarroCompra() {
        return carroCompra;
    }

    public void setCarroCompra(CarroCompra carroCompra) {
        this.carroCompra = carroCompra;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
