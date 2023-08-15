package kr.zb.nengtul.favorite.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoriteDto {

    private Long id;

    private Long publisherId;

    private String publisherNickName;

    private String publisherProfilePhotoUrl;

    private int publisherPoint;

    private int publisherRecipeCount;

}
