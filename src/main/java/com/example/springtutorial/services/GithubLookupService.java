package com.example.springtutorial.services;

import com.example.springtutorial.dto.GithubUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class GithubLookupService {

    private final RestTemplate restTemplate;

    public GithubLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<GithubUser> getGithubUser(String user) throws InterruptedException {
        log.info("Looking up Github User {}", user);
        String url = String.format("https://api.github.com/users/%s", user);
        GithubUser githubUser = restTemplate.getForObject(url, GithubUser.class);
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(githubUser);
    }

}
