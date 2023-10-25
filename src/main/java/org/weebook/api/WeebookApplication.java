package org.weebook.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableCaching
@EnableWebSecurity
@EnableJpaAuditing
public class WeebookApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeebookApplication.class, args);
    }

}
