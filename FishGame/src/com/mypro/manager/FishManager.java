package com.mypro.manager;

import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import com.mypro.base.graphics.Bitmap;
import com.mypro.manager.ImageConfig.ActConfig;
import com.mypro.model.FishInfo;
import com.mypro.model.GamingInfo;
import com.mypro.model.fish.Fish;
import com.mypro.tools.LogTools;

/**
 * 鱼的管理器
 * @author Leslie Leung
 *
 */
public class FishManager {
	/**
	 * 单例模式
	 */
	private static FishManager fishManager;
	
	private FishManager(){
	}
	
	public static FishManager getFishMananger(){
		if(fishManager==null){
			fishManager = new FishManager();
		}
		return fishManager;
	}
	/**
	 * 根据名字保存所有鱼的配置信息
	 */
	private HashMap<String,FishInfo> allFishConfig = new HashMap<String,FishInfo>();
	/**
	 * 根据名字保存所有鱼的动作配置信息
	 */
	private HashMap<String,ActConfig[]> allFishActConfigs = new HashMap<String,ActConfig[]>();
	/**
	 * 根据名字保存所有鱼的捕获动作配置信息
	 */
	private HashMap<String,ActConfig[]> allFishCatchActConfigs = new HashMap<String,ActConfig[]>();
	/**
	 * 根据名字缓存的鱼的动作图片
	 */
	private HashMap<String,Bitmap[]> allFishActs = new HashMap<String,Bitmap[]>();
	/**
	 * 根据名字缓存的鱼的捕获动作图片
	 */
	private HashMap<String,Bitmap[]> allFishCatchActs = new HashMap<String,Bitmap[]>();
	/**
	 * 鱼的种类
	 */
	private ArrayList<String> allFish = new ArrayList<String>();
	/**
	 * 根据XML配置文件，初始化所有鱼
	 * 这里的配置文件还没定义，只是写在代码里了，以后可以改成通过读取配置文件来加载不同
	 * 资源的鱼
	 * @param initXml
	 */
	
	/**
	 * 是否可以创建新的鱼
	 * 这个值的改变在以下会发生:
	 * 每当调用updateFish方法时，会将这个值设置为false
	 * updateFish方法执行完毕时，会将这个值在改变回true
	 */
	private boolean createable = false;
	/**
	 * 初始化管理器
	 * 这里会读取fish文件夹下的FishConfig.plist文件，来加载所有其他配置信息
	 */
	public void initFish(){
		try{			
			HashMap<String,ActConfig> configs = new HashMap<String,ActConfig>();
			//创建一个鱼动作配置文件集合
			String fishActConfiges[]; 
			//鱼的基本信息配置文件名
			String fishInfoConfig;
			//加载管理器主配置文件
			XmlPullParser xml = XmlManager.getXmlParser("fish/FishConfig", "UTF-8");
			//获取fishActConfig信息
			XmlManager.gotoTagByTagName(xml, "string");
			fishActConfiges = XmlManager.getValueByCurrentTag(xml).split(";");
			//获取fishInfoConfig信息
			XmlManager.gotoTagByTagName(xml, "string");		
			fishInfoConfig = XmlManager.getValueByCurrentTag(xml);
			//清空Xml对象，释放空间
			xml = null;
			//初始化鱼的动作信息
			this.initFishAct(configs, fishActConfiges);
			//初始化鱼的基本信息
			this.initFishInfo(fishInfoConfig);
			
			int fishIndex = 1;
			StringBuffer fishName = new StringBuffer("fish01");
			while(getFishByName(fishName.toString(),configs)){
				fishName.delete(0, fishName.length());
				fishIndex++;
				if(fishIndex<10){
					fishName.append("fish0"+fishIndex);
				}else{
					fishName.append("fish"+fishIndex);
				}
			}
			configs = null;
			fishName = null;
			System.gc();
		}catch(Exception e){
			e.printStackTrace();
			LogTools.doLogForException(e);
		}
	}
	/**
	 * 根据鱼的名字获取一条鱼的实例
	 * @param fishName
	 * @return
	 */
	public Fish birthFishByFishName(String fishName){
		if(createable){
			Fish fish = new Fish(getFishActByFishName(fishName),getFishCatchActsByFishName(fishName),allFishConfig.get(fishName));
			/**
			 * 在这里应该还要设置一下鱼的配置对象FishConfig
			 */
			return fish;
		}else{
			System.out.println("FishManager:不能创建鱼，是否调用过updateFish方法?");
			return null;
		}
		
	}	
	/**
	 * 更新加载的鱼
	 * @param fish
	 */
	public void updateFish(String []fish){
		this.createable = false;
		HashMap<String,Bitmap[]> fishAct = new HashMap<String,Bitmap[]>();
		HashMap<String,Bitmap[]> fishCatchAct = new HashMap<String,Bitmap[]>();
		for(String fishName:fish){
			fishAct.put(fishName, getFishActByFishName(fishName));
			fishCatchAct.put(fishName, getFishCatchActsByFishName(fishName));
		}
		allFishActs = fishAct;
		allFishCatchActs = fishCatchAct;
		this.createable = true;
		//解析完所有的鱼以后，清理一下缓存
		ImageManager.getImageMnagaer().clearImageCache();
	}
	/**
	 * 获取所有鱼的名字
	 * @return
	 */
	public ArrayList<String> getAllFishName(){
		return allFish;
	}
	/**
	 * 销毁释放资源
	 */
	public static void destroy(){
		fishManager = null;
		System.gc();
	}
	
	/**
	 * 设置鱼的动作到管理器鱼动作结构中
	 * @param fishName
	 * @param fishActs
	 * @return true:放置成功 false:放置失败
	 */
	private boolean getFishByName(String fishName,HashMap<String,ActConfig> configs){
		//鱼的图全名(fish12_01.png)
		StringBuffer fishFullName = new StringBuffer();
		//鱼的被捕获图全名(fish12_catch_01.png)
		StringBuffer fishCatchFullName = new StringBuffer();
		//当前鱼的索引值，用于拼鱼的文件名
		int fishNum = 1;
		//临时存放当前鱼所有图片的集合
		ArrayList<ActConfig> allActs = new ArrayList<ActConfig>();
		//临时存放当前鱼所有被捕获图片的集合
		ArrayList<ActConfig> allCatchActs = new ArrayList<ActConfig>();
		//获取当前鱼的所有动作
		while(GamingInfo.getGamingInfo().isGaming()){
			fishFullName.delete(0, fishFullName.length());
			//一帧图片的引用
			ActConfig fishAct = null;
			if(fishNum<10){
				fishFullName.append(fishName+"_0"+fishNum+".png");
			}else{
				fishFullName.append(fishName+"_"+fishNum+".png");
			}
			fishNum++;
			if((fishAct = configs.get(fishFullName.toString()))!=null){
				allActs.add(fishAct);
			}else{
				break;
			}
		}
		fishFullName = null;
		System.gc();
		fishNum = 1;
		//获取当前鱼的所有被捕获动作
		while(GamingInfo.getGamingInfo().isGaming()){
			fishCatchFullName.delete(0, fishCatchFullName.length());
			//一帧图片的引用
			ActConfig fishCatchAct = null;
			if(fishNum<10){
				fishCatchFullName.append(fishName+"_catch_0"+fishNum+".png");
			}else{
				fishCatchFullName.append(fishName+"_catch_"+fishNum+".png");
			}
			fishNum++;
			if((fishCatchAct = configs.get(fishCatchFullName.toString()))!=null){
				allCatchActs.add(fishCatchAct);
			}else{
				break;
			}
		}
		fishCatchFullName = null;
		System.gc();
		//如果没有解析出鱼的动作
		if(allActs.size()==0){
			//返回null，表示没有这条鱼
			return false;
		}else{
			//根据当前给定名字放入对应这条鱼的所有动作
			ActConfig[] fishActArray = new ActConfig[allActs.size()];
			ActConfig[] fishCatchActsArray = new ActConfig[allCatchActs.size()];
			for(int i =0;i<allActs.size();i++){
				fishActArray[i] = allActs.get(i);
			}
			for(int i =0;i<allCatchActs.size();i++){
				fishCatchActsArray[i] = allCatchActs.get(i);
			}
			allActs = null;
			allCatchActs = null;		
			allFishActConfigs.put(fishName,fishActArray);
			allFishCatchActConfigs.put(fishName,fishCatchActsArray);
			allFish.add(fishName);
			System.gc();
			return true;
		}
	}
	
	/**
	 * 获取鱼的游动图片集
	 * @param fishName
	 * @return
	 */
	private Bitmap[] getFishActByFishName(String fishName){
		if(allFishActs.get(fishName)==null){
			Bitmap []acts = ImageManager.getImageMnagaer().getImagesByActConfigs(allFishActConfigs.get(fishName),ImageManager.getImageMnagaer().fishScaleNum);
			//将所有的图都旋转180度，因为程序设计是基于头冲右的
			for(int i =0;i<acts.length;i++){
				acts[i] = ImageManager.getImageMnagaer().rotateImage(180, acts[i]);
			}
			return acts;
			
		}else{			
			return allFishActs.get(fishName);
		}

	}
	/**
	 * 获取鱼的被捕获图片集
	 * @param fishName
	 * @return
	 */
	private Bitmap[] getFishCatchActsByFishName(String fishName){
		if(allFishCatchActs.get(fishName)==null){
			Bitmap []acts = ImageManager.getImageMnagaer().getImagesByActConfigs(allFishCatchActConfigs.get(fishName),ImageManager.getImageMnagaer().fishScaleNum);
			//将所有的图都旋转180度，因为程序设计是基于头冲右的
			for(int i =0;i<acts.length;i++){
				acts[i] = ImageManager.getImageMnagaer().rotateImage(180, acts[i]);
			}
			return acts;
		}else{
			return allFishCatchActs.get(fishName);
		}
		
	}
	/**
	 * 初始化鱼的配置信息
	 * @param config
	 */
	private void initFishInfo(String config){
		try{
			//如果配置信息没有找到，抛出异常
			if(config==null){
				throw new Exception("FishManager:读取配置文件出错，没有找到fishInfoConfig信息");
			}
			//加载鱼的基本信息配置文件
			XmlPullParser xml = XmlManager.getXmlParser(config, "UTF-8");
			//解析所有的鱼的基本信息
			while(GamingInfo.getGamingInfo().isGaming()&&XmlManager.gotoTagByTagName(xml, "key")){
				XmlManager.gotoTagByTagName(xml, "string");
				String fishName = XmlManager.getValueByCurrentTag(xml);
				FishInfo fishInfo = new FishInfo();
				//设置最大旋转角度
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setMaxRotate(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				//设置移动速度
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setFishRunSpeed(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				//设置动作速度
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setActSpeed(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				//设置鱼群最大数量
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setFishShoalMax(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				//设置鱼的图层ID
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setFishInLayer(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				//设置鱼的价值
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setWorth(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				//设置鱼的捕捉概率
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setCatchProbability(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				allFishConfig.put(fishName, fishInfo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 初始化鱼的动作信息
	 * @param configs			将解析出来的每个配置文件放入这个Map中
	 * @param fishActConfiges	所有的配置文件名称
	 */
	private void initFishAct(HashMap<String,ActConfig> configs,String fishActConfiges[]){
		try{
			//如果配置信息没有找到，抛出异常
			if(fishActConfiges==null){
				throw new Exception("FishManager:读取配置文件出错，没有找到fishActConfig信息");
			}
			for(String actConfig : fishActConfiges){
				configs.putAll(ImageManager.getImageMnagaer().createImageConfigByPlist(actConfig).getAllActs());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
