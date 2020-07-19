package CarroCompra.controladorServer;

import org.h2.tools.Server;

import java.sql.SQLException;

public class DataBaseControlador {
    /**
     * @throws SQLException
     */
    public static void startDb() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists").start();
        String status = Server.createWebServer("-trace","-web","-webAllowOthers","-webDaemon", "-webPort", "0").start().getStatus();
        //
        System.out.println("Status Web: " + status);
    }

    /**
     * @throws SQLException
     */
    public static void stopDb() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }


    /**
     * Metodo para recrear las tablas necesarios
     * @throws SQLException
     */
    /*
    public static void crearTablas() throws  SQLException{
        String sql = "CREATE TABLE if not exists Usuario\n" +
                "(\n" +
                "                Nombre varchar(255) NOT NULL,\n" +
                "                User varchar(255) PRIMARY KEY NOT NULL,\n" +
                "                Password varchar(255) NOT NULL\n" +
                "\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE if not exists Producto (\n" +
                "                          Id bigint PRIMARY KEY auto_increment,\n" +
                "                          Nombre varchar(255) NOT NULL,\n" +
                "                          Precio DECIMAL(20, 2) NOT NULL\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE if not exists CarroCompra (\n" +
                "                             Id bigint NOT NULL auto_increment,\n" +
                "                             CONSTRAINT CarroCompra_pk PRIMARY KEY (Id)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE if not exists VentasProducto (\n" +
                "                                Id bigint not null auto_increment,\n" +
                "                                FechaCompra timestamp NOT NULL,\n" +
                "                                NombreCliente varchar(255) NOT NULL,\n" +
                "                                CONSTRAINT VentasProducto_pk PRIMARY KEY (Id)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE if not exists CarroCompraProducto (\n" +
                "                                     IdCarroCompra bigint NOT NULL,\n" +
                "                                     IdProducto bigint NOT NULL,\n" +
                "                                     Cantidad bigint NOT NULL\n" +
                ");\n" +
                "CREATE TABLE  if not exists VentasProductoProductoComprado (\n" +
                "                                                               IdVentasProducto bigint NOT NULL,\n" +
                "                                                               IdProducto bigint NOT NULL,\n" +
                "                                                               Cantidad bigint NOT NULL,\n" +
                "                                                               Nombre varchar(255) NOT NULL,\n" +
                "                                                               Precio DECIMAL(20, 2) NOT NULL\n" +
                ");\n" +
                "CREATE TABLE if not exists ProductoComprado (\n" +
                "                                                Id bigint not null auto_increment,\n" +
                "                                                CONSTRAINT ProductoComprado_pk PRIMARY KEY (Id)\n" +
                ");\n" +
                "\n" +
                "\n" +
                "ALTER TABLE CarroCompraProducto ADD CONSTRAINT if not exists CarroCompraProducto_fk0 FOREIGN KEY (IdCarroCompra) REFERENCES CarroCompra(Id);\n" +
                "ALTER TABLE CarroCompraProducto ADD CONSTRAINT if not exists CarroCompraProducto_fk1 FOREIGN KEY (IdProducto) REFERENCES Producto(Id);\n" +
                "\n" +
                "ALTER TABLE VentasProductoProductoComprado ADD CONSTRAINT if not exists VentasProductoProductoComprado_fk0 FOREIGN KEY (IdVentasProducto) REFERENCES VentasProducto(Id);\n" +
                "ALTER TABLE VentasProductoProductoComprado ADD CONSTRAINT if not exists VentasProductoProductoComprado_fk1 FOREIGN KEY (IdProducto) REFERENCES ProductoComprado(Id);";
        Connection con = DataBaseServices.getInstancia().getConexion();
        Statement statement = con.createStatement();
        statement.execute(sql);
        statement.close();
        con.close();
    }*/
}
