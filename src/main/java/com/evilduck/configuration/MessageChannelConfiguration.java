package com.evilduck.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MessageChannelConfiguration {

    @Bean
    public MessageChannel commandInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel playChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel pauseChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel queueChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel skipChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel stopChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel volumeChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel autoFireCommandChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel bannedPhraseChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel helpChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel kickChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel penisChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel pingChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel pollChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel steamKeyChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel testCommandChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel whatAmIPlayingChannel() {
        return new DirectChannel();
    }

}
