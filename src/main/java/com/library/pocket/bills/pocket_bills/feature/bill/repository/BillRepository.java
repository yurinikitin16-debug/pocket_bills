package com.library.pocket.bills.pocket_bills.feature.bill.repository;

import com.library.pocket.bills.pocket_bills.feature.bill.entity.Bill;
import com.library.pocket.bills.pocket_bills.feature.meter.entity.Meter;
import com.library.pocket.bills.pocket_bills.feature.reading.entity.MeterReading;
import com.library.pocket.bills.pocket_bills.feature.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Optional<Bill> findByMeterReading(MeterReading meterReading);

    boolean existsByMeterReading(MeterReading meterReading);

    Optional<Bill> findByMeterAndPeriodYearAndPeriodMonth(
            Meter meter,
            Integer periodYear,
            Integer periodMonth
    );

    @Query("""
        select bill
        from Bill bill
        join fetch bill.meter meter
        join fetch meter.service service
        left join fetch bill.meterReading reading
        where bill.user = :user
          and bill.periodYear = :periodYear
          and (:periodMonth is null or bill.periodMonth = :periodMonth)
        order by bill.periodYear desc, bill.periodMonth desc, meter.id asc
        """)
    List<Bill> findUserBillsByPeriod(User user, Integer periodYear, Integer periodMonth);

}