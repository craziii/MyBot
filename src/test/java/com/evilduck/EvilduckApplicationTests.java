package com.evilduck;

import com.mongodb.MongoClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EvilduckApplicationTests {


    @Test
    public void contextLoads() {
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(new MongoClient(), "DB");
    }

}
