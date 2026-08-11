package com.erebelo.springstrategydemo;

import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringStrategyDemoApplication {

    public static void main(String[] args) {
        // Ensures validation and other localized messages are consistently in English.
        Locale.setDefault(Locale.US);

        SpringApplication.run(SpringStrategyDemoApplication.class, args);
    }
}
