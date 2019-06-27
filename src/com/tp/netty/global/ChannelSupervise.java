package com.tp.netty.global;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.tp.util.ToolUtil;

public class ChannelSupervise {
    private   static ChannelGroup GlobalGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private  static ConcurrentMap<String, ChannelId> ChannelMap=new ConcurrentHashMap();
    static Vector<String> vector = new Vector<String>();
    public  static void addChannel(Channel channel){
        GlobalGroup.add(channel);
        ChannelMap.put(channel.id().asShortText(),channel.id());
    }
    public static void removeChannel(Channel channel){
        GlobalGroup.remove(channel);
        ChannelMap.remove(channel.id().asShortText());
    }
    public static  Channel findChannel(String id){
        return GlobalGroup.find(ChannelMap.get(id));
    }
    public static void send2All(TextWebSocketFrame tws){
        GlobalGroup.writeAndFlush(tws);
    }
    
    public static void send(String id,TextWebSocketFrame tws){
    	findChannel(id).writeAndFlush(tws);
    }
    
    // 查看当前连接个数
    public static int onlineCount() {
    	return ChannelMap.size();
    }
    
    // 取出ChannelMap集合中的所有对象
    public static Vector<String> getElementFromChannelMap() {
    	Set<Entry<String, ChannelId>> entrySet = ChannelMap.entrySet();
    	vector.clear();
    	for (Entry<String, ChannelId> entry : entrySet) {
    		ChannelId channelId = entry.getValue();
    		Channel find = GlobalGroup.find(channelId);
    		String clientIp = ToolUtil.getClientIp(find);
    		vector.add(clientIp);
		}
		return vector;
    }
    
 // 根据IP地址拿到对应的channel
    public static String getChannelByIp(String ip) {
    	Set<Entry<String, ChannelId>> entrySet = ChannelMap.entrySet();
    	for (Entry<String, ChannelId> entry : entrySet) {
    		String key = entry.getKey();
    		ChannelId channelId = entry.getValue();
    		Channel find = GlobalGroup.find(channelId);
    		String clientIp = ToolUtil.getClientIp(find);
    		if(clientIp.equals(ip)) {
    			return key;
    		}
		}
		return null;
    }
}
