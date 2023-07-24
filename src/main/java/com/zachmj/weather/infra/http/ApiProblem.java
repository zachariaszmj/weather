package com.zachmj.weather.infra.http;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


@Builder
@Getter
@ToString
class ApiProblem {
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    @Builder.Default
    private String type = "about:blank";
    private String title;
    private int status;
    private String detail;
}
