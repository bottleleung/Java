package worm;

import java.awt.*;

/**
 * 网格类
 * @author Leslie Leung
 */
public class Cell {
	
	public static final int CELL_SIZE = 10;	//设置每个格子的大小，大小为10像素
	
	/* 定义格子坐标 */
	private int x;
	private int y;
	
	/**
	 * 构造方法，设置网格的坐标
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 获取网格的横坐标
	 * @return 网格的横坐标
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * 获取网格的纵坐标
	 * @return 网格的纵坐标
	 */
	public int getY() {
		return y;
	}

	public void paintCell(Graphics g) {
		g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
	}
}
