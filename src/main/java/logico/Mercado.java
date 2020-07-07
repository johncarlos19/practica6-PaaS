package logico;

import controladorServer.*;

import java.math.BigDecimal;
import java.util.*;

public class Mercado {
    private static ProductoServicios productoServicios = new ProductoServicios();
    private static UsuarioServicios usuarioServicios = new UsuarioServicios();
    private static VentasProductosServicios ventasProductosServicios = new VentasProductosServicios();
    private static CarroCompraServicios carroCompraServicios = new CarroCompraServicios();
    private static CarroCompra_ProductoServicios carroCompra_productoServicios = new CarroCompra_ProductoServicios();
    private static Mercado mercado;

    public Mercado() {
    }

    public static Mercado getInstance() {
        if (mercado == null)
            mercado = new Mercado();
        return mercado;
    }

    public void loadDataBase() {

        Usuario aux = new UsuarioServicios().getUsuario("admin");
        try {
            if (verificar_user(aux.getNombre(), aux.getPassword()) == false) {
                new UsuarioServicios().crearUsuario(new Usuario("admin", "Admin", "admin"));
            }
        } catch (NullPointerException e) {
            new UsuarioServicios().crearUsuario(new Usuario("admin", "Admin", "admin"));
            Producto pp = new Producto("Pan", BigDecimal.valueOf(50.00).setScale(2));
            pp.setDescripcion("Bueno");
            new ProductoServicios().crearProducto(pp);
            Producto dd = new Producto("Pizza", BigDecimal.valueOf(500.00).setScale(2));
            dd.setDescripcion("Malo");
            new ProductoServicios().crearProducto(dd);
            Producto ddc = new Producto("Agua", BigDecimal.valueOf(500.00).setScale(2));
            ddc.setDescripcion("Malo");
            new ProductoServicios().crearProducto(ddc);
            Producto ddr = new Producto("Leche", BigDecimal.valueOf(500.00).setScale(2));
            ddr.setDescripcion("Malo");
            new ProductoServicios().crearProducto(ddr);
            Producto dde = new Producto("Pipian", BigDecimal.valueOf(500.00).setScale(2));
            dde.setDescripcion("Malo");
            new ProductoServicios().crearProducto(dde);
            Producto ddet = new Producto("Lodo", BigDecimal.valueOf(500.00).setScale(2));
            ddet.setDescripcion("Malo");
            new ProductoServicios().crearProducto(ddet);
            Producto dd4 = new Producto("Burrito", BigDecimal.valueOf(500.00).setScale(2));
            dd4.setDescripcion("Malo");
            new ProductoServicios().crearProducto(dd4);
            Producto ddrt = new Producto("Estoy Jarto", BigDecimal.valueOf(5000.00).setScale(2));
            ddrt.setDescripcion("Malo");
            new ProductoServicios().crearProducto(ddrt);
            Producto ddtt = new Producto("Bueh", BigDecimal.valueOf(500.00).setScale(2));
            ddtt.setDescripcion("Malo");
            new ProductoServicios().crearProducto(ddtt);
            Producto ddt = new Producto("Se paso", BigDecimal.valueOf(500.00).setScale(2));
            ddt.setDescripcion("Malo");
            new ProductoServicios().crearProducto(ddt);
            Producto ddtd = new Producto("CC", BigDecimal.valueOf(500.00).setScale(2));
            ddtd.setDescripcion("Malo");
            new ProductoServicios().crearProducto(ddtd);
        }


    }


    public boolean verificar_user(String user, String password) {
        Usuario aux = new UsuarioServicios().getUsuario(user);

        if (aux.getUsuario().equalsIgnoreCase(user) && aux.getPassword().equalsIgnoreCase(password)) {
            return true;
        }

        return false;
    }

    public void agregar_Producto(Producto produ) {
        //int id = new ProductoServicios().getIdentityMax();
        //id+=1;
        new ProductoServicios().crearProducto(produ);

    }

    public int cant_product(Long id) {
        int cant = -1;
        CarroCompra aux = carroCompraServicios.getCarroCompra(id);
        try {

            if (aux != null) {
                cant = 0;
                for (CarroCompra_Producto pp : aux.getListaProductos()
                ) {
                    cant += pp.getCantidad();
                }

                return cant;

            }

            return cant;
        } catch (NullPointerException E) {
            return -1;
        }


    }

    public void procesar_compra(Long id_cliente, String nombre) {
        //int id = ventasProductos.size();
        //id+=1;
        Calendar fecha = new GregorianCalendar();
        java.util.Date utilDate = fecha.getTime();
        VentasProductos ven = new VentasProductos(nombre);
        ven.setListaProducto(carroCompraServicios.getCarroCompra(id_cliente).getListaProductos_PreComprado());
        ventasProductosServicios.crearVentasProductos(ven);
    }

    public void agregar_producto_a_cliente(long id_cliente, int id_producto, int cant) {
        int posi = devuelve_cliente(id_cliente);
        CarroCompra auc = carroCompraServicios.getCarroCompra(id_cliente);

        CarroCompra_Producto auxx = auc.add_producto(id_producto, cant);
        //if (new CarroCompra_ProductoServicios().getCarroCompra_Producto(auxx.getId())!=null){
        // new CarroCompra_ProductoServicios().getEditar(auxx);
        // }else {

        //auxx.setCarroCompra(auc);
        //new CarroCompra_ProductoServicios().getAgregar(auxx);
        carroCompraServicios.updateCarroCompraProducto(auc);
        // }
        //
    }

    public int devuelve_cliente(long id) {
        int cant = -1;
        if (carroCompraServicios.getCarroCompra(id).getId() == id) {
            cant = (int) id;
            return cant;
        }

        return cant;
    }

    public boolean borrar_producto_carro(long id_cliente, int id_producto) {
        CarroCompra aux = carroCompraServicios.getCarroCompra(id_cliente);
        for (CarroCompra_Producto pro : aux.getListaProductos()
        ) {
            if (pro.getProducto().getId() == id_producto) {
                ;
                carroCompraServicios.borrarProductoCarroCompra(id_cliente, pro.getProducto().getId());
                ;

                return true;

            }
        }
        return false;
    }

    public boolean borrar_todo_carro(long id_cliente) {
        new CarroCompraServicios().borrarTodoProductoCarroCompra(id_cliente);
        return true;

    }

    public int borrar_producto(int id) {
        int cant = -1;
        boolean borro = carroCompra_productoServicios.getBorrarProductoATodoLosClientes(id);
        productoServicios.borrarProducto(id);
        if (borro == true) {
            cant = id;
            return cant;

        }

        return cant;
    }

    public Producto return_Producto(int id) {

        return productoServicios.getProducto(id);

    }

    public void change_Producto(int id, String nombre, String precio) {
        Producto aux = new ProductoServicios().getProducto(id);
        aux.setNombre(nombre);
        aux.setPrecio(BigDecimal.valueOf(Double.parseDouble(precio)).setScale(2));
        new ProductoServicios().updateProducto(aux);

    }

    public static ProductoServicios getProductoServicios() {
        return productoServicios;
    }

    public static void setProductoServicios(ProductoServicios productoServicios) {
        Mercado.productoServicios = productoServicios;
    }

    public static UsuarioServicios getUsuarioServicios() {
        return usuarioServicios;
    }

    public static void setUsuarioServicios(UsuarioServicios usuarioServicios) {
        Mercado.usuarioServicios = usuarioServicios;
    }

    public static VentasProductosServicios getVentasProductosServicios() {
        return ventasProductosServicios;
    }

    public static void setVentasProductosServicios(VentasProductosServicios ventasProductosServicios) {
        Mercado.ventasProductosServicios = ventasProductosServicios;
    }

    public static CarroCompraServicios getCarroCompraServicios() {
        return carroCompraServicios;
    }

    public static void setCarroCompraServicios(CarroCompraServicios carroCompraServicios) {
        Mercado.carroCompraServicios = carroCompraServicios;
    }
}
