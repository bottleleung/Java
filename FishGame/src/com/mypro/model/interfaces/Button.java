package com.mypro.model.interfaces;
/**
 * 按钮的接口
 * @author Leslie Leung
 *
 */
public interface Button extends Drawable{
	/**
	 * 是否可用
	 * @return
	 */
	public boolean isEnable();
	/**
	 * 当按钮被点击
	 */
	public void onClick();
}
