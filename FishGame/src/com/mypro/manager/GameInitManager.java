package com.mypro.manager;

import com.mypro.constant.Constant;
import com.mypro.model.GamingInfo;

import com.mypro.tools.LogTools;


/**
 * 游戏初始化管理器
 * @author Leslie Leung
 *
 */
public class GameInitManager {
	private static GameInitManager manager;
	private boolean initing = true;
	/**
	 * 是否正在初始化
	 * @return
	 */
	public boolean isIniting(){
		return initing;
	}
	private GameInitManager(){}
	public static GameInitManager getGameInitManager(){
		if(manager == null){
			manager = new GameInitManager();
		}
		return manager;
	}
	public void init(){
		ImageManager.getImageMnagaer().initManager();

		initGame();//初始化游戏
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		beginGame();
		initing = false;
	}
	/**
	 * 初始化所有组件
	 */
	private void initComponents(){
		LayoutManager.getLayoutManager().init();
	}

	/**
	 * 初始化游戏
	 */
	private void initGame(){
		//初始化界面组件
		this.initComponents();
		//初始化得分管理器
		ScoreManager.getScoreManager().init();

		//初始化大炮管理器
		CannonManager.getCannonManager().init();

		//初始化鱼管理器
		FishManager.getFishMananger().initFish();

		//初始化鱼群管理器
		GamingInfo.getGamingInfo().setShoalManager(new ShoalManager());

		//初始化关卡管理器
		GamePartManager.getManager().prepare();

		//初始化大炮
		CannonManager.getCannonManager().initCannon();

	}

	/**
	 * 停止游戏
	 */
	public void stop(){

		try {
			//设置游戏结束
			GamingInfo.getGamingInfo().setGaming(false);
			Thread.sleep(1000);

			//注销鱼管理器
			FishManager.destroy();
			//注销游戏关卡管理器
			GamePartManager.getManager().destroy();

			//注销自己
			manager = null;
		} catch (Exception e) {
			LogTools.doLogForException(e);
		}
	}

	/**
	 * 开始游戏
	 */
	private void beginGame(){
		//开始
		GamePartManager.getManager().start();
	}

}
