package kr.zb.nengtul.global.exception;

import static kr.zb.nengtul.global.exception.CustomException.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiExceptionAdvice {

    // CustomException 이 발생했을 때 처리하는 핸들러 메서드
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomExceptionResponse> exceptionHandler(
            CustomException e) {
        log.error("'{}':'{}'", e.getErrorCode(), e.getErrorCode().getDetail());
        return ResponseEntity
                .status(e.getStatus())
                .body(CustomExceptionResponse.builder()
                        .message(e.getMessage())
                        .code(e.getErrorCode().name())
                        .status(e.getStatus()).build());
    }

    @MessageExceptionHandler(CustomException.class)
    @SendToUser(destinations="/queue/errors", broadcast=false)
    public CustomExceptionResponse websocketHandler(CustomException e) {

        return CustomException.CustomExceptionResponse.builder()
                .message(e.getMessage())
                .code(e.getErrorCode().name())
                .status(e.getStatus()).build();

    }
}