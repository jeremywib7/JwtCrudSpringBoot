package com.j23.server.controllers.exception;

import com.j23.server.models.utils.ErrorResponse;
import com.j23.server.models.utils.HttpErrorResponse;
import com.j23.server.services.exception.GlobalExceptionHandlerService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.core.config.Order;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpStatus.*;

/**
 * Kudos http://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-adding-validation-to-a-rest-api/
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandlerController {

  private final GlobalExceptionHandlerService gehService;

//  @ExceptionHandler(Exception.class)
//  public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
//    List<String> details = new ArrayList<>();
//    details.add(ex.getLocalizedMessage());
//    System.out.println("THE MESSAGE : " + getRootCauseMessage(ex.getCause()));
//    ErrorResponse error = new ErrorResponse(ex.getLocalizedMessage(), details);
//    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//  }

  // catch when field required null
  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public final ResponseEntity<ErrorResponse> handleAllExceptions(SQLIntegrityConstraintViolationException ex, WebRequest request) {
    List<String> details = new ArrayList<>();
    details.add(ex.getLocalizedMessage());
    ErrorResponse error = new ErrorResponse(
      ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT), 400, ex.getLocalizedMessage());
    return new ResponseEntity<>(error, BAD_REQUEST);
  }

  // catch when field required empty
  @ResponseStatus(BAD_REQUEST)
  @ResponseBody
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public HttpErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
    return gehService.processFieldErrors(fieldErrors);
  }

}
