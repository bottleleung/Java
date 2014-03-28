package com.mypro.model.componets;

import com.mypro.base.graphics.Bitmap;
import com.mypro.model.interfaces.Button;
import com.mypro.model.interfaces.OnClickListener;

public class ButtonAdapter extends Componet implements Button{
	private boolean enable = true;
	private OnClickListener onClickListener;
	private Bitmap btn_img;
	/**
	 *  创建一个按钮
	 * @param btn_img			没有点击时的按钮按钮
	 * @throws Exception		当btn_img为空时抛出一个错误
	 */
	public ButtonAdapter(Bitmap btn_img,OnClickListener onClickListener)throws Exception{
		if(btn_img==null){
			throw new Exception("ButtonAdapter:图片按钮不能为空");
		}
		this.onClickListener = onClickListener;
		this.btn_img = btn_img;
	}
	@Override
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return btn_img;
	}

	@Override
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return btn_img.getWidth();
	}

	@Override
	public int getPicHeight() {
		// TODO Auto-generated method stub
		return btn_img.getHeight();
	}
	
	@Override
	public void onClick() {
		// TODO Auto-generated method stub
		onClickListener.onClick();
	}
	@Override
	public boolean isEnable() {
		// TODO Auto-generated method stub
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
}
