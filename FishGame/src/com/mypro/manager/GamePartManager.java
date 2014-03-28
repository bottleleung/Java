package com.mypro.manager;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import com.mypro.base.graphics.Bitmap;
import com.mypro.base.tools.Log;
import com.mypro.constant.Constant;
import com.mypro.model.BackGround;
import com.mypro.model.GamingInfo;
import com.mypro.model.componets.BottomTime;
import com.mypro.tools.LogTools;

/**
 * 游戏关卡管理器
 * @author Leslie Leung
 *
 */
public class GamePartManager {
	/**
	 * 单例模式使用
	 */
	private	static GamePartManager manager;
	/**
	 * 管理的所有关卡
	 * key		为关卡名
	 * value	为关卡描述
	 */
	private ArrayList<GamePartInfo> games = new ArrayList<GamePartInfo>();
	/**
	 * 当前进行的关卡
	 */
	private GamePartInfo part;
	/**
	 * 当前进行的关卡的背景图片
	 */
	private BackGround background;
	/**
	 * 是否准备完毕
	 */
	private boolean prepared;
	/**
	 * 构造器
	 */
	private GamePartManager(){
		try {
			XmlPullParser xml = XmlManager.getXmlParser("config/GamePart", "UTF-8");
			initGamePart(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 准备
	 */
	public void prepare(){
		try{
			//设置背景
			setBg();
			//更新鱼管理
			FishManager.getFishMananger().updateFish(this.part.getFishName());
			prepared = true;
		}catch(Exception e){
			LogTools.doLogForException(e);
		}
		
	}
	/**
	 * 启动关卡管理器
	 */
	public void start(){
		if(!prepared){
			Log.e("GamePartManager", "管理器没有准备，是否调用过prepare方法？");
			return;
		}

		//设置鱼群管理器，通知可生成的鱼群种类
		GamingInfo.getGamingInfo().getShoalManager().start(this.part);	
		//启动给金币线程
		startGiveGoldThrad();
	}
	/**
	 * 启动定时给金币线程
	 */
	private void startGiveGoldThrad(){
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
					int time = Constant.GIVE_GOLD_TIME;
					BottomTime bt = LayoutManager.getLayoutManager().getBottomTime();
					while(GamingInfo.getGamingInfo().isGaming()){
						while(!GamingInfo.getGamingInfo().isPause()){
							if(time==0){
								giveGold();
								time = Constant.GIVE_GOLD_TIME;
							}
							bt.updateNumIndex(time);
							time--;
							Thread.sleep(1000);
						}
						break;
					}					
				} catch (Exception e) {
					LogTools.doLogForException(e);
				}
				
			}
			private void giveGold(){
				if(GamingInfo.getGamingInfo().getScore()<Constant.GIVE_GOLD_LESS){
					GamingInfo.getGamingInfo().setScore(Constant.GIVE_GOLD);
				}				
			}
		}).start();
	}
	/**
	 * 设置背景
	 */
	private void setBg(){
		try {
			if(background==null){
				background = new BackGround();
				try {
					background.setCurrentPic(ImageManager.getImageMnagaer().sacleImageByWidthAndHeight(ImageManager.getImageMnagaer().getBitmapByAssets(this.part.getBackground()), GamingInfo.getGamingInfo().getScreenWidth(), GamingInfo.getGamingInfo().getScreenHeight()));				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LogTools.doLogForException(e);
				}
				GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.BACK_GROUND_LAYER, background);
			}else{
				try {
					background.setCurrentPic(Bitmap.createScaledBitmap(ImageManager.getImageMnagaer().getBitmapByAssets(this.part.getBackground()), GamingInfo.getGamingInfo().getScreenWidth(), GamingInfo.getGamingInfo().getScreenHeight(), false));				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LogTools.doLogForException(e);
				}
			}
		} catch (Exception e) {
			LogTools.doLogForException(e);
		}
		
	}
	
	/**
	 * 初始化所有的关卡
	 * @param xml	需要解析的xml文件
	 */
	private void initGamePart(XmlPullParser xml){
		//循环所有的关卡
		while(GamingInfo.getGamingInfo().isGaming()&&XmlManager.gotoTagByTagName(xml, "key")){
			//创建关卡描述类
			GamePartInfo gamePartInfo = new GamePartInfo();
			//获取关卡名称
			XmlManager.gotoTagByTagName(xml, "string");
			gamePartInfo.setPartName(XmlManager.getValueByCurrentTag(xml));		
			//获取关卡出现的鱼
			XmlManager.gotoTagByTagName(xml, "string");
			gamePartInfo.setFishName(XmlManager.getValueByCurrentTag(xml).split(";"));
			//鱼的出现概率
			XmlManager.gotoTagByTagName(xml, "string");
			String probability[] = XmlManager.getValueByCurrentTag(xml).split(";");
			int[] showProbability = new int[probability.length];
			for(int i = 0;i<probability.length;i++){
				showProbability[i] = Integer.parseInt(probability[i]);
			}
			gamePartInfo.setShowProbability(showProbability);
			//获取可出现的鱼群总数
			XmlManager.gotoTagByTagName(xml, "integer");
			gamePartInfo.setShoalSumInScreen(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
			//获取关卡时间
			XmlManager.gotoTagByTagName(xml, "integer");
			gamePartInfo.setPartTime(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
			//获取下一关的名称
			XmlManager.gotoTagByTagName(xml, "string");
			gamePartInfo.setNextPart(XmlManager.getValueByCurrentTag(xml));
			//获取背景音乐
			XmlManager.gotoTagByTagName(xml, "string");
			gamePartInfo.setBgMusic(XmlManager.getValueByCurrentTag(xml));
			//获取背景图片
			XmlManager.gotoTagByTagName(xml, "string");
			gamePartInfo.setBackground(XmlManager.getValueByCurrentTag(xml));
			this.games.add(gamePartInfo);
		}
		//如果有关卡，默认第一个元素为开始关卡，这个集合不应该为空集合
		if(this.games.size()>0){
			this.part = this.games.get(0);
		}
		
	}
	/**
	 * 获取关卡管理器实例
	 * @return
	 */
	public static GamePartManager getManager(){
		if(manager==null){
			manager = new GamePartManager();
		}
		return manager;
	}
	/**
	 * 注销方法
	 */
	public void destroy(){
		manager = null;
		System.gc();
	}
	
}
