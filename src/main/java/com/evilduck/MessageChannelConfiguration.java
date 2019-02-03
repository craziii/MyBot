package com.evilduck;

import com.evilduck.Util.CommandHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class MessageChannelConfiguration {

    @Autowired
    private CommandHelper commandHelper;

    @Bean
    public MessageChannel commandInputChannel() {
        return new DirectChannel();
    }

//    @Bean
//    public MessageChannel pingChannel() {
//        return new DirectChannel();
////    }
//
//    @Bean
//    public MessageChannel penisChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    public MessageChannel kickChannel() {
//        return new DirectChannel();
//    }

    public void instantiateMessageChannels() {
//        final DefaultListableBeanFactory context = new DefaultListableBeanFactory();
//        final GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
//        genericBeanDefinition.setBeanClass(DirectChannel.class);
//
////        final MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
////        genericBeanDefinition.setPropertyValues(mutablePropertyValues);
//
//        final List<IsACommand> commandDetailList = commandHelper.getCommandDetailList();
//        commandDetailList.forEach(commandDetail -> context.registerBeanDefinition(commandDetail.getFullCommand(), genericBeanDefinition));

    }

}
