package com.mypro.model;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Scanner;

import com.mypro.constant.Constant;
import com.mypro.mainsurface.MainSurface;
import com.mypro.manager.ShoalManager;
//import com.mypro.manager.SoundManager;
import com.mypro.model.fish.Fish;

//游戏进行中一些需要共用的变量
public class GamingInfo {
	private int screenWidth;
	private int screenHeight;
	private static GamingInfo gameInfo; // 单例模式需要
	private boolean isGaming; // 是否处于游戏状态
	private boolean isPause;//是否处于暂停状态
	private MainSurface surface; // 主屏幕
	private ArrayList<Fish> fish = new ArrayList<Fish>(); // 所有的鱼
	private ShoalManager shoalManager; // 鱼群管理器
	private float cannonLayoutX;			//大炮旋转X坐标
	private float cannonLayoutY;			//大炮旋转Y坐标
	private int score = 100;//当前的分
	private int highestScore;//最高分

	/**
	 *
	 * @return 从文件读入最高分，覆盖当前的的最高分
	 */
	public int getHighestScoreFromeFile(){
		File file=new File(Constant.HIGHEST_SCORE_PATH);
		try {
			if(file.exists()){
				Scanner scanner=new Scanner(file);
				highestScore=scanner.nextInt();
				scanner.close();
				return highestScore;

			}else{
				file.createNewFile();
				highestScore=0;
				return 0;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;

	}
	public int getHighestScore() {
		return highestScore;
	}

	/**
	 * 设置最高分，并写入到文件中
	 * @param highestScore
	 */
	public void setHighestScore(int highestScore) {
		File file=new File(Constant.HIGHEST_SCORE_PATH);
		try {
			file.createNewFile();
			FileWriter fileWriter=new FileWriter(file);
			fileWriter.write(highestScore+"");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.highestScore = highestScore;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * 清除GamingInfo实例
	 */
	public static void clearGameInfo() {
		gameInfo = null;
	}

	private GamingInfo() {
		getHighestScoreFromeFile();
	}

	public static GamingInfo getGamingInfo() {
		if (gameInfo == null) {
			gameInfo = new GamingInfo();
		}
		return gameInfo;
	}

	public boolean isGaming() {
		return isGaming;
	}

	public void setGaming(boolean isGaming) {
		this.isGaming = isGaming;
	}

	public ArrayList<Fish> getFish() {
		return fish;
	}

	public void setFish(ArrayList<Fish> fish) {
		this.fish = fish;
	}

	public MainSurface getSurface() {
		return surface;
	}

	public void setSurface(MainSurface surface) {
		this.surface = surface;
	}

	public ShoalManager getShoalManager() {
		return shoalManager;
	}

	public void setShoalManager(ShoalManager shoalManager) {
		this.shoalManager = shoalManager;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public float getCannonLayoutX() {
		return cannonLayoutX;
	}

	public void setCannonLayoutX(float cannonLayoutX) {
		this.cannonLayoutX = cannonLayoutX;
	}

	public float getCannonLayoutY() {
		return cannonLayoutY;
	}

	public void setCannonLayoutY(float cannonLayoutY) {
		this.cannonLayoutY = cannonLayoutY;
	}

	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

}