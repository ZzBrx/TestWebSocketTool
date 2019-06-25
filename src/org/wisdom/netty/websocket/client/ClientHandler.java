package org.wisdom.netty.websocket.client;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandler extends ChannelHandlerAdapter {
	
	public void channelRead(ChannelHandlerContext cx, Object msg) throws UnsupportedEncodingException{
		  ByteBuf buf = (ByteBuf)msg;
		  
		  byte[] sendByte = new byte[buf.readableBytes()];
		  
		  buf.readBytes(sendByte);
		  
		 String receiveMsg =  new String(sendByte, "utf-8");
		 
		 System.out.println("收到服务端返回的信息："+receiveMsg);
	}
	
	public void exceptionCaught(ChannelHandlerContext cx,Throwable cause) {
		cx.close();
	}
  
}
