package com.tp.netty.websocket.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import java.util.Date;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import com.tp.netty.global.ChannelSupervise;
import com.tp.util.ToolUtil;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;

public class NioWebSocketHandler extends SimpleChannelInboundHandler<Object> {

	private WebSocketServerHandshaker handshaker; // 握手

	private JTextArea textArea;

	private static Channel channel;

	private JLabel lblNewLabel_1;

	private JComboBox<Object> comboBox;

	public NioWebSocketHandler(JTextArea textArea, JLabel lblNewLabel_1, JComboBox<Object> comboBox) {
		this.textArea = textArea;
		this.lblNewLabel_1 = lblNewLabel_1;
		this.comboBox = comboBox;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof FullHttpRequest) {
			// 以http请求形式接入，但是走的是websocket
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			// 处理websocket客户端的消息
			handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 添加连接
		ToolUtil.setText(textArea, "客户端加入连接：" + ctx.channel(), "服务器");
		ChannelSupervise.addChannel(ctx.channel());
		lblNewLabel_1.setText(String.valueOf(ChannelSupervise.onlineCount()));

		// 取出当前所有在线的客户端
		Vector<String> elementList = ChannelSupervise.getElementFromChannelMap();
		comboBox.removeAllItems();
		for (String element : elementList) {
			comboBox.addItem(element);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 断开连接
		System.out.println("客户端断开连接：" + ctx.channel());
		ToolUtil.setText(textArea, "客户端断开连接：" + ctx.channel(), "服务器");
		ChannelSupervise.removeChannel(ctx.channel());
		lblNewLabel_1.setText(String.valueOf(ChannelSupervise.onlineCount()));

		// 取出当前所有在线的客户端
		Vector<String> elementList = ChannelSupervise.getElementFromChannelMap();
		comboBox.removeAllItems();
		for (String element : elementList) {
			comboBox.addItem(element);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		channel = ctx.channel();
		// 判断是否关闭链路的指令
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			ToolUtil.setText(textArea, channel.id() + "退出连接", "服务器");
			return;
		}
		// 判断是否ping消息
		if (frame instanceof PingWebSocketFrame) {
			channel.write(new PongWebSocketFrame(frame.content().retain()));
			channel.flush();
			return;
		}

		// region 纯文本消息
		if (frame instanceof TextWebSocketFrame) {
			String text = ((TextWebSocketFrame) frame).text();
			ToolUtil.setText(textArea, "收到：" + text, "服务器");
			TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + channel.id() + "：" + "success");
//			channel.write(new TextWebSocketFrame(new Date() + " 服务器将你发的消息原样返回：" + text));
			ChannelSupervise.send2All(tws);
			ToolUtil.setText(textArea, "返回：" + "success", "服务器");
		}

		// region 二进制消息 此处使用了MessagePack编解码方式
		if (frame instanceof BinaryWebSocketFrame) {
			BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
			ByteBuf content = binaryWebSocketFrame.content();
			System.out.println("├ [二进制数据]:{}" + content);
			final int length = content.readableBytes();
			final byte[] array = new byte[length];
			ByteBuf bytes = content.getBytes(content.readerIndex(), array, 0, length);
			System.out.println(bytes);
//			MessagePack messagePack = new MessagePack();
//			WebSocketMessageEntity webSocketMessageEntity = messagePack.read(array, WebSocketMessageEntity.class);
//			LOGGER.info("├ [解码数据]: {}", webSocketMessageEntity);
//			WebSocketUsers.sendMessageToUser(webSocketMessageEntity.getAcceptName(),webSocketMessageEntity.getContent());
		}

	}

	/**
	 * 推送
	 */
	public static void push(Object content) {
		if (channel != null && !content.equals("")) {
			TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + channel.id() + "：" + content);
			ChannelSupervise.send2All(tws);
		}
	}
	
	
	/**
	 * 单个推送
	 */
	public static void singlePush(String id , Object content) {
		if (channel != null && !content.equals("")) {
			String channelByIp = ChannelSupervise.getChannelByIp(id);
			if(!channelByIp.equals("") && channelByIp != null) {
				TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + channel.id() + "：" + content);
				ChannelSupervise.send(channelByIp , tws);
			}
		}
	}

	/**
	 * 唯一的一次http请求，用于创建websocket
	 */
	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		// 要求Upgrade为websocket，过滤掉get/Post
		if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
			// 若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
			sendHttpResponse(ctx, req,
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
				"ws://localhost:8081/websocket", null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
		}
	}

	/**
	 * 拒绝不合法的请求，并返回错误信息
	 */
	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
		// 返回应答给客户端
		if (res.status().code() != 200) {
//			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
			ByteBuf buf = Unpooled.copiedBuffer("error".toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		// 如果是非Keep-Alive，关闭连接
		if (!isKeepAlive(req) || res.status().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}
}
