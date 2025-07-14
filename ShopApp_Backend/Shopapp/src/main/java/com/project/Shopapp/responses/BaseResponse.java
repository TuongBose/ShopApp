package com.project.Shopapp.responses;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private LocalDateTime NGAYTAO;
    private LocalDateTime CHINHSUA;
}
