package com.zachmj.weather.infra.utils;

import com.fasterxml.jackson.core.JsonParseException;
import io.reactivex.functions.BiPredicate;
import lombok.extern.slf4j.Slf4j;
import retrofit2.HttpException;

import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;


@Slf4j
public class RetrofitUtils {

    public static BiPredicate<Integer, Throwable> bailOnDurableErrorPredicate(int maxRetry) {
        return (retryCount, error) -> {
            log.warn("Exception of {} with message {}", error.getClass().getSimpleName(), error.getMessage());

            if (error instanceof HttpException httpException) {
                switch (httpException.code()) {
                    case 401, 403, 404 -> {
                        log.error("HttpException {} with message {}", httpException.code(), httpException.message());
                        return false;
                    }
                }
            }

            if (error instanceof UnknownHostException) {
                log.error("UnknownHostException with message {}", error.getMessage());
                return false;
            }

            if (error instanceof JsonParseException) {
                log.error("JsonParseException with message {}", error.getMessage());
                return false;
            }

            return retryCount < maxRetry;
        };
    }

    public static <T> T on404Return(HttpException ex, T onDefault) {
        if (ex.code() == 404) {
            return onDefault;
        }
        throw ex;
    }
}
