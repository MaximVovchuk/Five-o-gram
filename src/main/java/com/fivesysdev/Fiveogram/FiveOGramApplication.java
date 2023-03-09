package com.fivesysdev.Fiveogram;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class FiveOGramApplication {
    // TODO: 9/3/23 check all of your entities and set cascade types hierarchy to remove all child entities when parent entities deleting from DB
    // TODO: 9/3/23 check all your primitive types usage in code and replace with wrappers
    public static void main(String[] args) {
        SpringApplication.run(FiveOGramApplication.class, args);

    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
