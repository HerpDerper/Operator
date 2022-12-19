package operator.repositories;

import operator.models.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> { }