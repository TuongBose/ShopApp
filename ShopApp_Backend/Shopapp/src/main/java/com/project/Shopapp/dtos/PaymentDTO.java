package com.project.Shopapp.dtos;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private long amount;
    private String bankCode;
    private String language;
}
