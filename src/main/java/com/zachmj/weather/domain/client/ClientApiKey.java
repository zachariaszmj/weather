package com.zachmj.weather.domain.client;

public record ClientApiKey(long id, String apiKey, boolean revoked) {
}
