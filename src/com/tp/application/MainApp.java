package com.tp.application;


import javax.swing.UIManager;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import com.tp.soft.gui.IndexFrame;


public class MainApp {
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BeautyEyeLNFHelper.launchBeautyEyeLNF();
					// 设置本属性将改变窗口边框样式定义
					BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
					BeautyEyeLNFHelper.launchBeautyEyeLNF();
					UIManager.put("RootPane.setupButtonVisible", false);
					IndexFrame indexFrame = new IndexFrame();
					indexFrame.setVisible(true);
				} catch (Exception e) {
				}
			}
		});

	}

}
