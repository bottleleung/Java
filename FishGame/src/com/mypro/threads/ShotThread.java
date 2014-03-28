package com.mypro.threads;

import java.util.ArrayList;

import com.mypro.base.graphics.Matrix;
import com.mypro.constant.Constant;
import com.mypro.manager.CatchFishManager;
//import com.mypro.manager.ParticleEffectManager;
import com.mypro.model.Ammo;
import com.mypro.model.GamingInfo;
import com.mypro.model.fish.Fish;
import com.mypro.tools.LogTools;
import com.mypro.tools.Tool;

public class ShotThread extends Thread {
	private float targetX;
	private float targetY;
	private float currentX;
	private float currentY;
	private float ammoRotateX;
	private float ammoRotateY;
	private float speed_x; 														// 取一个近似值，代表每帧移动的像素数
	private float speed_y;
	private int ammo_speed = 1000 / Constant.ON_DRAW_SLEEP; 					// 子弹绘制速度,这个与屏幕刷新速度一样
	private Ammo ammo;															//子弹
	private boolean ammoActIsRun;												//子弹动画是否播放

	public ShotThread(float targetX, float targetY, Ammo ammo,float fromX,float fromY) {
		this.ammo = ammo;
		currentX = fromX;
		currentY = fromY;
		ammoRotateX = ammo.getPicWidth()/2;
		ammoRotateY = ammo.getPicHeight()/2;
		this.targetX = targetX;
		this.targetY = targetY;
		float x = Math.abs(this.targetX - fromX); 				// 获取目标距离子弹始发的X坐标长度
		float y = Math.abs(this.targetY - fromY); 				// 获取目标距离子弹始发的Y坐标长度
		float len = (float) Math.sqrt(x * x + y * y); 							// 目标和始发点之间的距离
		float time = len / (Constant.AMMO_SPEED / Constant.ON_DRAW_SLEEP); 		// 计算目标与始发之间子弹需要行走的帧数
		speed_x = x / time; 													// 计算子弹沿X轴行进的增量
		speed_y = y / time; 													// 计算子弹沿Y轴行进的增量
		if (targetX < fromX) {
			speed_x = -speed_x;
		}
		if (targetY < fromY) {
			speed_y = -speed_y;
		}
	}

	public void run() {
		try{
			//如果子弹帧数多于1，就播放子弹动画
			if(ammo.getAmmoPicLenght()>1){
				new Thread(this.playAmmoAct()).start();
			}
			// 计算子弹需要的旋转角度
			float angle = Tool.getAngle(targetX, targetY, currentX, currentY);

			int ammoRedius = ammo.getPicHeight()/2;//这个半径的作用是用于计算子弹尾巴处出现粒子使用
//			effect.playEffect((float)(ammoRedius*Math.cos(Math.toRadians(angle+180)))+ammoRotateX,-(float)(ammoRedius*Math.sin(Math.toRadians(angle+180)))+ammoRotateY,currentX, currentY, speed_x, speed_y);
			// 计算子弹的旋转（原理与大炮一样）
			if (angle >= 90) {
				angle = -(angle - 90);
			} else {
				angle = 90 - angle;
			}
			// 创建变换矩阵
			Matrix matrix = ammo.getPicMatrix();
			matrix.setTranslate(currentX, currentY);
			matrix.preRotate(angle,ammoRotateX,ammoRotateY);
			GamingInfo.getGamingInfo().getSurface()
			.putDrawablePic(Constant.AMMO_LAYER, ammo); 				// 将子弹放入图层，等待被绘制
			// 根据增量移动子弹
			while (GamingInfo.getGamingInfo().isGaming()) {
				while(!GamingInfo.getGamingInfo().isPause()){
					matrix.reset();
					matrix.setTranslate(currentX, currentY);
					matrix.preRotate(angle,ammoRotateX,ammoRotateY);
					currentX += speed_x;
					currentY += speed_y;
//					effect.setEffectMatrix(currentX,currentY);
					if (checkHit()) {
//						effect.stopEffect();
						// 命中后删除这个子弹
						GamingInfo.getGamingInfo().getSurface()
								.removeDrawablePic(Constant.AMMO_LAYER, ammo);
						CatchFishManager.getCatchFishManager().catchFishByAmmo(currentX, currentY, ammo);
						// 如果超出屏幕，从图层中删除子弹
						GamingInfo.getGamingInfo().getSurface()
								.removeDrawablePic(Constant.AMMO_LAYER, ammo);
						this.ammoActIsRun = false;//停止子弹动画
						break;
					} else if (currentX - 100 >= GamingInfo.getGamingInfo().getScreenWidth()
							|| currentX + 100 <= 0 || currentY + 100 <= 0) {
						// 如果超出屏幕，从图层中删除子弹
//						effect.stopEffect();
						GamingInfo.getGamingInfo().getSurface()
								.removeDrawablePic(Constant.AMMO_LAYER, ammo);
						this.ammoActIsRun = false;//停止子弹动画
						break;
					}
					try {
						Thread.sleep(ammo_speed);
					} catch (Exception e) {
		
					}
				}
				break;
			}
		}catch(Exception e){
			LogTools.doLogForException(e);
		}
	}

	private Runnable playAmmoAct(){
		Runnable runnable = new Runnable(){			
			@Override
			public void run() {
				ammoActIsRun = true;
				int picIndex = 0;
				try {
					while(GamingInfo.getGamingInfo().isGaming()){
						while(!GamingInfo.getGamingInfo().isPause()&&ammoActIsRun){
							ammo.setCurrentId(picIndex);
							picIndex++;
							if(picIndex==ammo.getAmmoPicLenght()){
								picIndex=0;
							}
							Thread.sleep(200);
						}
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		};
		return runnable;
	}
	
	private boolean checkHit() {
		try{
			ArrayList<Fish> allFish = (ArrayList<Fish>)GamingInfo.getGamingInfo().getFish().clone();
			for (Fish fish : allFish) {
				if (currentX > fish.getFishOutlinePoint()[0]
						&& currentX < fish.getFishOutlinePoint()[1]
						&& currentY > fish.getFishOutlinePoint()[2]
						&& currentY < fish.getFishOutlinePoint()[3]) {
					return true;
				}
			}
		}catch(Exception e){
			LogTools.doLogForException(e);
		}
		return false;
	}
}
