package com.library.pocket.bills.pocket_bills.feature.meter.controller;

import com.library.pocket.bills.pocket_bills.feature.meter.dto.request.MeterTariffCreateRequest;
import com.library.pocket.bills.pocket_bills.feature.meter.dto.response.MeterTariffResponse;
import com.library.pocket.bills.pocket_bills.feature.meter.service.MeterTariffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meters/{meterId}/tariffs")
@RequiredArgsConstructor
public class MeterTariffController {

    private final MeterTariffService meterTariffService;

    @PostMapping
    public ResponseEntity<MeterTariffResponse> createMeterTariff(
            @PathVariable Long meterId,
            @Valid @RequestBody MeterTariffCreateRequest request,
            Authentication authentication
    ) {
        MeterTariffResponse response = meterTariffService.createMeterTariff(meterId, request, authentication);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MeterTariffResponse>> getMeterTariffs(
            @PathVariable Long meterId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(meterTariffService.getMeterTariffs(meterId, authentication));
    }

    @GetMapping("/current")
    public ResponseEntity<MeterTariffResponse> getCurrentMeterTariff(
            @PathVariable Long meterId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(meterTariffService.getCurrentMeterTariff(meterId, authentication));
    }
}
