package kr.zb.nengtul.crawling.crawling.batch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import kr.zb.nengtul.crawling.crawling.dto.CrawlInfo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CrawlingItemReader implements ItemReader<CrawlInfo> {

    private static final int MAX_RETRY_COUNT = 3;
    private static final int RETRY_DELAY_MS = 1000;
    private final List<CrawlInfo> crawlInfoList;
    private final AtomicInteger currentIndex;
    private final Map<Integer, String> cat4Map; // 만개의 레시피 카테고리별 구분 파라미터 값

    public CrawlingItemReader() throws Exception {
        crawlInfoList = new ArrayList<>();
        currentIndex = new AtomicInteger(0);

        cat4Map = getCat4Map();

        for (int cat4 : cat4Map.keySet()) {
            for (int pageIndex = 1; pageIndex <= 2; pageIndex++) {
                crawlLinksForCrawlInfo(cat4, pageIndex);
            }
        }
    }

    private Map<Integer, String> getCat4Map() {
        Map<Integer, String> cat4Map = new HashMap<>();
        cat4Map.put(63, "밑반찬");
        cat4Map.put(56, "메인반찬");
        cat4Map.put(54, "국/탕");
        cat4Map.put(55, "찌개");
        cat4Map.put(60, "디저트");
        cat4Map.put(53, "면/만두");
        cat4Map.put(52, "밥/죽/떡");
        cat4Map.put(61, "퓨전");
        cat4Map.put(57, "김치/젓갈/장류");
        cat4Map.put(58, "양념/소스/잼");
        cat4Map.put(65, "양식");
        cat4Map.put(64, "샐러드");
        cat4Map.put(68, "스프");
        cat4Map.put(66, "빵");
        cat4Map.put(69, "과자");
        cat4Map.put(59, "차/음료/술");
        cat4Map.put(62, "기타");
        return cat4Map;
    }

    private void crawlLinksForCrawlInfo(int cat4, int pageIndex) throws Exception {
        int retryCount = 0;
        while (retryCount < MAX_RETRY_COUNT) {
            try {
                String categoryPageUrl = "https://www.10000recipe.com/recipe/list.html?cat4=" + cat4
                        + "&order=reco&page=" + pageIndex;

                Document doc = Jsoup.connect(categoryPageUrl).get();
                Elements elements = doc.select(
                        "ul.common_sp_list_ul.ea4 li.common_sp_list_li a.common_sp_link");

                for (Element element : elements) {
                    String recipeUrl = element.attr("href");

                    Elements imgTags = element.select("img");
                    String mainPhotoUrl = (imgTags.size() > 1) ? imgTags.get(1).attr("src")
                            : imgTags.get(0).attr("src");

                    CrawlInfo crawlInfo = new CrawlInfo(cat4Map.get(cat4), recipeUrl, mainPhotoUrl);
                    crawlInfoList.add(crawlInfo);
                    log.info(recipeUrl);
                }
                // 크롤링에 성공하면 반복문 종료
                break;
            } catch (IOException e) {
                log.error("Error while crawling category: {}, page: {}, Retry count: {}",
                        cat4Map.get(cat4), pageIndex, retryCount + 1);
                log.error("Exception: {}", e.getMessage());
                retryCount++;
                if (retryCount < MAX_RETRY_COUNT) {
                    log.info("Retrying after {} ms...", RETRY_DELAY_MS);
                    Thread.sleep(RETRY_DELAY_MS);
                }
            }
        }
    }


    @Override
    public CrawlInfo read() {
        if (currentIndex.get() >= crawlInfoList.size()) {
            return null;
        }
        int index = currentIndex.getAndIncrement();
        return crawlInfoList.get(index);
    }
}