package com.project.Shopapp.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CartItemDTO {
    private int masanpham;
    private int quantity;
}
