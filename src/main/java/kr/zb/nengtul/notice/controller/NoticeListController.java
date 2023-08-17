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

@Tag(name = "NOTICE LIST API", description = "공지사항 조회 API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/noticelist")
public class NoticeListController {

  private final NoticeService noticeService;

  //전체 조회
  @Operation(summary = "공지사항 조회", description = "공지사항 목록을 전체 조회합니다. (Pageable 적용)")
  @GetMapping
  public ResponseEntity<Page<NoticeListDto>> getNoticeList(Pageable pageable) {
    return ResponseEntity.ok(
        noticeService.getNoticeList(pageable).map(NoticeListDto::buildNoticeListDto));
  }

  //상세 조회
  @Operation(summary = "공지사항 상세", description = "공지사항을 상세 조회합니다.")
  @GetMapping("/{noticeId}")
  public ResponseEntity<NoticeDetailDto> getNoticeDetails(
      @Parameter(name = "id)", description = "공지사항 ID") @PathVariable Long noticeId) {
    return ResponseEntity.ok(
        NoticeDetailDto.buildNoticeDetailDto(noticeService.getNoticeDetails(noticeId)));
  }
}
