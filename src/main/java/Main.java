import controladorServer.DataBaseControlador;
import controladorServer.DataBaseServices;
import controladorServer.ProductoServicios;
import controladorServer.RecibirDatosControlador;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import logico.Mercado;
import logico.Usuario;

import java.math.BigDecimal;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        DataBaseControlador.startDb();

        //Prueba de Conexión.
        DataBaseServices.getInstancia().testConexion();

        DataBaseControlador.crearTablas();
        Mercado.getInstance().loadDataBase();
        System.out.println(new ProductoServicios().getIdentityMax());

        //Mercado.getInstance().agregar_Producto("Pan", BigDecimal.valueOf(500));
        //Mercado.getInstance().agregar_Producto("Ajo", BigDecimal.valueOf(20));
        //Mercado.getInstance().agregar_Producto("Queso", BigDecimal.valueOf(1000));
        //Mercado.getInstance().agregar_Producto("Molondron", BigDecimal.valueOf(2000));
       // Mercado.getInstance().getUsuario().add(new Usuario("admin","admin","admin"));
        //System.out.println(Mercado.getInstance().getUsuario().get(0).getNombre());
        String mensaje = "Hola Mundo en Javalin :-D";
        System.out.println(mensaje);

        //Creando la instancia del servidor.
        Javalin app = Javalin.create(config ->{
            config.addStaticFiles("/publico"); //desde la carpeta de resources
            config.addStaticFiles("/publico/login");
            config.addStaticFiles("/publico/bootstrap-4.5.0-dist");
            config.addStaticFiles("/publico/css");
            config.registerPlugin(new RouteOverviewPlugin("/rutas")); //aplicando plugins de las rutas
        }).start(getHerokuAssignedPort());
        registrandoPlantillas();



        //creando el manejador
        //app.get("/", ctx -> ctx.render("publico/index.html"));




    //aplicando las rutas para el procesamiento de los datos.
        new RecibirDatosControlador(app).aplicarRutas();

    }

    /**
     * Metodo para indicar el puerto en Heroku
     * @return
     *
     */
    private static void registrandoPlantillas(){
        //registrando los sistemas de plantilla.
        //JavalinRenderer.register(JavalinFreemarker.INSTANCE, ".ftl");
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
        // JavalinRenderer.register(JavalinVelocity.INSTANCE, ".vm");
    }
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }
}
