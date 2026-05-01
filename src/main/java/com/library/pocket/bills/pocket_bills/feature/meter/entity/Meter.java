package com.library.pocket.bills.pocket_bills.feature.meter.entity;

import com.library.pocket.bills.pocket_bills.feature.service.entity.Service;
import com.library.pocket.bills.pocket_bills.feature.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "meters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}