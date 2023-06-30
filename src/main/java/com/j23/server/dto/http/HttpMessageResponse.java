package com.j23.server.dto.http;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
@Setter
public class HttpMessageResponse extends BaseResponse {
    public HttpMessageResponse(Integer httpStatusCode, HttpStatus httpStatus, @NonNull String message) {
        super(httpStatusCode, httpStatus, message);
    }
}
