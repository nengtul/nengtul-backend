package kr.zb.nengtul.recipe.domain.repository;

import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeSearchRepository extends ElasticsearchRepository<RecipeDocument, String> {

    void save(RecipeDocument recipeDocument);

    Optional<RecipeDocument> findById(String id);

}
