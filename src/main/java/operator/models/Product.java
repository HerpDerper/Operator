package operator.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idProduct;

    @NotBlank(message = "Наименование товара не должено быть пустым или состоять из одних лишь пробелов")
    @Size(min = 1, max = 30, message = "Наименование товара не должено быть от 1 до 30 символов")
    private String name;

    @NotBlank(message = "Описание товара не должено быть пустым или состоять из одних лишь пробелов")
    @Size(min = 1, message = "Описание товара не должено содержать минимум 1 символ")
    private String description;

    @Min(value = 1, message = "Цена товара должна быть больше 0")
    @NotNull(message = "Цена товара не должна быть пустой")
    private int price;

    @Min(value = 0, message = "Количество товара должно быть больше или равно 0")
    @NotNull(message = "Количество товара не должна быть пустым")
    private int count;

    @Pattern(regexp = "[0-9] [0-9]{6} [0-9]{6}")
    private String characteristic;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "imageId")
    private Image image;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Cart> cartList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Booking> bookingList;

    public Product() {
    }

    public Product(String name, String description, int price, int count, String characteristic) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.count = count;
        this.characteristic = characteristic;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    public List<Booking> getOrderList() {
        return bookingList;
    }

    public void setOrderList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

}