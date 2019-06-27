package com.tp.netty.websocket.server;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import com.tp.util.ToolUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NioWebSocketServer {

	private static NioEventLoopGroup boss;
	private static NioEventLoopGroup worker;
	private static boolean startResult;
	private static boolean closeServerResult;
	private static Channel channel;

	/**
	 * 启动服务器
	 */
	public static boolean init(JTextArea textArea, int port, JLabel lblNewLabel_1 , JComboBox<Object> comboBox) {
		ToolUtil.setText(textArea, "正在启动websocket服务器", "服务器");
		boss = new NioEventLoopGroup(); // 第一个经常被叫做'老板'，用来接收进来的连接。
		worker = new NioEventLoopGroup();// 第二个经常被叫做'工人'，用来处理已经被接收的连接，一旦'老板'接收到连接，就会把连接信息注册到'worker'上。
		ServerBootstrap bootstrap = new ServerBootstrap(); // 启动NIO服务的辅助启动类。你可以在这个服务中直接使用Channel
		bootstrap.group(boss, worker);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new NioWebSocketChannelInitializer(textArea , lblNewLabel_1 , comboBox));

		try {
			channel = bootstrap.bind(port).sync().channel();
			startResult = true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			startResult = false;
			e.printStackTrace();
		}
		ToolUtil.setText(textArea, "webSocket服务器启动成功：" + channel, "服务器");
		return startResult;

	}

	/**
	 * 关闭服务器
	 */
	public static boolean closeServer(JTextArea textArea) {
		if (boss != null && worker != null) {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
			closeServerResult = true;
			ToolUtil.setText(textArea, "服务器关闭", "服务器");
		} else {
			closeServerResult = false;
		}
		return closeServerResult;
	}
}
