package kr.zb.nengtul.recipe.service;

import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.recipe.domain.constants.RecipeCategory;
import kr.zb.nengtul.recipe.domain.dto.RecipeAddDto;
import kr.zb.nengtul.recipe.domain.dto.RecipeGetDetailDto;
import kr.zb.nengtul.recipe.domain.dto.RecipeGetListDto;
import kr.zb.nengtul.recipe.domain.dto.RecipeUpdateDto;
import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import kr.zb.nengtul.recipe.domain.repository.RecipeSearchRepository;
import kr.zb.nengtul.user.entity.domain.User;
import kr.zb.nengtul.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeSearchRepository recipeSearchRepository;

    private final UserRepository userRepository;

    public void addRecipe(Principal principal, RecipeAddDto recipeAddDto) {

        User user = userRepository.findByEmail(principal.getName()).
                orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        recipeSearchRepository.save(RecipeDocument.builder()
                .userId(user.getId())
                .title(recipeAddDto.getTitle())
                .intro(recipeAddDto.getIntro())
                .ingredient(recipeAddDto.getIngredient())
                .cookingStep(recipeAddDto.getCookingStep())
                .imageUrl(recipeAddDto.getImageUrl())
                .cookingTime(recipeAddDto.getCookingTime())
                .serving(recipeAddDto.getServing())
                .category(recipeAddDto.getCategory())
                .videoUrl(recipeAddDto.getVideoUrl())
                .viewCount(0L)
                .build());

    }

    public Page<RecipeGetListDto> getAllRecipe(Pageable pageable) {

        return recipeSearchRepository.findAll(pageable)
                .map(recipeDocument -> {
                    RecipeGetListDto recipeGetListDto =
                            RecipeGetListDto.fromRecipeDocument(recipeDocument);

                    User user = userRepository.findById(recipeDocument.getUserId())
                            .orElseGet(() -> User.builder()
                                    .nickname("냉장고를털어라")
                                    .build());

//                    Todo like count 추가
//                    Long likeCount =

                    recipeGetListDto.setNickName(user.getNickname());

                    return recipeGetListDto;
                });
    }

    public RecipeGetDetailDto getRecipeById(String recipeId) {

        RecipeGetDetailDto recipeGetDetailDto = RecipeGetDetailDto.fromRecipeDocument(
                recipeSearchRepository.findById(recipeId).orElseThrow());

        User user = userRepository.findById(recipeGetDetailDto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        recipeGetDetailDto.setNickName(user.getNickname());

        return recipeGetDetailDto;
    }

    public Page<RecipeGetListDto> getRecipeByCategory(RecipeCategory category, Pageable pageable) {

        return recipeSearchRepository.findAllByCategory(category, pageable)
                .map(recipeDocument -> {
                    RecipeGetListDto recipeGetListDto =
                            RecipeGetListDto.fromRecipeDocument(recipeDocument);

                    User user = userRepository.findById(recipeDocument.getUserId())
                            .orElseGet(() -> User.builder()
                                    .nickname("냉장고를털어라")
                                    .build());

                    //Todo like count 추가
//                    Long likeCount = 
//
                    recipeGetListDto.setNickName(user.getNickname());

                    return recipeGetListDto;
                });
    }

    public Page<RecipeGetListDto> getRecipeByTitle(String title, Pageable pageable) {

        return recipeSearchRepository.findAllByTitle(title, pageable)
                .map(recipeDocument -> {
                    RecipeGetListDto recipeGetListDto =
                            RecipeGetListDto.fromRecipeDocument(recipeDocument);

                    User user = userRepository.findById(recipeDocument.getUserId())
                            .orElseGet(() -> User.builder()
                                    .nickname("냉장고를털어라")
                                    .build());

                    //Todo like count 추가
//                    Long likeCount =
//
                    recipeGetListDto.setNickName(user.getNickname());

                    return recipeGetListDto;
                });
    }

    public Page<RecipeGetListDto> getRecipeByIngredient(String ingredient, Pageable pageable) {

        return recipeSearchRepository.findAllByIngredient(ingredient, pageable)
                .map(recipeDocument -> {
                    RecipeGetListDto recipeGetListDto =
                            RecipeGetListDto.fromRecipeDocument(recipeDocument);

                    User user = userRepository.findById(recipeDocument.getUserId())
                            .orElseGet(() -> User.builder()
                                    .nickname("냉장고를털어라")
                                    .build());

                    //Todo like count 추가
//                    Long likeCount =
//
                    recipeGetListDto.setNickName(user.getNickname());

                    return recipeGetListDto;
                });
    }

    @Transactional
    public void updateRecipe(Principal principal, String recipeId, RecipeUpdateDto recipeUpdateDto) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECIPE));

        if (!Objects.equals(user.getId(), recipeDocument.getUserId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        recipeDocument.updateRecipe(recipeUpdateDto);

        recipeSearchRepository.save(recipeDocument);
    }

    public void deleteRecipe(Principal principal, String recipeId) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECIPE));

        if (!Objects.equals(user.getId(), recipeDocument.getUserId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        recipeSearchRepository.delete(recipeDocument);
    }




}
