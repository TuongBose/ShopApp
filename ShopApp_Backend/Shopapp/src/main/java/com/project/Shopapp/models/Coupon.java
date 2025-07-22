package com.project.Shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "coupons")
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "active", nullable = false)
    private boolean active;
}
/*
INSERT INTO coupons (code) VALUES ('HEAVEN');
INSERT INTO coupons (code) VALUES ('DISCOUNT20');

INSERT INTO coupon_conditions (coupon_id, attribute, operator, value,discount_amount)
VALUES (1, 'minimum_amount', '>', '100', 10);

INSERT INTO coupon_conditions (coupon_id, attribute, operator, value,discount_amount)
VALUES (1, 'application_date', 'BETWEEN', '2023-12-25', 5);

INSERT INTO coupon_conditions (coupon_id, attribute, operator, value,discount_amount)
VALUES (2, 'minimum_amount', '>', '200', 20);
*/