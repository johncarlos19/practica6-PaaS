package logico;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Mercado {
    private static Mercado mercado;
    private static ArrayList<Usuario> usuario;
    private static ArrayList<CarroCompra> carroCompras;
    private static ArrayList<VentasProductos> ventasProductos;
    private static ArrayList<Producto> productos;

    public Mercado() {
        this.usuario = new ArrayList<>();
        this.carroCompras = new ArrayList<>();
        this.ventasProductos = new ArrayList<>();
        this.productos = new ArrayList<>();
    }

    public static Mercado getInstance( ) {
        if (mercado == null)
            mercado=new Mercado();
        return mercado;
    }

    public static ArrayList<Usuario> getUsuario() {
        return usuario;
    }

    public boolean verificar_user(String user, String password){
        for (int i = 0; i < usuario.size();i++) {
            if (usuario.get(i).getUsuario().equalsIgnoreCase(user) && usuario.get(i).getPassword().equalsIgnoreCase(password)){
                return true;
            }
        }
        return false;
    }

    public void agregar_Producto(String nombre, BigDecimal precio){
        int id = productos.size();
        id+=1;
        productos.add(new Producto(id,nombre,precio));
    }
    public int cant_product(Long id){
        int cant = -1;
        for (int i=0;i<carroCompras.size();i++){
            if (carroCompras.get(i).getId()==id){
                cant = 0;
                for (int j = 0; j < carroCompras.get(i).getListaProductos().size(); j++){
                    cant += carroCompras.get(i).getListaProductos().get(j).getCantidad();
                }
                return cant;

            }
        }
        return cant;
    }

    public void procesar_compra(Long id_cliente, String nombre){
        int id = ventasProductos.size();
        id+=1;
        Calendar fecha = new GregorianCalendar();
        java.util.Date utilDate = fecha.getTime();
        VentasProductos ven = new VentasProductos(Long.parseLong(Integer.toString(id)),utilDate,nombre);
        ven.setListaProducto((ArrayList<Producto>) carroCompras.get(devuelve_cliente(id_cliente)).getListaProductos().clone());
        ventasProductos.add(ven);
    }

    public void agregar_producto_a_cliente(long id_cliente, int id_producto, int cant){
        int posi = devuelve_cliente(id_cliente);
        carroCompras.get(posi).add_producto(id_producto,cant);
    }

    public int devuelve_cliente(long id){
        int cant = -1;
        for (int i=0;i<carroCompras.size();i++){
            if (carroCompras.get(i).getId()==id){
                cant = i;
                return cant;
            }
        }
        return cant;
    }
    public boolean borrar_producto_carro(long id_cliente, int id_producto){
        int id = devuelve_cliente(id_cliente);
        for (int i=0; i<carroCompras.get(id).getListaProductos().size();i++){
            if (carroCompras.get(id).getListaProductos().get(i).getId()==id_producto){ ;
                carroCompras.get(id).getListaProductos().remove(i);
                return true;
            }
        }
        return false;
    }
    public boolean borrar_todo_carro(long id_cliente){
        int id = devuelve_cliente(id_cliente);
        for (int i=0; i<carroCompras.get(id).getListaProductos().size();i++){
            carroCompras.get(id).getListaProductos().clear();
            return true;

        }
        return false;
    }

    public int borrar_producto(int id){
        int cant = -1;
        for (int i=0;i<productos.size();i++){
            if (productos.get(i).getId()==id){ ;
                productos.remove(i);
                cant = i;
                return cant;
            }
        }
        return cant;
    }
    public Producto return_Producto(int id){
        for (int i = 0; i< productos.size();i++){
            if (productos.get(i).getId()==id){
                return productos.get(i);
            }
        }
        return null;
    }
    public void change_Producto(int id, String nombre, String precio){
        for (int i = 0; i< productos.size();i++){
            if (productos.get(i).getId()==id){
                productos.get(i).setNombre(nombre);
                productos.get(i).setPrecio(BigDecimal.valueOf(Double.parseDouble(precio)).setScale(2));
            }
        }
    }

    public static void setUsuario(ArrayList<Usuario> usuario) {
        Mercado.usuario = usuario;
    }

    public static ArrayList<CarroCompra> getCarroCompras() {
        return carroCompras;
    }

    public static void setCarroCompras(ArrayList<CarroCompra> carroCompras) {
        Mercado.carroCompras = carroCompras;
    }

    public static ArrayList<VentasProductos> getVentasProductos() {
        return ventasProductos;
    }

    public static void setVentasProductos(ArrayList<VentasProductos> ventasProductos) {
        Mercado.ventasProductos = ventasProductos;
    }

    public static ArrayList<Producto> getProductos() {
        return productos;
    }

    public static void setProductos(ArrayList<Producto> productos) {
        Mercado.productos = productos;
    }
}
