package com.library.pocket.bills.pocket_bills.feature.bill.service;

import com.library.pocket.bills.pocket_bills.common.exception.ResourceNotFoundException;
import com.library.pocket.bills.pocket_bills.feature.bill.dto.response.BillResponse;
import com.library.pocket.bills.pocket_bills.feature.bill.entity.Bill;
import com.library.pocket.bills.pocket_bills.feature.bill.repository.BillRepository;
import com.library.pocket.bills.pocket_bills.feature.meter.entity.Meter;
import com.library.pocket.bills.pocket_bills.feature.meter.entity.MeterTariff;
import com.library.pocket.bills.pocket_bills.feature.reading.entity.MeterReading;
import com.library.pocket.bills.pocket_bills.feature.user.entity.User;
import com.library.pocket.bills.pocket_bills.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class BillService {

    private final UserRepository userRepository;
    private final BillRepository billRepository;

    @Transactional
    public BillResponse createBill(MeterReading reading, MeterTariff tariff) {
        if (billRepository.existsByMeterReading(reading)) {
            throw new IllegalArgumentException("Bill for this reading already exists");
        }

        BigDecimal amount = reading.getConsumption().multiply(tariff.getRate());

        Bill bill = Bill.builder()
                .user(reading.getMeter().getUser())
                .meterReading(reading)
                .tariffRate(tariff.getRate())
                .amount(amount)
                .periodMonth(reading.getPeriodMonth())
                .periodYear(reading.getPeriodYear())
                .build();

        Bill savedBill = billRepository.save(bill);
        return toResponse(savedBill);
    }

    public BillResponse getBillByReading(MeterReading reading) {
        Bill bill = billRepository.findByMeterReading(reading)
                .orElseThrow(() -> new ResourceNotFoundException("Bill"));

        return toResponse(bill);
    }

    public List<BillResponse> getBills(
            Integer periodYear,
            Integer periodMonth,
            Authentication authentication
    ) {
        User user = getUserFromAuthentication(authentication);

        return billRepository.findUserBillsByPeriod(user, periodYear, periodMonth)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public BillResponse createOrUpdateMeteredBill(MeterReading reading, MeterTariff tariff) {
        Bill bill = billRepository.findByMeterReading(reading)
                .orElseGet(() -> Bill.builder()
                        .user(reading.getMeter().getUser())
                        .meter(reading.getMeter())
                        .meterReading(reading)
                        .periodMonth(reading.getPeriodMonth())
                        .periodYear(reading.getPeriodYear())
                        .build());

        bill.setTariffRate(tariff.getRate());
        bill.setConsumption(reading.getConsumption());
        bill.setAmount(reading.getConsumption().multiply(tariff.getRate()));

        Bill savedBill = billRepository.save(bill);
        return toResponse(savedBill);
    }

    @Transactional
    public BillResponse createOrUpdateFixedBill(
            Meter meter,
            MeterTariff tariff,
            Integer periodMonth,
            Integer periodYear
    ) {
        Bill bill = billRepository.findByMeterAndPeriodYearAndPeriodMonth(meter, periodYear, periodMonth)
                .orElseGet(() -> Bill.builder()
                        .user(meter.getUser())
                        .meter(meter)
                        .meterReading(null)
                        .periodMonth(periodMonth)
                        .periodYear(periodYear)
                        .build());

        bill.setTariffRate(tariff.getRate());
        bill.setConsumption(null);
        bill.setAmount(tariff.getRate());

        Bill savedBill = billRepository.save(bill);
        return toResponse(savedBill);
    }



    private User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
    }


    private BillResponse toResponse(Bill bill) {
        Meter meter = bill.getMeter();
        MeterReading reading = bill.getMeterReading();

        BigDecimal previousValue = null;
        BigDecimal currentValue = null;

        if (reading != null) {
            currentValue = reading.getValue();

            if (reading.getConsumption() != null) {
                previousValue = reading.getValue().subtract(reading.getConsumption());
            }
        }

        return new BillResponse(
                bill.getId(),
                reading != null ? reading.getId() : null,
                meter.getId(),
                meter.getService().getId(),
                meter.getService().getName(),
                meter.getService().getUnit(),
                meter.getService().getBillingType(),
                previousValue,
                currentValue,
                bill.getConsumption(),
                bill.getTariffRate(),
                bill.getAmount(),
                bill.getPeriodMonth(),
                bill.getPeriodYear(),
                bill.getCalculatedAt()
        );
    }
}
