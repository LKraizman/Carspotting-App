package com.carspottingapp.client;

import com.carspottingapp.exception.GitHubApiException;
import com.carspottingapp.model.OAuthGoogleInfo;
import com.carspottingapp.model.response.authModel.GoogleUserEmailResponse;
import com.carspottingapp.service.properties.GoogleAuthProperties;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@Service
@EnableConfigurationProperties(GoogleAuthProperties.class)
@RequiredArgsConstructor
public class GoogleClient {

    private final GoogleAuthProperties googleAuthProperties;
    private final WebClient allSourceWebClient;
    private static final Logger logger = LogManager.getLogger(GoogleClient.class);

    public URI getOauthUri() {
        String state = UUID.randomUUID().toString();
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(googleAuthProperties.getOauthUrl())
                .path(googleAuthProperties.getOauthPath())
                .queryParam("scope", googleAuthProperties.getClient().getScope())
                .queryParam("include_granted_scopes", googleAuthProperties.isIncludeGrantedScopes())
                .queryParam("response_type", googleAuthProperties.getResponseType())
                .queryParam("state", state)
                .queryParam("redirect_uri", googleAuthProperties.getClient().getRedirectUri())
                .queryParam("client_id", googleAuthProperties.getClient().getClientId())
                .build().encode().toUri();
    }

    public Mono<OAuthGoogleInfo> getOauthUserToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", googleAuthProperties.getClient().getClientId());
        body.add("client_secret", googleAuthProperties.getClient().getClientSecret());
        body.add("code", code);
        body.add("grant_type", googleAuthProperties.getGrantType());
        body.add("redirect_uri", googleAuthProperties.getClient().getRedirectUri());
        return allSourceWebClient.post()
                .uri(googleAuthProperties.getApiUrl())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .bodyToMono(OAuthGoogleInfo.class)
                .doOnError(e -> logger.error("Google OAuth info fetch failed", e))
                .onErrorResume(e -> Mono.error(new GitHubApiException("Google OAuth info fetch failed", e)));
    }

    public Mono<GoogleUserEmailResponse> getGoogleUserEmail(String googleAccessToken) {
        return allSourceWebClient.get()
                .uri(googleAuthProperties.getUserApiUrl())
                .headers(h -> h.setBearerAuth(googleAccessToken))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GoogleUserEmailResponse.class)
                .doOnError(e -> logger.error("Github OAuth info fetch failed", e))
                .onErrorResume(e -> Mono.error(new GitHubApiException("GitHub OAuth info fetch failed", e)));
    }

}
