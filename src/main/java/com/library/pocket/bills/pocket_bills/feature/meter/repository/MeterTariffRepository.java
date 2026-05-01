package com.library.pocket.bills.pocket_bills.feature.meter.repository;

import com.library.pocket.bills.pocket_bills.feature.meter.entity.Meter;
import com.library.pocket.bills.pocket_bills.feature.meter.entity.MeterTariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeterTariffRepository extends JpaRepository<MeterTariff, Long> {

    List<MeterTariff> findAllByMeterOrderByEstablishedDateDesc(Meter meter);

    Optional<MeterTariff> findByMeterAndEndDateIsNull(Meter meter);

    @Query("""
            select tariff
            from MeterTariff tariff
            where tariff.meter = :meter
              and tariff.establishedDate <= :periodDate
              and (tariff.endDate is null or tariff.endDate >= :periodDate)
            order by tariff.establishedDate desc
            """)
    List<MeterTariff> findActualTariffs(
            Meter meter,
            LocalDate periodDate,
            Pageable pageable
    );
}
