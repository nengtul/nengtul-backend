package kr.zb.nengtul.recipe.service;

import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.likes.domain.repository.LikesRepository;
import kr.zb.nengtul.recipe.domain.constants.RecipeCategory;
import kr.zb.nengtul.recipe.domain.dto.RecipeAddDto;
import kr.zb.nengtul.recipe.domain.dto.RecipeGetDetailDto;
import kr.zb.nengtul.recipe.domain.dto.RecipeGetListDto;
import kr.zb.nengtul.recipe.domain.dto.RecipeUpdateDto;
import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import kr.zb.nengtul.recipe.domain.repository.RecipeSearchRepository;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import s3bucket.service.AmazonS3Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeSearchRepository recipeSearchRepository;

    private final UserRepository userRepository;

    private final LikesRepository likesRepository;

    private final AmazonS3Service amazonS3Service;

    public void addRecipe(Principal principal, RecipeAddDto recipeAddDto,
                          List<MultipartFile> images, MultipartFile thumbnail) {

        User user = userRepository.findByEmail(principal.getName()).
                orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        String uuid = UUID.randomUUID().toString();

        recipeSearchRepository.save(RecipeDocument.builder()
                .userId(user.getId())
                .title(recipeAddDto.getTitle())
                .intro(recipeAddDto.getIntro())
                .ingredient(recipeAddDto.getIngredient())
                .cookingStep(recipeAddDto.getCookingStep())
                .imageUrl(
                        amazonS3Service.uploadFileForRecipeCookingStep(images, uuid)
                )
                .thumbnailUrl(
                        amazonS3Service.uploadFileForRecipeThumbnail(thumbnail, uuid)
                )
                .cookingTime(recipeAddDto.getCookingTime())
                .serving(recipeAddDto.getServing())
                .category(recipeAddDto.getCategory())
                .videoUrl(recipeAddDto.getVideoUrl())
                .viewCount(0L)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build());

    }

    public void addRecipeForCrawling(Principal principal, RecipeAddDto recipeAddDto,
                                     String images, String thumbnail) {

        User user = userRepository.findByEmail(principal.getName()).
                orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        recipeSearchRepository.save(RecipeDocument.builder()
                .userId(user.getId())
                .title(recipeAddDto.getTitle())
                .intro(recipeAddDto.getIntro())
                .ingredient(recipeAddDto.getIngredient())
                .cookingStep(recipeAddDto.getCookingStep())
                .imageUrl(images)
                .thumbnailUrl(thumbnail)
                .cookingTime(recipeAddDto.getCookingTime())
                .serving(recipeAddDto.getServing())
                .category(recipeAddDto.getCategory())
                .videoUrl(recipeAddDto.getVideoUrl())
                .viewCount(0L)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build());

    }

    public Page<RecipeGetListDto> getAllRecipe(Pageable pageable) {

        return recipeSearchRepository.findAll(pageable)
                .map(this::settingRecipeGetListDto);
    }

    public RecipeGetDetailDto getRecipeDetailById(String recipeId) {

        RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECIPE));

        recipeDocument.updateViewCount();
        recipeSearchRepository.save(recipeDocument);

        RecipeGetDetailDto recipeGetDetailDto =
                RecipeGetDetailDto.fromRecipeDocument(recipeDocument);

        User user = userRepository.findById(recipeDocument.getUserId())
                .orElseGet(() -> User.builder()
                    .profileImageUrl("")
                    .nickname("냉장고를털어라").build());

        recipeGetDetailDto.setUserProfileUrl(user.getProfileImageUrl());
        recipeGetDetailDto.setPoint(user.getPoint());
        recipeGetDetailDto.setNickName(user.getNickname());

        return recipeGetDetailDto;
    }

    public Page<RecipeGetListDto> getRecipeByCategory(RecipeCategory category, Pageable pageable) {

        return recipeSearchRepository.findAllByCategory(category, pageable)
                .map(this::settingRecipeGetListDto);
    }

    public Page<RecipeGetListDto> getRecipeByTitle(String title, Pageable pageable) {

        return recipeSearchRepository.findAllByTitle(title, pageable)
                .map(this::settingRecipeGetListDto);
    }

    public Page<RecipeGetListDto> getRecipeByIngredient(String ingredient, Pageable pageable) {

        return recipeSearchRepository.findAllByIngredient(ingredient, pageable)
                .map(this::settingRecipeGetListDto);
    }

    @Transactional
    public void updateRecipe(
            Principal principal, String recipeId, RecipeUpdateDto recipeUpdateDto,
            List<MultipartFile> images, MultipartFile thumbnail) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECIPE));

        if (!Objects.equals(user.getId(), recipeDocument.getUserId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        if (!thumbnail.isEmpty()) {
            amazonS3Service.updateFile(thumbnail, recipeDocument.getThumbnailUrl());
        }

        if (!recipeUpdateDto.getImagesUrl().isEmpty() &&
                !images.get(0).isEmpty()) {

            String[] imageUrlArr = recipeUpdateDto.getImagesUrl().split("\\\\");

            for (int i = 0; i < imageUrlArr.length; i++) {
                amazonS3Service.updateFile(images.get(i), imageUrlArr[i]);
            }
        }

        recipeDocument.updateRecipe(recipeUpdateDto);

        recipeSearchRepository.save(recipeDocument);
    }


    @Transactional
    public void deleteRecipe(Principal principal, String recipeId) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECIPE));

        if (!Objects.equals(user.getId(), recipeDocument.getUserId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        deleteRecipeS3UploadFile(
                recipeDocument.getImageUrl(), recipeDocument.getThumbnailUrl());

        recipeSearchRepository.delete(recipeDocument);
    }

    private RecipeGetListDto settingRecipeGetListDto(RecipeDocument recipeDocument) {

        RecipeGetListDto recipeGetListDto =
                RecipeGetListDto.fromRecipeDocument(recipeDocument);

        User user = userRepository.findById(recipeDocument.getUserId())
                .orElseGet(() -> User.builder()
                        .nickname("냉장고를털어라")
                        .build());

        Long likeCount = likesRepository.countByRecipeId(recipeDocument.getId());
        System.out.println(likeCount);

        recipeGetListDto.setNickName(user.getNickname());
        recipeGetListDto.setLikeCount(likeCount);

        return recipeGetListDto;
    }

    private void deleteRecipeS3UploadFile(String imagesUrl, String thumbnailUrl) {

        amazonS3Service.deleteFile(thumbnailUrl);

        String[] imagesUrlArr = imagesUrl.split("\\\\");

        for (String imageUrl : imagesUrlArr) {
            amazonS3Service.deleteFile(imageUrl);
        }
    }


}
