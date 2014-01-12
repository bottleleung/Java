package traingame;

import java.awt.FlowLayout;
import javax.swing.JFrame;

public class TrainGameFrame extends JFrame {
	
	/**
	 * 构造方法
	 */
	public TrainGameFrame() {
		setSize(1024, 700);	//设置窗体大小
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);	//设置窗体于屏幕中央
		setTitle("TrainGame");		//设置标题为TrainGame
		setResizable(false);		//不允许窗体缩放
		setLayout(new FlowLayout());	//设置布局管理器
		
		TrainGame game = new TrainGame();
		add(game);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new TrainGameFrame();
	}
}
