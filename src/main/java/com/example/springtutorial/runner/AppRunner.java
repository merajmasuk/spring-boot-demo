package com.example.springtutorial.runner;

import com.example.springtutorial.dto.GithubUser;
import com.example.springtutorial.services.GithubLookupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final GithubLookupService githubLookupService;

    @Override
    public void run(String... args) throws Exception {
        long start = System.currentTimeMillis();

        CompletableFuture<GithubUser> page1 = githubLookupService.getGithubUser("PivotalSoftware");
        CompletableFuture<GithubUser> page2 = githubLookupService.getGithubUser("CloudFoundry");
        CompletableFuture<GithubUser> page3 = githubLookupService.getGithubUser("Spring-Projects");

        CompletableFuture.allOf(page1, page2, page3).join();

        log.info("Elapsed time: {} ms", System.currentTimeMillis() - start);
        log.info("--> {}", page1.get());
        log.info("--> {}", page2.get());
        log.info("--> {}", page3.get());
    }
}
