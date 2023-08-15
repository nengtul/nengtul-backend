package kr.zb.nengtul.likes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@DisplayName("좋아요 서비스 테스트")
class LikesServiceTest {

  private LikesService likesService;

  private LikesRepository likesRepository;

  private UserRepository userRepository;

  private RecipeSearchRepository recipeSearchRepository;

  @BeforeEach
  void setUp() {
    likesRepository = mock(LikesRepository.class);
    userRepository = mock(UserRepository.class);
    recipeSearchRepository = mock(RecipeSearchRepository.class);

    likesService = new LikesService(
        likesRepository, userRepository, recipeSearchRepository);

  }

  @Test
  @DisplayName("좋아요 등록 성공")
  void addLikes_SUCCESS() {
    //given
    User publisher = new User();

    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(new User()));

    when(recipeSearchRepository.findById(any()))
        .thenReturn(Optional.of(new RecipeDocument()));

    when(userRepository.findById(any()))
        .thenReturn(Optional.of(publisher));

    Principal principal = new UsernamePasswordAuthenticationToken(
        "test@test.com", null);

    //when
    likesService.addLikes(principal, "recipeId");

    //then
    verify(likesRepository, times(1))
        .save(any(Likes.class));
    assertEquals(publisher.getPoint(), UserPoint.LIKES.getPoint());
  }

  @Test
  @DisplayName("좋아요 등록 실패 - 이미 좋아요 누름")
  void addLikes_FAIL_Already_Like_Recipe() {
    //given
    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(new User()));

    when(recipeSearchRepository.findById(any()))
        .thenReturn(Optional.of(new RecipeDocument()));

    when(likesRepository.findByUserIdAndRecipeId(any(), any()))
        .thenReturn(Optional.of(new Likes()));

    Principal principal = new UsernamePasswordAuthenticationToken(
        "test@test.com", null);

    //when
    CustomException customException =
        Assertions.assertThrows(CustomException.class,
            () -> likesService.addLikes(principal, "recipeId"));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_LIKES_RECIPE);
  }

  @Test
  @DisplayName("좋아요 가져오기 성공")
  void getLikes_SUCCESS() {
    //given
    RecipeDocument recipeDocument = RecipeDocument.builder()
        .id("recipeId")
        .title("testTitle")
        .thumbnailUrl("testThumbnailUrl")
        .viewCount(20L)
        .build();

    Likes likes = Likes.builder()
        .id(1L)
        .recipeId(recipeDocument.getId())
        .createdAt(LocalDateTime.now())
        .build();

    List<Likes> likesList = new ArrayList<>();
    likesList.add(likes);

    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(new User()));

    when(likesRepository.findAllByUserId(any(), any()))
        .thenReturn(new PageImpl<>(likesList));

    when(recipeSearchRepository.findById(any()))
        .thenReturn(Optional.of(recipeDocument));

    when(userRepository.findById(any()))
        .thenReturn(Optional.of(User.builder().nickname("테스트 닉네임").build()));

    when(likesRepository.countByRecipeId(any()))
        .thenReturn(50L);

    Principal principal = new UsernamePasswordAuthenticationToken(
        "test@test.com", null);

    //when
    LikesDto likesDto =
        likesService.getLikes(principal, Pageable.unpaged())
            .getContent().get(0);

    //then
    assertEquals(likesDto.getTitle(), recipeDocument.getTitle());
    assertEquals(likesDto.getThumbnailUrl(), recipeDocument.getThumbnailUrl());
    assertEquals(likesDto.getRecipeId(), recipeDocument.getId());
    assertEquals(likesDto.getRecipeUserNickName(), "테스트 닉네임");
    assertEquals(likesDto.getLikeCount(), 50);
    assertEquals(likesDto.getViewCount(), 20);
  }

  @Test
  @DisplayName("좋아요 삭제 성공")
  void deleteLikes_SUCCESS() {
    //given
    User user = mock(User.class);
    User publisher = new User();
    publisher.setPlusPoint(UserPoint.LIKES);

    Likes likes = Likes.builder().user(user).build();

    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(user));

    when(likesRepository.findById(any()))
        .thenReturn(Optional.of(likes));

    when(recipeSearchRepository.findById(any()))
        .thenReturn(Optional.of(new RecipeDocument()));

    when(userRepository.findById(any()))
        .thenReturn(Optional.of(publisher));

    when(user.getId())
        .thenReturn(1L);

    Principal principal = new UsernamePasswordAuthenticationToken(
        "test@test.com", null);

    //when
    likesService.deleteLikes(principal, 1L);

    //then
    verify(likesRepository, times(1))
        .delete(any(Likes.class));
    assertEquals(publisher.getPoint(), 0);
  }


}