package operator.repositories;

import operator.models.PhoneNumber;
import operator.models.Tariff;
import org.springframework.data.repository.CrudRepository;

public interface PhoneNumberRepository extends CrudRepository<PhoneNumber, Long> {

    Iterable<PhoneNumber> findByNumberContains(String number);

    Iterable<PhoneNumber> findByTariff(Tariff tariff);

    Iterable<PhoneNumber> findByTariffAndNumberContains(Tariff tariff, String number);

    PhoneNumber findPhoneNumberByNumber(String number);

}