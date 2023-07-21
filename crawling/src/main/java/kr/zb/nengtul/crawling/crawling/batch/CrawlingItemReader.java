package kr.zb.nengtul.crawling.crawling.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CrawlingItemReader implements ItemReader<String> {

    private final List<String> urls;
    private final AtomicInteger currentIndex;

    public CrawlingItemReader() {
        urls = new ArrayList<>();
        currentIndex = new AtomicInteger(0);

        // 크롤링할 URL 목록 초기화
        for (int number = 7006377; number > 7005377; number--) {
            String url = "https://www.10000recipe.com/recipe/" + number;
            urls.add(url);
        }
    }

    @Override
    public String read() {
        if (currentIndex.get() >= urls.size()) {
            return null; // URL 목록을 모두 읽었을 경우 중지
        }

        int index = currentIndex.getAndIncrement();
        String url = urls.get(index);
        log.info(url);
        return url; // URL 반환
    }
}
