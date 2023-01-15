package operator.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCart;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "idProduct")
    private Product product;

    @Min(value = 0, message = "Количество товара должно быть больше или равно 0")
    @NotNull(message = "Количество товара не должно быть пустым")
    private int productCount;

    public Cart() {
    }

    public Cart(Product product, int productCount) {
        this.product = product;
        this.productCount = productCount;
    }

    public Long getIdCart() {
        return idCart;
    }

    public void setIdCart(Long idCart) {
        this.idCart = idCart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

}