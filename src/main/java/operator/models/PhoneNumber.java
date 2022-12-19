package operator.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPhoneNumber;

    @Column(unique = true)
    private String number;

    @Pattern(regexp = "[0-9]{18}")
    @Column(unique = true)
    private String ICCID;

    @Pattern(regexp = "[0-9]{8}")
    @Column(unique = true)
    private String PUK;

    @NotNull
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "tariffId", referencedColumnName = "idTariff")
    private Tariff tariff;

    public PhoneNumber() {
    }

    public PhoneNumber(String number, String ICCID, String PUK, boolean status, Tariff tariff) {
        this.number = number;
        this.ICCID = ICCID;
        this.PUK = PUK;
        this.status = status;
        this.tariff = tariff;
    }

    public Long getIdPhoneNumber() {
        return idPhoneNumber;
    }

    public void setIdPhoneNumber(Long idPhoneNumber) {
        this.idPhoneNumber = idPhoneNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getICCID() {
        return ICCID;
    }

    public void setICCID(String ICCID) {
        this.ICCID = ICCID;
    }

    public String getPUK() {
        return PUK;
    }

    public void setPUK(String PUK) {
        this.PUK = PUK;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

}
