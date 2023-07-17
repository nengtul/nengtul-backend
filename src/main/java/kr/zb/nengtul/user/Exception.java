package kr.zb.nengtul.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class Exception extends RuntimeException{
  static final String EXCEPTION = "오류발생.";
  public Exception( ) {
    super(EXCEPTION);
  }
}