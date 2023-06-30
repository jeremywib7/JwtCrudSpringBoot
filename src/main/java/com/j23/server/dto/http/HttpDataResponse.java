package com.j23.server.dto.http;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
@Setter
public class HttpDataResponse<T> extends BaseResponse {

    public HttpDataResponse(Integer httpStatusCode, HttpStatus httpStatus, @NonNull String message, T data) {
        super(httpStatusCode, httpStatus, message);
        this.data = data;
    }

    private T data;
}
