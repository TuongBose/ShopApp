package com.project.Shopapp.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RefreshTokenDTO {
    private String refreshToken;
}
