package com.tp.soft.gui;

import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.tp.netty.global.ChannelSupervise;
import com.tp.netty.websocket.server.NioWebSocketHandler;
import com.tp.netty.websocket.server.NioWebSocketServer;
import com.tp.util.ToolUtil;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class ServerFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	// 单例模式
	public static final ServerFrame instance = new ServerFrame();
	private JTextField textField;
	private JButton button;
	private JTextArea textArea;
	private JButton button_1;
	private JTextField textField_1;
	private JButton button_2;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JComboBox<Object> comboBox;
	private JCheckBox chckbxNewCheckBox;

	private ServerFrame() {
		try {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(700, 550);
			setTitle("服务端");
			setResizable(false);
			int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;// 得到计算机屏幕的宽度
			int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;// 得到计算机屏幕的高度
			int locationX = (screenWidth - getWidth()) / 2;
			int locationY = (screenHeight - getHeight()) / 2;
			setLocation(locationX, locationY);// 设置窗口的左上角坐标

			String ip = InetAddress.getLocalHost().getHostAddress();
			initComponent(ip);
			initListener();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initComponent(String ip) {
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUrl = new JLabel("URL：");
		lblUrl.setFont(new Font("宋体", Font.PLAIN, 13));
		lblUrl.setBounds(10, 30, 115, 15);
		contentPane.add(lblUrl);

		textField = new JTextField();
		textField.setText("ws://" + ip + ":9006");
		textField.setBounds(47, 27, 264, 21);
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

		button = new JButton("启动服务");
		button.setFont(new Font("宋体", Font.PLAIN, 13));
		button.setBounds(321, 26, 113, 23);
		contentPane.add(button);

		button_1 = new JButton("清空");
		button_1.setFont(new Font("宋体", Font.PLAIN, 13));
		button_1.setBounds(444, 26, 113, 23);
		contentPane.add(button_1);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(10, 478, 603, 21);
		contentPane.add(textField_1);

		button_2 = new JButton("推送");
		button_2.setFont(new Font("宋体", Font.PLAIN, 13));
		button_2.setBounds(625, 477, 59, 23);
		contentPane.add(button_2);

		lblNewLabel = new JLabel("当前");
		lblNewLabel.setBounds(10, 53, 31, 15);
		contentPane.add(lblNewLabel);

		lblNewLabel_1 = new JLabel("0");
		lblNewLabel_1.setBounds(57, 55, 41, 15);
		contentPane.add(lblNewLabel_1);

		JLabel label = new JLabel("在线");
		label.setBounds(91, 53, 54, 15);
		contentPane.add(label);

//		String[] element = { "" };
		comboBox = new JComboBox<Object>();
		comboBox.setBounds(10, 432, 190, 21);
		contentPane.add(comboBox);
		
		chckbxNewCheckBox = new JCheckBox("全部推送");
		chckbxNewCheckBox.setBounds(611, 431, 73, 23);
		contentPane.add(chckbxNewCheckBox);
	}

	// 监听器
	public void initListener() {
		/**
		 * 打开服务器
		 */
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (button.getText().equals("启动服务")) {
					String url = textField.getText(); // url地址
					String port = ToolUtil.getPort(url);
					if (!port.equals("") && port != null) {
						ServerCallable sc = new ServerCallable(port);
						FutureTask<Boolean> ft = new FutureTask<Boolean>(sc);
						Thread thread = new Thread(ft);
						thread.start();
						try {
							Boolean startResult = ft.get();
							if (startResult) {
								button.setText("关闭服务");
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(null, "请输入端口！");
					}
				} else {
					boolean closeServer = NioWebSocketServer.closeServer(textArea);
					if (closeServer) {
						comboBox.removeAllItems();
						button.setText("启动服务");
					}
				}

			}
		});

		/**
		 * 推送
		 */
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String pushContent = textField_1.getText();
				if (!pushContent.equals("") && pushContent != null) {
					if(chckbxNewCheckBox.isSelected() || (comboBox.getItemCount() == 0)) {
						// 群体推送
						NioWebSocketHandler.push(pushContent);
					}else {
						// 单个推送
						String id = (String) comboBox.getSelectedItem();
						NioWebSocketHandler.singlePush(id, pushContent);
					}
				}
			}
		});

		/**
		 * 清空
		 */
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
			}
		});
	}

	public class ServerCallable implements Callable<Boolean> {
		private String port;

		public ServerCallable(String port) {
			this.port = port;
		}

		@Override
		public Boolean call() throws Exception {
			boolean startResult = NioWebSocketServer.init(textArea, Integer.parseInt(port), lblNewLabel_1, comboBox); // 启动服务端
			return startResult;
		}

	}
}
