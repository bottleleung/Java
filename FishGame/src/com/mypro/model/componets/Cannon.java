package com.mypro.model.componets;

import com.mypro.base.graphics.Bitmap;
import com.mypro.base.tools.Log;
import com.mypro.model.DrawableAdapter;
import com.mypro.model.GamingInfo;

/**
 * 定义所有大炮的模型类
 * @author Leslie Leung
 *
 */
public class Cannon extends DrawableAdapter{
	private Bitmap[] cannonImage;
	private int currentId;
	//大炮的旋转点设置
	private int gun_rotate_point_x;
	private int gun_rotate_point_y;
	//大炮的位置
	private float x;
	private float y;
	
	public Cannon(Bitmap[] cannonImage){
		this.cannonImage = cannonImage;
	}
	public void init(){
		gun_rotate_point_x = this.getPicWidth()/2;
		gun_rotate_point_y = this.getPicHeight()/2;
		x = GamingInfo.getGamingInfo().getCannonLayoutX()-gun_rotate_point_x;
		y = GamingInfo.getGamingInfo().getCannonLayoutY()-gun_rotate_point_y;
		this.getPicMatrix().setTranslate(x, y);
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public int getGun_rotate_point_x() {
		return gun_rotate_point_x;
	}

	public int getGun_rotate_point_y() {
		return gun_rotate_point_y;
	}
	/**
	 * 播放发射炮弹的动作
	 */
	public void shot(){
		new Thread(new Runnable() {		
			@Override
			public void run() {
				// 因为默认就是第一张图，所以从第二张图开始播放
				for(int i =1;i<cannonImage.length;i++){
					try {
						currentId = i;
						Thread.sleep(300);
					} catch (Exception e) {
						Log.e("Cannon", e.toString());
					}
				}
				//最后还要恢复第一张图的样子
				currentId = 0;
			}
		}).start();
	}
	
	
	@Override
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return cannonImage[currentId];
	}
	@Override
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return cannonImage[currentId].getWidth();
	}
	@Override
	public int getPicHeight() {
		// TODO Auto-generated method stub
		return cannonImage[currentId].getHeight();
	}
}
