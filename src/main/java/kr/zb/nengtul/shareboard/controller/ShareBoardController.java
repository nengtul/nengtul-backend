package kr.zb.nengtul.shareboard.controller;

import jakarta.validation.Valid;
import java.security.Principal;
import kr.zb.nengtul.shareboard.domain.dto.ShareBoardDto;
import kr.zb.nengtul.shareboard.service.ShareBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/shareboard")
public class ShareBoardController {
  private final ShareBoardService shareBoardService;
  //생성
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody @Valid ShareBoardDto shareBoardDto,
      Principal principal) {
    shareBoardService.create(shareBoardDto, principal);
    return ResponseEntity.ok(null);
  }

  //수정
  @PutMapping("/{id}")
  public ResponseEntity<Void> create(@PathVariable Long id, @RequestBody @Valid ShareBoardDto shareBoardDto,
      Principal principal) {
    shareBoardService.update(id,shareBoardDto, principal);
    return ResponseEntity.ok(null);
  }

  //삭제
  @PutMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
    shareBoardService.delete(id, principal);
    return ResponseEntity.ok(null);
  }
}
