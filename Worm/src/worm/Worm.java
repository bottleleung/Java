package worm;

import java.awt.*;
import java.util.Arrays;

/**
 * 贪吃蛇类
 * @author Leslie Leung
 * @see Cell
 */
public class Worm {
	
	public static final int UP = 1;	//方向：上
	public static final int DOWN = -1;		//方向：下
	public static final int LEFT = 2;	//方向：左
	public static final int RIGHT = -2;	//方向：右
	public static final int DEFAULT_LENGTH = 6;	//设置贪吃蛇的默认长度为6
	public static final int DEFAULT_DIRECTION = RIGHT;	//默认运行方向
	public static final int INIT_SPEED = 100;	//贪吃蛇初始速度
	
	private int currentLength;		//贪吃蛇当前长度
	private int currentDirection;	//贪吃蛇当前方向
	private boolean eat;	//判断贪吃蛇是否吃到食物
	private Cell[] cells;
	
	/**
	 * 构造方法，初始化贪吃蛇
	 */
	public Worm() {
		cells = new Cell[DEFAULT_LENGTH];
		currentDirection = DEFAULT_DIRECTION;	//设置游戏一开始时的默认方向为DOWN
		currentLength = DEFAULT_LENGTH;		//初始化当前贪吃蛇长度为默认长度
		
		for(int i = 0;i < DEFAULT_LENGTH; i ++) {
			cells[i] = new Cell(DEFAULT_LENGTH - i - 1, 0);
		}

	}
	
	/**
	 * 获取贪吃蛇当前长度
	 * @return 贪吃蛇的当前长度
	 */
	public int getCurrentLength() {
		return currentLength;
	}
	
	/**
	 * 获取贪吃蛇的当前方向
	 * @return 贪吃蛇的当前方向
	 */
	public int getCurrentDirection() {
		return currentDirection;
	}
	
	/**
	 * 检查贪吃蛇数组是否与一个结点的位置重叠
	 * @param x 传入的结点的横坐标
	 * @param y 传入的结点的纵坐标
	 * @return 重叠，true；不重叠，false
	 */
	public boolean contains(int x, int y) {
		
		for(int i = 0; i < currentLength; i ++) {
			if(x == cells[i].getX() && y == cells[i].getY()) {
				return true;
			}
		}
			
		return false;
	}
	
	/**
	 * 改变方向
	 * @param 贪吃蛇新的爬行方向
	 */
	public void changeDirection(int direction) {
		/* 如果传入的新方向与当前贪吃蛇运行方向相同或相反，返回，不采取任何操作   */
		if(currentDirection == direction || currentDirection + direction == 0) {
			return;
		}
		
		currentDirection = direction;
	}
	
	/**
	 * 爬行算法：移除末尾结点，其余所有结点往后移，再把末尾结点添加到头结点的位置中
	 * @param direction 爬行方向
	 * @return 贪吃蛇是否吃到食物，true表示吃到，false表示吃不到
	 */
	public boolean creep(int direction, Cell food) {
		eat = false;
		currentDirection = direction;	//将爬行方向设置为输入的方向
		Cell head = newHead(currentDirection);	//重设头结点
		
		/* 如果贪吃蛇爬行的下一位置上存在食物，进行数组扩容，生成新贪吃蛇，并重新生成食物 */
		if( head.getX() == food.getX() && head.getY() == food.getY() ) {
			cells = Arrays.copyOf(cells, cells.length + 1);
			eat = true;
			currentLength ++;	//吃到食物，长度自增
		}
		
		for(int i = cells.length - 1; i > 0; i --) {
			cells[i] = cells[i - 1];
		}
		
		cells[0] = head;
		
		return eat;
	}
	
	/**
	 * 重新生成头结点算法：根据爬行方向重新生成头结点
	 * @param currentDirection 当前爬行方向
	 * @return 新建的头结点
	 */
	public Cell newHead(int currentDirection) {
		Cell newHead = null;
		
		switch(currentDirection) {
			case UP: 
				newHead = new Cell(cells[0].getX(), cells[0].getY() - 1);
				break;
			case DOWN:
				newHead = new Cell(cells[0].getX(), cells[0].getY() + 1);
				break;
			case LEFT: 
				newHead = new Cell(cells[0].getX() - 1, cells[0].getY());
				break;
			case RIGHT:
				newHead = new Cell(cells[0].getX() + 1, cells[0].getY());
				break;
		}
		
		return newHead;
	}
	
	/**
	 * 检查贪吃蛇是否撞击算法
	 * @param direction 当前运动方向
	 * @return 是否产生撞击
	 */
	public boolean hit(int direction) {
		Cell nextHead = newHead(direction);
		
		/* 检查是否碰撞到自身 */
		if( this.contains(nextHead.getX(), nextHead.getY()) ) {
			return true;
		}
		
		/* 检查贪吃蛇是否碰壁 */
		if(nextHead.getX() < 0 || nextHead.getX() >= WormStage.COLUMNS
				||nextHead.getY() < 0 || nextHead.getY() >= WormStage.ROWS) {
			return true;
		}
		
		return false;
	}
	
	/* 绘制贪吃蛇 */
	public void paint(Graphics g) {
		for(int i = 0; i < cells.length; i ++) {
			cells[i].paintCell(g);
		}
	}
}
