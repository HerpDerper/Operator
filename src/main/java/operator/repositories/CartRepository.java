package operator.repositories;

import operator.models.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Long> { }