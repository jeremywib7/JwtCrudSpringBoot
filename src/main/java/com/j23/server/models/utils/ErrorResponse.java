package com.j23.server.models.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
  private String timestamp;
  private int status;
  private String message;
}
