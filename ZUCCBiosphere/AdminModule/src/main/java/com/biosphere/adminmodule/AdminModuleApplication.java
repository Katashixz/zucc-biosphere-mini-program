package com.biosphere.adminmodule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication()
@ComponentScan(basePackages = {"com.biosphere.adminmodule","com.biosphere.library"})
@MapperScan({"com.biosphere.adminmodule.mapper"})
@EnableTransactionManagement
public class AdminModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminModuleApplication.class, args);
    }

}
