package operator.models;

import javax.persistence.*;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idBooking;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "idUser")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "idProduct")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "phoneNumberId", referencedColumnName = "idPhoneNumber")
    private PhoneNumber phoneNumber;

    public Booking() {
    }

    public Booking(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public Booking(User user, PhoneNumber phoneNumber) {
        this.user = user;
        this.phoneNumber = phoneNumber;
    }

    public Long getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(Long idBooking) {
        this.idBooking = idBooking;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}