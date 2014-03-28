package com.mypro.manager;

import java.util.ArrayList;

import com.mypro.model.fish.Fish;
import com.mypro.threads.FishRunThread;

/**
 * 领头鱼
 * 这个类不是实质的鱼，代表一个点，带领所有鱼群移动
 * @author Leslie Leung
 *
 */
public class HeadFish {
	private int[] fishOutlinePoint = new int[4];		//鱼的外接矩形，x的最小值，最大值，Y的最小值，最大值
	//控制鱼移动的线程
	private FishRunThread fishRunThread;		
	private boolean isNew = true;						//是否刚生成的鱼	这个参数决定着进入屏幕时候的路线
	private float fish_x;								//鱼当前的X坐标
	private float fish_y;								//鱼当前的Y坐标
	private int currentRotate;							//鱼当前已旋转的角度
	private float lastX;								//最后一次旋转后的X增量	这组XY的作用是旋转后若走直线，就以这两个值↙
	private float lastY;								//最后一次旋转后的Y增量	递增就可以了
	private int rotateDirection;						//左转还是右转	这个值的用途在于，鱼在旋转后走直线时，要计算最后一次旋转后的增量，而这个记录了上次是左转还是右转用于计算角度得知直线时的增量
	//当前鱼群的鱼，鱼群的鱼都已它为参照，同样这个鱼也在鱼群集合里
	private Fish fish;
	//鱼群
	private ArrayList<Fish> shoal = new ArrayList<Fish>();
	//当前创建的领头鱼的起始位置
	private int currentFromPoint;
	public ArrayList<Fish> getShoal() {
		return shoal;
	}
	public void setShoal(ArrayList<Fish> shoal) {
		this.shoal = shoal;
	}
	public int getCurrentFromPoint() {
		return currentFromPoint;
	}
	public void setCurrentFromPoint(int currentFromPoint) {
		this.currentFromPoint = currentFromPoint;
	}
	public Fish getFish() {
		return fish;
	}
	public void setFish(Fish fish) {
		this.fish = fish;
		this.shoal.add(fish);
	}
	/*
	 * 鱼的X,Y坐标的getter and setter
	 * 
	 * */
	public void setFish_X(float x) {
		// TODO Auto-generated method stub
		this.setLastX(this.fish_x);
		this.fish_x = x;
	}
	public void setFish_Y(float y) {
		// TODO Auto-generated method stub
		this.setLastY(this.fish_y);
		this.fish_y = y;
	}
	public float getFish_X() {
		// TODO Auto-generated method stub
		return this.fish_x;
	}
	public float getFish_Y() {
		// TODO Auto-generated method stub
		return this.fish_y;
	}
	public float getLastX() {
		return lastX;
	}
	public void setLastX(float lastX) {
		this.lastX = lastX;
	}
	public float getLastY() {
		return lastY;
	}
	public void setLastY(float lastY) {
		this.lastY = lastY;
	}
	public int getCurrentRotate() {
		return currentRotate;
	}
	public void setCurrentRotate(int currentRotate) {
		if(currentRotate>=360||currentRotate<=-360){
			this.currentRotate = 0;
		}else{
			this.currentRotate = currentRotate;
		}	
	}
	public FishRunThread getFishRunThread() {
		return fishRunThread;
	}
	public void setFishRunThread(FishRunThread fishRunThread) {
		this.fishRunThread = fishRunThread;
	}
	public int getRotateDirection() {
		return rotateDirection;
	}
	public void setRotateDirection(int rotateDirection) {
		this.rotateDirection = rotateDirection;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public void clearFishOutlinePoint(){
		fishOutlinePoint[0] = 0;
		fishOutlinePoint[1] = 0;
		fishOutlinePoint[2] = 0;
		fishOutlinePoint[3] = 0;
	}
	public int[] getFishOutlinePoint() {
		return fishOutlinePoint;
	}
//	public void setFishOutlinePoint(int[] fishOutlinePoint) {
//		this.fishOutlinePoint = fishOutlinePoint;
//	}
}
