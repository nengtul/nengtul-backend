package kr.zb.nengtul.notice.controller;

import java.security.Principal;
import kr.zb.nengtul.notice.entitiy.dto.NoticeReqDto;
import kr.zb.nengtul.notice.entitiy.dto.NoticeResDto;
import kr.zb.nengtul.notice.service.NoticeService;
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
@RequestMapping("/v1/notice")
public class NoticeController {

  private final NoticeService noticeService;

  //생성
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody NoticeReqDto noticeReqDto, Principal principal) {
    noticeService.create(noticeReqDto, principal);
    return ResponseEntity.ok(null);
  }

  //수정
  @PutMapping("/{noticeId}")
  public ResponseEntity<Void> update(@PathVariable Long noticeId, @RequestBody NoticeReqDto noticeReqDto, Principal principal) {
    noticeService.update(noticeId, noticeReqDto, principal);
    return ResponseEntity.ok(null);
  }

}
