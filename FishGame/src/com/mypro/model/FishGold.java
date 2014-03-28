package com.mypro.model;

import com.mypro.base.graphics.Bitmap;
import com.mypro.base.graphics.Canvas;
import com.mypro.base.graphics.Paint;
import com.mypro.tools.LogTools;

/**
 * 显示捕捉到鱼后获得的金币数量
 * @author Leslie Leung
 *
 */
public class FishGold extends DrawableAdapter{
	private int gold;//这个值记录当前组件应显示的金币数
	private int[] num_index = new int[1];//所有数字的索引，这里第一个元素代表得分的最大位数，往后类推

	public FishGold(int gold){
		try {
			this.gold = gold;
			updateNumIndex();
		} catch (Exception e) {
			e.printStackTrace();
			LogTools.doLogForException(e);
		}
	
	}
	@Override
	public void onDraw(Canvas canvas, Paint paint) {	
		
	}
	/**
	 * 更新数字索引
	 */
	private void updateNumIndex(){
		String num = gold+"";
		num_index = new int[num.length()];
		int index = 0;
		for(char n:num.toCharArray()){
			num_index[index] = n-48;
			index++;
		}		
	}	
	
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPicWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getPicHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
