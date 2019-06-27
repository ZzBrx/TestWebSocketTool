package com.tp.netty.websocket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NioWebSocketClient {

	public void init() throws InterruptedException {
		System.out.println("正在启动websocket客户端");
		NioEventLoopGroup worker = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(worker)
		.channel(NioSocketChannel.class)
		.handler(new ClientChannelInitializer());

		ChannelFuture cf = bootstrap.connect("", 9006).sync(); // 异步连接	
		cf.channel().write(Unpooled.copiedBuffer("".getBytes()));
		cf.channel().flush();
		cf.channel().closeFuture().sync();
	}

}
