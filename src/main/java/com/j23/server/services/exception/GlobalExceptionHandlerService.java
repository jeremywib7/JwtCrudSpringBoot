package com.j23.server.services.exception;

import com.j23.server.models.utils.HttpErrorResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class GlobalExceptionHandlerService {
  public HttpErrorResponse processFieldErrors(List<FieldError> fieldErrors) {
    HttpErrorResponse error = new HttpErrorResponse(BAD_REQUEST.value(), "Validation error");
    for (org.springframework.validation.FieldError fieldError : fieldErrors) {
      error.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
    }
    return error;
  }

}
