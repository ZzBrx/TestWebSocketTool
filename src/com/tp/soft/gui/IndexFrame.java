package com.tp.soft.gui;


import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

public class IndexFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public IndexFrame() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("WebSocket调试工具");
		setSize( 554, 215);
		setResizable(false);
		
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;// 得到计算机屏幕的宽度
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;// 得到计算机屏幕的高度

		int locationX = (screenWidth - getWidth()) / 2;
		int locationY = (screenHeight - getHeight()) / 2;

		setLocation(locationX, locationY);// 设置窗口的左上角坐标
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("请选择调试工具");
		lblNewLabel.setBounds(82, 70, 116, 15);
		contentPane.add(lblNewLabel);
		
		String[] toolName={"客户端","服务端"};
		
		JComboBox<Object> comboBox = new JComboBox<Object>(toolName);
		comboBox.setBounds(203, 67, 267, 21);
		contentPane.add(comboBox);
		
		JButton btnOk = new JButton("OK");
		btnOk.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));

		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item = (String) comboBox.getSelectedItem();
				if (item.equals("客户端")) {
					dispose();
					ClientFrame clientFrame = new ClientFrame();
					clientFrame.setVisible(true);
				}else if(item.equals("服务端")) {
					dispose();
					ServerFrame serverFrame = ServerFrame.instance;
					serverFrame.setVisible(true);
				}			
			}
		});
		btnOk.setBounds(203, 112, 142, 23);
		contentPane.add(btnOk);
	}

}
