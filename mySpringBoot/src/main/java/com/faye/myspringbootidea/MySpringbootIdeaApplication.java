package com.faye.myspringbootidea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;

//@ServletComponentScan
@SpringBootApplication //表示这是一个SpringBoot应用，运行其主方法就会启动tomcat,默认端口是8080
public class MySpringbootIdeaApplication extends SpringBootServletInitializer {

//	可选
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return super.configure(builder);
//	}

	public static void main(String[] args) {
		SpringApplication.run(MySpringbootIdeaApplication.class, args);
	}
}
