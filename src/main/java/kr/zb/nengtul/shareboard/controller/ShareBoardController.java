package kr.zb.nengtul.shareboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import kr.zb.nengtul.shareboard.domain.dto.ShareBoardDto;
import kr.zb.nengtul.shareboard.domain.dto.ShareBoardListDto;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.shareboard.service.ShareBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "SHARE BOARD API", description = "나눔 게시판 API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/shareboards")
public class ShareBoardController {

  private final ShareBoardService shareBoardService;

  //생성
  @Operation(summary = "게시글 작성", description = "게시글을 작성합니다.")
  @PostMapping
  public ResponseEntity<Void> createShareBoard(
      @RequestPart(value = "shareBoardDto") @Valid ShareBoardDto shareBoardDto,
      @RequestPart(value = "image", required = false) MultipartFile image, Principal principal) {
    shareBoardService.createShareBoard(shareBoardDto, principal, image);
    return ResponseEntity.ok(null);
  }

  //전체 조회
  @Operation(summary = "나눔 게시물 조회", description = "내 위치 주변 나눔 게시물을 조회합니다."
      + "closed는 필수가 아니며 기입하지 않을 시 모든 상태에 대한 값이 반환되며, true = 거래완료 , false = 거래대기 상태의 게시물을 가져옵니다.")
  @GetMapping
  public ResponseEntity<List<ShareBoardListDto>> getShareBoardList(
      @Parameter(name = "lat", description = "경도") @RequestParam double lat,
      @Parameter(name = "lon", description = "위도") @RequestParam double lon,
      @Parameter(name = "range", description = "반경 범위") @RequestParam double range,
      @Parameter(name = "closed", description = "완료 여부") @RequestParam(required = false) Boolean closed) {
    return ResponseEntity.ok(shareBoardService.getShareBoardList(lat, lon, range, closed).stream()
        .map(ShareBoardListDto::buildShareBoardListDto)
        .collect(Collectors.toList()));
  }

  //수정
  @Operation(summary = "게시글 수정", description = "토큰을 통해 유저를 조회하고, 게시물 ID를 통해 유저 ID를 조회하여 비교 후 글의 작성자인 경우에 게시물을 수정할 수 있습니다.")
  @PostMapping("/{shareboardId}")
  public ResponseEntity<Void> updateShareBoard(
      @Parameter(name = "shareboardId", description = "게시물 ID") @PathVariable Long shareboardId,
      @RequestPart(value = "shareBoardDto") @Valid ShareBoardDto shareBoardDto,
      @RequestPart(value = "image", required = false) MultipartFile image,
      Principal principal) {
    shareBoardService.updateShareBoard(shareboardId, shareBoardDto, principal, image);
    return ResponseEntity.ok(null);
  }

  //삭제
  @Operation(summary = "게시글 삭제", description = "토큰을 통해 유저를 조회하고, 게시물 ID를 통해 유저 ID를 조회하여 비교 후 글의 작성자인 경우에 게시물을 삭제할 수 있습니다.")
  @DeleteMapping("/{shareboardId}")
  public ResponseEntity<Void> deleteShareBoard(
      @Parameter(name = "shareboardId", description = "게시물 ID") @PathVariable Long shareboardId,
      Principal principal) {
    shareBoardService.deleteShareBoard(shareboardId, principal);
    return ResponseEntity.ok(null);
  }

  //내 나눔게시물 리스트 조회
  @Operation(summary = "내 나눔게시물 조회", description = "토큰을 통해 유저를 조회하고, 내가 작성한 나눔게시물을 조회합니다.")
  @GetMapping("/mylist")
  public ResponseEntity<List<ShareBoard>> myShareBoard(Principal principal) {

    return ResponseEntity.ok(shareBoardService.getMyShareBoard(principal));
  }
}

