package kr.zb.nengtul.recipe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.favorite.domain.entity.Favorite;
import kr.zb.nengtul.favorite.domain.repository.FavoriteRepository;
import kr.zb.nengtul.global.entity.RoleType;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.likes.domain.entity.Likes;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;
import s3bucket.service.AmazonS3Service;

@DisplayName("레시피 서비스 테스트")
class RecipeServiceTest {

  private RecipeService recipeService;

  private RecipeSearchRepository recipeSearchRepository;

  private UserRepository userRepository;

  private AmazonS3Service amazonS3Service;

  private LikesRepository likesRepository;

  private FavoriteRepository favoriteRepository;

  private List<RecipeDocument> recipeDocuments;

  @BeforeEach
  void setUp() {
    // Mock 객체 초기화
    recipeSearchRepository = mock(RecipeSearchRepository.class);
    amazonS3Service = mock(AmazonS3Service.class);
    userRepository = mock(UserRepository.class);
    likesRepository = mock(LikesRepository.class);
    favoriteRepository = mock(FavoriteRepository.class);

    recipeService = new RecipeService(
        recipeSearchRepository, userRepository, likesRepository, favoriteRepository, amazonS3Service);

    recipeDocuments = new ArrayList<>();

    recipeDocuments.add(RecipeDocument.builder()
        .id("userId1")
        .userId(1L)
        .title("테스트 타이틀 1")
        .intro("테스트 인트로 1")
        .ingredient("재료1, 재료2, 재료3")
        .cookingStep("테스트 쿠킹 스텝")
        .imageUrl("testimageurl1")
        .cookingTime("30분")
        .serving("1인분")
        .viewCount(0L)
        .createdAt(LocalDateTime.now())
        .modifiedAt(LocalDateTime.now())
        .category(RecipeCategory.BREAD)
        .build());

    recipeDocuments.add(RecipeDocument.builder()
        .id("userId2")
        .userId(2L)
        .title("테스트 타이틀 2")
        .intro("테스트 인트로 2")
        .ingredient("재료1, 재료2, 재료3")
        .cookingStep("테스트 쿠킹 스텝")
        .imageUrl("testimageurl2")
        .cookingTime("30분")
        .serving("2인분")
        .viewCount(0L)
        .createdAt(LocalDateTime.now())
        .modifiedAt(LocalDateTime.now())
        .category(RecipeCategory.BREAD)
        .build());

    recipeDocuments.add(RecipeDocument.builder()
        .id("userId3")
        .userId(3L)
        .title("테스트 타이틀 3")
        .intro("테스트 인트로 3")
        .ingredient("재료1, 재료2, 재료3")
        .cookingStep("테스트 쿠킹 스텝")
        .imageUrl("testimageurl3")
        .cookingTime("30분")
        .serving("3인분")
        .viewCount(0L)
        .createdAt(LocalDateTime.now())
        .modifiedAt(LocalDateTime.now())
        .category(RecipeCategory.BREAD)
        .build());

    recipeDocuments.add(RecipeDocument.builder()
        .id("userId4")
        .userId(4L)
        .title("테스트 타이틀 4")
        .intro("테스트 인트로 4")
        .ingredient("재료1, 재료2, 재료3")
        .cookingStep("테스트 쿠킹 스텝")
        .imageUrl("testimageurl4")
        .cookingTime("30분")
        .serving("4인분")
        .viewCount(0L)
        .createdAt(LocalDateTime.now())
        .modifiedAt(LocalDateTime.now())
        .category(RecipeCategory.BREAD)
        .build());

    recipeDocuments.add(RecipeDocument.builder()
        .id("userId5")
        .userId(5L)
        .title("테스트 타이틀 5")
        .intro("테스트 인트로 5")
        .ingredient("재료1, 재료2, 재료3")
        .cookingStep("테스트 쿠킹 스텝")
        .imageUrl("testimageurl1")
        .cookingTime("30분")
        .serving("5인분")
        .viewCount(0L)
        .createdAt(LocalDateTime.now())
        .modifiedAt(LocalDateTime.now())
        .category(RecipeCategory.BREAD)
        .build());
  }

  @Test
  @DisplayName("레시피 추가 테스트")
  void addRecipe() {
    //given
    RecipeAddDto recipeAddDto = new RecipeAddDto();
    List<MultipartFile> images = Collections.singletonList(mock(MultipartFile.class));
    MultipartFile thumbnail = mock(MultipartFile.class);

    User user = new User();

    when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

    when(amazonS3Service.uploadFileForRecipeCookingStep(anyList(), anyString()))
        .thenReturn("image_url");

    //when
    recipeService.addRecipe(mock(Principal.class), recipeAddDto, images, thumbnail);

    //then
    verify(recipeSearchRepository, times(1))
        .save(any(RecipeDocument.class));
  }

  @Test
  @DisplayName("레시피 전체 리스트 가져오기")
  void getAllRecipe() {
    //given
    Pageable pageable = Pageable.ofSize(20);

    when(recipeSearchRepository.findAll(pageable))
        .thenReturn(new PageImpl<>(recipeDocuments));
    when(userRepository.findById(any()))
        .thenReturn(Optional.of(new User()));

    //when
    Page<RecipeGetListDto> allRecipe = recipeService.getAllRecipe(pageable);

    RecipeDocument recipeDocument = recipeDocuments.get(0);
    RecipeGetListDto recipeGetListDto = allRecipe.getContent().get(0);

    //then
    assertEquals(recipeDocuments.size(), allRecipe.getContent().size());
    assertEquals(allRecipe.getContent().get(0).getClass(), RecipeGetListDto.class);
    assertEquals(recipeDocument.getTitle(), recipeGetListDto.getTitle());
    assertEquals(recipeDocument.getViewCount(), recipeGetListDto.getViewCount());
    assertEquals(recipeDocument.getThumbnailUrl(), recipeGetListDto.getThumbnailUrl());
  }

  @Test
  @DisplayName("레시피 상세 내역 가져오기 - 좋아요, 즐겨찾기를 하지 않은 경우")
  void getRecipeDetailById_UnLikes_UnFavorite() {
    //given
    RecipeDocument recipeDocument = RecipeDocument.builder()
        .id("userId1")
        .userId(1L)
        .title("테스트 타이틀 1")
        .intro("테스트 인트로 1")
        .ingredient("재료1, 재료2, 재료3")
        .cookingStep("테스트 쿠킹 스텝")
        .imageUrl("testimageurl1")
        .cookingTime("30분")
        .serving("1인분")
        .viewCount(0L)
        .createdAt(LocalDateTime.now())
        .modifiedAt(LocalDateTime.now())
        .category(RecipeCategory.BREAD)
        .build();

    when(recipeSearchRepository.findById(any()))
        .thenReturn(Optional.of(recipeDocument));
    when(userRepository.findById(any()))
        .thenReturn(Optional.of(new User()));
    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(new User()));

    Principal principal = new UsernamePasswordAuthenticationToken("", "");

    //when
    RecipeGetDetailDto recipeDetailById = recipeService.getRecipeDetailById(any(), principal);

    //then
    assertEquals(recipeDetailById.getId(), recipeDocument.getId());
    assertEquals(recipeDetailById.getUserId(), recipeDocument.getUserId());
    assertEquals(recipeDetailById.getTitle(), recipeDocument.getTitle());
    assertEquals(recipeDetailById.getIntro(), recipeDocument.getIntro());
    assertEquals(recipeDetailById.getIngredient(), recipeDocument.getIngredient());
    assertEquals(recipeDetailById.getCookingStep(), recipeDocument.getCookingStep());
    assertEquals(recipeDetailById.getImageUrl(), recipeDocument.getImageUrl());
    assertEquals(recipeDetailById.getCookingTime(), recipeDocument.getCookingTime());
    assertEquals(recipeDetailById.getServing(), recipeDocument.getServing());
    assertEquals(recipeDetailById.getViewCount(), recipeDocument.getViewCount());
    assertEquals(recipeDetailById.getViewCount(), 1L);
    assertEquals(recipeDetailById.getCreatedAt(), recipeDocument.getCreatedAt());
    assertEquals(recipeDetailById.getModifiedAt(), recipeDocument.getModifiedAt());
    assertEquals(recipeDetailById.getCategory(), recipeDocument.getCategory().getKorean());
    assertFalse(recipeDetailById.isLikes());
    assertFalse(recipeDetailById.isFavorite());
  }

  @Test
  @DisplayName("레시피 상세 내역 가져오기 - 좋아요, 즐겨찾기를 한 경우")
  void getRecipeDetailById_Likes_Favorite() {
    //given
    RecipeDocument recipeDocument = RecipeDocument.builder()
        .id("userId1")
        .userId(1L)
        .title("테스트 타이틀 1")
        .intro("테스트 인트로 1")
        .ingredient("재료1, 재료2, 재료3")
        .cookingStep("테스트 쿠킹 스텝")
        .imageUrl("testimageurl1")
        .cookingTime("30분")
        .serving("1인분")
        .viewCount(0L)
        .createdAt(LocalDateTime.now())
        .modifiedAt(LocalDateTime.now())
        .category(RecipeCategory.BREAD)
        .build();

    when(recipeSearchRepository.findById(any()))
        .thenReturn(Optional.of(recipeDocument));
    when(userRepository.findById(any()))
        .thenReturn(Optional.of(new User()));
    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(new User()));
    when(likesRepository.findByUserIdAndRecipeId(any(),any()))
        .thenReturn(Optional.of(new Likes()));
    when(favoriteRepository.findByUserIdAndPublisherId(any(),any()))
        .thenReturn(Optional.of(new Favorite()));

    Principal principal = new UsernamePasswordAuthenticationToken("", "");

    //when
    RecipeGetDetailDto recipeDetailById = recipeService.getRecipeDetailById(any(), principal);

    //then
    assertEquals(recipeDetailById.getId(), recipeDocument.getId());
    assertEquals(recipeDetailById.getUserId(), recipeDocument.getUserId());
    assertEquals(recipeDetailById.getTitle(), recipeDocument.getTitle());
    assertEquals(recipeDetailById.getIntro(), recipeDocument.getIntro());
    assertEquals(recipeDetailById.getIngredient(), recipeDocument.getIngredient());
    assertEquals(recipeDetailById.getCookingStep(), recipeDocument.getCookingStep());
    assertEquals(recipeDetailById.getImageUrl(), recipeDocument.getImageUrl());
    assertEquals(recipeDetailById.getCookingTime(), recipeDocument.getCookingTime());
    assertEquals(recipeDetailById.getServing(), recipeDocument.getServing());
    assertEquals(recipeDetailById.getViewCount(), recipeDocument.getViewCount());
    assertEquals(recipeDetailById.getViewCount(), 1L);
    assertEquals(recipeDetailById.getCreatedAt(), recipeDocument.getCreatedAt());
    assertEquals(recipeDetailById.getModifiedAt(), recipeDocument.getModifiedAt());
    assertEquals(recipeDetailById.getCategory(), recipeDocument.getCategory().getKorean());
    assertTrue(recipeDetailById.isLikes());
    assertTrue(recipeDetailById.isFavorite());
  }

  @Test
  @DisplayName("레시피 리스트 카테고리로 가져오기")
  void getRecipeByCategory() {
    //given
    Pageable pageable = Pageable.ofSize(20);

    when(recipeSearchRepository.findAllByCategory(RecipeCategory.BREAD, pageable))
        .thenReturn(new PageImpl<>(recipeDocuments));
    when(userRepository.findById(any()))
        .thenReturn(Optional.of(new User()));

    //when
    Page<RecipeGetListDto> allRecipe =
        recipeService.getRecipeByCategory(RecipeCategory.BREAD, pageable);

    //then
    assertEquals(recipeDocuments.size(), allRecipe.getContent().size());
    assertEquals(allRecipe.getContent().get(0).getClass(), RecipeGetListDto.class);
    assertEquals(allRecipe.getContent().get(0).getTitle(), recipeDocuments.get(0).getTitle());
  }

  @Test
  @DisplayName("레시피 리스트 타이틀로 가져오기")
  void getRecipeByTitle() {
    //given
    Pageable pageable = Pageable.ofSize(20);

    when(recipeSearchRepository.findAllByTitle("title", pageable))
        .thenReturn(new PageImpl<>(recipeDocuments));
    when(userRepository.findById(any()))
        .thenReturn(Optional.of(new User()));

    //when
    Page<RecipeGetListDto> allRecipe =
        recipeService.getRecipeByTitle("title", pageable);

    //then
    assertEquals(recipeDocuments.size(), allRecipe.getContent().size());
    assertEquals(allRecipe.getContent().get(0).getClass(), RecipeGetListDto.class);
    assertEquals(allRecipe.getContent().get(0).getTitle(), recipeDocuments.get(0).getTitle());
  }

  @Test
  @DisplayName("레시피 리스트 재료로 가져오기")
  void getRecipeByIngredient() {
    //given
    Pageable pageable = Pageable.ofSize(20);

    when(recipeSearchRepository.findAllByIngredient("ingredient", pageable))
        .thenReturn(new PageImpl<>(recipeDocuments));
    when(userRepository.findById(any()))
        .thenReturn(Optional.of(new User()));

    //when
    Page<RecipeGetListDto> allRecipe =
        recipeService.getRecipeByIngredient("ingredient", pageable);

    //then
    assertEquals(recipeDocuments.size(), allRecipe.getContent().size());
    assertEquals(allRecipe.getContent().get(0).getClass(), RecipeGetListDto.class);
    assertEquals(allRecipe.getContent().get(0).getTitle(), recipeDocuments.get(0).getTitle());
  }

  @Test
  @DisplayName("레시피 수정하기")
  void updateRecipe() {
    //given
    RecipeUpdateDto recipeUpdateDto = RecipeUpdateDto.builder()
        .title("수정된 타이틀")
        .ingredient("수정된 재료")
        .category(RecipeCategory.ETC)
        .imagesUrl("수정된 Url")
        .intro("")
        .videoUrl("")
        .cookingStep("수정된 조리 스텝")
        .cookingTime("수정된 시간")
        .serving("수정된 양")
        .build();

    User user = User.builder()
        .name("테스트이름")
        .build();

    RecipeDocument recipeDocument = RecipeDocument.builder()
        .id("userId1")
        .userId(user.getId())
        .title("테스트 타이틀 1")
        .intro("테스트 인트로 1")
        .ingredient("재료1, 재료2, 재료3")
        .cookingStep("테스트 쿠킹 스텝")
        .imageUrl("testimageurl1")
        .thumbnailUrl("testThumbnailUrl")
        .cookingTime("30분")
        .serving("1인분")
        .viewCount(0L)
        .createdAt(LocalDateTime.now())
        .modifiedAt(LocalDateTime.now())
        .category(RecipeCategory.BREAD)
        .build();

    Principal principal = new UsernamePasswordAuthenticationToken(
        "updateRecipe@test.com", null);

    when(userRepository.findByEmail("updateRecipe@test.com"))
        .thenReturn(Optional.of(user));

    when(recipeSearchRepository.findById(any()))
        .thenReturn(Optional.of(recipeDocument));

    //when
    recipeService.updateRecipe(principal, recipeDocument.getId(), recipeUpdateDto,
        Collections.singletonList(mock(MultipartFile.class)), mock(MultipartFile.class));

    //then
    assertEquals(recipeDocument.getTitle(), recipeUpdateDto.getTitle());
  }

  @Test
  @DisplayName("레시피 삭제하기 성공")
  void deleteRecipe_SUCCESS() {
    //given
    User user = User.builder()
        .name("테스트이름")
        .build();

    RecipeDocument recipeDocument = RecipeDocument.builder()
        .id("recipdId")
        .userId(user.getId())
        .title("테스트 타이틀 1")
        .intro("테스트 인트로 1")
        .ingredient("재료1, 재료2, 재료3")
        .cookingStep("테스트 쿠킹 스텝")
        .imageUrl("testimageurl1")
        .cookingTime("30분")
        .serving("1인분")
        .viewCount(0L)
        .createdAt(LocalDateTime.now())
        .modifiedAt(LocalDateTime.now())
        .category(RecipeCategory.BREAD)
        .build();

    Principal principal = new UsernamePasswordAuthenticationToken(
        "updateRecipe@test.com", null);

    when(userRepository.findByEmail("updateRecipe@test.com"))
        .thenReturn(Optional.of(user));

    when(recipeSearchRepository.findById(any()))
        .thenReturn(Optional.of(recipeDocument));

    //when
    recipeService.deleteRecipe(principal, recipeDocument.getId());

    //then
    verify(recipeSearchRepository, times(1))
        .delete(any(RecipeDocument.class));
  }

  @Test
  @DisplayName("레시피 삭제하기 성공 - 어드민 계정")
  void deleteRecipe_SUCCESS_Admin_Account() {
    //given
    User adminAccount = mock(User.class);

    RecipeDocument recipeDocument = mock(RecipeDocument.class);

    Principal principal = new UsernamePasswordAuthenticationToken(
        "updateRecipe@test.com", null);

    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(adminAccount));
    when(recipeSearchRepository.findById(any()))
        .thenReturn(Optional.of(recipeDocument));

    when(recipeDocument.getUserId()).thenReturn(1L);
    when(adminAccount.getId()).thenReturn(2L);

    when(adminAccount.getRoles()).thenReturn(RoleType.ADMIN);

    when(recipeDocument.getThumbnailUrl()).thenReturn("");
    when(recipeDocument.getImageUrl()).thenReturn("");

    //when
    recipeService.deleteRecipe(principal, recipeDocument.getId());

    //then
    verify(recipeSearchRepository, times(1))
        .delete(any(RecipeDocument.class));
  }

  @Test
  @DisplayName("레시피 삭제 실패 - 레시피의 유저 이외 유저가 삭제 시도")
  void deleteRecipe_FAIL_NO_PERMISSION() {
    //given
    User noPermissionUser = mock(User.class);

    RecipeDocument recipeDocument = mock(RecipeDocument.class);

    Principal principal = new UsernamePasswordAuthenticationToken(
        "updateRecipe@test.com", null);

    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(noPermissionUser));
    when(recipeSearchRepository.findById(any()))
        .thenReturn(Optional.of(recipeDocument));

    when(recipeDocument.getUserId()).thenReturn(1L);
    when(noPermissionUser.getId()).thenReturn(2L);

    when(noPermissionUser.getRoles()).thenReturn(RoleType.USER);

    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> recipeService.deleteRecipe(principal, recipeDocument.getId()));

    //then
    assertEquals(ErrorCode.NO_PERMISSION, customException.getErrorCode());
  }

  @Test
  @DisplayName("유저 아이디로 레시피 전체 조회")
  void getAllRecipeByUser() {
    //given
    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(new User()));
    when(userRepository.findById(any()))
        .thenReturn(Optional.of(new User()));

    when(recipeSearchRepository.findAllByUserId(any(), any()))
        .thenReturn(new PageImpl<>(recipeDocuments));

    //when
    Page<RecipeGetListDto> allRecipeByUserId =
        recipeService.getAllRecipeByUserId(1L, Pageable.ofSize(5));

    RecipeDocument recipeDocument = recipeDocuments.get(0);
    RecipeGetListDto recipeGetListDto = allRecipeByUserId.getContent().get(0);

    //then
    assertEquals(recipeDocuments.size(), allRecipeByUserId.getContent().size());
    assertEquals(allRecipeByUserId.getContent().get(0).getClass(), RecipeGetListDto.class);
    assertEquals(recipeDocument.getTitle(), recipeGetListDto.getTitle());
    assertEquals(recipeDocument.getViewCount(), recipeGetListDto.getViewCount());
    assertEquals(recipeDocument.getThumbnailUrl(), recipeGetListDto.getThumbnailUrl());
  }

}
