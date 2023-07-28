package kr.zb.nengtul.shareboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import kr.zb.nengtul.shareboard.domain.dto.ShareBoardDto;
import kr.zb.nengtul.shareboard.domain.dto.ShareBoardListDto;
import kr.zb.nengtul.shareboard.service.ShareBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SHARE BOARD API", description = "나눔 게시판 API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/shareboard")
public class ShareBoardController {

  private final ShareBoardService shareBoardService;

  //생성
  @Operation(summary = "게시글 작성", description = "게시글을 작성합니다.")
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody @Valid ShareBoardDto shareBoardDto,
      Principal principal) {
    shareBoardService.create(shareBoardDto, principal);
    return ResponseEntity.ok(null);
  }

  //전체 조회
  @Operation(summary = "나눔 게시물 조회", description = "내 위치 주변 나눔 게시물을 조회합니다."
      + "closed는 필수가 아니며 기입하지 않을 시 모든 상태에 대한 값이 반환되며, true = 거래완료 , false = 거래대기 상태의 게시물을 가져옵니다.")
  @GetMapping
  public ResponseEntity<List<ShareBoardListDto>> getList(
      @Parameter(name = "lat", description = "경도") @RequestParam double lat,
      @Parameter(name = "lon", description = "위도") @RequestParam double lon,
      @Parameter(name = "range", description = "반경 범위") @RequestParam double range,
      @Parameter(name = "closed", description = "완료 여부") @RequestParam(required = false) Boolean closed) {
    return ResponseEntity.ok(shareBoardService.getList(lat, lon, range, closed));
  }

  //수정
  @Operation(summary = "게시글 수정", description = "토큰을 통해 유저를 조회하고, 게시물 ID를 통해 유저 ID를 조회하여 비교 후 글의 작성자인 경우에 게시물을 수정할 수 있습니다.")
  @PutMapping("/{id}")
  public ResponseEntity<Void> create(
      @Parameter(name = "id", description = "게시물 ID") @PathVariable Long id,
      @RequestBody @Valid ShareBoardDto shareBoardDto,
      Principal principal) {
    shareBoardService.update(id, shareBoardDto, principal);
    return ResponseEntity.ok(null);
  }

  //삭제
  @Operation(summary = "게시글 삭제", description = "토큰을 통해 유저를 조회하고, 게시물 ID를 통해 유저 ID를 조회하여 비교 후 글의 작성자인 경우에 게시물을 삭제할 수 있습니다.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(name = "id", description = "게시물 ID") @PathVariable Long id, Principal principal) {
    shareBoardService.delete(id, principal);
    return ResponseEntity.ok(null);
  }
}

