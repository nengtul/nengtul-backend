package kr.zb.nengtul.global.exception;

import java.util.List;
import java.util.Objects;
import kr.zb.nengtul.global.exception.CustomException.CustomExceptionResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CustomExceptionResponse> handleValidationException(
      MethodArgumentNotValidException ex) {

    List<String> errors = ex.getBindingResult().getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .filter(Objects::nonNull)
        .toList();

    CustomExceptionResponse errorResponse = new CustomExceptionResponse(
        HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
        String.join(", ", errors));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

}
