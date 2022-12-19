package operator.repositories;

import operator.models.Cheque;
import org.springframework.data.repository.CrudRepository;

public interface ChequeRepository extends CrudRepository<Cheque, Long> {

    Iterable<Cheque> findByProductName (String name);

}
