package kr.zb.nengtul.favorite.controller;

import kr.zb.nengtul.favorite.domain.dto.FavoriteDto;
import kr.zb.nengtul.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/publisher/{publisherId}")
    ResponseEntity<Void> addFavorite(Principal principal,
                              @PathVariable Long publisherId) {

        favoriteService.addFavorite(principal, publisherId);

        return ResponseEntity.ok(null);
    }

    @GetMapping
    ResponseEntity<Page<FavoriteDto>> getFavorite(Principal principal, Pageable pageable) {

        return ResponseEntity.ok(favoriteService.getFavorite(principal, pageable));
    }

    @DeleteMapping("/{favoriteId}")
    ResponseEntity<Void> deleteLike(Principal principal,
                                 @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(principal, favoriteId);

        return ResponseEntity.ok(null);
    }

}
