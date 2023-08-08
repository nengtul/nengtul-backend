package kr.zb.nengtul.favorite.service;

import kr.zb.nengtul.favorite.domain.dto.FavoriteDto;
import kr.zb.nengtul.favorite.domain.entity.Favorite;
import kr.zb.nengtul.favorite.domain.repository.FavoriteRepository;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.user.domain.constants.UserPoint;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final UserRepository userRepository;

    @Transactional
    public void addFavorite(Principal principal, Long publisherId) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        User publisher = userRepository.findById(publisherId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        favoriteRepository.findByUserIdAndPublisherId(user.getId(), publisherId)
                .ifPresent(favorite -> {
                    throw new CustomException(ErrorCode.ALREADY_ADDED_FAVORITE);
                });

        publisher.setPlusPoint(UserPoint.FAVORITE);

        userRepository.save(publisher);

        favoriteRepository.save(Favorite.builder()
                .user(user)
                .publisher(publisher)
                .build());

    }

    public Page<FavoriteDto> getFavorite(Principal principal, Pageable pageable) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return favoriteRepository.findAllByUserId(user.getId(), pageable)
                .map(favorite -> FavoriteDto.builder()
                        .id(favorite.getId())
                        .publisherId(favorite.getPublisher().getId())
                        .publisherNickName(favorite.getPublisher().getNickname())
                        .publisherProfilePhotoUrl(favorite.getPublisher().getProfileImageUrl())
                        .build());
    }

    @Transactional
    public void deleteFavorite(Principal principal, Long favoriteId) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FAVORITE));

        if (!user.getId().equals(favorite.getUser().getId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        favorite.getPublisher().setMinusPoint(UserPoint.FAVORITE);

        userRepository.save(favorite.getPublisher());

        favoriteRepository.delete(favorite);

    }
}
