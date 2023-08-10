package kr.zb.nengtul.crawling.crawling.batch;

import kr.zb.nengtul.crawling.recipe.domain.RecipeDocument;
import kr.zb.nengtul.crawling.recipe.repository.RecipeSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CrawlingItemWriter implements ItemWriter<RecipeDocument> {

    private final RecipeSearchRepository recipeSearchRepository;

    @Override
    public void write(Chunk<? extends RecipeDocument> recipes) {
        recipeSearchRepository.saveAllChunk(recipes);
    }
}
