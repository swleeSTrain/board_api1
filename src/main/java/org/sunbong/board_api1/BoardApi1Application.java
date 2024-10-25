package org.sunbong.board_api1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BoardApi1Application {

    public static void main(String[] args) {
        SpringApplication.run(BoardApi1Application.class, args);
    }

}
