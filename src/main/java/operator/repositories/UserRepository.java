package operator.repositories;

import operator.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

   User findUserByUsername(String username);

}
