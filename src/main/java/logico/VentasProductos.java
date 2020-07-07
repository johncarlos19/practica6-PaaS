package logico;

import controladorServer.VentasProductosServicios;
import jdk.jfr.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.sql.Date ;
import java.util.List;
import java.util.Set;

@Entity
public class VentasProductos  implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @Column(name = "FechaCompra", nullable = false)
    private java.util.Date fechaCompra;
    @Column(name = "NombreCliente")
    private String nombreCliente;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<ProductoComprado> listaProducto;


    public VentasProductos(){

    }
    public VentasProductos(String nombreCliente) {
        this.fechaCompra = new VentasProductosServicios().getDateCurrent();
        this.nombreCliente = nombreCliente;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public java.util.Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(java.util.Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public BigDecimal getTotalAcumulado(){
        double total = 0;
        //et(i).getTotal().doubleValue();
        for (ProductoComprado aux: listaProducto) {
            total+= aux.getTotal().doubleValue();
        }
        return BigDecimal.valueOf(total).setScale(2);
    }

    public Set<ProductoComprado> getListaProducto() {
        return listaProducto;
    }

    public void setListaProducto(Set<ProductoComprado> listaProducto) {
        this.listaProducto = listaProducto;
    }
}
