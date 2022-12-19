package operator.repositories;

import operator.models.*;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository  extends CrudRepository<Image, Long> {

    Image findImageByProduct(Product product);

}