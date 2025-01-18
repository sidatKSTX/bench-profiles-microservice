package com.consultingfirm.benchprofiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BenchProfilesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BenchProfilesServiceApplication.class, args);
    }

}
