package com.library.pocket.bills.pocket_bills.feature.meter.controller;

import com.library.pocket.bills.pocket_bills.feature.meter.dto.request.MeterCreateRequest;
import com.library.pocket.bills.pocket_bills.feature.meter.dto.request.MeterUpdateRequest;
import com.library.pocket.bills.pocket_bills.feature.meter.dto.response.MeterResponse;
import com.library.pocket.bills.pocket_bills.feature.meter.service.MeterService;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.request.MeterInitialReadingRequest;
import com.library.pocket.bills.pocket_bills.feature.reading.dto.response.MeterReadingResponse;
import com.library.pocket.bills.pocket_bills.feature.reading.service.MeterReadingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meters")
@RequiredArgsConstructor
public class MeterController {

    private final MeterService meterService;
    private final MeterReadingService meterReadingService;

    @PostMapping
    public ResponseEntity<MeterResponse> createMeter(
            @Valid @RequestBody MeterCreateRequest request,
            Authentication authentication
    ) {
        MeterResponse response = meterService.createMeter(request, authentication);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MeterResponse>> getMyMeters(Authentication authentication) {
        return ResponseEntity.ok(meterService.getMyMeters(authentication));
    }

    @PutMapping("/{meterId}")
    public ResponseEntity<MeterResponse> updateMeter(
            @PathVariable Long meterId,
            @Valid @RequestBody MeterUpdateRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(meterService.updateMeter(meterId, request, authentication));
    }

    @DeleteMapping("/{meterId}")
    public ResponseEntity<Void> deleteMeter(
            @PathVariable Long meterId,
            Authentication authentication
    ) {
        meterService.deleteMeter(meterId, authentication);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{meterId}/initial-reading")
    public ResponseEntity<MeterReadingResponse> createInitialReading(
            @PathVariable Long meterId,
            @Valid @RequestBody MeterInitialReadingRequest request
    ) {
        MeterReadingResponse response = meterReadingService.createInitialReading(
                meterId,
                request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
