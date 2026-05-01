package com.library.pocket.bills.pocket_bills.feature.bill.controller;

import com.library.pocket.bills.pocket_bills.feature.bill.dto.response.BillResponse;
import com.library.pocket.bills.pocket_bills.feature.bill.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @GetMapping
    public ResponseEntity<List<BillResponse>> getBills(
            @RequestParam Integer periodYear,
            @RequestParam(required = false) Integer periodMonth,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                billService.getBills(periodYear, periodMonth, authentication)
        );
    }
}
