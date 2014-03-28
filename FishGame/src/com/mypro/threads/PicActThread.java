package com.mypro.threads;

import com.mypro.model.GamingInfo;
import com.mypro.model.fish.Fish;
import com.mypro.tools.LogTools;
/**
 * 控制鱼的动作的线程
 * @author Leslie Leung
 *
 */
public class PicActThread extends Thread{
	private Fish fish;							//被控制的鱼
	private boolean notPause = true;			//是否需要暂停动作
	private boolean isAct = true;				//需要动作
	int len;									//获取鱼的所有动作
	private boolean isPause = false;			//线程是否已经暂停
	public PicActThread(Fish fish){
		this.fish = fish;
		len = fish.getFishActs();
		isPause = false;
	}
	
	public void run(){
		try{
			while(GamingInfo.getGamingInfo().isGaming()){
				while(!GamingInfo.getGamingInfo().isPause()&&isAct){
					while(notPause){							
						if(fish.getCurrentPicId()+1>=fish.getFishActs()){
							//循环放置一个动作给鱼的当前动作
							fish.setCurrentPicId(0);
						}else{
							//循环放置一个动作给鱼的当前动作
							fish.setCurrentPicId(fish.getCurrentPicId()+1);
						}
						
						Thread.sleep(fish.getFishInfo().getPicActSpeed());
					}
					isPause = true;
				}
				break;
		}
		}catch(Exception e){
			LogTools.doLogForException(e);
		}
	}
	/**
	 * 重置
	 * @param mode
	 */
	public void reset(){
		len = fish.getFishActs();
		notPause = true;
		isAct = true;
	}
	/**
	 * 播放
	 */
	public void play(){
		notPause = true;
		isPause = false;
	}
	/**
	 * 暂停播放
	 */
	public void pausePlay(){
		notPause = false;
		while(!isPause){
		}
	}
	/**
	 * 停止播放
	 */
	public void stopPlay(){
		notPause = false;
		isAct = false;
	}
	
}
