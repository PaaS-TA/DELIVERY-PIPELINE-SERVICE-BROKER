package org.paasta.servicebroker.deliverypipeline.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class Application {

    public static void main(String[] args) {
        // TODO
        SpringApplication.run(Application.class, args);
    }
    	
}