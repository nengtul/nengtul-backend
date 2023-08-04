package kr.zb.nengtul.favorite.service;

import kr.zb.nengtul.favorite.domain.entity.Favorite;
import kr.zb.nengtul.favorite.domain.repository.FavoriteRepository;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
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
    void addFavorite_SUCCESS(){
        //given
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(new User()));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(new User()));

        Principal principal =
                new UsernamePasswordAuthenticationToken("test@test.com", null);

        //when
        favoriteService.addFavorite(principal, 1L);

        //then
        verify(favoriteRepository, times(1))
                .save(any(Favorite.class));
    }

    @Test
    @DisplayName("즐겨찾기 등록 실패 - 이미 등록된 즐겨찾기")
    void addFavorite_Fail_Already_Added_Favorite(){
        //given
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(new User()));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(new User()));

        Principal principal =
                new UsernamePasswordAuthenticationToken("test@test.com", null);

        //when
        favoriteService.addFavorite(principal, 1L);

        //then
        verify(favoriteRepository, times(1))
                .save(any(Favorite.class));
    }


















}