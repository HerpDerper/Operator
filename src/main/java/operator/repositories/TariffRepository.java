package operator.repositories;

import operator.models.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepository extends JpaRepository<Tariff, Long> {

    Iterable<Tariff> findByNameContains (String name);

    Tariff findTariffByName (String name);

}