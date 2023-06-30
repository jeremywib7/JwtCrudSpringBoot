package com.j23.server.dto.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class HttpErrorFieldResponse<T> extends BaseResponse {

    public HttpErrorFieldResponse(Integer httpStatusCode, HttpStatus httpStatus, @NonNull String message, String url) {
        super(httpStatusCode, httpStatus, message);
        this.url = url;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String url;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FieldError> fieldErrors = new ArrayList<>();

    public void addFieldError(String path, String message) {
        FieldError error = new FieldError(path, message);
        fieldErrors.add(error);
    }
}
