package com.evilduck;

import com.evilduck.Entity.Member;
import com.evilduck.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableScheduling
public class EvilduckApplication implements CommandLineRunner {

    @Autowired private MemberRepository memberRepository;

    public static void main(String[] args) {
        SpringApplication.run(EvilduckApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        memberRepository.deleteAll();
        memberRepository.save(new Member("EvilDuck95"));

        System.out.println("Finding member..." + memberRepository.findByName("EvilDuck95"));

    }
}
