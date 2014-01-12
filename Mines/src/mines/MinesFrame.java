package mines;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * 扫雷主框架类
 * @author Leslie Leung
 */
public class MinesFrame extends JFrame{
	
	private MinesFields ms;		//表示扫雷游戏面板
	private JButton btnStart;	//表示“重新开始”按钮
	
	/**
	 * 构造方法
	 */
	public MinesFrame(){
		setSize(500, 500);	//设置窗体大小
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);	//设置窗体于屏幕中央
		setTitle("Mine");		//设置标题为Mine
		setResizable(false);		//不允许窗体缩放
		setLayout(new FlowLayout());	//设置布局管理器

		ms = new MinesFields();		//新建场景类对象
		btnStart = new JButton("重新开始");	//新建按钮

		add(btnStart);	//把按钮添加到框架中
		add(ms);		//把扫雷场景添加到框架中

		/* 注册鼠标事件 */
		addMouseListener(ms.getInnerInstance());
		ms.addMouseListener(ms.getInnerInstance());

		/* 使用匿名内部类的方式监听按钮事件，让游戏重新开始 */
		btnStart.addActionListener(new Restart());

		setVisible(true);
	}
	
	/**
	 * 内部类，用于实现点击按钮重新开始游戏的功能
	 * @author Leslie Leung
	 */
	private class Restart implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			remove(ms);		//先移除面板
			ms = new MinesFields();	//再新建
			add(ms);	//添加新建的MinesFields对象

			/* 注册鼠标事件 */
			addMouseListener(ms.getInnerInstance());
			ms.addMouseListener(ms.getInnerInstance());

			setVisible(true);
		}
	}

	public static void main(String[] args) {
		new MinesFrame();
	}

}
