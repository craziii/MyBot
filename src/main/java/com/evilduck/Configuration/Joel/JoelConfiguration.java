package com.evilduck.Configuration.Joel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JoelConfiguration {

    @Bean
    public Joel joelBean() {
        return new Joel();
    }

}
