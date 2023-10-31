package org.weebook.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching
public class WeebookApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeebookApplication.class, args);
    }

}
