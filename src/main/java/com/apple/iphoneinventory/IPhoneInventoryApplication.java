package com.apple.iphoneinventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IPhoneInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(IPhoneInventoryApplication.class, args);

    }

}
