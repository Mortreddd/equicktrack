package com.it43.equicktrack.configuration;

import com.vonage.client.HttpConfig;
import com.vonage.client.VonageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class VonageConfiguration {

    @Value("${vonage.api-secret}")
    private String apiSecret;

    @Value("${vonage.api-key}")
    private String apiKey;

    @Value("${vonage.api-base-url}")
    private String apiBaseUrl;

    @Value("${vonage.rest-base-url}")
    private String restBaseUrl;

    @Value("${vonage.api-eu-base-url}")
    private String apiEuBaseUrl;

    @Value("${vonage.video-base-url}")
    private String videoBaseUrl;

    @Bean
    public VonageClient vonageClient() {

        HttpConfig httpConfig = HttpConfig.builder()
                .apiBaseUri(apiBaseUrl)
                .restBaseUri(restBaseUrl)
                .apiEuBaseUri(apiEuBaseUrl)
                .videoBaseUri(videoBaseUrl)
                .build();


        VonageClient client = VonageClient.builder()
                .httpConfig(httpConfig)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();

        return client;
    }
}
