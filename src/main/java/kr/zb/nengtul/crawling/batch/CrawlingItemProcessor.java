package kr.zb.nengtul.crawling.batch;

import java.util.Objects;
import java.util.stream.Collectors;
import kr.zb.nengtul.board.domain.Recipe;
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
public class CrawlingItemProcessor implements ItemProcessor<String, Recipe> {

    @Override
    public Recipe process(String url) throws Exception {
        Document document = Jsoup.connect(url).get();

        Element recipeIntroContentElement = document.selectFirst(
                "div.view2_summary_in#recipeIntro"); // 설명

        if (recipeIntroContentElement == null) {
            return null;
        }
        Elements imageElements = document.select("div[id^=stepimg]"); // 조리과정 이미지주소

        String imageUrl = getImageUrls(imageElements);

        if (imageElements.isEmpty()) {
            return null;
        }

        Elements titleElements = document.select("div.view2_summary.st3 h3"); // 제목
        Element recipeVideoIframe = document.selectFirst("iframe#ifrmRecipeVideo"); //동영상 url
        Element ingredientElements = document.selectFirst("div.ready_ingre3"); // 재료
        Elements stepElements = document.select("div[id^=stepDiv]"); // 조리과정
        Elements servingElement = document.select("span.view2_summary_info1"); // 몇 인분
        Elements cookingTimeElements = document.select("span.view2_summary_info2"); // 조리시간

        String title = titleElements.text();

        String intro = Objects.requireNonNull(recipeIntroContentElement).text()
                .replace("<br>", "").trim();

        String ingredient =
                ingredientElements != null ? getIngredients(ingredientElements) : "레시피 마다 달라요!";

        String cookingStep = getCombinedSteps(stepElements);

        String serving = servingElement.text().isEmpty() ? "-" : servingElement.text();

        String cookingTime =
                cookingTimeElements.text().isEmpty() ? "-" : cookingTimeElements.text();

        String videoUrl = recipeVideoIframe != null ? recipeVideoIframe.attr("org_src") : "-";

        return Recipe.builder()
                .userId(1L)
                .title(title)
                .intro(intro)
                .ingredient(ingredient)
                .cookingStep(cookingStep)
                .imageUrl(imageUrl)
                .cookingTime(cookingTime)
                .serving(serving)
                .videoUrl(videoUrl)
                .build();

    }

    private String getIngredients(Element ingredientElements) {
        return ingredientElements.select("li").stream()
                .map(ingredientElement -> ingredientElement.text()
                        .replaceAll("구매", "")
                        .replaceAll("\\s+", " ")
                        .trim())
                .collect(Collectors.joining(", "));
    }

    private String getCombinedSteps(Elements stepElements) {
        StringBuilder stepBuilder = new StringBuilder();
        for (Element stepElement : stepElements) {
            String stepContent = stepElement.text();
            stepBuilder.append(stepContent).append("\n");
        }
        return stepBuilder.toString().trim();
    }

    private String getImageUrls(Elements imageElements) {
        StringBuilder imageBuilder = new StringBuilder();
        for (Element imageElement : imageElements) {
            Element imgElement = imageElement.selectFirst("img");

            if (imgElement == null) {
                imageBuilder.append("-");
                break;
            }

            String imageUrl = imgElement.attr("src");
            imageBuilder.append(imageUrl).append("\n");

        }
        return imageBuilder.toString().trim();
    }

}
