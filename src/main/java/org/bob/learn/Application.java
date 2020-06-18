package org.bob.learn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        log.info("Learn-Webflux应用启动开始！");
        SpringApplication.run(Application.class, args);
        log.info("Learn-Webflux应用启动成功！");
    }
}