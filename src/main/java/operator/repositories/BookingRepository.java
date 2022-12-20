package operator.repositories;

import operator.models.Booking;
import operator.models.PhoneNumber;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Long> {

    Iterable<Booking> findByUserUsername(String username);

    Iterable<Booking> findByPhoneNumber(PhoneNumber phoneNumber);

}