package com.tp.soft.gui;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.wisdom.netty.websocket.server.NioWebSocketServer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;

public class ServerFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	// 单例模式
	public static final ServerFrame instance = new ServerFrame();
	private JTextField textField;
	private JButton button;
	private JTextArea textArea;

	private ServerFrame() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 500);
		setTitle("服务端");
		setResizable(false);
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;// 得到计算机屏幕的宽度
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;// 得到计算机屏幕的高度
		int locationX = (screenWidth - getWidth()) / 2;
		int locationY = (screenHeight - getHeight()) / 2;
		setLocation(locationX, locationY);// 设置窗口的左上角坐标

		initComponent();
		initListener();

	}

	public void initComponent() {
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("请输入接收端口：");
		label.setFont(new Font("宋体", Font.PLAIN, 13));
		label.setBounds(10, 30, 115, 15);
		contentPane.add(label);

		textField = new JTextField();
		textField.setBounds(121, 27, 66, 21);
		contentPane.add(textField);
		textField.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 78, 674, 332);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		button = new JButton("绑定");
		button.setFont(new Font("宋体", Font.PLAIN, 13));
		button.setBounds(206, 26, 93, 23);
		contentPane.add(button);
	}

	// 监听器
	public void initListener() {
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String port = textField.getText();
				if (!port.equals("") && port != null) {
					NioWebSocketServer.init(textArea , Integer.parseInt(port)); // 启动服务端
					textArea.setText("启动服务端");
				} else {
					JOptionPane.showMessageDialog(null, "请输入端口！");
				}
			}
		});
	}
}
