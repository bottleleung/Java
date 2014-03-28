package com.mypro.manager;

import java.util.HashMap;

/**
 * 图片的配置信息类
 * @author Leslie Leung
 *
 */
public class ImageConfig {
	private String srcImageFileName;												//源图名称
	private int srcImageWidth;														//源图宽度
	private int srcImageHeight;														//源图高度
	private HashMap<String,ActConfig> allActs = new HashMap<String,ActConfig>();	//每一张图的细节信息
	/**
	 * 每一张图的细节信息
	 * @author Leslie Leung
	 *
	 */
	public static class ActConfig{
		//所属源图的信息
		private ImageConfig config;
		//图片名称
		private String imageName;
		//图片在源图的X坐标
		private int imageX = 0;
		//图片在源图的Y坐标
		private int imageY = 0;
		//图片在源图的宽度
		private int imageWidth = 0;
		//图片在源图的高度
		private int imageHeight = 0;
		//图片的源始宽度
		private int originalWidth = 0;
		//图片的源始高度
		private int originalHeight = 0;
		//偏移量X
		private float offsetX = 0;
		//偏移量Y
		private float offsetY = 0;
		
		public int getImageX() {
			return imageX;
		}
		public void setImageX(int imageX) {
			this.imageX = imageX;
		}
		public int getImageY() {
			return imageY;
		}
		public void setImageY(int imageY) {
			this.imageY = imageY;
		}
		public int getImageWidth() {
			return imageWidth;
		}
		public void setImageWidth(int imageWidth) {
			this.imageWidth = imageWidth;
		}
		public int getImageHeight() {
			return imageHeight;
		}
		public void setImageHeight(int imageHeight) {
			this.imageHeight = imageHeight;
		}
		public int getOriginalWidth() {
			return originalWidth;
		}
		public void setOriginalWidth(int originalWidth) {
			this.originalWidth = originalWidth;
		}
		public int getOriginalHeight() {
			return originalHeight;
		}
		public void setOriginalHeight(int originalHeight) {
			this.originalHeight = originalHeight;
		}
		public float getOffsetX() {
			return offsetX;
		}
		public void setOffsetX(float offsetX) {
			this.offsetX = offsetX;
		}
		public float getOffsetY() {
			return offsetY;
		}
		public void setOffsetY(float offsetY) {
			this.offsetY = offsetY;
		}
		public String getImageName() {
			return imageName;
		}
		public void setImageName(String imageName) {
			this.imageName = imageName;
		}
		public ImageConfig getConfig() {
			return config;
		}
		public void setConfig(ImageConfig config) {
			this.config = config;
		}
		
	}
	public String getSrcImageFileName() {
		return srcImageFileName;
	}
	public void setSrcImageFileName(String srcImageFileName) {
		this.srcImageFileName = srcImageFileName;
	}
	public int getSrcImageWidth() {
		return srcImageWidth;
	}
	public void setSrcImageWidth(int srcImageWidth) {
		this.srcImageWidth = srcImageWidth;
	}
	public int getSrcImageHeight() {
		return srcImageHeight;
	}
	public void setSrcImageHeight(int srcImageHeight) {
		this.srcImageHeight = srcImageHeight;
	}
	public HashMap<String,ActConfig> getAllActs() {
		return allActs;
	}
	public void setAllActs(HashMap<String,ActConfig> allActs) {
		this.allActs = allActs;
	}
	
	
	
}
