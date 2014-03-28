package com.mypro.manager;

import com.mypro.model.interfaces.Drawable;

/**
 * 布局管理器使用的类
 * @author Leslie Leung
 *
 */
public class LayoutInfo {
	private Drawable drawable;
	private float layout_x;
	private float layout_y;
	
	/**
	 * 构造器
	 * @param drawable
	 * @param layout_x
	 * @param layout_y
	 */
	public LayoutInfo(Drawable drawable,float layout_x,float layout_y){
		this.drawable = drawable;
		this.layout_x = layout_x;
		this.layout_y = layout_y;
	}

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public float getLayout_x() {
		return layout_x;
	}

	public void setLayout_x(float layout_x) {
		this.layout_x = layout_x;
	}

	public float getLayout_y() {
		return layout_y;
	}

	public void setLayout_y(float layout_y) {
		this.layout_y = layout_y;
	}
	
}
