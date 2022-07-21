package com.j23.server.models.utils;

import java.util.ArrayList;
import java.util.List;

public class HttpErrorResponse {

  public HttpErrorResponse(int status, String message) {
    this.status = status;
    this.message = message;
  }

  private final int status;
  private final String message;
  private List<FieldError> fieldErrors = new ArrayList<>();

  public int getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public void addFieldError(String path, String message) {
    FieldError error = new FieldError(path, message);
    fieldErrors.add(error);
  }

  public List<FieldError> getFieldErrors() {
    return fieldErrors;
  }
}
