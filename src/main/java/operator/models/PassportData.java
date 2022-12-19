package operator.models;

import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
public class PassportData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPassportData;

    @Pattern(regexp = "[а-яА-Я]{1,30}", message = "Имя должно быть от 1 до 30 символов и состоять только из букв")
    private String name;

    @Pattern(regexp = "[а-яА-Я]{1,30}", message = "Фамилия должна быть от 1 до 30 символов и состоять только из букв")
    private String surname;

    @Pattern(regexp = "[а-яА-Я]{0,30}", message = "Отчество должно быть от 0 до 30 символов и состоять только из букв")
    private String patronymic;

    @NotNull(message = "Дата рождения не должна быть пустой")
    @Past(message = "Дата рождения не может быть будущей")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateBirth;

    @NotBlank(message = "Место рождения не должно быть пустой, или состоять из одних лишь пробелов")
    private String placeBirth;

    @Pattern(regexp = "[0-9]{3}-[0-9]{3}", message = "Код подразделения должен состоять из 6 цифр и иметь данный формат: 999-999")
    private String issueCode;

    @NotBlank(message = "Орган выдачи паспорта не должен быть пустым")
    private String issued;

    @NotNull(message = "Дата  не должна быть пустой")
    @Past(message = "Дата выдачи паспорта не может быть будущей")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date issueDate;

    @Pattern(regexp = "[0-9]{4}", message = "Некорректный ввод номера паспорта")
    private String passportSeries;

    @Pattern(regexp = "[0-9]{6}", message = "Некорректный ввод серии паспорта")
    private String passportNumber;

    public PassportData() {
    }

    public PassportData(String name, String surname, String patronymic, Date dateBirth, String placeBirth, String issueCode,
                        String issued, Date issueDate, String passportSeries, String passportNumber) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.dateBirth = dateBirth;
        this.placeBirth = placeBirth;
        this.issueCode = issueCode;
        this.issued = issued;
        this.issueDate = issueDate;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
    }

    public Long getIdPassportData() {
        return idPassportData;
    }

    public void setIdPassportData(Long idPassportData) {
        this.idPassportData = idPassportData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getPlaceBirth() {
        return placeBirth;
    }

    public void setPlaceBirth(String placeBirth) {
        this.placeBirth = placeBirth;
    }

    public String getIssueCode() {
        return issueCode;
    }

    public void setIssueCode(String issueCode) {
        this.issueCode = issueCode;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getPassportSeries() {
        return passportSeries;
    }

    public void setPassportSeries(String passportSeries) {
        this.passportSeries = passportSeries;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

}