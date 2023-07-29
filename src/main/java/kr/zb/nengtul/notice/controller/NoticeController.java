package kr.zb.nengtul.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import kr.zb.nengtul.notice.domain.dto.NoticeDetailDto;
import kr.zb.nengtul.notice.domain.dto.NoticeListDto;
import kr.zb.nengtul.notice.domain.dto.NoticeReqDto;
import kr.zb.nengtul.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "NOTICE API", description = "공지사항 API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/notice")
public class NoticeController {

  private final NoticeService noticeService;

  //생성
  @Operation(summary = "공지사항 작성", description = "토큰을 통해 관리자여부를 확인 후 공지사항을 작성합니다.")
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody @Valid NoticeReqDto noticeReqDto,
      Principal principal) {
    noticeService.create(noticeReqDto, principal);
    return ResponseEntity.ok(null);
  }

  //수정
  @Operation(summary = "공지사항 수정", description = "토큰을 통해 관리자여부, 작성자 여부를 확인 후 공지사항을 수정합니다.")
  @PutMapping("/{noticeId}")
  public ResponseEntity<Void> update(
      @Parameter(name = "email", description = "공지사항 ID") @PathVariable Long noticeId,
      @RequestBody @Valid NoticeReqDto noticeReqDto, Principal principal) {
    noticeService.update(noticeId, noticeReqDto, principal);
    return ResponseEntity.ok(null);
  }

  //삭제
  @Operation(summary = "공지사항 삭제", description = "토큰을 통해 관리자여부, 작성자 여부를 확인 후 공지사항을 삭제합니다.")
  @DeleteMapping("/{noticeId}")
  public ResponseEntity<Void> delete(
      @Parameter(name = "id)", description = "공지사항 ID") @PathVariable Long noticeId,
      Principal principal) {
    noticeService.delete(noticeId, principal);
    return ResponseEntity.ok(null);
  }

  //전체 조회
  @Operation(summary = "공지사항 조회", description = "공지사항 목록을 전체 조회합니다. (Pageable 적용)")
  @GetMapping("/list")
  public ResponseEntity<Page<NoticeListDto>> getList(Pageable pageable) {
    return ResponseEntity.ok(noticeService.getList(pageable));
  }

  //상세 조회
  @Operation(summary = "공지사항 상세", description = "공지사항을 상세 조회합니다.")
  @GetMapping("/list/{noticeId}")
  public ResponseEntity<NoticeDetailDto> getDetails(
      @Parameter(name = "id)", description = "공지사항 ID") @PathVariable Long noticeId) {
    return ResponseEntity.ok(noticeService.getDetails(noticeId));
  }
}
