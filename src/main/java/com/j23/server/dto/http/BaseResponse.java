package com.j23.server.dto.http;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class BaseResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
    private Date timeStamp = new Date();

    @NonNull
    private Integer httpStatusCode;

    @NonNull
    private HttpStatus httpStatus;

    @NonNull
    private String message;
}
