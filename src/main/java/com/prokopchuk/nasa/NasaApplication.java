package com.prokopchuk.nasa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class NasaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NasaApplication.class, args);
    }

    @Scheduled(cron = "0 0 * * * *")
    @CacheEvict(value = "largestPictureCache", allEntries = true)
    public void clearCache() {
        //clears cache
    }
}
