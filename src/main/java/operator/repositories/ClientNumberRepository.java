package operator.repositories;

import operator.models.ClientNumber;
import org.springframework.data.repository.CrudRepository;

public interface ClientNumberRepository extends CrudRepository<ClientNumber, Long> {

    Iterable<ClientNumber> findByPhoneNumberNumberContains(String number);

}