package com.tp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

import io.netty.channel.Channel;

public class ToolUtil {
	
	/**
	 * 拿到url中的port
	 * @param url
	 * @return
	 */
	public static String getPort(String url) {
		String[] split = url.split(":");
		String port = split[2].toString().trim();
		return port;		
	}
	
	public static String formatDateMs(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss:SSS");
		return sdf.format(date);
	}
	
	
	public static void setText(JTextArea textArea,Object msg,String side) {
		textArea.append("\r\n" +side + formatDateMs(new Date()) + "\r\n" + msg);
	}
	
	/**
	 * 拿到客户端IP地址
	 * @param url
	 * @return
	 */
	public static String getClientIp(Channel url) {
		String ip = null;
		if(url != null) {
			String[] split = url.toString().split("R:/");
			ip = split[1].toString().replace("]", "").trim();
		}
		return ip;		
	}
}
