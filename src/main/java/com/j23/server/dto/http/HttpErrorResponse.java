package com.j23.server.dto.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
@Setter
public class HttpErrorResponse<T> extends BaseResponse {

    public HttpErrorResponse(Integer httpStatusCode, HttpStatus httpStatus, @NonNull String message, String url) {
        super(httpStatusCode, httpStatus, message);
        this.url = url;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String url;
}
