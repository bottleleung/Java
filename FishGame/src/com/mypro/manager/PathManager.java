package com.mypro.manager;

import com.mypro.model.GamingInfo;

/**
 * 路径生成器
 * @author Leslie Leung
 *
 */
public class PathManager {
	public static int PATH_MODE_STRAIGHT = 1;	//行进模式:直走
	public static int PATH_MODE_ROTATE = 0;		//行进模式:转弯
	/**
	 * 获取一个默认路径，格式为{{移动方式,大小},...}
	 * 默认路径只有4个元素，如果行走完还没有到达屏幕边缘，则可以再调用这个方法
	 * @param maxRotate	最大旋转角度
	 * @return
	 */
	public static int[][] getDefaultPath(HeadFish fish){
		//如果鱼的最大旋转角度为0，只能走直线
		if(fish.getFish().getFishInfo().getMaxRotate()==0){
			return new int[][]{{PATH_MODE_STRAIGHT,GamingInfo.getGamingInfo().getScreenWidth()*2}};
		}
		
		int [][]path = new int[4][2];
		int i = 0;
		//如果鱼还没有从屏幕出来
		if(fish.getFish_X()+fish.getFish().getPicWidth()<=0||fish.getFish_X()>=GamingInfo.getGamingInfo().getScreenWidth()){	
			path[0][0] = PATH_MODE_STRAIGHT;
			path[0][1] = (int)(Math.random()*(GamingInfo.getGamingInfo().getScreenWidth())+1);
			i = 1;
		}
		for(;i<4;i++){
			if(Math.random()>0.5){
				if(i>0&&path[i-1][0]==PATH_MODE_STRAIGHT){
					//转弯
					path[i][0] = PATH_MODE_ROTATE;
					path[i][1] = (int)(45+Math.random()*(fish.getFish().getFishInfo().getMaxRotate()-45)+1);	//这里现在是最小45度，最大为鱼的最大旋转角度，但是后期考虑改成最大90度
				}else{
					//直走
					path[i][0] = PATH_MODE_STRAIGHT;
					path[i][1] = (int)(30+Math.random()*(GamingInfo.getGamingInfo().getScreenHeight()/2)+1);	//行进路线最大为屏幕高度的一半，其实不准确，不过也不用太深究这个值
				}
			}else{
				if(i>0&&path[i-1][0]==PATH_MODE_ROTATE){
					//直走
					path[i][0] = PATH_MODE_STRAIGHT;
					path[i][1] = (int)(30+Math.random()*(GamingInfo.getGamingInfo().getScreenHeight()/2)+1);	//行进路线最大为屏幕高度的一半，其实不准确，不过也不用太深究这个值
				}else{
					//转弯
					path[i][0] = PATH_MODE_ROTATE;
					path[i][1] = (int)(45+Math.random()*(fish.getFish().getFishInfo().getMaxRotate()-45)+1);
				}
			}
		}
		return path;
	}
//	/**
//	 * 获取一个路径，格式为{{移动方式,大小},...}
//	 * 这个方法主要是为以后特殊关卡预留，比如转圈游泳,此方法暂时不实现
//	 * @param Model	模式
//	 * @return
//	 */
//	public static int[][] getPath(int Model){
//		
//		return null;
//	}
	
	private PathManager(){}
}
