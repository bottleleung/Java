package mines;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.*;

/**
 * 扫雷场景类
 * @author Leslie Leung
 */
public class MinesFields extends JPanel {
	public static final int ROWS = 16;	//整个场景的行数
	public static final int COLUMNS = 16;	//整个场景的列数
	public static final int MINES_NUM = 40;		//地雷的数目
	
	private Map<String, Field> fields;		//表示雷域中所有Field
	private List<Field> notMineFields;		//表示所有不是地雷的Field的集合
	private GameRunScript mouseListener;	//表示鼠标事件的监听器
	
	/**
	 * MinesFields类构造方法
	 */
	public MinesFields() {
		setPreferredSize(new Dimension(ROWS * Field.FIELD_SIZE, COLUMNS * Field.FIELD_SIZE));	//设置扫雷面板大小
		
		fields = new HashMap<String, Field>();	//用哈希表表示所有的Field
		notMineFields = new ArrayList<Field>();
		mouseListener = new GameRunScript();
		Random random = new Random();
		
		int mineX, mineY;	//记录生成地雷的坐标
		List<Field> aroundList;		//表示获取的该Field的附近的Field的集合
		
		/* 生成雷域中的每个Field */
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				fields.put(x + "," + y, new Field(x, y));	//创建Field对象，并把对象添加到fields中
			}
		}
		
		/* 随机生成40个地雷并设置附近地雷数 */
		for(int i = 0; i < MINES_NUM; i ++) {
			/* 如果生成的地雷坐标重复，重新设置地雷的坐标 */
			do {
				mineX = random.nextInt(COLUMNS);
				mineY = random.nextInt(ROWS);
			} while(isMine(mineX, mineY));
			
			setMine(mineX, mineY);	//根据相应的键值把该Field设置为地雷
			aroundList = getAround(mineX, mineY);		//获取该地雷附近的所有Field
			
			/* 遍历aroundList，把不是地雷的Field的地雷值加1 */
			for(Field field: aroundList) {
				if(!field.isMine()) {
					field.setMineValue(field.getMineValue() + 1);
				}
			}
			
		}
		
		/* 把不是地雷的Field添加到notMineFields中 */
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				if(!getField(x, y).isMine()) {
					notMineFields.add(getField(x, y));
				}
			}
		}
	}
	
	/**
	 * 获取内部类的实例
	 * @return 内部类的实例
	 */
	public GameRunScript getInnerInstance() {
		return mouseListener;
	}
	
	/**
	 * 内部类，用于实现用鼠标在游戏界面上操作的功能
	 * @author Leslie Leung
	 */
	 private class GameRunScript extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			Point p = e.getPoint();		//获取所点击位置的那个点
			int y = p.x / Field.FIELD_SIZE;		//所获取的点的横坐标
			int x = p.y / Field.FIELD_SIZE;		//所获取的点的纵坐标

			/* 如果单击鼠标左键 */
			if(e.getButton() == MouseEvent.BUTTON1) {
				open(x, y);
			} 
			/* 如果双击鼠标左键 */
			if(e.getClickCount() == 2) {
				openAround(x, y);
			}
			/* 如果单击鼠标右键,标记该域或取消该域的标记 */
			if(e.getButton() == MouseEvent.BUTTON3) {
				mark(x, y);
			}
				
		} 
	}
	
	/**
	 * 对于所有不是地雷的Field，判断是否全部都已代开
	 * @return true，全部都已打开；false，部分或全部都没打开
	 */
	public boolean isAllOpened() {
		for(Field field: notMineFields) {
			if(!field.isOpened()) {
				return false;
			}
		}
		explode();
		return true;
	}
	 
	/**
	 * 返回一个Field对象
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @return Field对象
	 */
	public Field getField(int x, int y) {
		return fields.get(x + "," + y);
	}
	
	/**
	 * 返回不是地雷的所有Field
	 * @param x 某Field的横坐标
	 * @param y 某Field的纵坐标
	 * @return 不是地雷的所有Field，存放在集合里
	 */
	public List<Field> getAround(int x, int y) { 
		List<Field> aroundList = new ArrayList<Field>();
		
		for(int m = -1; m <= 1; m ++) {
			for(int n = -1; n <= 1; n ++) {
				/* 如果所选中的对象为地雷本身，跳出本次循环 */
				if(m == 0 && n == 0) {
					continue;
				}
				
				/* 先判断是否获取附近的地雷的坐标是否越界，若否，把不是地雷的所有Field放进aroundList里面 */
				if(x + m < COLUMNS && x + m >= 0 && y + n >= 0 && y + n < ROWS) {
					aroundList.add(getField(x + m, y + n));
				}
				
			}
		}
		
		return aroundList;
		
	}
	
	/**
	 * 当该Field的地雷值为0时，递归打开其附近的Field
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
	public void open(int x, int y) {
		if(isCovered(x, y)) {
			/* 如果是该Field是地雷的话，打开全部Field */
			if(isMine(x, y)) {
				explode();
				JOptionPane.showMessageDialog(null, "你输了");
				return;
			}			
			setOpened(x, y);	//把该Field设置为打开状态
			repaint();
			
			if(isAllOpened()) {//如果全部已被打开
				JOptionPane.showMessageDialog(null, "恭喜你，你赢了！！！");
			}	
			
			/* 当该field的地雷值为0才打开附近的Field */
			if(getMineValue(x, y) == 0) {
				List<Field> aroundList = getAround(x, y);
				
				/* 递归调用打开某Field附近的所有Field */
				for(Field field: aroundList) {
					open(field.getX(), field.getY());
				}
			}
			
		}
	}
	
	/**
	 * 当在该Field附近的其他域作标记后，用于处理“打开附近Field”的方法
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
	public void openAround(int x, int y) {
		if(isOpened(x, y) && getMineValue(x, y) > 0) {//如果该Field已打开并且它附近的地雷值大于0
			
			List<Field> aroundList = getAround(x, y);
			int mineNum = 0;
			
			for(Field field: aroundList) {
				/* 如果该Field已被标记，mineNum加1 */
				if(field.isMarked()) {
					mineNum ++;
				}
			}
			
			/* 当该Field附近的地雷值和mineNum相等时才执行真正的操作  */
			if(getMineValue(x, y) == mineNum) {
				for(Field field: aroundList) {	
					if(field.isMarked() && !field.isMine()) {
						explode();	//如果该Field被标记且该Field不是地雷，马上打开所有Field
						JOptionPane.showMessageDialog(null, "你输了");
					} else if(!field.isMarked() && !field.isMine()) {
						open(field.getX(), field.getY());//当附近的field不是地雷时调用open方法
					}
				}
				
			}
		}
		
	}
	
	/**
	 * 把某个Field设置为打开状态
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
	public void setOpened(int x, int y) {
		getField(x, y).setStyle(Field.STYLE_OPENED);
	}
	
	/**
	 * 根据标记状态设置其为标记或取消标记
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
	public void mark(int x, int y) {
		if(isCovered(x, y)) {
			setMarked(x, y);
			repaint();
		} else if(isMarked(x, y)) {
			setCovered(x, y);
			repaint();
		}
	}
	
	/**
	 * 把某个Field设置为被标记
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
	public void setMarked(int x, int y) {
		getField(x, y).setStyle(Field.STYLE_MARKED);
	}
	
	/**
	 * 判断一个Field是否被标记
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @return 若被标记，true；否则，false
	 */
	public boolean isMarked(int x, int y) {
		return getField(x, y).isMarked();
	}
	
	/**
	 * 判断某个Field是否地雷
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @return true，是地雷；false，不是地雷
	 */
	public boolean isMine(int x, int y) {
		return getField(x, y).isMine();
	}
		
	/**
	 * 通过把mineValue值设置为-100表示该Field为地雷
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
	public void setMine(int x, int y) {
		getField(x, y).setMine();
	}
	
	/**
	 * 判断该Field是否已打开
	 * @param x
	 * @param y
	 * @return true，已被打开；false，没打开
	 */
	public boolean isOpened(int x, int y) {
		return getField(x, y).isOpened();
	}
	
	/**
	 * 获得指定Field的地雷值
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @return 该Field的地雷值
	 */
	public int getMineValue(int x, int y) {
		return getField(x, y).getMineValue();
	}
	
	/**
	 * 判断某个Field是否为覆盖
	 * @param x 横坐标 
	 * @param y 纵坐标
	 * @return 若为覆盖，true；否则，false
	 */
	public boolean isCovered(int x, int y) {
		return getField(x, y).isCovered();
	}
	
	/**
	 * 把该Field的样式设置为覆盖
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
	public void setCovered(int x, int y) {
		getField(x, y).setStyle(Field.STYLE_COVERED);
	}
	
	/**
	 * 打开全部Field
	 */
	public void explode() {
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				setOpened(x, y);
			}
		}
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getBounds().width, getBounds().height);
		
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				getField(x, y).paintField(g);
			}
		}
			
	}
	
}
