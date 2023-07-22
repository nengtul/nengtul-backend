package kr.zb.nengtul.crawling.crawling.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CrawlInfo {

    private String category;
    private String recipeUrl;
    private String mainPhotoUrl;

}
