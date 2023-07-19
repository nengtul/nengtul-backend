package kr.zb.nengtul.crawling.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CrawlingItemReader implements ItemReader<String> {

    private final AtomicInteger currentIndex = new AtomicInteger(0);
    private final List<String> urls = new ArrayList<>(); // URL 목록 저장 리스트
    private boolean executed = false;

    public CrawlingItemReader() {
        // 크롤링할 URL 목록 초기화
        for (int number = 7006377; number > 7005377; number--) {
            String url = "https://www.10000recipe.com/recipe/" + number;
            urls.add(url);
        }
    }

    @Override
    public String read() {

        if (executed) {
            return null; // 이미 실행되었으므로 중지
        }

        if (currentIndex.get() >= urls.size()) {
            setExecuted(true);
            return null; // 데이터 읽기 작업 종료를 알리기 위해 null 반환
        }

        int index = currentIndex.getAndIncrement();
        String url = urls.get(index);
        log.info(url);
        return url; // URL 반환
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }
}
