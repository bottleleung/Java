package com.mypro.model;

import com.mypro.base.graphics.Bitmap;
/**
 * ∞Ÿ∑÷œ‘ æ
 * @author Leslie Leung
 *
 */
public class HighPoint extends DrawableAdapter{
	private Bitmap[] imgs;
	private int currentPicId;
	public HighPoint(Bitmap[] imgs){
		this.imgs = imgs;
	}
	public int getActPicLength(){
		return imgs.length;
	}
	public void setCurrentPicId(int currentPicId){
		this.currentPicId = currentPicId;
	}
	@Override
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return imgs[currentPicId];
	}

	@Override
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return getCurrentPic().getWidth();
	}

	@Override
	public int getPicHeight() {
		// TODO Auto-generated method stub
		return getCurrentPic().getHeight();
	}

}
