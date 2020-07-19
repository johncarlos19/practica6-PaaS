package CarroCompra.logico;

import javax.persistence.*;

@Entity
public class Comentario {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @Column
    private String nombreCliente;
    @Column
    private String comentario;
    @Column
    private String id_cliente;

    public Comentario() {
    }

    public Comentario(String nombreCliente, String comentario, String id_cliente) {
        this.nombreCliente = nombreCliente;
        this.comentario = comentario;
        this.id_cliente = id_cliente;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
