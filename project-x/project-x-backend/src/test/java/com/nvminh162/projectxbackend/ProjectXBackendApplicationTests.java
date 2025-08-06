package com.nvminh162.projectxbackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ProjectXBackendApplicationTests {

    @Value("${spring.application.name}")
    private String title;

    @Test
    void contextLoads() {
        System.out.println(">>> RUN HERE: " + title);
    }

}
