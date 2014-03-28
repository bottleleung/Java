package com.mypro.base.graphics;


/*
 * 矩阵接口
 */
public interface Matrix {
	/*
	 * 设置平移
	 */
	public void setTranslate(float x, float y);
	/*
	 * 重置矩阵
	 */
	public void reset();
	/*
	 * 缩放
	 */
	public void preScale(float x,float y);
	/*
	 * 旋转
	 */
	public void preRotate(float angle,float x,float y);
}
