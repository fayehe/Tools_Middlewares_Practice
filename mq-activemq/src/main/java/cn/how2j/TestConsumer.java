package cn.how2j;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.how2j.util.ActiveMQUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring_jms.xml")
public class TestConsumer {
	@Before
    public void checkServer() {
    	ActiveMQUtil.checkServer();
    }
	
    @Test
    public void test(){
        try {
        	//写这个是为了不让当前测试退出。  因为 spring的配置， MyMessageListener 会自动启动
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }    
}