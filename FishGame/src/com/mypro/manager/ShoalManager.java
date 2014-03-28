package com.mypro.manager;

import com.mypro.base.graphics.Matrix;
import com.mypro.base.tools.Log;
import com.mypro.constant.Constant;
import com.mypro.model.GamingInfo;
import com.mypro.model.fish.Fish;
import com.mypro.threads.FishRunThread;
import com.mypro.threads.PicActThread;
import com.mypro.tools.LogTools;

/**
 * 鱼群管理器
 * @author Leslie Leung
 *
 */
public class ShoalManager{
	private String[] fish;							//可生成的鱼群
	private int[] showProbability;					//鱼的生成概率
	private boolean createable;						//是否可生成鱼群

	public ShoalManager(){
	}
	/**
	 * 启动鱼群管理器
	 * @param gamePartInfo
	 */
	public void start(final GamePartInfo gamePartInfo){
		this.fish = gamePartInfo.getFishName();							//设置可以创建的鱼群
		this.showProbability = gamePartInfo.getShowProbability();
		createable = true;												//设置可以创建鱼群
		new Thread(new Runnable(){
			public void run(){
				for(int i = 0;i<gamePartInfo.getShoalSumInScreen()&&GamingInfo.getGamingInfo().isGaming();i++){
					createShoal();
					try {
						Thread.sleep((long)(Math.random()*100*gamePartInfo.getShoalSumInScreen()));
					} catch (Exception e) {
						LogTools.doLogForException(e);
					}
				}
			}
		}).start();	
	}
	/**
	 * 创建一个鱼群
	 */
	public void createShoal(){
		try {
			if(!createable){
				Log.w("ShoalManager", "不允许创建新鱼群");
				return;
			}
			HeadFish fish = birthHeadFish();
			createShoal(fish);
			fishRun(fish);
			startFishAct(fish.getFish());
			setRandomShoalPositionByHeadFish(fish);
		} catch (Exception e) {
			LogTools.doLogForException(e);
		}
		
	}
	
	
	
	/**
	 * 生成一条领头鱼
	 */
	private HeadFish birthHeadFish(){
		//创建一个领头鱼
		HeadFish headFish = new HeadFish();
		Fish fish = null;
		int probability = (int)(Math.random()*100+1);		//1~100
		int sumProbability = 0;
		if(probability<=showProbability[0]){
			fish = FishManager.getFishMananger().birthFishByFishName(this.fish[0]);
		}else{
			sumProbability = showProbability[0];
			for(int index=1;index<this.showProbability.length;index++){
				sumProbability += this.showProbability[index];
				if(probability<=sumProbability){
					fish = FishManager.getFishMananger().birthFishByFishName(this.fish[index]);
					break;
				}
			}
		}		 

		fish.setCanRun(true);
		GamingInfo.getGamingInfo().getFish().add(fish);
		headFish.setFish(fish);
		fish.setHeadFish(headFish);
		//创建了一条领头鱼后，将方向设置到类属性currentFromPoint上，供鱼群使用
		headFish.setCurrentFromPoint(getFromPoint(headFish));		
		
		return headFish;
	}
	/**
	 * 让鱼开始游动
	 * @param fish			需要游动的鱼
	 */
	private void fishRun(HeadFish fish){
		FishRunThread prt = new FishRunThread(fish);	// 创建当前鱼的移动线程	
		fish.setFishRunThread(prt);
		prt.setRun(true);	//设置鱼开始移动	
		prt.start();
	}
	/**
	 * 开始播放鱼的动作
	 * @param fish			需要游动的鱼
	 */
	private void startFishAct(Fish fish){
		//将鱼群的鱼放入图层
		GamingInfo.getGamingInfo().getSurface().putDrawablePic(fish.getFishInfo().getFishInLayer(), fish);
		PicActThread pat = new PicActThread(fish); 				// 创建当前鱼的动作线程		
		fish.setPicActThread(pat);
		pat.start();// 鱼的动作控制线程启动
	}
	/**
	 * 生成鱼群 
	 * @param fish	领头鱼
	 */
	private void createShoal(HeadFish fish){
		try{
			if(GamingInfo.getGamingInfo().isGaming()&&fish.getFish().getFishInfo().getFishShoalMax()!=0){
				Fish flagFish;					//代表鱼群中的鱼的临时变量
				int sum = (int)(Math.random()*fish.getFish().getFishInfo().getFishShoalMax()+1);
				for(int i = 0;i<sum;i++){
					flagFish = fish.getFish().getFish();
					flagFish.setHeadFish(fish);
					fish.getShoal().add(flagFish);
					GamingInfo.getGamingInfo().getFish().add(flagFish);
				}	
			}	
		}catch(Exception e){
			LogTools.doLogForException(e);
		}
	}
	/**
	 * 根据领头鱼的位置设置随机位置,并将随机位置的偏移量赋值给给定鱼群
	 * @param headFish			领头鱼
	 */
	private void setRandomShoalPositionByHeadFish(final HeadFish headFish){
		try{
		new Thread(new Runnable(){			
			public void run() {
				try{
				float fromY = headFish.getFish_Y();
				int shoalIndex = 1;
				int sumAtOut = headFish.getShoal().size();//鱼群中还有多少条鱼在屏幕外
				Fish fishFlag;			
				float randomMinY;
				float randomMaxY;
				float flagFishX,flagFishY;//生成的鱼的坐标
				Fish startFish = headFish.getFish();
				
				// TODO Auto-generated method stub
				while(GamingInfo.getGamingInfo().isGaming()){
					while(!GamingInfo.getGamingInfo().isPause()&&sumAtOut>1){
						fishFlag = headFish.getShoal().get(shoalIndex);
						
						randomMinY = (float)(fromY-fishFlag.getPicHeight());
						randomMaxY = (float)(fromY+fishFlag.getPicHeight());
						
						if(headFish.getCurrentFromPoint()<=Constant.FROM_BOTTOM_LEFT){						
							flagFishX = 0-headFish.getFish().getPicWidth();
							flagFishY = (float)(randomMinY+Math.random()*(randomMaxY-randomMinY));
							
						}else{
							flagFishX = GamingInfo.getGamingInfo().getScreenWidth();
							flagFishY = (float)(randomMinY+Math.random()*(randomMaxY-randomMinY));						
							
						}
						
						while(!canRun(startFish,flagFishX,flagFishY,headFish)&&GamingInfo.getGamingInfo().isGaming()){
							try{
								Thread.sleep(50);
							}catch(Exception e){
								
							}
						}
						
						fishFlag.setDistanceHeadFishX(headFish.getFish_X()-flagFishX);
						fishFlag.setDistanceHeadFishY(headFish.getFish_Y()-flagFishY);
						fishFlag.setCanRun(true);
						startFishAct(fishFlag);
						startFish = fishFlag;					
						shoalIndex++;
						sumAtOut--;		
						try{
							Thread.sleep(200);
						}catch(Exception e){
							
						}
					}
					break;
				}
			}catch(Exception e){}}}).start();
		}catch(Exception e){}
	}
	/**
	 * 判断鱼是否可以移动了
	 * 这里的作用是根据参照与的位置，来决定准备移动的鱼是否可以移动了
	 * @param firstFish		参照鱼
	 * @param fishFlag		准备移动的鱼
	 * @param headFish		领头鱼
	 * @return
	 */
	private boolean canRun(Fish firstFish,float fishX,float fishY,HeadFish headFish){
		//如果起始边在屏幕左边
		if(headFish.getCurrentFromPoint()<=Constant.FROM_BOTTOM_LEFT){						
			if(fishY+firstFish.getPicHeight()<headFish.getFish_Y()-firstFish.getDistanceHeadFishY()
					||fishY>headFish.getFish_Y()-firstFish.getDistanceHeadFishY()+firstFish.getPicHeight()
					||headFish.getFish_X()-firstFish.getDistanceHeadFishX()>0){
				return true;
			}
		}else{				
			if(fishY+firstFish.getPicHeight()<headFish.getFish_Y()-firstFish.getDistanceHeadFishY()
					||fishY>headFish.getFish_Y()-firstFish.getDistanceHeadFishY()+firstFish.getPicHeight()
					||headFish.getFish_X()-firstFish.getDistanceHeadFishX()+firstFish.getPicWidth()<GamingInfo.getGamingInfo().getScreenWidth()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 通知鱼群管理器，领头鱼已经离开屏幕
	 */
	public void notifyFishIsOutOfScreen(){
		try{			
			createShoal();	
		}catch(Exception e){
			LogTools.doLogForException(e);
		}
	}
	
	/**
	 * 设置鱼的起始坐标 将屏幕分成了四个方位
	 */
	private int getFromPoint(HeadFish fish) {
		int mode = (int) (Math.random() * 4);
		Matrix matrix = fish.getFish().getPicMatrix();
		switch (mode) {
		case Constant.FROM_TOP_LEFT:
			// 左上
			fish.setFish_X(-(float) fish.getFish().getPicWidth());
			fish.setFish_Y((float) Math.random()
					* (GamingInfo.getGamingInfo().getScreenHeight() / 2 - fish.getFish().getPicHeight() + 1));
			matrix.setTranslate(fish.getFish_X(), fish.getFish_Y());		
			fish.setLastX(1);			
			return Constant.FROM_TOP_LEFT;
		case Constant.FROM_BOTTOM_LEFT:
			// 左下
			fish.setFish_X(-(float) fish.getFish().getPicWidth());
			fish.setFish_Y((float) (GamingInfo.getGamingInfo().getScreenHeight() / 2 + Math.random()
					* (GamingInfo.getGamingInfo().getScreenHeight() - fish.getFish().getPicHeight() + 1 - GamingInfo.getGamingInfo().getScreenHeight() / 2)));
			matrix.setTranslate(fish.getFish_X(), fish.getFish_Y());
			fish.setLastX(1);
			return Constant.FROM_BOTTOM_LEFT;
		case Constant.FROM_TOP_RIGHT:
			// 右上
			fish.setFish_X((float) GamingInfo.getGamingInfo().getScreenWidth());
			fish.setFish_Y((float) Math.random()
					* (GamingInfo.getGamingInfo().getScreenHeight() / 2 - fish.getFish().getPicHeight() + 1));
			matrix.setTranslate(fish.getFish_X(), fish.getFish_Y());
			matrix.preRotate(180, fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y());
			fish.setCurrentRotate(180);
			fish.setLastX(-1);
			return Constant.FROM_TOP_RIGHT;
		case Constant.FROM_BOTTOM_RIGHT:
			// 右下
			fish.setFish_X((float) GamingInfo.getGamingInfo().getScreenWidth());
			fish.setFish_Y((float) (GamingInfo.getGamingInfo().getScreenHeight() / 2 + Math.random()
					* (GamingInfo.getGamingInfo().getScreenHeight() - fish.getFish().getPicHeight() + 1 - GamingInfo.getGamingInfo().getScreenHeight() / 2)));			
			matrix.setTranslate(fish.getFish_X(), fish.getFish_Y());
			matrix.preRotate(180, fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y());
			fish.setCurrentRotate(180);
			fish.setLastX(-1);
			return Constant.FROM_BOTTOM_RIGHT;
		}
		return 0;
	}
	
	
	/**
	 * 更新鱼群
	 * @param fish
	 */
	public void stop(){
		this.createable = false;
	}
	
}
