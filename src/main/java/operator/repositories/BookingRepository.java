package operator.repositories;

import operator.models.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Long> {

    Iterable<Booking> findByUserUsername(String username);

}