package com.biosphere.usermodule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication()
@MapperScan({"com.biosphere.usermodule.mapper"})
public class UserModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserModuleApplication.class, args);
    }

}
