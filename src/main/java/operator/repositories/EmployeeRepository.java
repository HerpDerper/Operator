package operator.repositories;

import operator.models.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    Iterable<Employee> findByEmailContains(String email);

    Employee findEmployeeByUserUsername(String username);

}