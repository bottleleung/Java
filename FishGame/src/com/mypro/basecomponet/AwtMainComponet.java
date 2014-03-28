package com.mypro.basecomponet;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import com.mypro.mainsurface.MainSurface;
import com.mypro.manager.CannonManager;
import com.mypro.manager.GameInitManager;
import com.mypro.manager.LayoutManager;
import com.mypro.model.GamingInfo;

public class AwtMainComponet{
	public static void main(String[] args) throws Exception  {
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();
		JFrame frame = new JFrame();
        GamingInfo.getGamingInfo().setGaming(true);
        GamingInfo.getGamingInfo().setScreenWidth(900);
    	GamingInfo.getGamingInfo().setScreenHeight(600);  
    	frame.setSize(GamingInfo.getGamingInfo().getScreenWidth(), GamingInfo.getGamingInfo().getScreenHeight());
    	frame.setUndecorated(true); // 去掉窗口的装饰 
//    	frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);//采用指定的窗口装饰风格 
//		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		MainSurface pane = new MainSurface();
		GamingInfo.getGamingInfo().setSurface(pane);
		frame.setContentPane(pane);
//		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(GameInitManager.getGameInitManager().isIniting()){
					return ;
				}
				//先看布局管理器是否有相应
				if(!LayoutManager.getLayoutManager().onClick(e.getX(), e.getY())){
					//发射子弹
					CannonManager.getCannonManager().shot(e.getX(),  e.getY());
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
//		frame.pack();
		pane.action();
		/**
    	 * 创建一个线程来异步初始化游戏内容
    	 */
    	new Thread(new Runnable(){

			public void run() {
				//使用游戏初始化管理器初始化游戏
				GameInitManager.getGameInitManager().init();
			}
    		
    	}).start();  	
	}

}
