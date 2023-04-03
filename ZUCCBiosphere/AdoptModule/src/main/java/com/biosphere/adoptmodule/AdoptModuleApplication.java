package com.biosphere.adoptmodule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"com.biosphere.adoptmodule.mapper"})
@ComponentScan(basePackages = {"com.biosphere.adoptmodule","com.biosphere.library"})
@EnableScheduling //开启基于注解的定时任务
@EnableTransactionManagement
public class AdoptModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdoptModuleApplication.class, args);
    }

}
