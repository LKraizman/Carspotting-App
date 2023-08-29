package com.carspottingapp.client;

import com.carspottingapp.exception.GitHubApiException;
import com.carspottingapp.model.OAuthGitHubInfo;
import com.carspottingapp.model.response.authModel.GitHubUserEmailResponse;
import com.carspottingapp.service.properties.GitHubAuthProperties;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

@Service
@EnableConfigurationProperties(GitHubAuthProperties.class)
@RequiredArgsConstructor
public class GitHubClient {

    private final GitHubAuthProperties gitHubAuthProperties;
    private final WebClient allSourceWebClient;
    private static final Logger logger = LogManager.getLogger(GitHubClient.class);

    public URI getOauthUri() {
        String state = UUID.randomUUID().toString();
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(gitHubAuthProperties.getOauthUrl())
                .path(gitHubAuthProperties.getOauthPath())
                .queryParam("client_id", gitHubAuthProperties.getClient().getClientId())
                .queryParam("redirect_uri", gitHubAuthProperties.getClient().getRedirectUri())
                .queryParam("scope", gitHubAuthProperties.getClient().getScope())
                .queryParam("state", state)
                .build().encode().toUri();
    }

    public Mono<OAuthGitHubInfo> getOauthUserToken(String code, String state) {
        Map<String, String> body = new HashMap<>();
        body.put("client_id", gitHubAuthProperties.getClient().getClientId());
        body.put("client_secret", gitHubAuthProperties.getClient().getClientSecret());
        body.put("code", code);
        return allSourceWebClient.post()
                .uri("https://"+gitHubAuthProperties.getOauthUrl() + gitHubAuthProperties.getApiTokenPath())
                .body(BodyInserters.fromValue(body))
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(OAuthGitHubInfo.class)
                .doOnError(e -> logger.error("Github OAuth info fetch failed", e))
                .onErrorResume(e -> Mono.error(new GitHubApiException("Github OAuth info fetch failed", e)));
    }

    public Flux<GitHubUserEmailResponse> getGitHubUserEmails(String gitHubAccessToken) {
        return allSourceWebClient.get()
                .uri(gitHubAuthProperties.getApiUrl() + gitHubAuthProperties.getApiUserEmailsPath())
                .headers(h -> h.setBearerAuth(gitHubAccessToken))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(GitHubUserEmailResponse.class)
                .doOnError(e -> logger.error("Github OAuth info fetch failed", e))
                .onErrorResume(e -> Mono.error(new GitHubApiException("Github OAuth info fetch failed", e)));
    }

}
