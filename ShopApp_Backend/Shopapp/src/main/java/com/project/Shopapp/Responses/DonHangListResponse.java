package com.project.Shopapp.Responses;

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
