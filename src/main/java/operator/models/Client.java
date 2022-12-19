package operator.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idClient;

    @NotBlank(message = "Адрес не должен быть пустым или состоять из одних лишь пробелов")
    private String address;

    @OneToOne
    @JoinColumn(name = "passportDataId", referencedColumnName = "idPassportData")
    private PassportData passportData;

    @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE)
    private List<ClientNumber> clientNumberList;

    public Client() {
    }

    public Client( String address, PassportData passportData) {
        this.address = address;
        this.passportData = passportData;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PassportData getPassportData() {
        return passportData;
    }

    public void setPassportData(PassportData passportData) {
        this.passportData = passportData;
    }

    public List<ClientNumber> getClientNumberList() {
        return clientNumberList;
    }

    public void setClientNumberList(List<ClientNumber> clientNumberList) {
        this.clientNumberList = clientNumberList;
    }

}