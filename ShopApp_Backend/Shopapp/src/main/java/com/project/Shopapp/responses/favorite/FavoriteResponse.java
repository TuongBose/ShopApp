package com.project.Shopapp.responses.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.Shopapp.models.Favorite;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteResponse {
    @JsonProperty("id")
    private int id;

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("user_id")
    private int userId;

    public static FavoriteResponse fromFavorite(Favorite favorite) {
        return FavoriteResponse.builder()
                .id(favorite.getId())
                .userId(favorite.getUser().getUSERID())
                .productId(favorite.getProduct().getMASANPHAM())
                .build();
    }
}
