package com.mypro.model.componets;

import com.mypro.base.graphics.Canvas;
import com.mypro.base.graphics.Paint;
import com.mypro.model.DrawableAdapter;
/**
 * 组件的父类
 * @author Leslie Leung
 *
 */
public abstract class Componet extends DrawableAdapter{
	/**
	 * 组件所在屏幕的x,y坐标
	 */
	private float layout_x;
	private float layout_y;
	
	/**
	 * 获取组件在屏幕的X坐标
	 * @return
	 */
	public float getLayout_x() {
		return layout_x;
	}
	/**
	 * 设置组件在屏幕的X坐标
	 * @param layout_x
	 */
	public void setLayout_x(float layout_x) {
		this.layout_x = layout_x;
	}
	/**
	 * 获取组件在屏幕的Y坐标
	 * @return
	 */
	public float getLayout_y() {
		return layout_y;
	}
	/**
	 * 设置组件在屏幕的Y坐标
	 * @param layout_y
	 */
	public void setLayout_y(float layout_y) {
		this.layout_y = layout_y;
	}
	
	public void onDraw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(this.getCurrentPic(),
				layout_x,layout_y, paint);		
	}
}
