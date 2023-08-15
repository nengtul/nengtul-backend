package kr.zb.nengtul.likes.service;

import java.security.Principal;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.likes.domain.dto.LikesDto;
import kr.zb.nengtul.likes.domain.entity.Likes;
import kr.zb.nengtul.likes.domain.repository.LikesRepository;
import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import kr.zb.nengtul.recipe.domain.repository.RecipeSearchRepository;
import kr.zb.nengtul.user.domain.constants.UserPoint;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

  private final LikesRepository likesRepository;

  private final UserRepository userRepository;

  private final RecipeSearchRepository recipeSearchRepository;

  @Transactional
  public void addLikes(Principal principal, String recipeId) {

    User user = userRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECIPE));

    likesRepository.findByUserIdAndRecipeId(user.getId(), recipeId)
        .ifPresent(likes -> {
          throw new CustomException(ErrorCode.ALREADY_LIKES_RECIPE);
        });

    User publisher = userRepository.findById(recipeDocument.getUserId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    publisher.setPlusPoint(UserPoint.LIKES);

    userRepository.save(publisher);

    likesRepository.save(Likes.builder()
        .recipeId(recipeId)
        .user(user)
        .build());
  }

  public Page<LikesDto> getLikes(Principal principal, Pageable pageable) {

    User user = userRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    return likesRepository.findAllByUserId(user.getId(), pageable)
        .map(likes -> {
          RecipeDocument recipeDocument = recipeSearchRepository.findById(likes.getRecipeId())
              .orElseGet(() -> RecipeDocument.builder()
                  .title("삭제된 레시피 입니다.")
                  .id("")
                  .title("")
                  .thumbnailUrl("")
                  .build());

          User recipeUser = userRepository.findById(recipeDocument.getUserId())
              .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

          return LikesDto.builder()
              .id(likes.getId())
              .createdAt(likes.getCreatedAt())
              .recipeId(recipeDocument.getId())
              .title(recipeDocument.getTitle())
              .thumbnailUrl(recipeDocument.getThumbnailUrl())
              .likeCount(likesRepository.countByRecipeId(recipeDocument.getId()))
              .recipeUserNickName(recipeUser.getNickname())
              .viewCount(recipeDocument.getViewCount())
              .build();
        });
  }


  public void deleteLikes(Principal principal, Long likeId) {

    User user = userRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    Likes likes = likesRepository.findById(likeId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_LIKE));

    if (!user.getId().equals(likes.getUser().getId())) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    recipeSearchRepository.findById(likes.getRecipeId())
        .ifPresent(recipe -> {
          User publisher = userRepository.findById(recipe.getUserId())
              .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

          publisher.setMinusPoint(UserPoint.LIKES);
          userRepository.save(publisher);
        });

    likesRepository.delete(likes);
  }

}
