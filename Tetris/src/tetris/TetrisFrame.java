package tetris;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 框架类
 * @author Leslie Leung
 */
public class TetrisFrame extends JFrame {
	private TetrisPane tp;	//俄罗斯方块主游戏场景类
	private JLabel mention;		//游戏的提示信息
	
	/**
	 * 构造方法
	 */
	public TetrisFrame() {
		setSize(550, 600);	//设置窗体大小
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);	//设置窗体于屏幕中央
		setTitle("Tetris");		//设置标题为Tetris
		setResizable(false);		//不允许窗体缩放
		setLayout(new FlowLayout());	//设置布局管理器
		
		tp = new TetrisPane();	//新建场景类对象
		mention = new JLabel("按A键逆时针转，按D顺时针转，按方向键控制向左、向右和向下的运动，按空格键硬下落");
		
		add(mention);		//把标签添加到主框架中
		add(tp);		//把游戏主场景面板添加到主框架中
		
		/* 注册键盘事件 */
		addKeyListener(tp.getInnerInstanceOfKeyControl());
		tp.addKeyListener(tp.getInnerInstanceOfKeyControl());
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new TetrisFrame();
	}
}
