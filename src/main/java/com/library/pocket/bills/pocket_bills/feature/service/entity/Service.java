package com.library.pocket.bills.pocket_bills.feature.service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = true, length = 30)
    private String unit;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "billing_type", nullable = false, length = 20)
    private BillingType billingType = BillingType.METERED;
}