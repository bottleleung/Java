package mines;

import java.awt.*;

/**
 * 网格类
 * @author Leslie Leung
 */
public class Field {
	public static final int STYLE_COVERED = 1;	//Field覆盖时的样式
	public static final int STYLE_OPENED = 2;	//Field打开时的样式
	public static final int STYLE_MARKED = 3;	//Field被标记时的样式
	public static final int FIELD_SIZE = 25;	//一个格子的大小

	private int mineValue;	//Field的附近地雷值
	private int x;	//Field的横坐标
	private int y;	//Field的纵坐标
	private int style;	//Field的样式

	/**
	 * Field类构造方法
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
	public Field(int x, int y) {
		this.x = x;
		this.y = y;
		style = STYLE_COVERED;	//初始化样式为覆盖
		mineValue = 0;	//初始化该Field不为地雷，用mineValue值为0来表示
	}

	/**
	 * 获取该Field的横坐标
	 * @return 横坐标
	 */
	public int getX() {
		return x;
	}

	/**
	 * 获取该Field的纵坐标
	 * @return 纵坐标
	 */
	public int getY() {
		return y;
	}

	/**
	 * 设置某个Field的样式
	 * @param style 样式
	 */
	public void setStyle(int style) {
		this.style = style;
	}

	/**
	 * 通过把mineValue值设置为-100表示该Field为地雷
	 */
	public void setMine() {
		mineValue = -100;
	}

	/**
	 * 返回该Field的mineValue
	 * @return mineValue
	 */
	public int getMineValue() {
		return mineValue;
	}

	/**
	 * 设置该Field的地雷值
	 * @param value 地雷值
	 */
	public void setMineValue(int value) {
		mineValue = value;
	}

	/**
	 * 判断该Field是否地雷
	 * @return true，是；否则，false
	 */
	public boolean isMine() {
		return mineValue == -100;
	}

	/**
	 * 判断该Field是否被标记
	 * @return true，被标记；false，没被标记
	 */
	public boolean isMarked() {
		return style == STYLE_MARKED;
	}

	/**
	 * 判断该Field是否覆盖
	 * @return true，覆盖；false，没覆盖
	 */
	public boolean isCovered() {
		return style == STYLE_COVERED;
	}

	/**
	 * 判断该Field是否已打开
	 * @return true，已打开；false，没打开
	 */
	public boolean isOpened() {
		return style == STYLE_OPENED;
	}

	/**
	 * 绘图方法
	 * @param g Graphics g
	 */
	public void paintField(Graphics g) {
		int yCoordinate = x * FIELD_SIZE + 1;
		int xCoordinate = y * FIELD_SIZE + 1;
		
		if(isCovered()) {//样式为覆盖时候所做的绘图
			g.setColor(Color.CYAN);
			g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
		}
		
		if(isOpened()) {//样式为打开时所做的绘图
			if(mineValue > 0) {//当该Field的附近地雷值大于0时
				g.setColor(Color.WHITE);
				g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
				g.setColor(Color.BLACK);
				g.drawString(mineValue + "", y * FIELD_SIZE + FIELD_SIZE / 2, x * FIELD_SIZE + FIELD_SIZE / 2);
				
			} else if(mineValue == -100) {//当该Field是地雷时
				g.setColor(Color.RED);
				g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
				
			} else if(mineValue == 0) {//当该Field的附近地雷值为0
				g.setColor(Color.WHITE);
				g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
				
			}
		}

		if(isMarked()) {//样式为标记时所做的绘图
			g.setColor(Color.CYAN);
			g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
			g.setColor(Color.MAGENTA);
			g.drawString("？", y * FIELD_SIZE + FIELD_SIZE / 2, x * FIELD_SIZE + FIELD_SIZE / 2);
		}
	}
}
