package cn.how2j.springcloud.util;

import java.util.HashMap;

import cn.hutool.http.HttpUtil;

//使用 post 的方式访问 http://localhost:8012/actuator/bus-refresh 地址，之所以要专门做一个 FreshConfigUtil 类，就是为了可以使用 post 访问，
//因为它不支持 get 方式访问，直接把这个地址放在浏览器里，是会抛出 405错误的。
public class FreshConfigUtil {


	public static void main(String[] args) {
		HashMap<String,String> headers =new HashMap<>();
		headers.put("Content-Type", "application/json; charset=utf-8"); 
		System.out.println("因为要去git获取，还要刷新config-server, 会比较卡，所以一般会要好几秒才能完成，请耐心等待");

		//客户端引入了rmq的依赖，rmq就是消息总线，当我们调用/actuator/refresh接口时，
		// 其实就是向消息总线发送了一个更新配置的请求消息，然后消息总线再将此消息广播给所有使用此配置的微服务，微服务再重新去服务端拉取配置。
		// rmq起作用都是自动的，我们没有去参与。
		String result = HttpUtil.createPost("http://localhost:8012/actuator/bus-refresh").addHeaders(headers).execute().body();
		System.out.println("result:"+result);
		System.out.println("refresh 完成");
	}
}
