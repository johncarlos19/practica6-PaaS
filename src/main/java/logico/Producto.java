package logico;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Producto {
    private int id;
    private String nombre;
    private BigDecimal precio;
    private int cantidad;
    private BigDecimal total;

    public Producto(int id, String nombre, BigDecimal precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = 0;
    }
    public BigDecimal getTotal(){
        double tot = precio.doubleValue() * cantidad;
        total = BigDecimal.valueOf(tot).setScale(2);
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
