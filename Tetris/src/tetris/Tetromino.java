package tetris;

/**
 * 四格方块类
 * @author Leslie Leung
 */
public class Tetromino {
	protected Cell[] cells;		//用对象数组cells存储四格方块
	protected Cell axis;	//旋转轴
	protected Cell[] rotateCells;	//需要旋转的格子集合
	
	/**
	 * 实现四格方块逆时针转的算法
	 * @param axis 旋转轴，以cells中下标为0的Cell为旋转轴
	 * @param rotateCells 要旋转的格子的集合
	 */
	protected void anticlockwiseRotate(Cell axis, Cell[] rotateCells) {
		int oldX;	//用以表示传进来的rotateCell的横坐标
		int oldY;	//用以表示传进来的rotateCell的纵坐标
		int newX;	//用以表示传进来的rotateCell旋转后的横坐标
		int newY;	//用以表示传进来的rotateCell旋转后的纵坐标
		
		for(int i = 0; i < 3; i ++) {
			oldX = rotateCells[i].getX();
			oldY = rotateCells[i].getY();
			
			newX = axis.getX() - axis.getY() + oldY;	//新横坐标计算算法
			newY = axis.getY() + axis.getX() - oldX;	//新纵坐标计算算法
			
			rotateCells[i].setX(newX);		//重新设置目标格子的横坐标
			rotateCells[i].setY(newY);		//重新设置目标格子的纵坐标
		}
	}
	
	/**
	 * 实现四格方块顺时针转的算法
	 * @param axis 旋转轴，以cells中下标为0的Cell为旋转轴
	 * @param rotateCells 要旋转的格子的集合
	 */
	protected void clockwiseRotate(Cell axis, Cell[] rotateCells) {
		int oldX;	//用以表示传进来的rotateCell的横坐标
		int oldY;	//用以表示传进来的rotateCell的纵坐标
		int newX;	//用以表示传进来的rotateCell旋转后的横坐标
		int newY;	//用以表示传进来的rotateCell旋转后的纵坐标
		
		for(int i = 0; i < 3; i ++) {
			oldX = rotateCells[i].getX();
			oldY = rotateCells[i].getY();
			
			newX = axis.getX() - oldY + axis.getY();	//新横坐标计算算法
			newY = axis.getY() + oldX - axis.getX();	//新纵坐标计算算法
			
			rotateCells[i].setX(newX);		//重新设置目标格子的横坐标
			rotateCells[i].setY(newY);		//重新设置目标格子的纵坐标
		}
	}
	
	/**
	 * 实现四格方块的自动下落
	 */
	protected void softDrop() {
		int oldY;	//某个格子下落前的纵坐标
		int newY;	//某个格子下落后的纵坐标
		
		/* 所有格子下移 */
		for(int i = 0; i < cells.length; i ++) {
			oldY = cells[i].getY();
			newY = oldY + 1;
			
			cells[i].setY(newY);
		}
	}
	
	/**
	 * 实现四格方块左移的算法
	 */
	protected void moveLeft() {
		int oldX;	//某个格子左移前的横坐标
		int newX;	//某个格子左移后的横坐标
		
		/* 所有格子左移 */
		for(int i = 0; i < cells.length; i ++) {
			oldX = cells[i].getX();
			newX = oldX - 1;
			
			cells[i].setX(newX);
		}
	}
	
	/**
	 * 实现四格方块右移的算法
	 */
	protected void moveRight() {
		int oldX;	//某个格子右移前的横坐标
		int newX;	//某个格子右移后的横坐标
		
		/* 所有格子右移 */
		for(int i = 0; i < cells.length; i ++) {
			oldX = cells[i].getX();
			newX = oldX + 1;
			
			cells[i].setX(newX);
		}
	}
	
	/**
	 * 返回四格方块的格子的集合
	 * @return Cell的集合
	 */
	protected Cell[] getCells() {
		return cells;
	}
	
	/**
	 * 获取旋转轴
	 * @return 旋转轴
	 */
	protected Cell getAxis() {
		return axis;
	}
	
	/**
	 * 获取需要旋转的目标格子的集合
	 * @return 目标格子的集合
	 */
	protected Cell[] getRotateCells() {
		return rotateCells;
	}
	
	/**
	 * 把cells[0]设置为旋转轴
	 */
	protected void setAxis() {
		axis = cells[0];
	}
	
	/**
	 * 新建长度为3的数组并把cells[1]、cells[2]、cells[3]添加到rotateCells中
	 */
	protected void setRotateCells() {
		rotateCells = new Cell[]{cells[1], cells[2], cells[3]};
	}
}
