package com.fivesysdev.Fiveogram;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class FiveOGramApplication {

    public static void main(String[] args) {
        SpringApplication.run(FiveOGramApplication.class, args);

    }
//TODO mark notification reccom by hashtag, subs on hashtag
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
