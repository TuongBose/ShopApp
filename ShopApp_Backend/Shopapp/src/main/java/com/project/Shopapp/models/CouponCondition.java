package com.project.Shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "coupon_conditions")
@Builder
public class CouponCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @JoinColumn(name = "attribute", nullable = false)
    private String attribute;

    @JoinColumn(name = "operator", nullable = false)
    private String operator;

    @JoinColumn(name = "value", nullable = false)
    private String value;

    @JoinColumn(name = "discount_amount", nullable = false)
    private BigDecimal discountAmount;
}
