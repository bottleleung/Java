package com.mypro.basecomponet;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.mypro.mainsurface.MainSurface;
import com.mypro.manager.CannonManager;
import com.mypro.manager.GameInitManager;
import com.mypro.manager.LayoutManager;
import com.mypro.model.GamingInfo;

import static javax.swing.JOptionPane.showConfirmDialog;

public class AwtMainComponet{
	public static void main(String[] args) throws Exception  {
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();
		JFrame frame = new JFrame();
		GamingInfo.getGamingInfo().setGaming(true);
		GamingInfo.getGamingInfo().setScreenWidth(900);
		GamingInfo.getGamingInfo().setScreenHeight(600);
		frame.setSize(GamingInfo.getGamingInfo().getScreenWidth(), GamingInfo.getGamingInfo().getScreenHeight());
//    	frame.setUndecorated(true); // 去掉窗口的装饰
//    	frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);//采用指定的窗口装饰风格
//		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		MainSurface pane = new MainSurface();
		GamingInfo.getGamingInfo().setSurface(pane);
//		Label scoreLabel=new JLabel("最高分：200");
//		scoreLabel.setForeground(Color.blue);
//		scoreLabel.setVisible(true);
//		scoreLabel.setOpaque(true);
//		scoreLabel.setSize(600,200);
//		scoreLabel.setDisplayedMnemonic(-1);

		Label scoreLabel=new Label("最高分："+GamingInfo.getGamingInfo().getHighestScore());
		scoreLabel.setBackground(new Color(0,80,0,0));
		scoreLabel.setForeground(new Color(255,0,0));
		pane.add(scoreLabel);
		frame.setContentPane(pane);
//		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				int flag=JOptionPane.showConfirmDialog(pane,"是否退出？","提示",JOptionPane.OK_CANCEL_OPTION);
				if(flag==0) {
					int score = GamingInfo.getGamingInfo().getScore();
					if (score > GamingInfo.getGamingInfo().getHighestScore()) {
						GamingInfo.getGamingInfo().setHighestScore(score);
						JOptionPane.showConfirmDialog(pane,"新记录："+score,"恭喜",JOptionPane.OK_CANCEL_OPTION);
					}
					System.exit(0);
				}
			}
		});
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