package com.project.Shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.SanPham;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FeedbackDTO {
    @JsonProperty("user_id")
    private int USERID;

    @JsonProperty("content")
    private String NOIDUNG;

    private int SOSAO;

    @JsonProperty("product_id")
    private int MASANPHAM;
}
