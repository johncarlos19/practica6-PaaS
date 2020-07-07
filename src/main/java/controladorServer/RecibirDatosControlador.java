package controladorServer;

import io.javalin.Javalin;
import logico.*;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.*;

import javax.naming.Context;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


import static io.javalin.apibuilder.ApiBuilder.*;

public class RecibirDatosControlador extends JavalinControlador {

    AES256TextEncryptor userEncryptor = new AES256TextEncryptor();
    AES256TextEncryptor passwordEncryptor = new AES256TextEncryptor();

    public RecibirDatosControlador(Javalin app) {
        super(app);
        userEncryptor.setPassword("admin");
        passwordEncryptor.setPassword("admin");

    }

    /**
     * Registrando los sistemas de plantillas utilizados.
     */


    @Override
    public void aplicarRutas() {

        app.before(ctx -> {

            long cant;
            int aux = 0;
            String value = ctx.sessionAttribute("carroCompra");
            String valueCookie = ctx.cookie("carroCompra");

            if (value != null || valueCookie != null) {
                if (value != null) {
                    cant = Long.parseLong(value);
                } else {
                    cant = Long.parseLong(valueCookie);
                    value = valueCookie;
                    ctx.sessionAttribute("carroCompra", valueCookie);

                }

                if (Mercado.getInstance().cant_product(cant) != -1) {
                    aux = Mercado.getInstance().cant_product(cant);
                } else {
                    CarroCompra carr = new CarroCompra(cant);
                    new CarroCompraServicios().crearCarroCompra(carr);

                    aux = Mercado.getInstance().cant_product(cant);
                }

            } else {
                long id = new CarroCompraServicios().getIdentityMax() + 1;
                ctx.sessionAttribute("carroCompra", Long.toString(id));
                ctx.cookie("carroCompra", Long.toString(id), 66000000);
                CarroCompra carr = new CarroCompra(id);
                new CarroCompraServicios().crearCarroCompra(carr);

                value = Long.toString(id);
                valueCookie = Long.toString(id);
            }

        });


        app.get("/login", ctx -> {

            //
            String ident = ctx.queryParam("ident");
            System.out.println(ident);
            String user = ctx.cookie("Login");
            String pass = ctx.cookie("LoginS");


            if (user != null && pass != null) {
                String userDescr = userEncryptor.decrypt(user);
                String passDescr = passwordEncryptor.decrypt(pass);
                if (Mercado.getInstance().verificar_user(userDescr, passDescr) == true) {
                    switch (ident) {
                        case "venta":
                            //ctx.cookie("Login", ctx.req.getSession().getId(), 120);
                            ctx.redirect("/venta_producto");
                            break;
                        case "administrar":
                            //ctx.cookie("Login", ctx.req.getSession().getId(), 120);
                            ctx.redirect("/administrar");
                            break;
                        case "logout":
                            //ctx.cookie("Login", ctx.req.getSession().getId(), 120);
                            ctx.removeCookie("Login");
                            ctx.removeCookie("LoginS");
                            ctx.redirect("/");
                            break;
                        default:
                            ctx.redirect("/");
                            break;
                    }
                } else {
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("valor", ident);

                    ctx.render("/publico/login/login.html", modelo);
                }
            } else {
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("valor", ident);

                ctx.render("/publico/login/login.html", modelo);
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
            String id_cliente = ctx.sessionAttribute("carroCompra");
            String id_accion = ctx.queryParam("id_accion");
            String procee = " ";
            switch (id_accion) {
                case "Eliminar":
                    id = ctx.queryParam("id");
                    Mercado.getInstance().borrar_producto_carro(Long.parseLong(id_cliente), Integer.parseInt(id));
                    break;
                case "Procesar Compra":
                    if (Mercado.getInstance().cant_product(Long.parseLong(id_cliente)) != 0) {
                        nombre = ctx.queryParam("nombre");
                        Mercado.getInstance().procesar_compra(Long.parseLong(id_cliente), nombre);
                        Mercado.getInstance().borrar_todo_carro(Long.parseLong(id_cliente));
                        ctx.redirect("/carrito_hecho");

                    } else {
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

            List<Producto> listaProducto = Mercado.getInstance().getProductoServicios().listaProducto(1);

            int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
            List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductosServicios().listaVentasProductos();
            CarroCompra carroProducto = Mercado.getInstance().getCarroCompraServicios().getCarroCompra(Long.parseLong(id_cliente));
            String administrar = "display: none";
            String compra = "display: none";
            Map<String, Object> modelo = new HashMap<>();
            String userenc = userEncryptor.decrypt(ctx.cookie("Login"));
            if (userenc != null) {
                modelo.put("user", userenc + " - Salir");
                modelo.put("log", "logout");
            } else {
                userenc = "Iniciar Seccion";
                modelo.put("user", userenc);
                modelo.put("log", "login");
            }
            modelo.put("cant", aux);
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
        });


        app.get("/carrito", ctx -> {
            String id_cliente = ctx.sessionAttribute("carroCompra");
            if (id_cliente != null) {
                List<String> salida = new ArrayList<>();
                salida.add("Mostrando todos los parametros enviados:");
                //listando la informacion.
                ctx.queryParamMap().forEach((key, lista) -> {
                    salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
                });
                //

                CarroCompra carroProducto = Mercado.getInstance().getCarroCompraServicios().getCarroCompra(Mercado.getInstance().devuelve_cliente(Long.parseLong(id_cliente)));
                //List<Producto> listaProducto = Mercado.getInstance().getProductoServicios().listaProducto();
                List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductosServicios().listaVentasProductos();
                int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
                String procee = " ";
                String administrar = "display: none";
                String compra = "display: none";
                Map<String, Object> modelo = new HashMap<>();
                String userenc = userEncryptor.decrypt(ctx.cookie("Login"));
                if (userenc != null) {
                    modelo.put("user", userenc + " - Salir");
                    modelo.put("log", "logout");
                } else {
                    userenc = "Iniciar Seccion";
                    modelo.put("user", userenc);
                    modelo.put("log", "login");
                }
                modelo.put("cant", aux);
                modelo.put("cant", aux);
               // modelo.put("listaProducto", listaProducto);
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
            } else {
                ctx.redirect("/");
            }

        });


        /**
         * Ejemplo para leer por parametros de consulta (query param)
         * http://localhost:7000/parametros?matricula=20011126&nombre=Carlos%20Camacho
         */




        app.get("/doItChange", ctx -> {
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
            switch (id_action) {
                case "Editar":
                    ctx.redirect("/editar/"+id);
                    break;
                case "Eliminar":
                    Mercado.getInstance().borrar_producto(Integer.parseInt(id));
                    ctx.redirect("/administrar");
                    break;
                default:
                    ctx.redirect("/");
                    break;
            }

            String id_cliente = ctx.sessionAttribute("carroCompra");
            //List<Producto> listaProducto = Mercado.getInstance().getProductoServicios().listaProducto();
            List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductosServicios().listaVentasProductos();
            CarroCompra carroProducto = Mercado.getInstance().getCarroCompraServicios().getCarroCompra(Long.parseLong(id_cliente));
            int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
            String administrar = "display: block";
            String compra = "display: none";
            Map<String, Object> modelo = new HashMap<>();
            String userenc = userEncryptor.decrypt(ctx.cookie("Login"));
            if (userenc != null) {
                modelo.put("user", userenc + " - Salir");
                modelo.put("log", "logout");
            } else {
                userenc = "Iniciar Seccion";
                modelo.put("user", userenc);
                modelo.put("log", "login");
            }
            modelo.put("cant", aux);
            modelo.put("cant", aux);
            //modelo.put("listaProducto", listaProducto);
            modelo.put("administrar", administrar);
            modelo.put("compra", compra);
            modelo.put("carrito", "display: none");
            modelo.put("carrito_hecho", "display: none");
            modelo.put("venta_producto", "display: none");
            modelo.put("listaVenta", listaVenta);
            modelo.put("carroProducto", carroProducto);
            //ctx.render("/publico/index.html", modelo);
            System.out.println(salida);


        });

        app.post("/new_product", ctx -> {
            Set<Foto> listaFoto = new HashSet<Foto>();
            String nombre_producto = ctx.formParam("producto");
            String precio_producto = ctx.formParam("precio");
            String descripcion_producto = ctx.formParam("descripcion");
            Producto produ = new Producto(nombre_producto, BigDecimal.valueOf(Double.parseDouble(precio_producto)));
            produ.setDescripcion(descripcion_producto);
            ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                try {
                    byte[] bytes = uploadedFile.getContent().readAllBytes();
                    String encodedString = Base64.getEncoder().encodeToString(bytes);
                    Foto foto = new Foto(uploadedFile.getFilename(), uploadedFile.getContentType(), encodedString);

                    listaFoto.add(foto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            produ.setFotos(listaFoto);
            Mercado.getInstance().agregar_Producto(produ);
            //
            String id_cliente = ctx.sessionAttribute("carroCompra");
            //List<Producto> listaProducto = Mercado.getInstance().getProductoServicios().listaProducto();
            List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductosServicios().listaVentasProductos();
            CarroCompra carroProducto = Mercado.getInstance().getCarroCompraServicios().getCarroCompra(Long.parseLong(id_cliente));
            int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
            String administrar = "display: block";
            String compra = "display: none";
            Map<String, Object> modelo = new HashMap<>();
            String userenc = userEncryptor.decrypt(ctx.cookie("Login"));
            if (userenc != null) {
                modelo.put("user", userenc + " - Salir");
                modelo.put("log", "logout");
            } else {
                userenc = "Iniciar Seccion";
                modelo.put("user", userenc);
                modelo.put("log", "login");
            }
            modelo.put("cant", aux);
            modelo.put("cant", aux);
            //modelo.put("listaProducto", listaProducto);
            modelo.put("administrar", administrar);
            modelo.put("compra", compra);
            modelo.put("carrito", "display: none");
            modelo.put("carrito_hecho", "display: none");
            modelo.put("venta_producto", "display: none");
            modelo.put("listaVenta", listaVenta);
            modelo.put("carroProducto", carroProducto);
            ctx.redirect("/administrar");
            // ctx.render("/publico/index.html", modelo);

        });
        app.get("/editar/:id", ctx -> {

            String id_cliente = ctx.sessionAttribute("carroCompra");

            Producto producto = Mercado.getInstance().getProductoServicios().getProducto(ctx.pathParam("id", Integer.class).getOrNull());
            int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
            Map<String, Object> modelo = new HashMap<>();
            String userenc = userEncryptor.decrypt(ctx.cookie("Login"));
            if (userenc != null) {
                modelo.put("user", userenc + " - Salir");
                modelo.put("log", "logout");
            } else {
                userenc = "Iniciar Seccion";
                modelo.put("user", userenc);
                modelo.put("log", "login");
            }
            modelo.put("cant", aux);
            modelo.put("producto", producto);
            ctx.render("/publico/EditarProducto.html",modelo);
        });
        app.get("/editar/eliminar/:id_foto/:id_producto", ctx -> {
            int id_producto = ctx.pathParam("id_producto", Integer.class).getOrNull();
            long id_foto = ctx.pathParam("id_foto", Long.class).getOrNull();
            Producto produ = Mercado.getInstance().getProductoServicios().getProducto(id_producto);
            System.out.println("Eliminar out"+id_foto);
            produ.eliminarFoto(id_foto);
            Mercado.getInstance().getProductoServicios().updateProducto(produ);
            ctx.redirect("/editar/"+id_producto);

        });
        app.get("/comentar", ctx -> {
            String id_cliente = ctx.sessionAttribute("carroCompra");
            String id = ctx.queryParam("id");
            String comentar = ctx.queryParam("comentar");
            String nombreCliente = ctx.queryParam("nombreCliente");
            Producto produ = Mercado.getInstance().getProductoServicios().getProducto(Integer.parseInt(id));
            produ.addComentario(comentar,nombreCliente,id_cliente);
            Mercado.getInstance().getProductoServicios().updateProducto(produ);
            ctx.redirect("/producto?id="+id);

        });
        app.get("/EliminarComentario", ctx -> {

            String id = ctx.queryParam("id");
            String id_comentario = ctx.queryParam("id_comentario");

            Producto produ = Mercado.getInstance().getProductoServicios().getProducto(Integer.parseInt(id));
            produ.deleteComentario(id_comentario);
            Mercado.getInstance().getProductoServicios().updateProducto(produ);
            ctx.redirect("/producto?id="+id);

        });
        app.post("/edit_now", ctx -> {
            String id_producto = ctx.formParam("id_producto");
            String nombre_producto = ctx.formParam("producto");
            String precio_producto = ctx.formParam("precio");
            String descripcion_producto = ctx.formParam("descripcion");
            Producto produ = Mercado.getInstance().getProductoServicios().getProducto(Integer.parseInt(id_producto));
            produ.setNombre(nombre_producto);
            produ.setPrecio(BigDecimal.valueOf(Double.parseDouble(precio_producto)).setScale(2));
            produ.setDescripcion(descripcion_producto);
            Set<Foto> listaFoto = new HashSet<Foto>();
            for (Foto au:produ.getFotos()
                 ) {
                listaFoto.add(au);
            }
            System.out.println("Cantidad:"+listaFoto.size());
            ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                try {
                    byte[] bytes = uploadedFile.getContent().readAllBytes();
                    String encodedString = Base64.getEncoder().encodeToString(bytes);
                    Foto foto = new Foto(uploadedFile.getFilename(), uploadedFile.getContentType(), encodedString);

                    listaFoto.add(foto);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("Cantidad lis:"+listaFoto.size());
            produ.setFotos(listaFoto);
            System.out.println("Cantidad:"+produ.getFotos().size());
            Mercado.getInstance().getProductoServicios().updateProducto(produ);


            ctx.redirect("/editar/"+id_producto);


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
            String id_cliente = ctx.sessionAttribute("carroCompra");
            System.out.println(id_cliente);
            Mercado.getInstance().agregar_producto_a_cliente(Long.parseLong(id_cliente), Integer.parseInt(id), Integer.parseInt(cant));

            System.out.println(salida);
            //List<Producto> listaProducto = Mercado.getInstance().getProductoServicios().listaProducto();
            List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductosServicios().listaVentasProductos();
            CarroCompra carroProducto = Mercado.getInstance().getCarroCompraServicios().getCarroCompra(Long.parseLong(id_cliente));
            int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
            String administrar = "display: none";
            String compra = "display: block";
            Map<String, Object> modelo = new HashMap<>();

            String userenc = userEncryptor.decrypt(ctx.cookie("Login"));
            if (userenc != null) {
                modelo.put("user", userenc + " - Salir");
                modelo.put("log", "logout");
            } else {
                userenc = "Iniciar Seccion";
                modelo.put("user", userenc);
                modelo.put("log", "login");
            }

            modelo.put("cant", aux);
           // modelo.put("listaProducto", listaProducto);
            modelo.put("administrar", administrar);
            modelo.put("compra", compra);
            modelo.put("carrito", "display: none");
            modelo.put("carrito_hecho", "display: none");
            modelo.put("venta_producto", "display: none");
            modelo.put("listaVenta", listaVenta);
            modelo.put("carroProducto", carroProducto);
            //
            ctx.redirect("/");
            System.out.println("id quien agrego: " + ctx.req.getSession().getId());
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
            System.out.println("(" + user + ")" + "(" + pass + ")" + "(" + ident + ")");
            if (Mercado.getInstance().verificar_user(user, pass) == true) {
                //userEncryptor.setPassword("admin");
                //passwordEncryptor.setPassword("admin");
                String encryptedUser = userEncryptor.encrypt(user);
                String passEncryp = passwordEncryptor.encrypt(pass);
                if (ctx.formParam("statu") != null) {

                    if (ctx.cookie("LoginS") == null) {
                        ctx.cookie("LoginS", passEncryp, 604800);
                        ctx.cookie("Login", encryptedUser, 604800);
                    }
                } else {
                    ctx.cookie("LoginS", passEncryp, 120);
                    ctx.cookie("Login", encryptedUser, 120);
                }
                System.out.println("entro");
                switch (ident) {

                    case "venta":


                        ctx.redirect("/venta_producto");
                        break;
                    case "administrar":

                        ctx.redirect("/administrar");
                        break;
                    case "login":

                        ctx.redirect("/");
                        break;
                    default:
                        ctx.redirect("/");
                        break;
                }
            }

        });

        app.routes(() -> {
            /*
            path("/editar/:id", () -> {

                get(ctx -> {


                    String id_cliente = ctx.sessionAttribute("carroCompra");

                    Producto producto = Mercado.getInstance().getProductoServicios().getProducto(ctx.pathParam("id", Integer.class).getOrNull());
                    int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
                    Map<String, Object> modelo = new HashMap<>();
                    String userenc = userEncryptor.decrypt(ctx.cookie("Login"));
                    if (userenc != null) {
                        modelo.put("user", userenc + " - Salir");
                        modelo.put("log", "logout");
                    } else {
                        userenc = "Iniciar Seccion";
                        modelo.put("user", userenc);
                        modelo.put("log", "login");
                    }
                    modelo.put("cant", aux);
                    modelo.put("producto", producto);
                    ctx.render("/publico/EditarProducto.html",modelo);
                });
        });*/
            path("/producto", () -> {
                get(ctx -> {

                        String id_cliente = ctx.sessionAttribute("carroCompra");

                        String id = ctx.queryParam("id");

                        Producto producto = Mercado.getInstance().getProductoServicios().getProducto(Integer.parseInt(id));
                        int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
                        Map<String, Object> modelo = new HashMap<>();
                        String userenc = userEncryptor.decrypt(ctx.cookie("Login"));
                        if (userenc != null) {
                            modelo.put("user", userenc);
                            modelo.put("user11", userenc + " - Salir");
                            modelo.put("log", "logout");
                        } else {
                            userenc = "Iniciar Seccion";
                            modelo.put("user11", userenc);
                            modelo.put("log", "login");
                        }
                        modelo.put("id_cliente", id_cliente);
                        modelo.put("cant", aux);
                        modelo.put("fotoCantidad",Integer.toString(producto.getFotos().size()-1));
                        modelo.put("producto", producto);
                        ctx.render("/publico/Producto.html",modelo);

                });
            });

            path("/venta_producto", () -> {

                get(ctx -> {
                    if (ctx.cookie("Login") != null) {
                        List<String> salida = new ArrayList<>();
                        salida.add("Mostrando todos los parametros enviados:");
                        //listando la informacion.
                        ctx.queryParamMap().forEach((key, lista) -> {
                            salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
                        });
                        //
                        String id_cliente = ctx.sessionAttribute("carroCompra");
                        //List<Producto> listaProducto = Mercado.getInstance().getProductoServicios().listaProducto();
                        List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductosServicios().listaVentasProductos();
                        CarroCompra carroProducto = Mercado.getInstance().getCarroCompraServicios().getCarroCompra(Long.parseLong(id_cliente));
                        int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
                        String administrar = "display: none";
                        String compra = "display: none";
                        Map<String, Object> modelo = new HashMap<>();
                        String userenc = userEncryptor.decrypt(ctx.cookie("Login"));
                        if (userenc != null) {
                            modelo.put("user", userenc + " - Salir");
                            modelo.put("log", "logout");
                        } else {
                            userenc = "Iniciar Seccion";
                            modelo.put("user", userenc);
                            modelo.put("log", "login");
                        }
                        modelo.put("cant", aux);
                        modelo.put("cant", aux);
                       // modelo.put("listaProducto", listaProducto);
                        modelo.put("administrar", administrar);
                        modelo.put("compra", compra);
                        modelo.put("carrito", "display: none");
                        modelo.put("venta_producto", "display: block");
                        modelo.put("listaVenta", listaVenta);
                        modelo.put("carrito_hecho", "display: none");
                        modelo.put("carroProducto", carroProducto);
                        ctx.render("/publico/index.html", modelo);

                        System.out.println(salida);
                    } else {
                        ctx.redirect("/");
                    }
                });
            });


            path("/administrar", () -> {

                get(ctx -> {
                    if (ctx.cookie("Login") != null) {
                        List<String> salida = new ArrayList<>();
                        salida.add("Mostrando todos los parametros enviados:");
                        //listando la informacion.
                        ctx.queryParamMap().forEach((key, lista) -> {
                            salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
                        });
                        //
                        String id_cliente = ctx.sessionAttribute("carroCompra");
                        List<Producto> listaProducto = Mercado.getInstance().getProductoServicios().listaProductoCompleto();
                        List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductosServicios().listaVentasProductos();
                        CarroCompra carroProducto = Mercado.getInstance().getCarroCompraServicios().getCarroCompra(Long.parseLong(id_cliente));
                        int aux = Mercado.getInstance().cant_product(Long.parseLong(id_cliente));
                        String administrar = "display: block";
                        String compra = "display: none";
                        Map<String, Object> modelo = new HashMap<>();
                        String userenc = userEncryptor.decrypt(ctx.cookie("Login"));
                        if (userenc != null) {
                            modelo.put("user", userenc + " - Salir");
                            modelo.put("log", "logout");
                        } else {
                            userenc = "Iniciar Seccion";
                            modelo.put("user", userenc);
                            modelo.put("log", "login");
                        }
                        modelo.put("cant", aux);
                        modelo.put("cant", aux);
                        modelo.put("listaProducto", listaProducto);
                        modelo.put("administrar", administrar);
                        modelo.put("compra", compra);
                        modelo.put("carrito", "display: none");
                        modelo.put("venta_producto", "display: none");
                        modelo.put("listaVenta", listaVenta);
                        modelo.put("carrito_hecho", "display: none");
                        modelo.put("carroProducto", carroProducto);
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
                get(ctx -> {
                    //List<Producto> listaProducto = Mercado.getInstance().getProductoServicios().listaProducto();
                    List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductosServicios().listaVentasProductos();


                    long cant;
                    int aux = 0;
                    String value = ctx.sessionAttribute("carroCompra");
                    CarroCompra carroProducto = Mercado.getInstance().getCarroCompraServicios().getCarroCompra(Long.parseLong(value));

                    String administrar = "display: none";
                    String compra = "display: none";
                    Map<String, Object> modelo = new HashMap<>();
                    String userenc = userEncryptor.decrypt(ctx.cookie("Login"));
                    if (userenc != null) {
                        modelo.put("user", userenc + " - Salir");
                        modelo.put("log", "logout");
                    } else {
                        userenc = "Iniciar Seccion";
                        modelo.put("user", userenc);
                        modelo.put("log", "login");
                    }
                    modelo.put("cant", aux);
                    modelo.put("cant", aux);
                    //modelo.put("listaProducto", listaProducto);
                    modelo.put("administrar", administrar);
                    modelo.put("compra", compra);
                    modelo.put("carrito", "display: none");
                    modelo.put("carrito_hecho", "display: block");
                    modelo.put("venta_producto", "display: none");
                    modelo.put("listaVenta", listaVenta);
                    modelo.put("carroProducto", carroProducto);
                    //
                    ctx.render("/publico/index.html", modelo);
                });
            });
            path("/", () -> {


                get(ctx -> {

                    //ctx.cookie("carroCompra","1");

                    long cant;
                    int aux = 0;
                    String value = ctx.sessionAttribute("carroCompra");
                    String valueCookie = ctx.cookie("carroCompra");

                    if (value != null || valueCookie != null) {
                        if (value != null) {
                            cant = Long.parseLong(value);
                        } else {
                            cant = Long.parseLong(valueCookie);
                            value = valueCookie;
                            ctx.sessionAttribute("carroCompra", valueCookie);

                        }

                        if (Mercado.getInstance().cant_product(cant) != -1) {
                            aux = Mercado.getInstance().cant_product(cant);
                        } else {
                            CarroCompra carr = new CarroCompra(cant);
                            new CarroCompraServicios().crearCarroCompra(carr);

                            aux = Mercado.getInstance().cant_product(cant);
                        }

                    } else {
                        long id = new CarroCompraServicios().getIdentityMax() + 1;
                        ctx.sessionAttribute("carroCompra", Long.toString(id));
                        ctx.cookie("carroCompra", Long.toString(id), 66000000);
                        CarroCompra carr = new CarroCompra(id);
                        new CarroCompraServicios().crearCarroCompra(carr);

                        value = Long.toString(id);
                        valueCookie = Long.toString(id);
                    }
                    String page = ctx.queryParam("page");
                    List<Producto> listaProducto = null;
                    if (page!=null){
                        listaProducto = Mercado.getInstance().getProductoServicios().listaProducto(Integer.parseInt(page));

                    }else {
                        listaProducto = Mercado.getInstance().getProductoServicios().listaProducto(1);
                        page = "1";
                    }

                    //List<VentasProductos> listaVenta = Mercado.getInstance().getVentasProductosServicios().listaVentasProductos();

                    //value = ctx.cookie("carroCompra");
                    //CarroCompra carroProducto = Mercado.getInstance().getCarroCompraServicios().getCarroCompra(Long.parseLong(value));
                    String administrar = "display: none";
                    String compra = "display: block";
                    int vv = Mercado.getInstance().getProductoServicios().listaProductoCompleto().size();
                    int pagina = (int) Math.ceil( ((double) vv) /10);
                    Map<String, Object> modelo = new HashMap<>();
                    String userenc = userEncryptor.decrypt(ctx.cookie("Login"));
                    if (userenc != null) {
                        modelo.put("user", userenc + " - Salir");
                        modelo.put("log", "logout");
                    } else {
                        userenc = "Iniciar Seccion";
                        modelo.put("user", userenc);
                        modelo.put("log", "login");
                    }
                    modelo.put("cant", aux);

                    modelo.put("listaProducto", listaProducto);
                   // modelo.put("administrar", administrar);
                    modelo.put("compra", compra);
                    modelo.put("pagina", pagina);
                    modelo.put("page", page);
                   // modelo.put("carrito", "display: none");
                   // modelo.put("carrito_hecho", "display: none");
                    //modelo.put("venta_producto", "display: none");
                   // modelo.put("listaVenta", listaVenta);
                    //modelo.put("carroProducto", carroProducto);
                    //
                    ctx.render("/publico/Home.html", modelo);
                });
            });
        });

    }
}
