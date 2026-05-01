package com.library.pocket.bills.pocket_bills.feature.meter.service;

import com.library.pocket.bills.pocket_bills.common.exception.ResourceNotFoundException;
import com.library.pocket.bills.pocket_bills.feature.meter.dto.request.MeterCreateRequest;
import com.library.pocket.bills.pocket_bills.feature.meter.dto.request.MeterUpdateRequest;
import com.library.pocket.bills.pocket_bills.feature.meter.dto.response.MeterResponse;
import com.library.pocket.bills.pocket_bills.feature.meter.entity.Meter;
import com.library.pocket.bills.pocket_bills.feature.meter.mapper.MeterMapper;
import com.library.pocket.bills.pocket_bills.feature.meter.repository.MeterRepository;
import com.library.pocket.bills.pocket_bills.feature.reading.service.MeterReadingService;
import com.library.pocket.bills.pocket_bills.feature.service.entity.Service;
import com.library.pocket.bills.pocket_bills.feature.service.repository.ServiceRepository;
import com.library.pocket.bills.pocket_bills.feature.user.entity.User;
import com.library.pocket.bills.pocket_bills.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class MeterService {

    private final UserRepository userRepository;
    private final MeterRepository meterRepository;
    private final ServiceRepository serviceRepository;
    private final MeterMapper meterMapper;
    private final MeterTariffService meterTariffService;
    private final MeterReadingService meterReadingService;

    @Transactional
    public MeterResponse createMeter(MeterCreateRequest request, Authentication authentication) {

        User user = getUserFromAuthentication(authentication);

        Service service = serviceRepository.findById(request.serviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service"));

        Meter meter = Meter.builder()
                .user(user)
                .service(service)
                .isActive(true)
                .build();

        Meter savedMeter = meterRepository.save(meter);
        meterTariffService.createInitialTariff(savedMeter, request.initialTariff());
        meterReadingService.createInitialReading(savedMeter, request.initialReading());

        return meterMapper.toResponse(savedMeter);
    }

    public List<MeterResponse> getMyMeters(Authentication authentication) {
        return meterRepository.findAllByUser(getUserFromAuthentication(authentication))
                .stream()
                .map(meterMapper::toResponse)
                .toList();
    }

    @Transactional
    public MeterResponse updateMeter(Long meterId, MeterUpdateRequest request, Authentication authentication) {
        User user = getUserFromAuthentication(authentication);

        Meter meter = meterRepository.findByIdAndUser(meterId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Meter"));

        Service service = serviceRepository.findById(request.serviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service"));

        meter.setService(service);
        meter.setIsActive(request.isActive());

        Meter savedMeter = meterRepository.save(meter);
        return meterMapper.toResponse(savedMeter);
    }

    @Transactional
    public void deleteMeter(Long meterId, Authentication authentication) {
        User user = getUserFromAuthentication(authentication);

        Meter meter = meterRepository.findByIdAndUser(meterId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Meter"));

        meter.setIsActive(false);
        meterRepository.save(meter);
    }


    private User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
    }
}
