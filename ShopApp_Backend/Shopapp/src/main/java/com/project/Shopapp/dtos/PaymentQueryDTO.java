package com.project.Shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentQueryDTO {
    @JsonProperty("order_id")
    private String orderId; // Mã đơn hàng cần truy vấn
    @JsonProperty("trans_date")
    private String transDate; // Ngày giao dịch (định dạng yyyyMMddHHmmss)
    @JsonProperty("ip_address")
    private String ipAddress; // Địa chỉ IP của người dùng
}
