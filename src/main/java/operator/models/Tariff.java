package operator.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTariff;

    @NotBlank(message = "Наименование тарифа не должено быть пустым или состоять из одних лишь пробелов")
    @Size(min = 1, max = 30, message = "Наименование тарифа должно быть от 1 до 30 символов")
    private String name;

    @Min(value = 1, message = "Цена тарифа должна быть больше или равна 1")
    @NotNull(message = "Цена тарифа не должна быть пустой")
    private int priceMount;

    @Min(value = 1, message = "Количество минут должно быть больше или равно 1")
    @NotNull(message = "Количество минут не должно быть пустым")
    private int minutes;

    @Min(value = 0, message = "Количество гигабайт должно быть больше или равно 0")
    @NotNull(message = "Количество гигабайт не должно быть пустым")
    private int GB;

    @Min(value = 0, message = "Количество SMS должно быть больше или равно 0")
    @NotNull(message = "Количество SMS не должно быть пустым")
    private int SMS;

    public Tariff() {
    }

    public Tariff(String name, int priceMount, int minutes, int GB, int SMS) {
        this.name = name;
        this.priceMount = priceMount;
        this.minutes = minutes;
        this.GB = GB;
        this.SMS = SMS;
    }

    public Long getIdTariff() {
        return idTariff;
    }

    public void setIdTariff(Long idTariff) {
        this.idTariff = idTariff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriceMount() {
        return priceMount;
    }

    public void setPriceMount(int priceMount) {
        this.priceMount = priceMount;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getGB() {
        return GB;
    }

    public void setGB(int GB) {
        this.GB = GB;
    }

    public int getSMS() {
        return SMS;
    }

    public void setSMS(int SMS) {
        this.SMS = SMS;
    }

}