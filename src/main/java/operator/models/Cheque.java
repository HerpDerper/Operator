package operator.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Cheque {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCheque;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    @ManyToOne
    @JoinColumn(name = "employeeId", referencedColumnName = "idEmployee")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "idProduct")
    private Product product;

    @Min(value = 0, message = "Количество товара должно быть больше или равно 0")
    @NotNull(message = "Количество товара не должно быть пустым")
    private int productCount;

    public Cheque() {
    }

    public Cheque(Date dateCreation, Employee employee, Product product, int productCount) {
        this.dateCreation = dateCreation;
        this.employee = employee;
        this.product = product;
        this.productCount = productCount;
    }

    public Long getIdCheque() {
        return idCheque;
    }

    public void setIdCheque(Long idCheque) {
        this.idCheque = idCheque;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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