package com.tp.netty.websocket.server;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NioWebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
	private JTextArea textArea;
	private JLabel lblNewLabel_1;
	private JComboBox<Object> comboBox;
	
	public NioWebSocketChannelInitializer(JTextArea textArea , JLabel lblNewLabel_1 , JComboBox<Object> comboBox) {
		this.textArea = textArea;
		this.lblNewLabel_1 = lblNewLabel_1;
		this.comboBox = comboBox;
	}
    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast("logging",new LoggingHandler("DEBUG"));//设置log监听器，并且日志级别为debug，方便观察运行流程
        ch.pipeline().addLast("http-codec",new HttpServerCodec());//设置解码器
        ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));//聚合器，使用websocket会用到
        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//用于大数据的分区传输
        ch.pipeline().addLast("handler",new NioWebSocketHandler(textArea , lblNewLabel_1 , comboBox));//自定义的业务handler
    }
}
