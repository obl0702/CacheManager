package com.ongbl.caffeinecache;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class CaffeinecacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaffeinecacheApplication.class, args);
    }

}
