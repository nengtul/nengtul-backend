package kr.zb.nengtul.crawling.crawling.batch;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kr.zb.nengtul.crawling.recipe.domain.RecipeDocument;
import kr.zb.nengtul.crawling.recipe.type.RecipeCategory;
import kr.zb.nengtul.crawling.crawling.dto.CrawlInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CrawlingItemProcessor implements ItemProcessor<CrawlInfo, RecipeDocument> {

    private static final int MAX_RETRY_COUNT = 3;
    private static final int RETRY_DELAY_MS = 1000;

    @Override
    public RecipeDocument process(CrawlInfo item) throws Exception {
        int retryCount = 0;
        RecipeCategory category = item.getCategory();
        String url = "https://www.10000recipe.com"+item.getRecipeUrl();
        String mainPhotoUrl = item.getMainPhotoUrl();
        log.info(url);
        while (retryCount < MAX_RETRY_COUNT) {
            try {
                Document document = Jsoup.connect(url).get();
                Elements imageElements = document.select("div[id^=stepimg]");
                Element recipeIntroElement = document.selectFirst(
                        "div.view2_summary_in#recipeIntro");

                if (imageElements.isEmpty()||recipeIntroElement==null) {
                    return null;
                }

                Element ingredientElements = document.selectFirst("div.ready_ingre3");
                Element recipeVideoIframe = document.selectFirst("iframe#ifrmRecipeVideo");
                Elements titleElements = document.select("div.view2_summary.st3 h3");
                Elements stepElements = document.select("div[id^=stepDiv]");
                Elements servingElement = document.select("span.view2_summary_info1");
                Elements cookingTimeElements = document.select("span.view2_summary_info2");

                String title = titleElements.text();
                String intro = Objects.requireNonNull(recipeIntroElement).text()
                        .replace("<br>", "").trim();
                String ingredient = ingredientElements != null ? getIngredients(ingredientElements)
                        : "레시피 마다 달라요!";
                String cookingStep = getCombinedSteps(stepElements);
                String serving = servingElement.text().isEmpty() ? "" : servingElement.text();
                String cookingTime =
                        cookingTimeElements.text().isEmpty() ? "" : cookingTimeElements.text();
                String videoUrl =
                        recipeVideoIframe != null ? recipeVideoIframe.attr("org_src") : "";
                String imageUrl = getImageUrls(imageElements);

                return RecipeDocument.builder()
                        .userId(1L)
                        .title(title)
                        .intro(intro)
                        .ingredient(ingredient)
                        .cookingStep(cookingStep)
                        .imageUrl(imageUrl)
                        .cookingTime(cookingTime)
                        .serving(serving)
                        .category(category)
                        .videoUrl(videoUrl)
                        .thumbnailUrl(mainPhotoUrl)
                        .viewCount(0L)
                        .build();
            } catch (IOException e) {
                log.error("Error while crawling URL: {}", url);
                log.error("Exception: {}", e.getMessage());
                retryCount++;
                if (retryCount < MAX_RETRY_COUNT) {
                    log.info("Retrying after {} ms...", RETRY_DELAY_MS);
                    Thread.sleep(RETRY_DELAY_MS);
                }
            }
        }
        return null;
    }


    private String getIngredients(Element ingredientElements) {
        return ingredientElements.select("li").stream()
                .map(ingredientElement -> ingredientElement.text().replaceAll("구매", "")
                        .replaceAll("\\s+", " ").trim())
                .collect(Collectors.joining(","));
    }

    private String getCombinedSteps(Elements stepElements) {
        return stepElements.stream()
                .map(Element::text)
                .collect(Collectors.joining("\\"))
                .trim();
    }

    private String getImageUrls(Elements imageElements) {
        List<String> imageUrls = imageElements.stream()
                .map(imageElement -> imageElement.selectFirst("img"))
                .map(imgElement -> imgElement != null ? imgElement.attr("src") : "")
                .collect(Collectors.toList());

        return String.join("\\", imageUrls);
    }
}
