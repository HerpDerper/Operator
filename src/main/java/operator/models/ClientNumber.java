package operator.models;

import javax.persistence.*;

@Entity
public class ClientNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idClientNumber;

    @ManyToOne
    @JoinColumn(name = "clientId", referencedColumnName = "idClient")
    private Client client;

    @OneToOne
    @JoinColumn(name = "phoneNumberId", referencedColumnName = "idPhoneNumber")
    private PhoneNumber phoneNumber;

    public ClientNumber() {
    }

    public ClientNumber(Client client, PhoneNumber phoneNumber) {
        this.client = client;
        this.phoneNumber = phoneNumber;
    }

    public Long getIdClientNumber() {
        return idClientNumber;
    }

    public void setIdClientNumber(Long idClientNumber) {
        this.idClientNumber = idClientNumber;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}