package com.zachmj.weather.infra.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity handleValidationException(NativeWebRequest request, IllegalArgumentException ex) {
        return create(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }

    @ExceptionHandler(value = {CannotGetCurrentWeatherInfo.class})
    protected ResponseEntity handleUnauthorized(NativeWebRequest request, Exception ex) {
        return create(HttpStatus.EXPECTATION_FAILED, ex.getMessage(), ex);
    }

    private ResponseEntity create(HttpStatus status, String detail, Throwable ex) {
        ApiProblem apiProblem = ApiProblem.builder()
                .type(ex.getClass().getSimpleName())
                .title(status.getReasonPhrase())
                .status(status.value())
                .detail(detail)
                .build();
        return create(apiProblem, ex);
    }

    private ResponseEntity create(ApiProblem apiProblem, Throwable ex) {
        log.error("Exception occurred: {} converted to {}", ex.getMessage(), apiProblem, ex);
        return ResponseEntity
                .status(HttpStatus.valueOf(apiProblem.getStatus()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiProblem);
    }
}
