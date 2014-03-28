package com.mypro.manager;

import java.util.ArrayList;

import com.mypro.model.Ammo;
import com.mypro.model.GamingInfo;
import com.mypro.model.fish.Fish;
import com.mypro.tools.CircleRectangleIntersect;
import com.mypro.tools.LogTools;

/**
 * 捕捉管理器
 * @author Leslie Leung
 *
 */
public class CatchFishManager {
	private static CatchFishManager manager;
	private CatchFishManager(){
		
	}
	public static CatchFishManager getCatchFishManager(){
		if(manager==null){
			manager = new CatchFishManager();
		}
		return manager;
	}
	/**
	 * 根据子弹以及碰撞点张网捕捉
	 * @param netX
	 * @param netY
	 * @param ammo
	 */
	public void catchFishByAmmo(final float netX,final float netY,final Ammo ammo){
		new Thread(new Runnable() {
			@Override
			public void run() {
				showNet(netX,netY,ammo);
				//调用捕捉检测方法
				catchFish(netX,netY,ammo);
			}
		}).start();
	}
	/**
	 * 显示渔网
	 * @param netX
	 * @param netY
	 * @param ammo
	 */
	private void showNet(final float netX,final float netY,final Ammo ammo){
		new Thread(new Runnable() {				
			@Override
			public void run() {
				try {
//					SoundManager.playSound(SoundManager.SOUND_BGM_NET);//播放张网音效
					ammo.getNet().playNetAct(netX, netY);//显示渔网
				} catch (Exception e) {
					LogTools.doLogForException(e);
				}
			}
		}).start();			
		
	}
	/**
	 * 捕捉检测方法，这个方法检测渔网与屏幕中的鱼是否有交集，然后通知所有有交集的鱼的捕捉方法
	 * @param netX
	 * @param netY
	 * @param ammo
	 */
	private synchronized void catchFish(float netX,float netY,Ammo ammo){
		ArrayList<Fish> allFish = (ArrayList<Fish>)GamingInfo.getGamingInfo().getFish().clone();
		for(Fish fish:allFish){
			if(!fish.isAlive()){
				continue;
			}
			if(CircleRectangleIntersect.isIntersect(netX, netY, fish.getHeadFish().getFish_X()-fish.getDistanceHeadFishX()+fish.getPicWidth()/2, fish.getHeadFish().getFish_Y()-fish.getDistanceHeadFishY()+fish.getPicHeight()/2, fish.getPicHeight(), fish.getPicWidth(), ammo.getNet().getPicWidth()/2)){
				if(checkCatch(ammo,fish)){
					//调用鱼已经被捕捉成功的方法
					fish.onCatched(ammo,netX,netY);
				}else{
					//调用鱼没有捕捉成功的捕捉方法
					fish.onCatch(ammo,netX,netY);
				}				
				//如果都被捕捉，停止移动线程
				if(checkAllCatch(fish)){
					fish.getHeadFish().getFishRunThread().setRun(false);
					//通知鱼群管理器，这条鱼已经离开屏幕
					GamingInfo.getGamingInfo().getShoalManager().notifyFishIsOutOfScreen();
				}
			}
		}
	}
	/**
	 * 检测鱼是否被捕捉成功
	 * @param ammo		对应的子弹
	 * @param fish		被捕捉的鱼
	 * @return			true:被捕捉成功		false:没有捕捉成功
	 */
	private boolean checkCatch(Ammo ammo,Fish fish){
		double probability = ammo.getAmmoQuality()*10+fish.getFishInfo().getCatchProbability();
		if(Math.random()*1000+1<=probability){
			return true;
		}
		return false;
	}
	/**
	 * 判断当前鱼所在的鱼群是否都已经被捕获了
	 * @param fish
	 * @return
	 */
	private synchronized boolean checkAllCatch(Fish fish){
		for(Fish f:fish.getHeadFish().getShoal()){
			if(f.isAlive()){
				return false;
			}
		}
		return true;
	}
}
