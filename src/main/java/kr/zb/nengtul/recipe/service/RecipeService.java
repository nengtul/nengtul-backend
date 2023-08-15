package kr.zb.nengtul.recipe.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import kr.zb.nengtul.favorite.domain.repository.FavoriteRepository;
import kr.zb.nengtul.global.entity.RoleType;
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

@Service
@RequiredArgsConstructor
public class RecipeService {

  private final RecipeSearchRepository recipeSearchRepository;

  private final UserRepository userRepository;

  private final LikesRepository likesRepository;

  private final FavoriteRepository favoriteRepository;

  private final AmazonS3Service amazonS3Service;

  public String addRecipe(Principal principal, RecipeAddDto recipeAddDto,
      List<MultipartFile> images, MultipartFile thumbnail) {

    User user = userRepository.findByEmail(principal.getName()).
        orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    String uuid = UUID.randomUUID().toString();

    RecipeDocument recipeDocument = RecipeDocument.builder()
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
        .build();

    recipeSearchRepository.save(recipeDocument);

    return recipeDocument.getId();
  }

  public Page<RecipeGetListDto> getAllRecipe(Pageable pageable) {

    return recipeSearchRepository.findAll(pageable)
        .map(this::settingRecipeGetListDto);
  }

  public RecipeGetDetailDto getRecipeDetailById(String recipeId, Principal principal) {

    RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECIPE));

    recipeDocument.updateViewCount();
    recipeSearchRepository.save(recipeDocument);

    RecipeGetDetailDto recipeGetDetailDto =
        RecipeGetDetailDto.fromRecipeDocument(recipeDocument);

    User recipeUser = userRepository.findById(recipeDocument.getUserId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    recipeGetDetailDto.setUserProfileUrl(recipeUser.getProfileImageUrl());
    recipeGetDetailDto.setPoint(recipeUser.getPoint());
    recipeGetDetailDto.setNickName(recipeUser.getNickname());

    if (principal != null) {

      User user = userRepository.findByEmail(principal.getName())
          .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

      likesRepository.findByUserIdAndRecipeId(user.getId(), recipeId)
          .ifPresent(likes -> recipeGetDetailDto.setLikes(true));

      favoriteRepository.findByUserIdAndPublisherId(user.getId(), recipeUser.getId())
          .ifPresent(favorite -> recipeGetDetailDto.setFavorite(true));

    }

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

    if (!Objects.equals(user.getId(), recipeDocument.getUserId()) &&
        !user.getRoles().equals(RoleType.ADMIN)) {
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
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    Long likeCount = likesRepository.countByRecipeId(recipeDocument.getId());

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

  public RecipeDocument findById(String recipeId) {
    return recipeSearchRepository.findById(recipeId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECIPE));
  }

  public Page<RecipeGetListDto> getAllRecipeByUserId(Long userId, Pageable pageable) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    return recipeSearchRepository.findAllByUserId(user.getId(), pageable)
        .map(this::settingRecipeGetListDto);

  }

}
