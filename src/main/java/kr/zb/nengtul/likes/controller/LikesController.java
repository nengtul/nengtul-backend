package kr.zb.nengtul.likes.controller;

import java.security.Principal;

import kr.zb.nengtul.likes.domain.dto.LikesDto;
import kr.zb.nengtul.likes.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/likes")
@RequiredArgsConstructor
public class LikesController {

  private final LikesService likesService;

  @PostMapping("/recipe/{recipeId}")
  ResponseEntity<Void> addLike(Principal principal,
      @PathVariable String recipeId) {

    likesService.addLikes(principal, recipeId);

    return ResponseEntity.ok(null);
  }

  @GetMapping
  ResponseEntity<Page<LikesDto>> getLike(Principal principal, Pageable pageable) {

    return ResponseEntity.ok(likesService.getLikes(principal, pageable));
  }

  @DeleteMapping("/{likeId}")
  ResponseEntity<Void> deleteLike(Principal principal,
      @PathVariable Long likeId) {
    likesService.deleteLikes(principal, likeId);

    return ResponseEntity.ok(null);
  }

}
