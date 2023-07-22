package kr.zb.nengtul.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ApiExceptionAdvice {
  // CustomException 이 발생했을 때 처리하는 핸들러 메서드
  @ExceptionHandler({CustomException.class})
  public ResponseEntity<CustomException.CustomExceptionResponse> exceptionHandler(CustomException e){
    // CustomExceptionResponse 객체를 생성하여 응답 반환
    return ResponseEntity
        .status(e.getStatus())
        .body(CustomException.CustomExceptionResponse.builder()
            .message(e.getMessage())
            .code(e.getErrorCode().name())
            .status(e.getStatus()).build());
  }
}