package com.library.pocket.bills.pocket_bills.feature.reading.entity;

import com.library.pocket.bills.pocket_bills.feature.meter.entity.Meter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "meter_readings",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_meter_period",
                columnNames = {"meter_id", "period_year", "period_month"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_id", nullable = false)
    private Meter meter;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal value;

    @Column(precision = 12, scale = 3)
    private BigDecimal consumption;        // може бути null для першого показника

    @Column(name = "period_month", nullable = false)
    private Integer periodMonth;           // 1-12

    @Column(name = "period_year", nullable = false)
    private Integer periodYear;

    @Column(name = "reading_date")
    @Builder.Default
    private LocalDateTime readingDate = LocalDateTime.now();
}