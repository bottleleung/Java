package com.mypro.model;

import com.mypro.base.graphics.Bitmap;

public class BackGround extends DrawableAdapter{
	private Bitmap background;
	public void setCurrentPic(Bitmap background){
		this.background = background;
	}
	@Override
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return background;
	}

	@Override
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return background.getWidth();
	}

	@Override
	public int getPicHeight() {
		// TODO Auto-generated method stub
		return background.getHeight();
	}

}
