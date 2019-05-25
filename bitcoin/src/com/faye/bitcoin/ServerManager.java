package com.faye.bitcoin;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
 
public class ServerManager {
	//ServerManager 中维护了一个线程安全的集合servers, 用于因为浏览器发起连接请求而创建的BitCoinServer. 
    private static Collection<BitCoinServer> servers = Collections.synchronizedCollection(new ArrayList<BitCoinServer>());
    
    //broadCast 方法遍历这个集合，让每个Server向浏览器发消息。
    public static void broadCast(String msg){
        for (BitCoinServer bitCoinServer : servers) {
            try {
                bitCoinServer.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
     
    public static int getTotal(){
        return servers.size();
    }
    public static void add(BitCoinServer server){
        System.out.println("有新连接加入！ 当前总连接数是："+ servers.size());
        servers.add(server);
    }
    public static void remove(BitCoinServer server){
        System.out.println("有连接退出！ 当前总连接数是："+ servers.size());
        servers.remove(server);
    }
     
}