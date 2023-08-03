package com.example.wantedpreonboardingbackend.util.global;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@AllArgsConstructor
public class ResponseMessage<T> {
    private final HttpStatus httpStatus;
    private final String message;

    private final T data;

    public static <T> ResponseEntity<ResponseMessage<T>> SuccessResponse(String message,T data) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseMessage.<T>builder()
                .httpStatus(HttpStatus.OK)
                .message(message)
                .data(data)
                .build());
    }
    public static <T> ResponseEntity<ResponseMessage<T>> ErrorResponse(GlobalErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ResponseMessage.<T>builder()
                .httpStatus(errorCode.getHttpStatus())
                .message(errorCode.getMessage())
                .build());
    }
}
