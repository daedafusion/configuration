package com.daedafusion.configuration.providers;

import com.daedafusion.configuration.ConfigurationProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AwsSecretsManagerProvider implements ConfigurationProvider {
    private static final Logger log = LogManager.getLogger(AwsSecretsManagerProvider.class);

    private final Region region;
    private SecretsManagerClient secretsClient;

    // Caching here means service restart is required to get new values
    private final Map<String, String> cache;

    public AwsSecretsManagerProvider() {
        region = Region.of(System.getProperty("aws.region", Region.US_EAST_1.toString()));
        cache = new HashMap<>();
    }

    @Override
    public String getName() {
        return AwsSecretsManagerProvider.class.getName();
    }

    @Override
    public void init() throws IOException {
        secretsClient = SecretsManagerClient.builder()
                .region(region)
                .build();
    }

    @Override
    public String getValue(String key) throws IOException {
        if(cache.containsKey(key)) {
            return cache.get(key);
        }

        try {
            GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                    .secretId(key)
                    .build();

            GetSecretValueResponse valueResponse = secretsClient.getSecretValue(valueRequest);
            String secret = valueResponse.secretString();
            cache.put(key, secret);
            return secret;
        } catch (SecretsManagerException e) {
            log.warn("SecretsManagerError", e);
        }

        return null;
    }
}
