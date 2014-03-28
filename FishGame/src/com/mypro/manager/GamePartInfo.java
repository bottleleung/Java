package com.mypro.manager;
/**
 * 描述每一关的详细信息
 * @author Leslie Leung
 *
 */
public class GamePartInfo {
	//关卡名称
	private String partName;
	//出现的鱼的名字
	private String[] fishName;
	//鱼的出现几率
	private int[] showProbability;
	//可出现的鱼群数量
	private int shoalSumInScreen;
	//下一关的名字
	private String nextPart;
	//背景音乐
	private String bgMusic;
	//背景图片 
	private String background;
	//当前关卡时间(以秒为单位)
	private int partTime;
	public String[] getFishName() {
		return fishName;
	}
	public void setFishName(String[] fishName) {
		this.fishName = fishName;
	}
	public String getNextPart() {
		return nextPart;
	}
	public void setNextPart(String nextPart) {
		this.nextPart = nextPart;
	}
	public int getPartTime() {
		return partTime;
	}
	public void setPartTime(int partTime) {
		this.partTime = partTime;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getBgMusic() {
		return bgMusic;
	}
	public void setBgMusic(String bgMusic) {
		this.bgMusic = bgMusic;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	public int getShoalSumInScreen() {
		return shoalSumInScreen;
	}
	public void setShoalSumInScreen(int shoalSumInScreen) {
		this.shoalSumInScreen = shoalSumInScreen;
	}
	public int[] getShowProbability() {
		return showProbability;
	}
	public void setShowProbability(int[] showProbability) {
		this.showProbability = showProbability;
	}
	
	
}
