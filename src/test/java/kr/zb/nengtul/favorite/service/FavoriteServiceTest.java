package kr.zb.nengtul.favorite.service;

import kr.zb.nengtul.favorite.domain.dto.FavoriteDto;
import kr.zb.nengtul.favorite.domain.entity.Favorite;
import kr.zb.nengtul.favorite.domain.repository.FavoriteRepository;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.user.domain.constants.UserPoint;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.domain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("즐겨찾기 서비스 테스트")
class FavoriteServiceTest {

    private FavoriteService favoriteService;

    private FavoriteRepository favoriteRepository;

    private UserRepository userRepository;

    @BeforeEach
    void init() {

        favoriteRepository = mock(FavoriteRepository.class);
        userRepository = mock(UserRepository.class);

        favoriteService = new FavoriteService(favoriteRepository, userRepository);

    }

    @Test
    @DisplayName("즐겨찾기 등록 성공")
    void addFavorite_SUCCESS() {
        //given
        User publisher = User.builder().build();

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(new User()));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(publisher));

        Principal principal =
                new UsernamePasswordAuthenticationToken("test@test.com", null);

        //when
        favoriteService.addFavorite(principal, 1L);

        //then
        verify(favoriteRepository, times(1))
                .save(any(Favorite.class));
        assertEquals(publisher.getPoint(), UserPoint.FAVORITE.getPoint());
    }

    @Test
    @DisplayName("즐겨찾기 등록 실패 - 이미 등록된 즐겨찾기")
    void addFavorite_Fail_Already_Added_Favorite() {
        //given
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(new User()));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(new User()));
        when(favoriteRepository.findByUserIdAndPublisherId(any(), any()))
                .thenReturn(Optional.of(new Favorite()));

        Principal principal =
                new UsernamePasswordAuthenticationToken("test@test.com", null);

        //when
        CustomException customException =
                Assertions.assertThrows(CustomException.class,
                        () -> favoriteService.addFavorite(principal, 1L));

        //then
        assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_ADDED_FAVORITE);
    }

    @Test
    @DisplayName("즐겨찾기 가져오기 성공")
    void getFavorite_SUCCESS() {
        //given
        User publisher = User.builder()
                .nickname("testNickName")
                .profileImageUrl("testProfile")
                .build();

        Favorite favorite = Favorite.builder()
                .user(new User())
                .publisher(publisher)
                .build();

        List<Favorite> favorites = new ArrayList<>();
        favorites.add(favorite);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(new User()));
        when(favoriteRepository.findAllByUserId(any(), any()))
                .thenReturn(new PageImpl<>(favorites));

        Principal principal =
                new UsernamePasswordAuthenticationToken("test@test.com", null);

        //when
        FavoriteDto favoriteDto =
                favoriteService.getFavorite(principal, Pageable.ofSize(20))
                .getContent().get(0);

        //then
        assertEquals(favoriteDto.getClass(), FavoriteDto.class);
        assertEquals(publisher.getNickname(), favoriteDto.getPublisherNickName());
        assertEquals(publisher.getId(), favoriteDto.getPublisherId());
        assertEquals(publisher.getProfileImageUrl(), favoriteDto.getPublisherProfilePhotoUrl());
    }

    @Test
    @DisplayName("즐겨찾기 삭제 성공")
    void deleteFavorite_SUCCESS() {
        //given
        User user = mock(User.class);
        User publisher = new User();
        publisher.setPlusPoint(UserPoint.FAVORITE);
        when(user.getId())
                .thenReturn(1L);
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));
        when(favoriteRepository.findById(any()))
                .thenReturn(Optional.of(
                        Favorite.builder()
                            .user(user)
                            .publisher(publisher)
                            .build()));

        Principal principal =
                new UsernamePasswordAuthenticationToken("test@test.com", null);

        //when
        favoriteService.deleteFavorite(principal, 1L);

        //then
        verify(favoriteRepository, times(1))
                .delete(any(Favorite.class));
        assertEquals(publisher.getPoint(), 0);
    }

    @Test
    @DisplayName("즐겨찾기 삭제 실패 - 유저Id와 즐겨찾기의 유저 Id 불일치")
    void deleteFavorite_Fail_Mismatch_UserAndFavoriteUserId() {
        //given
        User user = mock(User.class);
        User diffrentUser = mock(User.class);
        when(user.getId())
                .thenReturn(1L);
        when(diffrentUser.getId())
                .thenReturn(2L);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));
        when(favoriteRepository.findById(any()))
                .thenReturn(Optional.of(
                        Favorite.builder().user(diffrentUser).build()));

        Principal principal =
                new UsernamePasswordAuthenticationToken("test@test.com", null);

        //when
        CustomException customException =
                Assertions.assertThrows(CustomException.class,
                        () -> favoriteService.deleteFavorite(principal, 1L));

        //then
        assertEquals(customException.getErrorCode(), ErrorCode.NO_PERMISSION);
    }


}