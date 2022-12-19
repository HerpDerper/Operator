package operator.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idEmployee;

    @Pattern(regexp = "[0-9]{3}-[0-9]{3}-[0-9]{3} [0-9]{2}", message = "СНИЛС должен состоять из 11 цифр и иметь данный формат: 999-999-999 99")
    private String SNILS;

    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7}$", message = "Номера телефона должен состоять из 11 цифр и иметь данный формат: +7(999)9999999 или 8(999)9999999")
    private String phone;

    @Email(message = "Некорректный ввод email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "idUser")
    private User user;

    @OneToOne
    @JoinColumn(name = "passportDataId", referencedColumnName = "idPassportData")
    private PassportData passportData;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE)
    private List<Cheque> chequeList;

    public Employee() {
    }

    public Employee(String SNILS, String phone, String email, User user, PassportData passportData) {
        this.SNILS = SNILS;
        this.phone = phone;
        this.email = email;
        this.user = user;
        this.passportData = passportData;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getSNILS() {
        return SNILS;
    }

    public void setSNILS(String SNILS) {
        this.SNILS = SNILS;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PassportData getPassportData() {
        return passportData;
    }

    public void setPassportData(PassportData passportData) {
        this.passportData = passportData;
    }

    public List<Cheque> getChequeList() {
        return chequeList;
    }

    public void setChequeList(List<Cheque> chequeList) {
        this.chequeList = chequeList;
    }
}