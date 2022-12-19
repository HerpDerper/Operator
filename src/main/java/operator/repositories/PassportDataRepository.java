package operator.repositories;

import operator.models.PassportData;
import org.springframework.data.repository.CrudRepository;

public interface PassportDataRepository extends CrudRepository<PassportData, Long> {

    PassportData findPassportDataByPassportSeriesAndPassportNumber(String passportSeries, String passportNumber);

}