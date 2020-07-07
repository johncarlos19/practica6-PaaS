package logico;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CarrocompraProductoId implements Serializable {

    @Column(name = "CarroCompraId")
    private Long carroCompraId;

    @Column(name = "ProductoId")
    private int productoId;

    public CarrocompraProductoId() {}

    public CarrocompraProductoId(
            Long carroCompraId,
            int productoId) {
        this.carroCompraId = carroCompraId;
        this.productoId = productoId;
    }

    //Getters omitted for brevity

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CarrocompraProductoId that = (CarrocompraProductoId) o;
        return Objects.equals(carroCompraId, that.carroCompraId) &&
                Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carroCompraId, productoId);
    }

    public Long getcarroCompraId() {
        return carroCompraId;
    }

    public void setcarroCompraId(Long carroCompraId) {
        this.carroCompraId = carroCompraId;
    }

    public int getproductoId() {
        return productoId;
    }

    public void setproductoId(int productoId) {
        this.productoId = productoId;
    }
}
