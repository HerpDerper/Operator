package operator.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUser;

    @NotBlank(message = "Логин не должен быть пустым или состоять из одних лишь пробелов")
    @Size(min = 1, max = 16, message = "Логин должен быть от 1 до 16 символов")
    private String username;

    @Size(min = 8, message = "Пароль должен быть от 8 символов")
    private String password;

    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "userRole", joinColumns = @JoinColumn(name = "userId"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Booking> bookingList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Employee> employeeList;

    public User() {
    }

    public User(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}