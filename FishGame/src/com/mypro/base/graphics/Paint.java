package com.mypro.base.graphics;

/*
 * 画笔属性
 */
public interface Paint {
	public void setTypeface(Object obj); // 设置Paint的字体
	public void setAntiAlias(boolean tf);
	public void setFilterBitmap(boolean tf);
	public void setDither(boolean tf);
	public void setTextSize(int size);  // 根据不同分辨率设置字体大小
	public void setColor(int color);
}
