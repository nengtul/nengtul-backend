package kr.zb.nengtul.crawling.batch;

import kr.zb.nengtul.board.domain.Recipe;
import kr.zb.nengtul.board.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CrawlingItemWriter implements ItemWriter<Recipe> {

    private final RecipeRepository recipeRepository;

    @Override
    public void write(Chunk<? extends Recipe> recipes) {
        recipeRepository.saveAll(recipes);
    }
}
