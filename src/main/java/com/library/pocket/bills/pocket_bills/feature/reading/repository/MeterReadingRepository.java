package com.library.pocket.bills.pocket_bills.feature.reading.repository;

import com.library.pocket.bills.pocket_bills.feature.meter.entity.Meter;
import com.library.pocket.bills.pocket_bills.feature.reading.entity.MeterReading;
import com.library.pocket.bills.pocket_bills.feature.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {

    boolean existsByMeter(Meter meter);
    
    Optional<MeterReading> findByMeterAndPeriodYearAndPeriodMonth(
            Meter meter,
            Integer periodYear,
            Integer periodMonth
    );

    boolean existsByMeterAndPeriodYearAndPeriodMonth(
            Meter meter,
            Integer periodYear,
            Integer periodMonth
    );

    @Query("""
            select reading
            from MeterReading reading
            where reading.meter = :meter
              and (
                    reading.periodYear < :periodYear
                    or (reading.periodYear = :periodYear and reading.periodMonth < :periodMonth)
              )
            order by reading.periodYear desc, reading.periodMonth desc
            """)
    List<MeterReading> findPreviousReadings(
            Meter meter,
            Integer periodYear,
            Integer periodMonth,
            Pageable pageable
    );

    @Query("""
            select reading
            from MeterReading reading
            where reading.meter.user = :user
              and reading.periodYear = :periodYear
              and (:periodMonth is null or reading.periodMonth = :periodMonth)
            order by reading.periodYear desc, reading.periodMonth desc, reading.meter.id asc
            """)
    List<MeterReading> findUserReadingsByPeriod(
            User user,
            Integer periodYear,
            Integer periodMonth
    );
}
