package worm;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 贪吃蛇框架类
 * @author Leslie Leung
 */
public class WormFrame extends JFrame {
	private WormStage ws;
	private JLabel label;
	
	/**
	 * 构造方法
	 */
	public WormFrame() {
		setSize(500, 500);	//设置窗体大小
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);	//设置窗体于屏幕中央
		setTitle("Worm");		//设置标题为Worm
		setResizable(false);		//不允许窗体缩放
		setLayout(new FlowLayout());	//设置布局管理器

		ws = new WormStage();		//新建舞台类对象
		
		label = new JLabel("请按空格键控制游戏的开始和暂停，按方向键控制贪吃蛇运动方向");
		
		add(label);
		add(ws);
				
		/* 监听贪吃蛇运动事件 */
		addKeyListener(ws.getInnerInstanceOfKeyControl());
		ws.addKeyListener(ws.getInnerInstanceOfKeyControl());
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new WormFrame();
	}
}
