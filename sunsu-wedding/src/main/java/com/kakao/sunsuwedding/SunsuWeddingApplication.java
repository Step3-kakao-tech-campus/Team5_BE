package com.kakao.sunsuwedding;

import com.kakao.sunsuwedding._core.config.EnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:env/env.yaml"},
                factory = EnvConfig.class)
public class SunsuWeddingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SunsuWeddingApplication.class, args);
    }

}
