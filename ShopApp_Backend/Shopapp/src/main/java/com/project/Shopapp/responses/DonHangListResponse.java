package com.project.Shopapp.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonHangListResponse {
    private List<DonHangResponse> donHangResponseList;
    private int totalPages;
}
