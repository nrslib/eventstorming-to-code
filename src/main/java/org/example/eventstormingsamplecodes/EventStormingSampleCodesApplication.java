package org.example.eventstormingsamplecodes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(nameGenerator = FullyQualifiedBeanNameGenerator.class)
public class EventStormingSampleCodesApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventStormingSampleCodesApplication.class, args);
    }

}
