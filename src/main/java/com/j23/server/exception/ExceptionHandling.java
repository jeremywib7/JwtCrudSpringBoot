package com.j23.server.exception;

import com.j23.server.dto.http.HttpErrorFieldResponse;
import com.j23.server.dto.http.HttpErrorResponse;
import com.j23.server.dto.http.HttpMessageResponse;
import com.j23.server.models.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ExceptionHandling implements ErrorController {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> emailExistsException(BadCredentialsException exception) {
        log.error(exception.getMessage());
        return createHttpResponse(BAD_REQUEST,  "Username or password incorrect");
    }

    @ExceptionHandler(GoogleTokenException.class)
    public ResponseEntity<HttpResponse> emailExistsException(GoogleTokenException exception) {
        log.error(exception.getMessage());
        return createHttpResponse(exception.getHttpStatus(),  exception.getErrorMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
        log.error(exception.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
    }

    public static ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }

    public static ResponseEntity<HttpMessageResponse> createHttpMessageResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpMessageResponse(httpStatus.value(), httpStatus, message), httpStatus);
    }


    public static ResponseEntity<HttpErrorResponse> createHttpErrorResponse(HttpStatus httpStatus, String message, String endpoint) {
        return new ResponseEntity<>(new HttpErrorResponse(httpStatus.value(), httpStatus, message, endpoint), httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpErrorFieldResponse> methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return createHttpResponseWithFieldErrors(fieldErrors, getExceptionEndpoint(request));
    }

    private ResponseEntity<HttpErrorFieldResponse> createHttpResponseWithFieldErrors(List<FieldError> fieldErrors, String endpoint) {
        HttpErrorFieldResponse httpErrorFieldResponse = new HttpErrorFieldResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                "Validation error", endpoint);
        for (FieldError fieldError : fieldErrors) {
            httpErrorFieldResponse.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(httpErrorFieldResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(NoHandlerFoundException.class)
//    protected ResponseEntity<HttpResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
//        return createHttpResponse(NOT_FOUND, ex.getMessage());
//
//    }

    @RequestMapping(value = "/error")
    public ResponseEntity<HttpResponse> notFound404() {
        return createHttpResponse(NOT_FOUND, "There is no mapping for this URL");
    }

    private static String getExceptionEndpoint(HttpServletRequest request) {
        String endpoint = request.getRequestURI(); // Get the requested URL
        log.error("Exception occurred at endpoint: {}", endpoint);
        return endpoint;
    }

}
