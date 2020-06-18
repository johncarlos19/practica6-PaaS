package logico;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentasProductos {
    private long id;
    private Date fechaCompra;
    private String nombreCliente;
    private ArrayList<Producto> listaProducto;

    public VentasProductos(long id, Date fechaCompra, String nombreCliente) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.nombreCliente = nombreCliente;
        this.listaProducto = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
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
        for (int i = 0; i< listaProducto.size() ; i++){
            total += listaProducto.get(i).getTotal().doubleValue();
        }
        return BigDecimal.valueOf(total).setScale(2);
    }

    public ArrayList<Producto> getListaProducto() {
        return listaProducto;
    }
    public List<Producto> getListaProd() {
        return listaProducto;
    }
    public void setListaProducto(ArrayList<Producto> listaProducto) {
        this.listaProducto = listaProducto;
    }
}
