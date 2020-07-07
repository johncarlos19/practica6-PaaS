package logico;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class ProductoComprado implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name = "Nombre")
    private String nombre;
    @Column(name = "Precio")
    private BigDecimal precio;
    @Column(name = "Cantidad")
    private int cantidad;

    public ProductoComprado(){

    }

    public ProductoComprado(String nombre, BigDecimal precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }
    public BigDecimal getTotal(){
        double tot = precio.doubleValue() * cantidad;
        BigDecimal total = BigDecimal.valueOf(tot).setScale(2);
        return total;
    }
    public int getId() {
        return id;
    }

    public void agregar_cantidad(int i){
        this.cantidad+=i;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        BigDecimal produ = precio.setScale(2);
        return produ;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}

