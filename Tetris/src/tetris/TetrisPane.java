package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 俄罗斯方块游戏场景类
 * @author Leslie Leung
 */
public class TetrisPane extends JPanel {
	public static final int ROWS = 20;	//整个场景的行数
	public static final int COLUMNS = 16;	//整个场景的列数
	
	/* 表示7种不同的四格方块 */
	public static final int I_SHAPED = 0;
	public static final int S_SHAPED = 1;
	public static final int T_SHAPED = 2;
	public static final int Z_SHAPED = 3;
	public static final int L_SHAPED = 4;
	public static final int O_SHAPED = 5;
	public static final int J_SHAPED = 6;
	
	public static final int KIND = 7;	//表示四格方块有7个种类
	public static final int INIT_SPEED = 1000;	//表示下落的初始速度
	
	private static int randomNum = 0;	//表示已生成的俄罗斯方块的数目
	
	private Random random;
	private Tetromino currentTetromino;	//表示当前的四格方块
	private Cell[][] wall;		//表示墙，null表示方块内没对象
	private Timer autoDrop;		//实现自动下落的计时器
	private KeyControl keyListener;	//表示键盘事件监控变量
	
	/**
	 * 构造方法
	 */
	public TetrisPane() {
		setPreferredSize(new Dimension(COLUMNS * Cell.CELL_SIZE, ROWS * Cell.CELL_SIZE));
		
		random = new Random();
		wall = new Cell[ROWS][COLUMNS];
		autoDrop = new Timer();
		keyListener = new KeyControl();
		
		randomOne();
		
		autoDrop.schedule(new DropExecution(), (long)interval(), (long)interval());
	}
	
	/**
	 * 随机生成一个四格方块
	 */
	public void randomOne() {
		Tetromino tetromino = null;
		
		/* 随机生成7种四格方块的其中一种 */
		switch(random.nextInt(KIND)) {
			case I_SHAPED: 
				tetromino = new IShaped();
				break;
			case S_SHAPED: 
				tetromino = new SShaped();
			   	break;
			case T_SHAPED: 
				tetromino = new TShaped();
			    break;
			case Z_SHAPED: 
				tetromino = new ZShaped();
			    break;
			case L_SHAPED: 
				tetromino = new LShaped();
			    break;
			case O_SHAPED: 
				tetromino = new OShaped();
			    break;
			case J_SHAPED: 
				tetromino = new JShaped();
			    break;
		}
		currentTetromino = tetromino;	//当前的四格方块为生成的四格方块
		randomNum ++;
	}
	
	/**
	 * 判断玩家是否输了
	 * @return true，输了；false，没输
	 */
	public boolean isGameOver() {
		int x, y;	//当前俄罗斯方块格子的横坐标和纵坐标
		for(int i = 0; i < getCurrentCells().length; i ++) {
			x = getCurrentCells()[i].getX();
			y = getCurrentCells()[i].getY();
			
			if(isContain(x, y)) {//看其刚生成的位置是否已存在方块对象，存在的话，表示输了
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 每生成一个俄罗斯方块，通过改变TimerTask的时间间隔来加快下落速度
	 * @return 时间间隔
	 */
	public double interval() {
		return INIT_SPEED * Math.pow((double)39 / 38, 0 - randomNum);
	}
	
	/**
	 * 返回KeyControl类的实例
	 * @return KeyControl类实例
	 */
	public KeyControl getInnerInstanceOfKeyControl() {
		return keyListener;
	}
	
	/**
	 * 内部类，用于实现俄罗斯方块的自动下落
	 * @author Leslie Leung
	 */
	private class DropExecution extends TimerTask {	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			if(isGameOver()) {//如果输了
				JOptionPane.showMessageDialog(null, "你输了");
				autoDrop.cancel();
				removeKeyListener(keyListener);
				return;
			}
			
			if(!isReachBottomEdge()) {
				currentTetromino.softDrop();
			} else {
				landIntoWall();		//把俄罗斯方块添加到墙上
				removeRows();	//如满行，删除行
				randomOne();	//马上新建一个俄罗斯方块
				
				autoDrop.cancel();
				autoDrop = new Timer();
				autoDrop.schedule(new DropExecution(), (long)interval(), (long)interval());
			}
			
			repaint();
		}	
	}
	
	/**
	 * 把俄罗斯方块添加到墙上
	 */
	public void landIntoWall() {
		int x, y;	//定义俄罗斯方块不能移动后的横坐标和纵坐标
		
		for(int i = 0; i < getCurrentCells().length; i ++) {
			x = getCurrentCells()[i].getX();
			y = getCurrentCells()[i].getY();
			
			wall[y][x] = getCurrentCells()[i];	//添加到墙上
		}
	}
	
	/**
	 * 内部类，用于实现键盘的事件控制
	 * @author Leslie Leung
	 */
	private class KeyControl extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT: //往左移动
					
					if(!isReachLeftEdge()) {//当俄罗斯方块没到达左边界时，往左移动
						currentTetromino.moveLeft();
						repaint();
					}					
					break;
					
				case KeyEvent.VK_RIGHT:	//往右移动
					
					if(!isReachRightEdge()) {//当俄罗斯方块没到达右边界时，往右移动
						currentTetromino.moveRight();
						repaint();
					}
					break;
				
				case KeyEvent.VK_DOWN:	//向下移动
					
					if(!isReachBottomEdge()) {//当俄罗斯方块没到达下边界时，往下移动
						currentTetromino.softDrop();
						repaint();
					}
					
					break;
					
				case KeyEvent.VK_SPACE:	//硬下落
					
					hardDrop();	//硬下落
					landIntoWall();		//添加到墙中
					removeRows();	//如满行，删除行
					
					randomOne();
					autoDrop.cancel();
					autoDrop = new Timer();
					autoDrop.schedule(new DropExecution(), (long)interval(), (long)interval());
					
					repaint();
					break;
					
				case KeyEvent.VK_D:	//顺时针转
					
					if(!clockwiseRotateIsOutOfBounds() && !(currentTetromino instanceof OShaped)) {//俄罗斯方块没越界且其不为O形时，旋转
						currentTetromino.clockwiseRotate(getAxis(), getRotateCells());
						repaint();
					}
					break;
					
				case KeyEvent.VK_A:	//逆时针转
					
					if(!anticlockwiseRotateIsOutOfBounds() && !(currentTetromino instanceof OShaped)) {//俄罗斯方块没越界且其不为O形时，旋转
						currentTetromino.anticlockwiseRotate(getAxis(), getRotateCells());
						repaint();
					}
					break;
			}
		}
	}
	
	/**
	 * 内部类，I形的四格方块，继承了Tetromino类
	 * @author Leslie Leung
	 */
	private class IShaped extends Tetromino {
		/**
		 * 构造方法
		 */
		public IShaped() {
			cells = new Cell[4];
			
			cells[1] = new Cell(3, 0, Cell.COLOR_CYAN);
			cells[0] = new Cell(4, 0, Cell.COLOR_CYAN);
			cells[2] = new Cell(5, 0, Cell.COLOR_CYAN);
			cells[3] = new Cell(6, 0, Cell.COLOR_CYAN);
			
			/* 设置旋转轴和要旋转的格子 */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * 内部类，S形的四格方块，继承了Tetromino类
	 * @author Leslie Leung
	 */
	private class SShaped extends Tetromino {
		/**
		 * 构造方法
		 */
		public SShaped() {
			cells = new Cell[4];
			
			cells[0] = new Cell(4, 0, Cell.COLOR_BLUE);
			cells[1] = new Cell(5, 0, Cell.COLOR_BLUE);
			cells[2] = new Cell(3, 1, Cell.COLOR_BLUE);
			cells[3] = new Cell(4, 1, Cell.COLOR_BLUE);
			
			/* 设置旋转轴和要旋转的格子 */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * 内部类，T形的四格方块，继承了Tetromino类
	 * @author Leslie Leung
	 */
	private class TShaped extends Tetromino {
		/**
		 * 构造方法
		 */
		public TShaped() {
			cells = new Cell[4];
			
			cells[1] = new Cell(3, 0, Cell.COLOR_GREEN);
			cells[0] = new Cell(4, 0, Cell.COLOR_GREEN);
			cells[2] = new Cell(5, 0, Cell.COLOR_GREEN);
			cells[3] = new Cell(4, 1, Cell.COLOR_GREEN);
			
			/* 设置旋转轴和要旋转的格子 */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * 内部类，Z形的四格方块，继承了Tetromino类
	 * @author Leslie Leung
	 */
	private class ZShaped extends Tetromino {
		/**
		 * 构造方法
		 */
		public ZShaped() {
			cells = new Cell[4];
			
			cells[1] = new Cell(3, 0, Cell.COLOR_ORANGE);
			cells[2] = new Cell(4, 0, Cell.COLOR_ORANGE);
			cells[0] = new Cell(4, 1, Cell.COLOR_ORANGE);
			cells[3] = new Cell(5, 1, Cell.COLOR_ORANGE);
			
			/* 设置旋转轴和要旋转的格子 */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * 内部类，L形的四格方块，继承了Tetromino类
	 * @author Leslie Leung
	 */
	private class LShaped extends Tetromino {
		/**
		 * 构造方法
		 */
		public LShaped() {
			cells = new Cell[4];
			
			cells[1] = new Cell(3, 0, Cell.COLOR_PINK);
			cells[0] = new Cell(4, 0, Cell.COLOR_PINK);
			cells[2] = new Cell(5, 0, Cell.COLOR_PINK);
			cells[3] = new Cell(3, 1, Cell.COLOR_PINK);
			
			/* 设置旋转轴和要旋转的格子 */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * 内部类，O形的四格方块，继承了Tetromino类
	 * @author Leslie Leung
	 */
	private class OShaped extends Tetromino {
		/**
		 * 构造方法
		 */
		public OShaped() {
			cells = new Cell[4];
			
			cells[0] = new Cell(4, 0, Cell.COLOR_RED);
			cells[1] = new Cell(5, 0, Cell.COLOR_RED);
			cells[2] = new Cell(4, 1, Cell.COLOR_RED);
			cells[3] = new Cell(5, 1, Cell.COLOR_RED);
			
			/* 设置旋转轴和要旋转的格子 */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * 内部类，J形的四格方块，继承了Tetromino类
	 * @author Leslie Leung
	 */
	private class JShaped extends Tetromino {
		/**
		 * 构造方法
		 */
		public JShaped() {
			cells = new Cell[4];
			
			cells[1] = new Cell(3, 0, Cell.COLOR_YELLOW);
			cells[0] = new Cell(4, 0, Cell.COLOR_YELLOW);
			cells[2] = new Cell(5, 0, Cell.COLOR_YELLOW);
			cells[3] = new Cell(5, 1, Cell.COLOR_YELLOW);
			
			/* 设置旋转轴和要旋转的格子 */
			setAxis();
			setRotateCells();
			
			repaint();
		}
	}
	
	/**
	 * 删除若干行
	 */
	public void removeRows() {
		for(int i = 0; i < getCurrentCells().length; i ++) {
			removeRow(getCurrentCells()[i].getY());
		}
	}
	
	/**
	 * 获取旋转轴
	 * @return 旋转轴
	 */
	public Cell getAxis() {
		return currentTetromino.getAxis();
	}
	
	/**
	 * 获取需要旋转的格子
	 * @return 需要旋转的格子
	 */
	public Cell[] getRotateCells() {
		return currentTetromino.getRotateCells();
	}
	
	/**
	 * 获取当前俄罗斯方块的所有格子
	 * @return 当前俄罗斯方块的所有格子
	 */
	public Cell[] getCurrentCells() {
		return currentTetromino.getCells();
	}
	
	/**
	 * 判断俄罗斯方块是否到达底部(包括是否到达面板底部和下一位置是否有方块)
	 * @return true，到达；false，没到达
	 */
	public boolean isReachBottomEdge() {
		int oldY, newY, oldX;		//定义格子旧的纵坐标、新的纵坐标和旧的横坐标
		
		for(int i = 0; i < getCurrentCells().length; i ++) {
			oldY = getCurrentCells()[i].getY();
			newY = oldY + 1;
			oldX = getCurrentCells()[i].getX();
			
			if(oldY == ROWS - 1) {//到达面板底部
				return true;
			}
			
			if(isContain(oldX, newY)) {//下一位置有方块
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断俄罗斯方块是否到达左边界(包括是否超出面板左边界和下一位置是否出现与其他方块碰撞)
	 * @return true，已到达；false，没到达
	 */
	public boolean isReachLeftEdge() {
		int oldX, newX, oldY;		//定义格子旧的横坐标、新的横坐标和旧的纵坐标
		
		for(int i = 0; i < getCurrentCells().length; i ++) {
			oldX = getCurrentCells()[i].getX();
			newX = oldX - 1;
			oldY = getCurrentCells()[i].getY();
			
			if(oldX == 0 || isContain(newX, oldY)) {//到达左边界
				return true;
			}
			
			if(isContain(newX, oldY)) {//下一位置有方块
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断俄罗斯方块是否到达右边界(包括是否超出面板右边界和下一位置是否出现与其他方块碰撞)
	 * @return true，已到达；false，没到达
	 */
	public boolean isReachRightEdge() {
		int oldX, newX, oldY;		//定义格子旧的横坐标、新的横坐标和旧的纵坐标
		
		for(int i = 0; i < getCurrentCells().length; i ++) {
			oldX = getCurrentCells()[i].getX();
			newX = oldX + 1;
			oldY = getCurrentCells()[i].getY();
			
			if(oldX == COLUMNS - 1 || isContain(newX, oldY)) {//到达右边界
				return true;
			}
			
			if(isContain(newX, oldY)) {//下一位置有方块
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断俄罗斯方块顺时针转后是否超出边界(包括是否超出面板边界和下一位置是否有方块)
	 * @return true，超出边界；false，没超边界
	 */
	public boolean clockwiseRotateIsOutOfBounds() {
		int oldX;	//rotateCell的横坐标
		int oldY;	//rotateCell的纵坐标
		int newX;	//rotateCell旋转后的横坐标
		int newY;	//rotateCell旋转后的纵坐标
		
		for(int i = 0; i < 3; i ++) {
			oldX = getRotateCells()[i].getX();
			oldY = getRotateCells()[i].getY();
			
			newX = getAxis().getX() - oldY + getAxis().getY();	//新横坐标计算算法
			newY = getAxis().getY() + oldX - getAxis().getX();	//新纵坐标计算算法
			
			if(newX < 0 || newY < 0 || newX > COLUMNS - 1 || newY > ROWS - 1) {//如果越界，返回true
				return true;
			}
			
			if(isContain(newX, newY)) {//如果越界，返回true
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 判断俄罗斯方块逆时针转后是否超出边界(包括是否超出面板边界和下一位置是否有方块)
	 * @return true，超出边界；false，没超边界
	 */
	public boolean anticlockwiseRotateIsOutOfBounds() {
		int oldX;	//rotateCell的横坐标
		int oldY;	//rotateCell的纵坐标
		int newX;	//rotateCell旋转后的横坐标
		int newY;	//rotateCell旋转后的纵坐标
		
		for(int i = 0; i < 3; i ++) {
			oldX = getRotateCells()[i].getX();
			oldY = getRotateCells()[i].getY();
			
			newX = getAxis().getX() - getAxis().getY() + oldY;	//新横坐标计算算法
			newY = getAxis().getY() + getAxis().getX() - oldX;	//新纵坐标计算算法
			
			if(newX < 0 || newY < 0 || newX > COLUMNS - 1 || newY > ROWS - 1) {//如果越界，返回true
				return true;
			}
			
			if(isContain(newX, newY)) {//如果越界，返回true
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 判断某个格子是否存在方块对象
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @return true，存在对象；false，不存在对象
	 */
	public boolean isContain(int x, int y) {
		if(wall[y][x] == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 实现俄罗斯方块的硬下落
	 */
	public void hardDrop() {
		while(!isReachBottomEdge()) {
			currentTetromino.softDrop();
		}
	}
	
	/**
	 * 消除单行
	 * @param i 行的下标
	 */
	public void removeRow(int i) {
		int oldY, newY;	
		
		for(int j = 0; j < COLUMNS; j ++) {
			if(wall[i][j] == null) {//如果其中一个方块没有填满，return
				return;
			}
		}
		
		/* 消除行并把该行上面的方块往下移 */
		for(int k = i; k >= 1; k --){
			System.arraycopy(wall[k - 1], 0, wall[k], 0, COLUMNS);
			
			for(int m = 0; m < COLUMNS; m ++) {
				if(wall[k][m] != null) {//对于不是空的对象，要重设其纵坐标
					oldY = wall[k][m].getY();
					newY = oldY + 1;
					wall[k][m].setY(newY);				
				}
			}
			
		}
		Arrays.fill(wall[0], null);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getBounds().width, getBounds().height);
		
		/* 画墙 */
		for(int i = 0; i < ROWS; i ++) {
			for(int j = 0; j < COLUMNS; j ++) {
				if(wall[i][j] == null) {//某点的方块为空时
					g.setColor(Color.WHITE);
					g.fillRect(j * Cell.CELL_SIZE + 1, i * Cell.CELL_SIZE + 1, Cell.CELL_SIZE - 2, Cell.CELL_SIZE - 2);
				} else {//当方块不为空时
					wall[i][j].paintCell(g);
				}
			}
		}
		
		/* 画当前俄罗斯方块 */
		for(int i = 0; i < getCurrentCells().length; i ++) {
			getCurrentCells()[i].paintCell(g);
		}
		
	}
}
