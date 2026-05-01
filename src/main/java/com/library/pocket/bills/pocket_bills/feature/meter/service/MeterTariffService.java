package com.library.pocket.bills.pocket_bills.feature.meter.service;

import com.library.pocket.bills.pocket_bills.common.exception.ResourceNotFoundException;
import com.library.pocket.bills.pocket_bills.feature.meter.dto.request.MeterTariffCreateRequest;
import com.library.pocket.bills.pocket_bills.feature.meter.dto.response.MeterTariffResponse;
import com.library.pocket.bills.pocket_bills.feature.meter.entity.Meter;
import com.library.pocket.bills.pocket_bills.feature.meter.entity.MeterTariff;
import com.library.pocket.bills.pocket_bills.feature.meter.mapper.MeterTariffMapper;
import com.library.pocket.bills.pocket_bills.feature.meter.repository.MeterRepository;
import com.library.pocket.bills.pocket_bills.feature.meter.repository.MeterTariffRepository;
import com.library.pocket.bills.pocket_bills.feature.user.entity.User;
import com.library.pocket.bills.pocket_bills.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class MeterTariffService {

    private final UserRepository userRepository;
    private final MeterRepository meterRepository;
    private final MeterTariffRepository meterTariffRepository;
    private final MeterTariffMapper meterTariffMapper;

    @Transactional
    public MeterTariffResponse createMeterTariff(
            Long meterId,
            MeterTariffCreateRequest request,
            Authentication authentication
    ) {
        Meter meter = getUserMeter(meterId, authentication);
        LocalDate establishedDate = resolveEstablishedDate(request);

        closeCurrentTariffIfExists(meter, establishedDate);

        MeterTariff savedTariff = createTariff(meter, request, establishedDate);
        return meterTariffMapper.toResponse(savedTariff);
    }

    @Transactional
    public MeterTariff createInitialTariff(Meter meter, MeterTariffCreateRequest request) {
        return createTariff(meter, request, resolveEstablishedDate(request));
    }

    public List<MeterTariffResponse> getMeterTariffs(Long meterId, Authentication authentication) {
        Meter meter = getUserMeter(meterId, authentication);

        return meterTariffRepository.findAllByMeterOrderByEstablishedDateDesc(meter)
                .stream()
                .map(meterTariffMapper::toResponse)
                .toList();
    }

    public MeterTariffResponse getCurrentMeterTariff(Long meterId, Authentication authentication) {
        Meter meter = getUserMeter(meterId, authentication);

        MeterTariff tariff = meterTariffRepository.findByMeterAndEndDateIsNull(meter)
                .orElseThrow(() -> new ResourceNotFoundException("Current meter tariff"));

        return meterTariffMapper.toResponse(tariff);
    }

    private MeterTariff createTariff(
            Meter meter,
            MeterTariffCreateRequest request,
            LocalDate establishedDate
    ) {
        MeterTariff meterTariff = MeterTariff.builder()
                .meter(meter)
                .rate(request.rate())
                .establishedDate(establishedDate)
                .endDate(null)
                .build();

        return meterTariffRepository.save(meterTariff);
    }

    private LocalDate resolveEstablishedDate(MeterTariffCreateRequest request) {
        return request.establishedDate() != null
                ? request.establishedDate()
                : LocalDate.now();
    }

    private void closeCurrentTariffIfExists(Meter meter, LocalDate newTariffStartDate) {
        meterTariffRepository.findByMeterAndEndDateIsNull(meter)
                .ifPresent(currentTariff -> {
                    if (!currentTariff.getEstablishedDate().isBefore(newTariffStartDate)) {
                        throw new IllegalArgumentException("New tariff must start after current tariff");
                    }

                    currentTariff.setEndDate(newTariffStartDate.minusDays(1));
                    meterTariffRepository.save(currentTariff);
                });
    }

    private Meter getUserMeter(Long meterId, Authentication authentication) {
        User user = getUserFromAuthentication(authentication);

        return meterRepository.findByIdAndUser(meterId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Meter"));
    }

    private User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
    }
}
