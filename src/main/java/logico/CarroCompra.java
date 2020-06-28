package logico;

import controladorServer.CarroCompraServicios;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CarroCompra {
    private long id;
    private ArrayList<Producto> listaProductos;

    public CarroCompra(long id) {
        this.id = id;
        this.listaProductos = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void add_producto(int id_produ, int cantidad){
        boolean enco = false;
        for (int i = 0; i<listaProductos.size();i++){
            if (listaProductos.get(i).getId() == id_produ){
                listaProductos.get(i).agregar_cantidad(cantidad);
                new CarroCompraServicios().updateCarroCompraProducto(listaProductos.get(i),id);
                enco = true;
                break;
            }
        }
        if(enco == false){
            Producto produ = new Producto(Mercado.getInstance().return_Producto(id_produ).getId(), Mercado.getInstance().return_Producto(id_produ).getNombre(), Mercado.getInstance().return_Producto(id_produ).getPrecio());
            listaProductos.add(produ);
            for (int i = 0; i<listaProductos.size();i++){
                if (listaProductos.get(i).getId() == id_produ){
                    listaProductos.get(i).agregar_cantidad(cantidad);
                    new CarroCompraServicios().addProductoCarroCompra(listaProductos.get(i),id);
                    enco = true;
                    break;
                }
            }
        }
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }
    public BigDecimal getTotalAcumulado(){
        double total = 0;
        for (int i = 0; i< listaProductos.size() ; i++){
            total += listaProductos.get(i).getTotal().doubleValue();
        }
        return BigDecimal.valueOf(total).setScale(2);
    }
    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }
}
