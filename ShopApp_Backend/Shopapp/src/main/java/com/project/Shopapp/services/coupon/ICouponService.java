package com.project.Shopapp.services.coupon;

public interface ICouponService {
    double calculateCouponValue(String couponCode, double totalAmount);
}
