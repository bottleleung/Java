package worm;

import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * 舞台类
 * @author Leslie Leung
 * @see Worm
 * @see Food
 */
public class WormStage extends JPanel {
	
	public static final int ROWS = 35;	//设置整个场景的行数
	public static final int COLUMNS = 35;	//设置整个场景的列数
	
	private Cell food;	//食物类对象
	private Worm worm;	//贪吃蛇类对象
	private boolean on;	//判断贪吃蛇游戏是开始还是暂停
	private Timer timer;		//定时器	
	private Random random = new Random();
	private KeyControl keyListener;		//键盘时间监听器
	
	/**
	 * 构造方法，初始化贪吃蛇舞台
	 */
	public WormStage() {
		setPreferredSize(new Dimension(ROWS * Cell.CELL_SIZE, COLUMNS * Cell.CELL_SIZE));
		
		worm = new Worm();	//新建贪吃蛇
		food = randomFood();	//新建食物
		on = false;
		keyListener = new KeyControl();
	}
	
	/**
	 * 返回KeyControl类实例
	 * @return KeyControl类实例
	 */
	public KeyControl getInnerInstanceOfKeyControl() {
		return keyListener;
	}
	
	/**
	 * 每吃一次食物，通过减小timertask的时间间隔来加快速度
	 * @return 改变后的间隔值
	 */
	public double interval() {
		return Worm.INIT_SPEED * Math.pow((double)39 / 38, Worm.DEFAULT_LENGTH - worm.getCurrentLength());
	}

	/**
	 * 建立内部类继承TimerTask，实现贪吃蛇的自动运行
	 * @author Leslie Leung
	 *
	 */
	private class Move extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(worm.hit(worm.getCurrentDirection())) {//判断是否产生撞击
				
				JOptionPane.showMessageDialog(null, "你输了");	//输了提示
				timer.cancel();	//取消定时器
				on = false;
				
				worm = new Worm();
				food = randomFood();
				
			} else if(worm.creep(worm.getCurrentDirection(), food)) {//调用爬行算法并判断食物是被吃，被吃的话重新生成食物
				
				food = randomFood();	//生成食物
				
				timer.cancel();
				timer = new Timer();
				
				timer.scheduleAtFixedRate(new Move(), 0, (long)interval());	//interval()函数用于每次吃掉食物以后加速
				
			}
			
			repaint();
		}
		
	}
	
	/**
	 * 建立内部类继承KeyAdapter，实现对贪吃蛇的事件控制
	 * @author Leslie Leung
	 *
	 */
	private class KeyControl extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			switch(e.getKeyCode()) {
				case KeyEvent.VK_UP: //往上爬行
					worm.changeDirection(Worm.UP);
					break;
					
				case KeyEvent.VK_DOWN:	//往下爬行
					worm.changeDirection(Worm.DOWN);
					break;
					
				case KeyEvent.VK_LEFT:	//往左爬行
					worm.changeDirection(Worm.LEFT);
					break;
					
				case KeyEvent.VK_RIGHT:	//往右爬行
					worm.changeDirection(Worm.RIGHT);
					break;
					
				case KeyEvent.VK_SPACE:	//控制游戏暂停和开始
					if(on) {
						/* 如果游戏开始，按空格键表示暂停 */
						timer.cancel();
						
						on = false;
						break;
					} else {
						/* 重新创建定时器对象并调用定时器 */
						timer = new Timer();
						timer.scheduleAtFixedRate(new Move(), 0, (long)interval());
						
						on = true;
						break;
					}
			}
		}
	}
	
	/**
	 * 随机生成食物
	 * @return 食物对象
	 */
	public Cell randomFood() {
		int x, y;
		
		do {
			x = random.nextInt(COLUMNS);
			y = random.nextInt(ROWS);
		} while(worm.contains(x, y));
		
		return new Cell(x, y);
	}
	
	@Override
	public void paint(Graphics g) {	
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getBounds().width, getBounds().height);
		
		g.setColor(Color.BLUE);
		worm.paint(g);	//绘制贪吃蛇
		
		g.setColor(Color.YELLOW);
		food.paintCell(g);	//绘制食物
	}

}

