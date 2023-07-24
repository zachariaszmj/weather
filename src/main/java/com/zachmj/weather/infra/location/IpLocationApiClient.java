package com.zachmj.weather.infra.location;

import com.zachmj.weather.domain.location.GeoLocation;
import com.zachmj.weather.infra.config.GlobalCacheConfig;
import com.zachmj.weather.infra.utils.RetrofitUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import retrofit2.HttpException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
class IpLocationApiClient implements LocationInfoProvider {

    @Value("${retrofit.retry.default-attempts:3}")
    private int retryAttempts;
    @Value("${ip-api.timeout-millis:1000}")
    private int timeout;
    private final IpLocationApi ipLocationApi;

    @Cacheable(GlobalCacheConfig.LOCATION)
    public Optional<GeoLocation> getGeoLocationByIpAddress(String ipAddress) {
        log.debug("Getting geo location by ip address {}", ipAddress);
        try {
            IpLocationApi.GeoLocationDto weatherInfoDto = this.ipLocationApi
                    .getGeoLocationInfo(ipAddress)
                    .firstOrError()
                    .retry(RetrofitUtils.bailOnDurableErrorPredicate(retryAttempts))
                    .timeout(timeout, TimeUnit.MILLISECONDS)
                    .blockingGet();
            return Optional.of(map(weatherInfoDto));
        } catch (HttpException ex) {
            return RetrofitUtils.on404Return(ex, Optional.empty());
        }
    }

    private GeoLocation map(IpLocationApi.GeoLocationDto dto) {
        return new GeoLocation(dto.getLatitude(), dto.getLongitude());
    }
}
