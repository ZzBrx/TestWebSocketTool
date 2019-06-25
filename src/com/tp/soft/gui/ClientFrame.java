package com.tp.soft.gui;

import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static ClientFrame instance;
	
	private JTextArea textArea;
	private JTextField textField;
	private JTextField textField_1;

	// 单例模式
	public static ClientFrame getInstance() {

		if (instance == null) {
			instance = new ClientFrame();
		}
		return instance;
	}

	/**
	 * Create the frame.
	 */
	public ClientFrame() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setTitle("客户端");
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(409, 25, 375, 502);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		JLabel lblwslocalhost = new JLabel("输入服务器地址，类似：ws://localhost:8888，然后进行连接");
		lblwslocalhost.setFont(new Font("宋体", Font.PLAIN, 13));
		lblwslocalhost.setBounds(26, 30, 373, 15);
		contentPane.add(lblwslocalhost);
		
		textField = new JTextField();
		textField.setBounds(26, 55, 373, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("连接");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		button.setFont(new Font("宋体", Font.PLAIN, 14));
		button.setBounds(93, 86, 84, 25);
		contentPane.add(button);
		
		JButton button_1 = new JButton("断开");
		button_1.setFont(new Font("宋体", Font.PLAIN, 14));
		button_1.setBounds(251, 86, 77, 25);
		contentPane.add(button_1);
		
		JLabel lblid = new JLabel("输入ID");
		lblid.setFont(new Font("宋体", Font.PLAIN, 13));
		lblid.setBounds(26, 144, 63, 15);
		contentPane.add(lblid);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("宋体", Font.PLAIN, 13));
		textField_1.setBounds(77, 140, 320, 21);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lbljsluatable = new JLabel("输入发送内容，支持自定义的格式，例如js的数组和lua的table");
		lbljsluatable.setFont(new Font("宋体", Font.PLAIN, 13));
		lbljsluatable.setBounds(26, 189, 373, 15);
		contentPane.add(lbljsluatable);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(26, 214, 359, 217);
		contentPane.add(scrollPane_1);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setWrapStyleWord(true);
		textArea_1.setLineWrap(true);
		textArea_1.setEditable(true);
		scrollPane_1.setViewportView(textArea_1);

	}

	// 监听器
	public void initListener() {
		
	}
}
