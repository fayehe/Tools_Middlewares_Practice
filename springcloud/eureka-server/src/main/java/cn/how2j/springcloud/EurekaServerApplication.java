package cn.how2j.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import cn.hutool.core.util.NetUtil;

/**
 * EurekaServer 启动类。
	这是一个 EurekaServer ，它扮演的角色是注册中心，用于注册各种微服务，以便于其他微服务找到和访问。 所以 Eureka 这个单词是 “找到啦” 的意思。
	EurekaServer 本身就是个 Springboot 微服务, 所以它有 @SpringBootApplication 注解。
	@EnableEurekaServer 表示这是个 EurekaServer 。
	启动的时候，端口号没有在配置文件里，而是直接放在代码里，这么做是为了提示同学这个端口号是否被占用了，否则有时候端口号被占用了，同学老是启动不了，搞得自己晕头转向的~
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
	
    public static void main(String[] args) {
    	//8761 这个端口是默认的，就不要修改了，后面的子项目，都会访问这个端口。
    	int port = 8761;
		if(!NetUtil.isUsableLocalPort(port)) {
			System.err.printf("端口%d被占用了，无法启动%n", port );
    		System.exit(1);
    	}
        new SpringApplicationBuilder(EurekaServerApplication.class).properties("server.port=" + port).run(args);
    }
}
