package logico;

import org.hibernate.annotations.SortNatural;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Producto implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name = "Nombre")
    private String nombre;
    @Column(name = "Precio")
    private BigDecimal precio;
    @Column(name = "Descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "carroCompra", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<CarroCompra_Producto> carroCompra_productos = new HashSet<CarroCompra_Producto>();
    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL , orphanRemoval = true)
    private Set<Foto> fotos = new HashSet<Foto>();
    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL , orphanRemoval = true)
    private Set<Comentario> comentarios = new HashSet<Comentario>();

    public Set<Foto> getFotos() {
        return fotos;
    }


    public void setFotos(Set<Foto> fotos) {
        this.fotos = fotos;
    }

    public void addPicture(@NotNull Foto foto){

            this.fotos.add(foto);

    }

    public Producto(){

    }

    public Producto(String nombre, BigDecimal precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public Set<CarroCompra_Producto> getCarroCompra_productos() {
        return carroCompra_productos;
    }

    public void setCarroCompra_productos(Set<CarroCompra_Producto> carroCompra_productos) {
        this.carroCompra_productos = carroCompra_productos;

    }
    public boolean deleteComentario(String id_comentario){
        for (Comentario aux: this.comentarios
             ) {
            if (aux.getId() == Long.parseLong(id_comentario)){
                Comentario borrar = aux;
                this.comentarios.remove(aux);
                aux=null;
                break;
            }
            return true;
        }

        return true;
    }
    public boolean addComentario(String comentatio, String nombre, String id_cliente){
        this.comentarios.add(new Comentario(nombre,comentatio,id_cliente));
        return true;
    }

    public Set<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Set<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public int getId() {
        return id;
    }

    private Foto buscarFoto(Long ID) {
        Foto foto = null;

        for (Foto auxFoto : this.fotos) {
            if (auxFoto.getId() == ID) {
                foto = auxFoto;
            }
        }

        return foto;
    }

    public boolean eliminarFoto(Long ID) {
        boolean ok = false;
        Foto foto = this.buscarFoto(ID);

        if (foto != null) {
            this.fotos.remove(foto);
            ok = true;
        }

        return ok;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
