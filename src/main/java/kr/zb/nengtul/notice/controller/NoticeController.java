package kr.zb.nengtul.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import kr.zb.nengtul.notice.domain.dto.NoticeDetailDto;
import kr.zb.nengtul.notice.domain.dto.NoticeListDto;
import kr.zb.nengtul.notice.domain.dto.NoticeReqDto;
import kr.zb.nengtul.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "NOTICE API", description = "공지사항 API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/notices")
public class NoticeController {

  private final NoticeService noticeService;

  //생성
  @Operation(summary = "공지사항 작성", description = "토큰을 통해 관리자여부를 확인 후 공지사항을 작성합니다.")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping
  public ResponseEntity<Void> createNotice(
      @RequestPart(value = "noticeReqDto") @Valid NoticeReqDto noticeReqDto,
      @RequestPart(value = "images", required = false) List<MultipartFile> images,
      Principal principal) {
    noticeService.createNotice(noticeReqDto, principal, images);
    return ResponseEntity.ok(null);
  }

  //수정
  @Operation(summary = "공지사항 수정", description = "토큰을 통해 관리자여부, 작성자 여부를 확인 후 공지사항을 수정합니다.")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/{noticeId}")
  public ResponseEntity<Void> updateNotice(
      @Parameter(name = "email", description = "공지사항 ID") @PathVariable Long noticeId,
      @RequestPart(value = "noticeReqDto") @Valid NoticeReqDto noticeReqDto,
      @RequestPart(value = "images", required = false) List<MultipartFile> images,
      Principal principal) {
    noticeService.updateNotice(noticeId, noticeReqDto, principal, images);
    return ResponseEntity.ok(null);
  }

  //삭제
  @Operation(summary = "공지사항 삭제", description = "토큰을 통해 관리자여부, 작성자 여부를 확인 후 공지사항을 삭제합니다.")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/{noticeId}")
  public ResponseEntity<Void> deleteNotice(
      @Parameter(name = "id)", description = "공지사항 ID") @PathVariable Long noticeId,
      Principal principal) {
    noticeService.deleteNotice(noticeId, principal);
    return ResponseEntity.ok(null);
  }
}
