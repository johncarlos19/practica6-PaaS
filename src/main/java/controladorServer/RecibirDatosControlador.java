package controladorServer;

import io.javalin.Javalin;
import logico.CarroCompra;
import logico.Mercado;
import logico.Producto;
import logico.VentasProductos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static io.javalin.apibuilder.ApiBuilder.*;

public class RecibirDatosControlador extends JavalinControlador{

    public RecibirDatosControlador(Javalin app) {
        super(app);

    }


    /**
     * Registrando los sistemas de plantillas utilizados.
     */


    @Override
    public void aplicarRutas() {


        app.get("/login", ctx -> {

            //
            String ident = ctx.queryParam("ident");
            System.out.println(ident);


            if (ctx.cookie("Login")!=null){
                switch (ident){
                    case "venta":
                        ctx.cookie("Login",ctx.req.getSession().getId(),120);
                        ctx.redirect("/venta_producto");
                        break;
                    case "administrar":
                        ctx.cookie("Login",ctx.req.getSession().getId(),120);
                        ctx.redirect("/administrar");
                        break;
                    default:
                        ctx.redirect("/");
                        break;
                }

            }else{
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("valor",ident);

                ctx.render("/publico/login/login.html",modelo);
            }
            //

        });

        app.get("/carrito_process", ctx -> {
            List<String> salida = new ArrayList<>();
            salida.add("Mostrando todos los parametros enviados:");
            //listando la informacion.
            ctx.queryParamMap().forEach((key, lista) -> {
                salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
            });
            //
            System.out.println(salida);
            String id;
            String nombre;
            String id_cliente = ctx.cookie("carroCompra");
            String id_accion = ctx.queryParam("id_accion");
            String procee = " ";
            switch (id_accion){
                case "Eliminar":
                    id = ctx.queryParam("id");
                    Mercado.getInstance().borrar_producto_carro(Long.parseLong(id_cliente), Integer.parseInt(id));
                    break;
                case "Procesar Compra":
                    if (Mercado.getInstance().cant_product(Long.parseLong(id_cliente)) != 0){
                        nombre = ctx.queryParam("nombre");
                        Mercado.getInstance().procesar_compra(Long.parseLong(id_cliente),nombre);
                        Mercado.getInstance().borrar_todo_carro(Long.parseLong(id_cliente));
                        ctx.redirect("/carrito_hecho");

                    }else{
                        procee = "Debe ingresar algun producto para procesar la compra";
                    }
                    break;
                case "Eliminar Todo los Articulos":
                    id = ctx.queryParam("id");
                    Mercado.getInstance().borrar_todo_carro(Long.parseLong(id_cliente));
                    break;
                default:
                    ctx.redirect("/");
                    break;
            }

            List<Producto> listaProducto = Mercado.getInstance().getCarroCompras().get(Mercado.getInstance().devuelve_cliente(Long.parseLong(id_cliente))).getListaProductos();

            int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
            List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductos();
            CarroCompra carroProducto = Mercado.getInstance().getCarroCompras().get(Mercado.getInstance().devuelve_cliente(Long.parseLong(id_cliente)));
            String administrar = "display: none";
            String compra = "display: none";
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("cant", aux);
            modelo.put("listaProducto", listaProducto);
            modelo.put("administrar", administrar);
            modelo.put("compra", compra);
            modelo.put("carrito","display: block");
            modelo.put("procee",procee);
            modelo.put("carrito_hecho","display: none");
            modelo.put("venta_producto","display: none");
            modelo.put("listaVenta", listaVenta);
            modelo.put("carroProducto",carroProducto);
            ctx.render("/publico/index.html", modelo);

            System.out.println(salida);
        });


        app.get("/carrito", ctx -> {
            String id_cliente = ctx.cookie("carroCompra");
            if (id_cliente!=null) {
                List<String> salida = new ArrayList<>();
                salida.add("Mostrando todos los parametros enviados:");
                //listando la informacion.
                ctx.queryParamMap().forEach((key, lista) -> {
                    salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
                });
                //

                CarroCompra carroProducto = Mercado.getInstance().getCarroCompras().get(Mercado.getInstance().devuelve_cliente(Long.parseLong(id_cliente)));
                List<Producto> listaProducto = Mercado.getInstance().getProductos();
                List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductos();
                int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
                String procee = " ";
                String administrar = "display: none";
                String compra = "display: none";
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("cant", aux);
                modelo.put("listaProducto", listaProducto);
                modelo.put("administrar", administrar);
                modelo.put("compra", compra);
                modelo.put("carrito", "display: block");
                modelo.put("procee", procee);
                modelo.put("carrito_hecho", "display: none");
                modelo.put("venta_producto", "display: none");
                modelo.put("listaVenta", listaVenta);
                modelo.put("carroProducto", carroProducto);
                ctx.render("/publico/index.html", modelo);
                System.out.println(salida);
            }else {
                ctx.redirect("/");
            }

        });


        /**
         * Ejemplo para leer por parametros de consulta (query param)
         * http://localhost:7000/parametros?matricula=20011126&nombre=Carlos%20Camacho
         */
        app.get("/new_product", ctx -> {
            String nombre_producto = ctx.queryParam("producto");
            String precio_producto = ctx.queryParam("precio");
            Mercado.getInstance().agregar_Producto(nombre_producto, BigDecimal.valueOf(Long.parseLong(precio_producto)));
            //
            String id_cliente = ctx.cookie("carroCompra");
            List<Producto> listaProducto = Mercado.getInstance().getProductos();
            List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductos();
            CarroCompra carroProducto = Mercado.getInstance().getCarroCompras().get(Mercado.getInstance().devuelve_cliente(Long.parseLong(id_cliente)));
            int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
            String administrar = "display: block";
            String compra = "display: none";
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("cant",aux);
            modelo.put("listaProducto", listaProducto);
            modelo.put("administrar",administrar);
            modelo.put("compra",compra);
            modelo.put("carrito","display: none");
            modelo.put("carrito_hecho","display: none");
            modelo.put("venta_producto","display: none");
            modelo.put("listaVenta", listaVenta);
            modelo.put("carroProducto",carroProducto);
            ctx.redirect("/administrar");
           // ctx.render("/publico/index.html", modelo);

        });
        app.get("/edit", ctx -> {
            List<String> salida = new ArrayList<>();
            salida.add("Mostrando todos los parametros enviados:");
            //listando la informacion.
            ctx.queryParamMap().forEach((key, lista) -> {
                salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
            });
            //
            String id_action = ctx.queryParam("id_accion");
            String precio = ctx.queryParam("precio");
            String id = ctx.queryParam("id");
            String nombre = ctx.queryParam("nombre");
            switch (id_action){
                case "Editar":
                    Mercado.getInstance().change_Producto(Integer.parseInt(id),nombre,precio);
                    ctx.redirect("/administrar");
                    break;
                case "Eliminar":
                    Mercado.getInstance().borrar_producto(Integer.parseInt(id));
                    ctx.redirect("/administrar");
                    break;
                default:
                    ctx.redirect("/");
                    break;
            }
            String id_cliente = ctx.cookie("carroCompra");
            List<Producto> listaProducto = Mercado.getInstance().getProductos();
            List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductos();
            CarroCompra carroProducto = Mercado.getInstance().getCarroCompras().get(Mercado.getInstance().devuelve_cliente(Long.parseLong(id_cliente)));
            int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
            String administrar = "display: block";
            String compra = "display: none";
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("cant",aux);
            modelo.put("listaProducto", listaProducto);
            modelo.put("administrar",administrar);
            modelo.put("compra",compra);
            modelo.put("carrito","display: none");
            modelo.put("carrito_hecho","display: none");
            modelo.put("venta_producto","display: none");
            modelo.put("listaVenta", listaVenta);
            modelo.put("carroProducto",carroProducto);
            //ctx.render("/publico/index.html", modelo);
            System.out.println(salida);


        });



        app.get("/parametros", ctx -> {
            List<String> salida = new ArrayList<>();
            salida.add("Mostrando todos los parametros enviados:");
            //listando la informacion.
            ctx.queryParamMap().forEach((key, lista) -> {
                salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
            });
            //
            ctx.result(String.join("\n", salida));
        });

        app.get("/add", ctx -> {
            List<String> salida = new ArrayList<>();
            salida.add("Mostrando todos los parametros enviados:");
            //listando la informacion.
            ctx.queryParamMap().forEach((key, lista) -> {
                salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
            });
            //
            String id = ctx.queryParam("id");
            String cant = ctx.queryParam("cantidad");
            String id_cliente = ctx.cookie("carroCompra");
            System.out.println(id_cliente);
            Mercado.getInstance().agregar_producto_a_cliente(Long.parseLong(id_cliente),Integer.parseInt(id),Integer.parseInt(cant));

            System.out.println(salida);
            List<Producto> listaProducto = Mercado.getInstance().getProductos();
            List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductos();
            CarroCompra carroProducto = Mercado.getInstance().getCarroCompras().get(Mercado.getInstance().devuelve_cliente(Long.parseLong(id_cliente)));
            int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
            String administrar = "display: none";
            String compra = "display: block";
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("cant",aux);
            modelo.put("listaProducto", listaProducto);
            modelo.put("administrar",administrar);
            modelo.put("compra",compra);
            modelo.put("carrito","display: none");
            modelo.put("carrito_hecho","display: none");
            modelo.put("venta_producto","display: none");
            modelo.put("listaVenta", listaVenta);
            modelo.put("carroProducto",carroProducto);
            //
            ctx.redirect("/");
            System.out.println("id quien agrego: "+ctx.req.getSession().getId());
        });


        /**
         * Ejemplo de parametros como parte de la URL, notar los ':' en el path.
         * http://localhost:7000/parametros/20011136/
         */
        app.get("/parametros/:matricula/", ctx -> {
            ctx.result("El Estudiante tiene la matricula: "+ctx.pathParam("matricula"));
        });

        /**
         * Ejemplo de parametros como parte de la URL, notar los ':' en el path.
         * Puedo hacer combinaciones
         * http://localhost:7000/parametros/20011136/nombre/carloscamacho
         */
        app.get("/parametros/:matricula/nombre/:nombre", ctx -> {
            ctx.result("El Estudiante tiene la matricula: "+ctx.pathParam("matricula")+" - nombre: "+ctx.pathParam("nombre"));
        });

        /**
         * Ejemplo de información en el cuerpo del mensaje
         * http://localhost:7000/formulario.html para el formulario
         */
        app.post("/parametros", ctx -> {
            List<String> salida = new ArrayList<>();
            salida.add("Mostrando todos la informacion enviada en el cuerpo:");
            //listando la informacion.
            ctx.formParamMap().forEach((key, lista) -> {
                salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
            });
            //
            ctx.result(String.join("\n", salida));
            System.out.println(salida);
        });
        app.post("/login", ctx -> {
            List<String> salida = new ArrayList<>();
            ctx.formParamMap().forEach((key, lista) -> {
                salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
            });
            System.out.println(salida);
            String user = ctx.formParam("user");
            String pass = ctx.formParam("password");
            String ident = ctx.formParam("ident");
            System.out.println("("+user+")"+"("+pass+")"+"("+ident+")");
            if (Mercado.getInstance().verificar_user(user,pass) == true){
                System.out.println("entro");
                switch (ident){

                    case "venta":
                        ctx.cookie("Login",ctx.req.getSession().getId(),120);
                        ctx.redirect("/venta_producto");
                        break;
                    case "administrar":
                        ctx.cookie("Login",ctx.req.getSession().getId(),120);
                        ctx.redirect("/administrar");
                        break;
                    default:
                        ctx.redirect("/");
                        break;
                }
            }

        });

        /**
         * En cualquier situación puedo los encabezados de la trama HTTP.
         * http://localhost:7000/leerheaders
         */
        app.get("leerheaders", ctx -> {
            List<String> salida = new ArrayList<>();
            salida.add("Mostrando la informacion enviada en los headers:");
            //listando la informacion.
            ctx.headerMap().forEach((key, valor) -> {
                salida.add(String.format("[%s] = [%s]", key, String.join(",", valor)));
            });
            //
            ctx.result(String.join("\n", salida));
        });

        app.routes(() -> {

            path("/venta_producto", () -> {

                get( ctx -> {
                    if (ctx.cookie("Login") != null) {
                        List<String> salida = new ArrayList<>();
                        salida.add("Mostrando todos los parametros enviados:");
                        //listando la informacion.
                        ctx.queryParamMap().forEach((key, lista) -> {
                            salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
                        });
                        //
                        String id_cliente = ctx.cookie("carroCompra");
                        List<Producto> listaProducto = Mercado.getInstance().getProductos();
                        List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductos();
                        CarroCompra carroProducto = Mercado.getInstance().getCarroCompras().get(Mercado.getInstance().devuelve_cliente(Long.parseLong(id_cliente)));
                        int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
                        String administrar = "display: none";
                        String compra = "display: none";
                        Map<String, Object> modelo = new HashMap<>();
                        modelo.put("cant", aux);
                        modelo.put("listaProducto", listaProducto);
                        modelo.put("administrar", administrar);
                        modelo.put("compra", compra);
                        modelo.put("carrito","display: none");
                        modelo.put("venta_producto","display: block");
                        modelo.put("listaVenta", listaVenta);
                        modelo.put("carrito_hecho","display: none");
                        modelo.put("carroProducto",carroProducto);
                        ctx.render("/publico/index.html", modelo);

                        System.out.println(salida);
                    } else {
                        ctx.redirect("/");
                    }
                });
            });


            path("/administrar", () -> {

                        get( ctx -> {
                            if (ctx.cookie("Login") != null) {
                                List<String> salida = new ArrayList<>();
                                salida.add("Mostrando todos los parametros enviados:");
                                //listando la informacion.
                                ctx.queryParamMap().forEach((key, lista) -> {
                                    salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
                                });
                                //
                                String id_cliente = ctx.cookie("carroCompra");
                                List<Producto> listaProducto = Mercado.getInstance().getProductos();
                                List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductos();
                                CarroCompra carroProducto = Mercado.getInstance().getCarroCompras().get(Mercado.getInstance().devuelve_cliente(Long.parseLong(id_cliente)));
                                int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
                                String administrar = "display: block";
                                String compra = "display: none";
                                Map<String, Object> modelo = new HashMap<>();
                                modelo.put("cant", aux);
                                modelo.put("listaProducto", listaProducto);
                                modelo.put("administrar", administrar);
                                modelo.put("compra", compra);
                                modelo.put("carrito","display: none");
                                modelo.put("venta_producto","display: none");
                                modelo.put("listaVenta", listaVenta);
                                modelo.put("carrito_hecho","display: none");
                                modelo.put("carroProducto",carroProducto);
                                ctx.render("/publico/index.html", modelo);

                                System.out.println(salida);
                            } else {
                                ctx.redirect("/");

                            }
                        });
                    });
            path("/carrito_hecho", () -> {

                /**
                 * http://localhost:7000/thymeleaf/
                 */
                get( ctx -> {
                    List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductos();
                    List<Producto> listaProducto = Mercado.getInstance().getProductos();
                    long cant;
                    int aux = 0;
                    String value = ctx.cookie("carroCompra");
                    CarroCompra carroProducto = Mercado.getInstance().getCarroCompras().get(Mercado.getInstance().devuelve_cliente(Long.parseLong(value)));

                    String administrar = "display: none";
                    String compra = "display: none";
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("cant",aux);
                    modelo.put("listaProducto", listaProducto);
                    modelo.put("administrar",administrar);
                    modelo.put("compra",compra);
                    modelo.put("carrito","display: none");
                    modelo.put("carrito_hecho","display: block");
                    modelo.put("venta_producto","display: none");
                    modelo.put("listaVenta", listaVenta);
                    modelo.put("carroProducto",carroProducto);
                    //
                    ctx.render("/publico/index.html", modelo);
                });
            });
            path("/", () -> {

                /**
                 * http://localhost:7000/thymeleaf/
                 */
                get( ctx -> {
                    List<Producto> listaProducto = Mercado.getInstance().getProductos();
                    List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductos();
                    long cant;
                    int aux = 0;
                    String value = ctx.cookie("carroCompra");
                    if (value!=null){
                        cant = Long.parseLong(value);
                        if (Mercado.getInstance().cant_product(cant)!=-1){
                            aux = Mercado.getInstance().cant_product(cant);
                        }else{
                            Mercado.getInstance().getCarroCompras().add(new CarroCompra(cant));
                            aux = Mercado.getInstance().cant_product(cant);
                        }

                    }else{
                        long id = Mercado.getInstance().getCarroCompras().size()+1;
                        ctx.cookie("carroCompra",Long.toString(id));

                        Mercado.getInstance().getCarroCompras().add(new CarroCompra(id));
                        value = Long.toString(id);
                    }
                    //value = ctx.cookie("carroCompra");
                    CarroCompra carroProducto = Mercado.getInstance().getCarroCompras().get(Mercado.getInstance().devuelve_cliente(Long.parseLong(value)));
                    String administrar = "display: none";
                    String compra = "display: block";
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("cant",aux);
                    modelo.put("listaProducto", listaProducto);
                    modelo.put("administrar",administrar);
                    modelo.put("compra",compra);
                    modelo.put("carrito","display: none");
                    modelo.put("carrito_hecho","display: none");
                    modelo.put("venta_producto","display: none");
                    modelo.put("listaVenta", listaVenta);
                    modelo.put("carroProducto",carroProducto);
                    //
                    ctx.render("/publico/index.html", modelo);
                });
            });
});

    }
}
