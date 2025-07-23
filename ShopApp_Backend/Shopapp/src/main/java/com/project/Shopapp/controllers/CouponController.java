package com.project.Shopapp.controllers;

import com.project.Shopapp.responses.ResponseObject;
import com.project.Shopapp.responses.coupon.CouponCalculationResponse;
import com.project.Shopapp.services.coupon.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final ICouponService couponService;

    @GetMapping("/calculate")
    public ResponseEntity<ResponseObject> calculateCouponValue(
            @RequestParam("couponCode") String couponCode,
            @RequestParam("totalAmount") double totalAmount
    ) {
        double finalAmount = couponService.calculateCouponValue(couponCode, totalAmount);

        CouponCalculationResponse response = CouponCalculationResponse.builder()
                .result(finalAmount)
                .build();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Calculate coupon successfully")
                .status(HttpStatus.OK)
                .data(response)
                .build());
    }
}
