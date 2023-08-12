package kr.zb.nengtul.recipe.domain.repository;

import java.util.List;
import kr.zb.nengtul.recipe.domain.constants.RecipeCategory;
import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeSearchRepository extends ElasticsearchRepository<RecipeDocument, String> {
    void save(RecipeDocument recipeDocument);
    Optional<RecipeDocument> findById(String id);
    Page<RecipeDocument> findAllByCategory(RecipeCategory category, Pageable pageable);
    Page<RecipeDocument> findAllByTitle(String title, Pageable pageable);
    Page<RecipeDocument> findAllByIngredient(String ingredient, Pageable pageable);
    List<RecipeDocument> findAllByUserId(Long userId);
    Page<RecipeDocument> findAllByUserId(Long userId, Pageable pageable);
    void delete(RecipeDocument recipeDocument);
    int countByUserId(Long userId);
}
