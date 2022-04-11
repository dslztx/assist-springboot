package me.dslztx.sprintboot;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class InitBean {

    @PostConstruct
    public void init() {
        System.out.println("spring boot starting, init InitBean successfully");
    }
}
