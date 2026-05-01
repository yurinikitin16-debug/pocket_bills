package com.library.pocket.bills.pocket_bills.feature.service.repository;

import com.library.pocket.bills.pocket_bills.feature.service.entity.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    Optional<Service> findByCode(String code);

    List<Service> findAll(Sort sort);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);
}
