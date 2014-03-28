package com.mypro.model;

import com.mypro.base.graphics.Bitmap;

public class Ammo extends DrawableAdapter{
	private int ammoQuality;//子弹品质
	private FishingNet net;//当前子弹对应的渔网
	private Bitmap[] pic;
	//当前图片的索引
	private int currentId;
	public Ammo(int ammoQuality){
		this.ammoQuality = ammoQuality;
	}
	public void setCurrentPic(Bitmap[] pic,FishingNet net){
		this.net = net;
		this.pic = pic;
	}
	
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return pic[currentId];
	}
	
	public FishingNet getNet() {
		return net;
	}
	public int getAmmoPicLenght(){
		return pic.length;
	}
	/**
	 * 设置表示当前图片的索引值,这个值是图片数组的索引值
	 * @param currentId
	 */
	public void setCurrentId(int currentId){
		this.currentId = currentId;
	}
	
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return pic[currentId].getWidth();
	}
	public int getPicHeight() {
		// TODO Auto-generated method stub
		return pic[currentId].getHeight();
	}

	public int getAmmoQuality() {
		return ammoQuality;
	}

}
