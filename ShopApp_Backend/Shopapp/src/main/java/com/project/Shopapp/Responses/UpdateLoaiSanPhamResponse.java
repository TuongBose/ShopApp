package com.project.Shopapp.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateLoaiSanPhamResponse {
    @JsonProperty("message")
    private String message;
}
