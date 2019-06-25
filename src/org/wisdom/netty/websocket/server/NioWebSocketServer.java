package org.wisdom.netty.websocket.server;

import javax.swing.JTextArea;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NioWebSocketServer {
	
	private static NioEventLoopGroup boss;
	private static NioEventLoopGroup work;
    
    public static void init(JTextArea textArea , int port){
    	System.out.println("正在启动websocket服务器");
    	textArea.setText("正在启动websocket服务器");
       boss=new NioEventLoopGroup(); // 第一个经常被叫做'老板'，用来接收进来的连接。
       work=new NioEventLoopGroup();// 第二个经常被叫做'工人'，用来处理已经被接收的连接，一旦'老板'接收到连接，就会把连接信息注册到'worker'上。
        try {
            ServerBootstrap bootstrap=new ServerBootstrap();  // 启动NIO服务的辅助启动类。你可以在这个服务中直接使用Channel
            bootstrap.group(boss,work);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new NioWebSocketChannelInitializer(textArea , String.valueOf(port)));
            
            Channel channel = bootstrap.bind(port).sync().channel();
            System.out.println("webSocket服务器启动成功："+channel);
            textArea.setText("webSocket服务器启动成功："+channel);
//            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("运行出错："+e);
        }finally {
        	boss.shutdownGracefully();
        	work.shutdownGracefully();
        	System.out.println("websocket服务器已关闭");
        	textArea.setText("websocket服务器已关闭");
        }
        
    }
    
}
