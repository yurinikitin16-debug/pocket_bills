package com.library.pocket.bills.pocket_bills.feature.reading.service;

import com.library.pocket.bills.pocket_bills.common.exception.ResourceNotFoundException;
import com.library.pocket.bills.pocket_bills.feature.bill.dto.response.BillResponse;
import com.library.pocket.bills.pocket_bills.feature.bill.service.BillService;
import com.library.pocket.bills.pocket_bills.feature.meter.entity.Meter;
import com.library.pocket.bills.pocket_bills.feature.meter.entity.MeterTariff;
import com.library.pocket.bills.pocket_bills.feature.meter.repository.MeterRepository;
import com.library.pocket.bills.pocket_bills.feature.meter.repository.MeterTariffRepository;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.request.BulkMeterReadingRequest;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.request.MeterInitialReadingRequest;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.request.MeterReadingSubmitItemRequest;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.response.BulkMeterReadingResponse;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.response.MeterReadingFormItemResponse;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.response.MeterReadingResponse;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.response.ReadingFormDataResponse;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.response.SubmittedReadingResponse;
import com.library.pocket.bills.pocket_bills.feature.reading.entity.MeterReading;
import com.library.pocket.bills.pocket_bills.feature.reading.repository.MeterReadingRepository;
import com.library.pocket.bills.pocket_bills.feature.service.entity.BillingType;
import com.library.pocket.bills.pocket_bills.feature.user.entity.User;
import com.library.pocket.bills.pocket_bills.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class MeterReadingService {

    private final UserService currentUserService;
    private final MeterRepository meterRepository;
    private final MeterTariffRepository meterTariffRepository;
    private final MeterReadingRepository meterReadingRepository;
    private final BillService billService;

    public ReadingFormDataResponse getFormData(
            Integer periodMonth,
            Integer periodYear,
            Authentication authentication
    ) {
        User user = currentUserService.getCurrentUser(authentication);
        LocalDate periodDate = toPeriodDate(periodYear, periodMonth);

        List<MeterReadingFormItemResponse> meters = meterRepository.findAllByUserAndIsActiveTrue(user)
                .stream()
                .map(meter -> buildFormItem(meter, periodYear, periodMonth, periodDate))
                .toList();

        return new ReadingFormDataResponse(periodMonth, periodYear, meters);
    }

    @Transactional
    public BulkMeterReadingResponse submitBulkReadings(
            BulkMeterReadingRequest request,
            Authentication authentication
    ) {
        User user = currentUserService.getCurrentUser(authentication);
        LocalDate periodDate = toPeriodDate(request.periodYear(), request.periodMonth());

        Map<Long, MeterReadingSubmitItemRequest> readingItemsByMeterId = request.readings()
                .stream()
                .collect(Collectors.toMap(MeterReadingSubmitItemRequest::meterId, item -> item));

        List<MeterReadingResponse> readingResponses = new ArrayList<>();
        List<BillResponse> billResponses = new ArrayList<>();

        List<Meter> activeMeters = meterRepository.findAllByUserAndIsActiveTrue(user);

        for (Meter meter : activeMeters) {
            MeterTariff tariff = getCurrentTariff(meter);

            if (meter.getService().getBillingType() == BillingType.FIXED) {
                BillResponse bill = billService.createOrUpdateFixedBill(
                        meter,
                        tariff,
                        request.periodMonth(),
                        request.periodYear()
                );
                billResponses.add(bill);
                continue;
            }

            MeterReadingSubmitItemRequest item = readingItemsByMeterId.get(meter.getId());

            if (item == null) {
                continue;
            }

            BigDecimal previousValue = getPreviousValue(meter, request.periodYear(), request.periodMonth());
            ResolvedReading resolved = resolveReading(item, previousValue);

            MeterReading savedReading = saveOrUpdateReading(
                    meter,
                    resolved,
                    request.periodMonth(),
                    request.periodYear()
            );

            readingResponses.add(toReadingResponse(savedReading, previousValue));

            BillResponse bill = billService.createOrUpdateMeteredBill(savedReading, tariff);
            billResponses.add(bill);
        }

        return new BulkMeterReadingResponse(
                request.periodMonth(),
                request.periodYear(),
                readingResponses,
                billResponses
        );
    }

    public List<MeterReadingResponse> getReadingsHistory(
            Integer periodYear,
            Integer periodMonth,
            Authentication authentication
    ) {
        User user = currentUserService.getCurrentUser(authentication);

        return meterReadingRepository.findUserReadingsByPeriod(user, periodYear, periodMonth)
                .stream()
                .map(reading -> toReadingResponse(
                        reading,
                        getPreviousValue(reading.getMeter(), reading.getPeriodYear(), reading.getPeriodMonth())
                ))
                .toList();
    }

    @Transactional
    public MeterReadingResponse createInitialReading(
            Long meterId,
            MeterInitialReadingRequest request,
            Authentication authentication
    ) {
        User user = currentUserService.getCurrentUser(authentication);
        Meter meter = getUserMeter(meterId, user);

        return createInitialReading(meter, request);
    }

    @Transactional
    public MeterReadingResponse createInitialReading(Long meterId, MeterInitialReadingRequest request) {
        Meter meter = meterRepository.findById(meterId).orElseThrow(() -> new IllegalArgumentException("Meter not found"));
        return createInitialReading(meter, request);
    }

    @Transactional
    public MeterReadingResponse createInitialReading(
            Meter meter,
            MeterInitialReadingRequest request
    ) {
        if (request == null) {
            return null;
        }

        if (meterReadingRepository.existsByMeter(meter)) {
            throw new IllegalArgumentException("Initial reading can be created only before regular readings");
        }

        MeterReading reading = MeterReading.builder()
                .meter(meter)
                .value(request.value())
                .consumption(null)
                .periodMonth(request.periodMonth())
                .periodYear(request.periodYear())
                .build();

        MeterReading savedReading = meterReadingRepository.save(reading);

        return toReadingResponse(savedReading, null);
    }

    private MeterReadingFormItemResponse buildFormItem(
            Meter meter,
            Integer periodYear,
            Integer periodMonth,
            LocalDate periodDate
    ) {
        BigDecimal previousValue = getPreviousValue(meter, periodYear, periodMonth);
        MeterTariff tariff = getCurrentTariff(meter);

        MeterReading submittedReading = meterReadingRepository
                .findByMeterAndPeriodYearAndPeriodMonth(meter, periodYear, periodMonth)
                .orElse(null);

        SubmittedReadingResponse submitted = submittedReading == null
                ? null
                : new SubmittedReadingResponse(
                submittedReading.getId(),
                submittedReading.getValue(),
                submittedReading.getConsumption()
        );

        return new MeterReadingFormItemResponse(
                meter.getId(),
                meter.getService().getId(),
                meter.getService().getName(),
                meter.getService().getUnit(),
                meter.getService().getBillingType(),
                previousValue,
                tariff.getRate(),
                submittedReading != null,
                submitted
        );
    }

    private MeterReading saveOrUpdateReading(
            Meter meter,
            ResolvedReading resolved,
            Integer periodMonth,
            Integer periodYear
    ) {
        MeterReading reading = meterReadingRepository
                .findByMeterAndPeriodYearAndPeriodMonth(meter, periodYear, periodMonth)
                .orElseGet(() -> MeterReading.builder()
                        .meter(meter)
                        .periodMonth(periodMonth)
                        .periodYear(periodYear)
                        .build());

        reading.setValue(resolved.value());
        reading.setConsumption(resolved.consumption());

        return meterReadingRepository.save(reading);
    }

    private MeterReadingResponse toReadingResponse(MeterReading reading, BigDecimal previousValue) {
        return new MeterReadingResponse(
                reading.getId(),
                reading.getMeter().getId(),
                reading.getMeter().getService().getId(),
                reading.getMeter().getService().getName(),
                reading.getMeter().getService().getUnit(),
                previousValue,
                reading.getValue(),
                reading.getConsumption(),
                reading.getPeriodMonth(),
                reading.getPeriodYear(),
                reading.getReadingDate()
        );
    }

    private ResolvedReading resolveReading(
            MeterReadingSubmitItemRequest item,
            BigDecimal previousValue
    ) {
        BigDecimal normalizedPreviousValue = previousValue != null
                ? previousValue
                : BigDecimal.ZERO;

        BigDecimal value = item.value();
        BigDecimal consumption = item.consumption();

        if (value == null && consumption == null) {
            throw new IllegalArgumentException("Either value or consumption must be provided");
        }

        if (value == null) {
            value = normalizedPreviousValue.add(consumption);
        }

        if (consumption == null) {
            consumption = value.subtract(normalizedPreviousValue);
        }

        validateResolvedReading(value, consumption, normalizedPreviousValue);

        return new ResolvedReading(value, consumption);
    }

    private void validateResolvedReading(
            BigDecimal value,
            BigDecimal consumption,
            BigDecimal previousValue
    ) {
        if (value.compareTo(previousValue) < 0) {
            throw new IllegalArgumentException("Value cannot be lower than previous reading value");
        }

        if (consumption.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Consumption must be greater than 0");
        }

        BigDecimal expectedConsumption = value.subtract(previousValue);

        if (expectedConsumption.compareTo(consumption) != 0) {
            throw new IllegalArgumentException("Value and consumption do not match");
        }
    }

    private BigDecimal getPreviousValue(Meter meter, Integer periodYear, Integer periodMonth) {
        return meterReadingRepository.findPreviousReadings(
                        meter,
                        periodYear,
                        periodMonth,
                        PageRequest.of(0, 1)
                )
                .stream()
                .findFirst()
                .map(MeterReading::getValue)
                .orElse(null);
    }

    private MeterTariff getCurrentTariff(Meter meter) {
        return meterTariffRepository.findByMeterAndEndDateIsNull(meter)
                .orElseThrow(() -> new ResourceNotFoundException("Current meter tariff"));
    }

    private Meter getUserMeter(Long meterId, User user) {
        return meterRepository.findByIdAndUser(meterId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Meter"));
    }

    private LocalDate toPeriodDate(Integer periodYear, Integer periodMonth) {
        return LocalDate.of(periodYear, periodMonth, 1);
    }

    private record ResolvedReading(
            BigDecimal value,
            BigDecimal consumption
    ) {}
}
