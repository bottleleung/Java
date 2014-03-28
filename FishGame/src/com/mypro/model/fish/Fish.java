package com.mypro.model.fish;



import com.mypro.base.graphics.Bitmap;
import com.mypro.base.tools.Log;
import com.mypro.manager.HeadFish;
import com.mypro.manager.ScoreManager;
import com.mypro.model.Ammo;
import com.mypro.model.DrawableAdapter;
import com.mypro.model.FishInfo;
import com.mypro.model.GamingInfo;
import com.mypro.threads.PicActThread;

public class Fish extends DrawableAdapter{
	/**
	 * 常量定义
	 */
	public static final int ROTATE_DIRECTION_LEFT = 1;	//左转
	public static final int ROTATE_DIRECTION_RIGHT = 2;	//右转
	/**
	 * 引用类型属性定义
	 */
	private FishInfo fishInfo;							//当前鱼的细节配置信息
	private Bitmap[] fishActs;							//当前鱼的所有动作
	private Bitmap[] fishCatchActs;						//当前鱼的所有被捕获动作
	private PicActThread picActThread; 					// 创建当前鱼的动作线程	
	/**
	 * 简单类型属性定义
	 */
	private int currentPicAct = 0;						//当前动作索引值	
	private int currentCatchPicAct = 0;					//当前被捕捉动作索引值	
	private boolean isAlive = true;						//当前鱼是否活着	
	private float distanceHeadFishX;					//距领头鱼X偏移量
	private float distanceHeadFishY;					//距领头鱼Y偏移量
	private HeadFish headFish;							//领头鱼
	private boolean canRun;						//鱼是否可以移动
	private int[] fishOutlinePoint = new int[4];		//鱼的外接矩形，x的最小值，最大值，Y的最小值，最大值
	public Fish(){
		
	}
	public Fish(Bitmap[] fishActs,Bitmap[] fishCatchActs,FishInfo fishInfo){
		this.fishActs = fishActs;
		this.fishCatchActs = fishCatchActs;
		this.fishInfo = fishInfo;
		this.getPicMatrix().setTranslate(GamingInfo.getGamingInfo().getScreenWidth(), GamingInfo.getGamingInfo().getScreenHeight());
	}
	
	//是否处于活动状态（在屏幕中游着）
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return isAlive;
	}
	//设置是否处于活动状态
	public void setAlive(boolean isAlive) {
		// TODO Auto-generated method stub
		this.isAlive = isAlive;
	}
	
	/**
	 * 鱼旋转点的X坐标
	 */
	public int getFishRotatePoint_X() {
		return getCurrentPic().getWidth()/2;
	}
	/**
	 * 鱼旋转点的Y坐标
	 */
	public int getFishRotatePoint_Y() {
		return getCurrentPic().getHeight()/2;
	}
	
	public PicActThread getPicActThread() {
		return picActThread;
	}

	public void setPicActThread(PicActThread picActThread) {
		this.picActThread = picActThread;
	}

	
	public float getDistanceHeadFishX() {
		return distanceHeadFishX;
	}
	public void setDistanceHeadFishX(float distanceHeadFishX) {
		this.distanceHeadFishX = distanceHeadFishX;
	}
	public float getDistanceHeadFishY() {
		return distanceHeadFishY;
	}
	public void setDistanceHeadFishY(float distanceHeadFishY) {
		this.distanceHeadFishY = distanceHeadFishY;
	}
	/**
	 * 获取所有的动作数量
	 * @return
	 */
	public int getFishActs() {
		// TODO Auto-generated method stub
		if(isAlive()){
			return fishActs.length;
		}else{
			return fishCatchActs.length;
		}
	}
	/**
	 * 设置当前动作图片的资源ID
	 * @param picId
	 */
	public void setCurrentPicId(int picId) {
		if(isAlive()){
			this.currentPicAct = picId;
		}else{
			this.currentCatchPicAct = picId;
		}
		
	}
	
	public int getCurrentPicId() {
		if(isAlive()){
			return currentPicAct;
		}else{
			return currentCatchPicAct;
		}
		
	}
	
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		if(isAlive()){
			return fishActs[currentPicAct];
		}else{
			return fishCatchActs[currentCatchPicAct];
		}
		
	}
	
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return getCurrentPic().getWidth();
	}

	public int getPicHeight() {
		// TODO Auto-generated method stub
		return getCurrentPic().getHeight();
	}

	/**
	 * 设置鱼的所有动作
	 * @param fishActs
	 */
	public void setFishActs(Bitmap[] fishActs) {
		// TODO Auto-generated method stub
		this.fishActs = fishActs;
	}
	/**
	 * 设置鱼的所有被捕获动作
	 * @param fishCatchActs
	 */
	public void setFishCatchActs(Bitmap[] fishCatchActs) {
		// TODO Auto-generated method stub
		this.fishCatchActs = fishCatchActs;
	}
	

	public FishInfo getFishInfo() {
		return fishInfo;
	}

	public void setFishInfo(FishInfo fishInfo) {
		this.fishInfo = fishInfo;
	}

	/**
	 * 根据当前鱼获取同类鱼实例
	 * @return
	 */
	public Fish getFish(){
		return new Fish(this.fishActs,this.fishCatchActs,this.fishInfo);
	}
	/**
	 * 触发捕捉事件的响应方法
	 */
	public void onCatch(Ammo ammo,final float targetX,final float targetY){		
//		System.out.println("鱼被捕捉了，但是没有捕捉到");
	}
	/**
	 * 触发已被捕捉事件的响应方法
	 * 当调用了这个方法，说明这条鱼已经被捕捉了
	 */
	public void onCatched(Ammo ammo,final float targetX,final float targetY){		
			this.setAlive(false);
			new Thread(new Runnable() {				
				@Override
				public void run() {
					try{		
					float fishX = getHeadFish().getFish_X()-getDistanceHeadFishX();
					float fishY = getHeadFish().getFish_Y()-getDistanceHeadFishY();
					GamingInfo.getGamingInfo().getFish().remove(Fish.this);
					Thread.sleep(1800);
					//调用增加分数方法
					ScoreManager.getScoreManager().addScore(getFishInfo().getWorth(), fishX, fishY);
					Thread.sleep(200);
					Fish.this.getPicActThread().stopPlay();
					GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Fish.this.getFishInfo().getFishInLayer(), Fish.this);		
					}catch(Exception e){
						Log.e("Fish_onCatched", e.toString());
					}
				}
			}).start();		
	}
	public HeadFish getHeadFish() {
		return headFish;
	}
	public void setHeadFish(HeadFish headFish) {
		this.headFish = headFish;
	}
	public int[] getFishOutlinePoint() {
		return fishOutlinePoint;
	}
	public boolean isCanRun() {
		return canRun;
	}
	public void setCanRun(boolean canRun) {
		this.canRun = canRun;
	}
	
}
