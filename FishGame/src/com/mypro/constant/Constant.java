package com.mypro.constant;

public class Constant {
	public static int PIC_WIDTH = 64;
	public static int PIC_HEIGHT = 64;
	public static int ON_DRAW_SLEEP = 40;				//每秒钟的帧数
	public static float PIC_X_SPEED = 1.5f;
	public static float PIC_Y_SPEED = 2;
	public static int PIC_RUN_SPEED = 1000/Constant.ON_DRAW_SLEEP;
//	public static int  SCALE_BASE_NUM = 850;			//缩放基数
	
	//在屏幕中的鱼群总量
	public static final int FISH_SUM_IN_SCREEN = 7; 	
	
	// 鱼的起始位置定义
	public static final int FROM_TOP_LEFT = 0; 			// 左上
	public static final int FROM_BOTTOM_LEFT = 1; 		// 左下
	public static final int FROM_TOP_RIGHT = 2; 		// 右上
	public static final int FROM_BOTTOM_RIGHT = 3; 		// 右下
	
	
	public static double RAD	= Math.PI / 180;		//弧度计算公式

	
	public static int FISHING_NET_SHOW_TINE = 1500;		//渔网出现的毫秒数
	
	//定义子弹移动速度
	public static int AMMO_SPEED = 200;					//每秒走200像素

	//定义金币移动速度
	public static int Gold_SPEED = 200;					//每秒走150像素
	
	//发射炮弹间隔
	public static int CANNON_RELOAD_TIME = 200;			//50毫秒
	
	//自动补钱间隔
	public static int GIVE_GOLD_TIME = 180;				//180秒
	//补金币的底线，只有当前金币数小于这个数，才补金币
	public static int GIVE_GOLD_LESS = 100;				//少于100金币，才补金币
	//补充金币数
	public static int GIVE_GOLD = 100;				   //补充100金币
	
	/****************************粒子*****************************/
	//粒子颜色数
	public static int PARTICLE_COLOR_SUM = 10;
	//粒子数量
	public static int PARTICLE_SUM = 10;
	//子弹粒子数量
	public static int AMMO_PARTICLE_SUM = 10;
	//粒子移动频率
	public static long PARTICLE_RUN = 30;
	/*******************其他组件所在图层相关定义*********************/
	//水波纹图层
	public static int HUNDRED_WATER_RIPPLE_LAYER = 103;
	//百分图层
	public static int HUNDRED_POINT_LAYER = HUNDRED_WATER_RIPPLE_LAYER-1;
	//高分图层
	public static int HIGH_POINT_LAYER = HUNDRED_POINT_LAYER-1;
	//更换大炮效果所在图层
	public static int CHANGE_CANNON_EFFECT_LAYER = HIGH_POINT_LAYER-1;
	//大炮所在图层
	public static int GUN_LAYER = CHANGE_CANNON_EFFECT_LAYER-1;
	//粒子图层
	public static int PARTICLE_EFFECT_LAYER = GUN_LAYER-1;
	//渔网所在图层
	public static int FISH_NET_LAYER = PARTICLE_EFFECT_LAYER-1;			
	//子弹所在图层
	public static int AMMO_LAYER = FISH_NET_LAYER-1;
	//场景中基本组件所在图层
	public static int COMPONENTS_LAYER = AMMO_LAYER-1;
	//金币图层
	public static int GOLD_LAYER = COMPONENTS_LAYER-1;
	//背景图层
	public static int BACK_GROUND_LAYER = 0;
	//最高分保存地址
	public static String HIGHEST_SCORE_PATH = "score/highest.txt";


}
