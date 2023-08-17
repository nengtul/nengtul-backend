package kr.zb.nengtul.crawling.recipe.repository;

import java.util.ArrayList;
import java.util.List;
import kr.zb.nengtul.crawling.recipe.domain.RecipeDocument;
import org.springframework.batch.item.Chunk;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeSearchRepository extends ElasticsearchRepository<RecipeDocument, String> {

    void saveAll(Iterable<RecipeDocument> recipeDocuments);
    default void saveAllChunk(Chunk<? extends RecipeDocument> recipes) {
        List<RecipeDocument> recipeList = new ArrayList<>();
        recipes.forEach(recipeList::add);
        saveAll(recipeList);
    }
}
