package com.biosphere.wikimodule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan({"com.biosphere.wikimodule.mapper"})
@EnableScheduling //开启基于注解的定时任务
public class WikiModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiModuleApplication.class, args);
    }

}
