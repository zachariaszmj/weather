package com.zachmj.weather.infra.client;

import com.zachmj.weather.domain.client.ClientApiKey;
import com.zachmj.weather.domain.client.ClientKeyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaClientKeyRepository extends ClientKeyRepository, JpaRepository<ClientApiKeyEntity, Long> {

    @Override
    default Optional<ClientApiKey> find(String apiKey) {
        return findByApiKey(apiKey).map(entity -> new ClientApiKey(entity.id, entity.apiKey, entity.revoked));
    }

    Optional<ClientApiKeyEntity> findByApiKey(String apiKey);
}
