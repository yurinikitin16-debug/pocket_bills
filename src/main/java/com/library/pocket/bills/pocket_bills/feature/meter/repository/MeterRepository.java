package com.library.pocket.bills.pocket_bills.feature.meter.repository;

import com.library.pocket.bills.pocket_bills.feature.meter.entity.Meter;
import com.library.pocket.bills.pocket_bills.feature.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterRepository extends JpaRepository<Meter, Long> {

    List<Meter> findAllByUser(User user);

    List<Meter> findAllByUserAndIsActiveTrue(User user);

    Optional<Meter> findByIdAndUser(Long id, User user);
}
