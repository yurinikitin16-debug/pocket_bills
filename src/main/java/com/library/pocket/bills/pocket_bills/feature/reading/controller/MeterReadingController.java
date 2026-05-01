package com.library.pocket.bills.pocket_bills.feature.reading.controller;

import com.library.pocket.bills.pocket_bills.feature.reading.dto.request.BulkMeterReadingRequest;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.request.MeterReadingCreateRequest;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.request.MeterReadingUpdateRequest;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.response.BulkMeterReadingResponse;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.response.MeterReadingResponse;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.response.ReadingFormDataResponse;
import com.library.pocket.bills.pocket_bills.feature.reading.service.MeterReadingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meter-readings")
@RequiredArgsConstructor
public class MeterReadingController {

    private final MeterReadingService meterReadingService;

    @GetMapping("/form-data")
    public ResponseEntity<ReadingFormDataResponse> getFormData(
            @RequestParam Integer periodMonth,
            @RequestParam Integer periodYear,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                meterReadingService.getFormData(periodMonth, periodYear, authentication)
        );
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkMeterReadingResponse> submitBulkReadings(
            @Valid @RequestBody BulkMeterReadingRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                meterReadingService.submitBulkReadings(request, authentication)
        );
    }

    @GetMapping
    public ResponseEntity<List<MeterReadingResponse>> getReadingsHistory(
            @RequestParam Integer periodYear,
            @RequestParam(required = false) Integer periodMonth,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                meterReadingService.getReadingsHistory(periodYear, periodMonth, authentication)
        );
    }
}

