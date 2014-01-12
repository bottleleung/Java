package traingame;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 纸牌类
 * @author Leslie Leung
 */
public class Card {
	public static final int SUIT_SPADE = 0;		//表示黑桃
	public static final int SUIT_HEART = 1;		//表示红桃
	public static final int SUIT_DIAMOND = 2;	//表示方块
	public static final int SUIT_CLUB = 3;		//表示梅花

	/* 表示13种点数   */
	public static final int POINT_A = 1;
	public static final int POINT_2 = 2;
	public static final int POINT_3 = 3;
	public static final int POINT_4 = 4;
	public static final int POINT_5 = 5;
	public static final int POINT_6 = 6;
	public static final int POINT_7 = 7;
	public static final int POINT_8 = 8;
	public static final int POINT_9 = 9;
	public static final int POINT_10 = 10;
	public static final int POINT_J = 11;
	public static final int POINT_Q = 12;
	public static final int POINT_K = 13;

	public static final int CARD_WIDTH = 71;		//纸牌的宽度
	public static final int CARD_HEIGHT = 96;		//纸牌的高度

	private int suit;	//表示纸牌的花式
	private int point;	//表示纸牌的点数
	private boolean selected;	//表示纸牌是否被选中
	private boolean face;	//表示纸牌是背面朝上还是正面朝上
	private int x;		//横坐标
	private int y;		//纵坐标

	/**
	 * 构造方法
	 * @param point 纸牌的点数
	 * @param suit 纸牌的花式
	 */
	public Card(int point, int suit) {
		this.point = point;	//设置纸牌的点数
		this.suit = suit;	//设置纸牌的花式
		selected = false;	//默认纸牌没被选中
		face = false;		//默认纸牌背面朝上
	}

	/**
	 * 获取该纸牌的横坐标
	 * @return 横坐标
	 */
	public int getX() {
		return x;
	}

	/**
	 * 获取该纸牌的纵坐标
	 * @return 纵坐标
	 */
	public int getY() {
		return y;
	}

	/**
	 * 设置纸牌的横坐标
	 * @param x 横坐标
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * 设置纸牌的纵坐标
	 * @param y 纵坐标
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * 获取某张纸牌的点数
	 * @return 该纸牌的点数
	 */
	public int getPoint() {
		return point;
	}

	/**
	 * 设置纸牌是否正面朝上
	 * @param isOpen 是否正面朝上
	 */
	public void setFace(boolean isOpen) {//isOpen为true，正面朝上；false，背面朝上
		face = isOpen;	
	}

	/**
	 * 设置纸牌是否被选中
	 * @param isSelected 是否被选中
	 */
	public void setSelected(boolean isSelected) {//isSelected为true，被选中；false，没选中
		selected = isSelected;
	}

	/**
	 * 判断纸牌是否被选中
	 * @return true，被选中；false，没被选中
	 */
	public boolean isSelected() {
		return selected == true;
	}

	/**
	 * 绘制纸牌
	 * @param g Graphics引用
	 */
	public void paintCard(Graphics g) {
		if(!selected && face) {//如果纸牌没被选中而且正面朝上
			g.drawImage(ImageFactory.createImage(suit + "_" + point), x, y, CARD_WIDTH, CARD_HEIGHT, null);
		} else if(!selected && !face) {//如果纸牌没被选中而且背面朝上
			g.drawImage(ImageFactory.createImage("back"), x, y, CARD_WIDTH, CARD_HEIGHT, null);
		} else if(selected && face) {//如果纸牌被选中且正面朝上
			g.setColor(Color.YELLOW);
			g.drawRect(x - 1, y - 1, CARD_WIDTH + 2, CARD_HEIGHT + 2);
			g.drawImage(ImageFactory.createImage(suit + "_" + point), x, y, CARD_WIDTH, CARD_HEIGHT, null);
		}
	}
}
