package tetris;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 网格类
 * @author Leslie Leung
 */
public class Cell {
	public static final int CELL_SIZE = 25;		//一个网格的大小

	/* 格子的所有颜色  */
	public static final int COLOR_CYAN = 0;
	public static final int COLOR_BLUE = 1;
	public static final int COLOR_GREEN = 2;
	public static final int COLOR_YELLOW = 3;
	public static final int COLOR_ORANGE = 4;
	public static final int COLOR_RED = 5;
	public static final int COLOR_PINK = 6;

	private int color;	//格子的颜色
	private int x;	//横坐标
	private int y;	//纵坐标

	/**
	 * 构造方法
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @param style 格子的样式，通过颜色来指定
	 */
	public Cell(int x, int y, int style) {
		/* 根据传进来的样式决定格子的颜色 */
		switch(style) {
			case 0: color = COLOR_CYAN; break;
			case 1: color = COLOR_BLUE; break;
			case 2: color = COLOR_GREEN; break;
			case 3: color = COLOR_YELLOW; break;
			case 4: color = COLOR_ORANGE; break;
			case 5: color = COLOR_RED; break;
			case 6: color = COLOR_PINK; break;
		}

		this.x = x;
		this.y = y;
	}

	/**
	 * 设置该格子的横坐标
	 * @param newX 新的横坐标
	 */
	public void setX(int newX) {
		x = newX;
	}

	/**
	 * 设置该格子的纵坐标
	 * @param newY 新的纵坐标
	 */
	public void setY(int newY) {
		y = newY;
	}

	/**
	 * 获取该Cell的横坐标
	 * @return 横坐标
	 */
	public int getX() {
		return x;
	}

	/**
	 * 获取该Cell的纵坐标
	 * @return 纵坐标
	 */
	public int getY() {
		return y;
	}

	/**
	 * 绘图方法
	 * @param g Graphics引用
	 */
	public void paintCell(Graphics g) {
		switch(color) {
			case COLOR_CYAN: g.setColor(Color.CYAN);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
			case COLOR_BLUE: g.setColor(Color.BLUE);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
			case COLOR_GREEN: g.setColor(Color.GREEN);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
			case COLOR_YELLOW: g.setColor(Color.YELLOW);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
			case COLOR_ORANGE: g.setColor(Color.ORANGE);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
			case COLOR_RED: g.setColor(Color.RED);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
			case COLOR_PINK: g.setColor(Color.PINK);
				g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
				break;
		}
	}
}
