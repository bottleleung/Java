package com.mypro.threads;

import com.mypro.constant.Constant;
import com.mypro.manager.HeadFish;
import com.mypro.manager.PathManager;
import com.mypro.model.GamingInfo;
import com.mypro.model.fish.Fish;
import com.mypro.tools.LogTools;

public class FishRunThread extends Thread {
	private boolean isRun; 												// 鱼是否可以游动
	private HeadFish fish; 												// 当前线程需要控制的领头鱼
	private boolean fishIsOut;											// 鱼是否处于屏幕外							
	private int fish_speed = 1000/Constant.ON_DRAW_SLEEP;				// 鱼绘制速度,这个与屏幕刷新速度一样
	private float fishrunSpeed;										// 鱼旋转长度
	public FishRunThread(HeadFish fish) {
		super();
		this.fish = fish;
		fishrunSpeed = (fish.getFish().getFishInfo().getFishRunSpeed() / Constant.ON_DRAW_SLEEP);//这里只是根据速度做了一个比例，让旋转速度看起来比较正常
	}

	public void run() {
		int[][] path = PathManager.getDefaultPath(fish);
		while (GamingInfo.getGamingInfo().isGaming()) {
			while(isRun &&!GamingInfo.getGamingInfo().isPause()){
				for (int[] pathMode : path) {
					if(fishIsOut||!isRun){
						break;
					}
					// 如果路径为旋转模式
					if (pathMode[0] == PathManager.PATH_MODE_ROTATE) {
						/**
						 * 这里做了一个处理，就是分析鱼头朝向和所在位置让鱼进行不同的旋转路线
						 */
						// 如果鱼处于第1或第2象限
						if (fish.getFish_X() <= GamingInfo.getGamingInfo().getScreenWidth() / 2
								&& fish.getFish_Y() <= GamingInfo.getGamingInfo().getScreenHeight() / 2
								|| fish.getFish_X() > GamingInfo.getGamingInfo().getScreenWidth() / 2
								&& fish.getFish_Y() <= GamingInfo.getGamingInfo().getScreenHeight() / 2) {
							// 如果鱼头是朝向x的负坐标方向
							if (fish.getCurrentRotate() >= 90
									&& fish.getCurrentRotate() <= 270
									|| fish.getCurrentRotate() <= -90
									&& fish.getCurrentRotate() >= -270) {
								rotateLeftFish(pathMode[1]);
							} else {
								rotateRightFish(pathMode[1]);
							}
							// 如果鱼处于第3或第4象限
						} else {
							// 如果鱼头是朝向x的负坐标方向
							if (fish.getCurrentRotate() >= 90
									&& fish.getCurrentRotate() <= 270
									|| fish.getCurrentRotate() <= -90
									&& fish.getCurrentRotate() >= -270) {
								rotateRightFish(pathMode[1]);
							} else {
								rotateLeftFish(pathMode[1]);
							}
						}
						// 如果路径为直行模式
					} else {
						goStraight(pathMode[1]);
					}
				}
				if(!fishIsOut){
				// 消费了所有的路径后，重新获取新路径
				path = PathManager.getDefaultPath(fish);
				}else{
					while(isRun && GamingInfo.getGamingInfo().isGaming()){
						//如果超出屏幕，走一个直线
						goStraight(100);
					}
					
				}
			}
			break;
		}
	}
	/**
	 * 移动鱼群
	 */
	private void moveShoal(){
		try{
			if(fish.getShoal()==null){
				return;
			}
			for(Fish fishFlag:fish.getShoal()){
				if(!fishFlag.isCanRun()||!fishFlag.isAlive()){
					continue;
				}
				fishFlag.getFishOutlinePoint()[0] = (int)(fish.getFishOutlinePoint()[0]-fishFlag.getDistanceHeadFishX());
				fishFlag.getFishOutlinePoint()[1] = (int)(fish.getFishOutlinePoint()[1]-fishFlag.getDistanceHeadFishX());
				fishFlag.getFishOutlinePoint()[2] = (int)(fish.getFishOutlinePoint()[2]-fishFlag.getDistanceHeadFishY());
				fishFlag.getFishOutlinePoint()[3] = (int)(fish.getFishOutlinePoint()[3]-fishFlag.getDistanceHeadFishY());
				
				fishFlag.getPicMatrix().setTranslate(fish.getFish_X()-fishFlag.getDistanceHeadFishX(), fish.getFish_Y()-fishFlag.getDistanceHeadFishY());
				fishFlag.getPicMatrix().preRotate(-fish.getCurrentRotate(),fishFlag.getFishRotatePoint_X(),fishFlag.getFishRotatePoint_Y());
			}
		}catch(Exception e){
			
		}
	}
	/**
	 * 根据给定长度走直线
	 * 
	 * @param len
	 * @return true:超出屏幕
	 */
	private void goStraight(int len) {
		// 如果是新出来的鱼，走直线，忽略旋转角度
		if (fish.isNew()) {
			fish.setNew(false);
			while (GamingInfo.getGamingInfo().isGaming()) {
				while(isRun &&!GamingInfo.getGamingInfo().isPause()){
					setFishOutlintPoint(0);
					if (fish.getCurrentFromPoint() <= 1) {
						fish.setFish_X(fish.getFish_X() + fishrunSpeed);
					} else {
						fish.setFish_X(fish.getFish_X() - fishrunSpeed);
					}
					moveShoal();
					// 是否走出了屏幕
					if (isAtOut()&&!fishIsOut) {
						setFishAtOut();
						break;
					}
					// 走够了长度，就停止循环
					if ((len-=fishrunSpeed) <= 0) {
						break;
					}
					try {
						Thread.sleep(fish_speed);
					} catch (Exception e) {
	
					}
				}
				break;
			}
		} else {
			fishRun(0,len);
		}
	}

	/**
	 * 将鱼向左旋转angle角度，并移动鱼
	 * 
	 * @param angle
	 */
	private void rotateLeftFish(int angle) {
		fishRun(1,angle);
	}

	/**
	 * 将鱼向右旋转angle角度，并移动鱼
	 * 
	 * @param angle
	 */
	private void rotateRightFish(int angle) {
		fishRun(-1,angle);
	}
	/**
	 * 鱼的行动方法
	 * @param mode	1:左转  -1：右转   0:直走
	 * @param len   移动长度
	 */
	private void fishRun(int mode,int len){
		int sumLen = 0;
		while (GamingInfo.getGamingInfo().isGaming()) {
			while(isRun &&!GamingInfo.getGamingInfo().isPause()){
				// 设置鱼的外接矩形
				setFishOutlintPoint(fish.getCurrentRotate());
				// 设置当前旋转角度
				fish.setCurrentRotate(fish.getCurrentRotate() + mode);
				// 根据增量变换坐标
				fish.setFish_X(fish.getFish_X()
						+ (float) (fishrunSpeed*Math.cos(Math.toRadians(fish.getCurrentRotate()))));
				fish.setFish_Y(fish.getFish_Y()
						+ (float) (fishrunSpeed*Math.sin((Math.toRadians(fish.getCurrentRotate())))*-1));
				moveShoal();
				// 是否走出了屏幕
				if (isAtOut()&&!fishIsOut) {
					setFishAtOut();
					break;
				}
				// 如果移动了总长度，停止循环
				if (++sumLen == len) {
					break;
				}
				try {
					Thread.sleep(fish_speed);
				} catch (Exception e) {
	
				}
			}
			break;
		}
	}
	

	/**
	 * 设置鱼的外接矩形X,Y的最大值和最小值
	 * 
	 * @param rotateAngle
	 *            计算是根据这个参数所旋转的角度来得到对应的X,Y值的
	 */
	private void setFishOutlintPoint(int rotateAngle) {
		fish.clearFishOutlinePoint();// 清空上一次的范围值
		int flagX = 0, flagY = 0;
		// 计算左上角坐标
		fish.getFishOutlinePoint()[0] = (int) getRotateX(0, 0,
				fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle)
				+ (int) fish.getFish_X();
		fish.getFishOutlinePoint()[1] = fish.getFishOutlinePoint()[0];
		fish.getFishOutlinePoint()[2] = (int) getRotateY(0, 0,
				fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle)
				+ (int) fish.getFish_Y();
		fish.getFishOutlinePoint()[3] = fish.getFishOutlinePoint()[2];
		// 计算左下角坐标
		flagX = (int) getRotateX(0, fish.getFish().getPicHeight(),
				fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle);
		flagY = (int) getRotateY(0, fish.getFish().getPicHeight(),
				fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle);
		putFishOutline((int) (flagX + fish.getFish_X()),
				(int) (flagY + fish.getFish_Y()));
		// 计算右上角坐标
		flagX = (int) getRotateX(fish.getFish().getPicWidth(), 0, fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle);
		flagY = (int) getRotateY(fish.getFish().getPicWidth(), 0, fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle);
		putFishOutline((int) (flagX + fish.getFish_X()),
				(int) (flagY + fish.getFish_Y()));
		// 计算右下角坐标
		flagX = (int) getRotateX(fish.getFish().getPicWidth(), fish.getFish().getPicHeight(),
				fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle);
		flagY = (int) getRotateY(fish.getFish().getPicWidth(), fish.getFish().getPicHeight(),
				fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle);
		putFishOutline((int) (flagX + fish.getFish_X()),
				(int) (flagY + fish.getFish_Y()));
	}

	// 只将四个范围内的最大值或最小值赋进去
	private void putFishOutline(int x, int y) {
		if (fish.getFishOutlinePoint()[1] < x) {
			fish.getFishOutlinePoint()[1] = x;
		} else if (fish.getFishOutlinePoint()[0] > x) {
			fish.getFishOutlinePoint()[0] = x;
		}
		if (fish.getFishOutlinePoint()[3] < y) {
			fish.getFishOutlinePoint()[3] = y;
		} else if (fish.getFishOutlinePoint()[2] > y) {
			fish.getFishOutlinePoint()[2] = y;
		}
	}

	/**
	 * 返回给定旋转角度以及旋转点和需要求旋转后点的旋转前X坐标
	 * 
	 * @param x
	 * @param y
	 * @param rotatePoint_x
	 * @param rotatePoint_y
	 * @param angle
	 * @return
	 */
	private float getRotateX(float x, float y, float rotatePoint_x,
			float rotatePoint_y, float angle) {
		return (float) ((x - rotatePoint_x) * Math.cos(angle)
				+ (y - rotatePoint_y) * Math.sin(angle) + rotatePoint_x);
	}

	/**
	 * 返回给定旋转角度以及旋转点和需要求旋转后点的旋转前Y坐标
	 * 
	 * @param x
	 * @param y
	 * @param rotatePoint_x
	 * @param rotatePoint_y
	 * @param angle
	 * @return
	 */
	private float getRotateY(float x, float y, float rotatePoint_x,
			float rotatePoint_y, float angle) {
		return (float) ((y - rotatePoint_y) * Math.cos(angle)
				- (x - rotatePoint_x) * Math.sin(angle) + rotatePoint_y);
	}

	/**
	 * 检查鱼是否已经处于屏幕外
	 * 
	 * @return true:处于屏幕外
	 */
	private boolean isAtOut() {
		// 如果鱼的外接矩形的上边缘大于屏幕高度或者下边缘小于0则认定鱼处于屏幕下或上边缘外
		if (fish.getFishOutlinePoint()[2] > GamingInfo.getGamingInfo().getScreenHeight()
				|| fish.getFishOutlinePoint()[3] < 0) {
			return true;

			// 如果鱼头是朝向x的负坐标方向
		} else if (fish.getCurrentRotate() >= 90
				&& fish.getCurrentRotate() <= 270
				|| fish.getCurrentRotate() <= -90
				&& fish.getCurrentRotate() >= -270) {
			// 如果鱼处于屏幕左边外
			if (fish.getFishOutlinePoint()[1] < 0) {
				return true;
			}
		} else {
			// 如果鱼处于屏幕右边外
			if (fish.getFishOutlinePoint()[0] > GamingInfo.getGamingInfo().getScreenWidth()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 处理鱼出了边界后的操作
	 */
	private void setFishAtOut() {
			fishIsOut = true;
			new Thread(new Runnable(){
				public void run() {
					try{
						// TODO Auto-generated method stub
						//如果领头鱼有鱼群
						for(Fish fishFlag:fish.getShoal()){
							while(GamingInfo.getGamingInfo().isGaming()){
								if(checkFishAtOut(fish,fishFlag)){
									GamingInfo.getGamingInfo().getFish().remove(fishFlag);
									GamingInfo.getGamingInfo().getSurface().removeDrawablePic(fishFlag.getFishInfo().getFishInLayer(), fishFlag);
									fishFlag.getPicActThread().stopPlay();//停止动作
									break;
								}
								try{
									Thread.sleep(10);
								}catch(Exception e){
									e.printStackTrace();
								}
							}
						}	
						//让鱼群移动线程停掉
						setRun(false);		
						//通知鱼群管理器，这条鱼已经离开屏幕
						if(GamingInfo.getGamingInfo().isGaming()){
							GamingInfo.getGamingInfo().getShoalManager().notifyFishIsOutOfScreen();
						}						
					}catch(Exception e){
						LogTools.doLogForException(e);
					}
				}
				
			}).start();			
	}
	/**
	 * 检查当前鱼是否已经超出屏幕
	 * @param fish
	 * @return		true:已经超出屏幕
	 */
	private boolean checkFishAtOut(HeadFish headFish,Fish fish){
		if(headFish.getFish_X()-fish.getDistanceHeadFishX()+fish.getPicWidth()<0||headFish.getFish_X()-fish.getDistanceHeadFishX()>GamingInfo.getGamingInfo().getScreenWidth()
				||headFish.getFish_Y()-fish.getDistanceHeadFishY()+fish.getPicHeight()<0||headFish.getFish_Y()-fish.getDistanceHeadFishY()>GamingInfo.getGamingInfo().getScreenHeight()){
			return true;
		}
		return false;
	}
	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
//		//如果设置的是false,表示即将停止该线程
//		if(!isRun){
//			//设置动作线程停止
//			this.fish.getPicActThread().stopPlay();
//		}
	}
}
