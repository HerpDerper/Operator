package operator.repositories;

import operator.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Iterable<Product> findByNameContains(String name);

    Product findProductByName(String name);

}