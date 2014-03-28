package com.mypro.manager;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.xmlpull.v1.XmlPullParser;

import com.mypro.base.graphics.Bitmap;
import com.mypro.manager.ImageConfig.ActConfig;
import com.mypro.model.GamingInfo;
import com.mypro.tools.LogTools;
/**
 * 图片管理器
 * @author Leslie Leung
 *
 */
public class ImageManager {
	private static ImageManager manager;
	private ImageManager(){
		
	}
	public static ImageManager getImageMnagaer(){
		if(manager == null){
			manager = new ImageManager();
		}
		return manager;
	}
	//缩放比例
	public float scaleNum = 1;
	//鱼缩放比例
	public float fishScaleNum = 1;
	//图片缓存，用于裁图时大图的降低加载次数
	private Bitmap baseImageCache;
	//缓存图片的名字
	private String baseImageString;
	/**
	 * 清楚管理器缓存，释放空间
	 */
	public void clearImageCache(){
		baseImageCache = null;
		baseImageString = null;
	}
//	/**
//	 * 初始化管理器
//	 */
	public void initManager(){
		int len  = GamingInfo.getGamingInfo().getScreenHeight();
		if(len<=500){
			scaleNum = 0.75f;
			fishScaleNum = 0.5f;
		}
	}
	/**
	 * 根据给定的配置文件，创建相关的配置信息类
	 * @param configFileName	相对assets的带路径的文件 例 fish/fish2(fish2.plist)
	 * @return	返回一个ImageConfig对象
	 */
	public ImageConfig createImageConfigByPlist(
			String configFileName) {
		ImageConfig config = new ImageConfig();
		try {
			XmlPullParser xml = XmlManager.getXmlParser(configFileName, "UTF-8");
			if(xml==null){
				throw new Exception("ImageManager:解析的xml文件为null!");
			}
			config.setSrcImageFileName(configFileName);
			while (GamingInfo.getGamingInfo().isGaming()) {
				/**
				 * 标签为key的
				 */
				XmlManager.gotoTagByTagName(xml, "key");
				String value = XmlManager.getValueByCurrentTag(xml);
				if (value != null) {
					/**
					 * 设置缩放源图信息
					 */
					if (value.equals("texture")) {
						setScaleInfo(xml, config);
						/**
						 * 设置截取每帧画面信息
						 */
					} else if (value.equals("frames")) {
						XmlManager.gotoTagByTagName(xml, "dict");
						getCutImageInfo(xml, config);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

	

	
	/**
	 * 设置源图缩放信息
	 * @param xml
	 * @param config
	 */
	private void setScaleInfo(XmlPullParser xml,ImageConfig config){
		XmlManager.gotoTagByTagName(xml, "key");
		String mode = XmlManager.getValueByCurrentTag(xml);
		XmlManager.gotoTagByTagName(xml, "integer");
		if (mode.equals("width")) {
			config.setSrcImageWidth(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
		} else if (mode.equals("height")) {
			config.setSrcImageHeight(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
		}
		XmlManager.gotoTagByTagName(xml, "key");
		mode = XmlManager.getValueByCurrentTag(xml);
		XmlManager.gotoTagByTagName(xml, "integer");
		if (mode.equals("width")) {
			config.setSrcImageWidth(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
		} else if (mode.equals("height")) {
			config.setSrcImageHeight(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
		}
	}
	/**
	 * 缩放素材图
	 * @param config	图片的配置信息
	 * @return			返回缩放后的图片，如果图片没找到返回null
	 */
	private synchronized Bitmap scaledSrcBitmap(ImageConfig config) {
		try {
			if(this.baseImageString!=null&&this.baseImageString.equals(config.getSrcImageFileName())){
				return this.baseImageCache;
			}else{	
				if(this.baseImageCache!=null){
//					this.baseImageCache.recycle();
					this.baseImageCache=null;
					System.gc();
				}				
				this.baseImageCache = getBitmapByAssets(config.getSrcImageFileName() + ".png");
				this.baseImageString = config.getSrcImageFileName();

				return this.baseImageCache;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取裁图信息
	 * @param xml	相应的配置文件xml文件
	 * @param ImageConfig	将解析的内容保存在这个配置对象里
	 */
	private void getCutImageInfo(XmlPullParser xml,
			ImageConfig config) {
		String imageName = null;
		while (GamingInfo.getGamingInfo().isGaming()&&XmlManager.gotoTagByTagName(xml, "key")) {
			imageName = XmlManager.getValueByCurrentTag(xml);
			if (XmlManager.gotoTagByTagName(xml, "dict")) {
				ActConfig actConfig = getActConfig(xml);
				config.getAllActs().put(imageName,actConfig);
				actConfig.setImageName(imageName);
				actConfig.setConfig(config);
//				getFishActImage(xml, src);
			}
		}
	}
	/**
	 * 解析图片配置信息
	 * 这里严格按照顺序解析，不忽略属性顺序问题，所以xml那边配置的顺序要比较严格
	 * @param xml
	 * @return
	 */
	private ActConfig getActConfig(XmlPullParser xml){
		ActConfig imageConfig = new ActConfig();
		//找到相应配置信息
		XmlManager.gotoTagByTagName(xml, "integer");
		imageConfig.setImageX(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
		XmlManager.gotoTagByTagName(xml, "integer");
		imageConfig.setImageY(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
		XmlManager.gotoTagByTagName(xml, "integer");
		imageConfig.setImageWidth(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
		XmlManager.gotoTagByTagName(xml, "integer");
		imageConfig.setImageHeight(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
		XmlManager.gotoTagByTagName(xml, "real");
		imageConfig.setOffsetX(Float.parseFloat(XmlManager.getValueByCurrentTag(xml)));
		XmlManager.gotoTagByTagName(xml, "real");
		imageConfig.setOffsetY(Float.parseFloat(XmlManager.getValueByCurrentTag(xml)));
		XmlManager.gotoTagByTagName(xml, "integer");
		imageConfig.setOriginalWidth(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
		XmlManager.gotoTagByTagName(xml, "integer");
		imageConfig.setOriginalHeight(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
		return imageConfig;
	}
	
	/**
	 * 根据图片的配置信息获取图片
	 * @param config	图片的配置文件
	 * @param src		源图
	 * @return			裁出来的图
	 */
	private Bitmap getImage(ActConfig config, Bitmap src,float proportion) {	
		// 创建一个图片
		BufferedImage newImage = new BufferedImage(config.getOriginalWidth(), config.getOriginalHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = newImage.getGraphics();
		// 裁出来的图片
		g.drawImage(
				src.getImage(), 
				(int)(config.getOriginalWidth() / 2 + config.getOffsetX() - config.getImageWidth()/ 2), 
				(int)(config.getOriginalHeight() / 2 - config.getOffsetY() - config.getImageHeight() / 2), 
				(int)(config.getOriginalWidth() / 2 + config.getOffsetX() - config.getImageWidth()/ 2)+config.getImageWidth(), 
				(int)(config.getOriginalHeight() / 2 - config.getOffsetY() - config.getImageHeight() / 2)+config.getImageHeight(), 
				config.getImageX(),
				config.getImageY(), 
				config.getImageX()+config.getImageWidth(), 
				config.getImageY()+config.getImageHeight(), 
				null
				);
		return new Bitmap(newImage);
	}
	/**
	 * 返回给定配置信息的一组图片
	 * @param configs
	 * @return
	 */
	public Bitmap[] getImagesByActConfigs(ActConfig[] configs,float proportion){
		Bitmap []imgs = new Bitmap[configs.length];
		Bitmap src = null;
		String srcFileName = null;
		for(int i =0;i<configs.length;i++){
			if(srcFileName==null||!srcFileName.equals(configs[i].getConfig().getSrcImageFileName())){
				srcFileName = configs[i].getConfig().getSrcImageFileName();							
				src = scaledSrcBitmap(configs[i].getConfig());			
			}
			imgs[i] = getImage(configs[i],src,proportion);
		}
		src = null;
		System.gc();
		return imgs;
	}
	/**
	 * 根据图片配置对象信息获取对应的一组图片的HashMap对象
	 * @param config	对应的图片配置对象	
	 * @return	一个HashMap集合 key:图片名称 value:对应的图片
	 */
	public HashMap<String,Bitmap> getImagesMapByImageConfig(ImageConfig config,float proportion){
		HashMap<String,Bitmap> allAct = new HashMap<String,Bitmap>();
		try{
			Bitmap src = scaledSrcBitmap(config);
			//将所有配置信息中的图片放置到集合中
			for(ActConfig act : config.getAllActs().values()){
				allAct.put(act.getImageName(), getImage(act,src,proportion));
			}
		}catch(Exception e){
			LogTools.doLogForException(e);
		}
		return allAct;
	}
	
	/**
	 * 旋转图片
	 * @param angle		给定角度
	 * @param newImage	旋转的图片
	 * @return
	 */
	public Bitmap rotateImage(int angle,Bitmap newImage){
		AffineTransform trans = new AffineTransform();
	    trans.rotate(Math.toRadians(angle), newImage.getWidth()/2, newImage.getHeight()/2);
	    BufferedImage img = new BufferedImage(newImage.getWidth(), newImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) img.getGraphics();

		g.drawImage(newImage.getImage(), trans, null);
		return new Bitmap(img);
	}
	
	/**
	 * 根据屏幕尺寸缩放图片
	 * 这里需要注意的一点是，要初始化GamingInfo里的屏幕尺寸，因为是依据这个来缩放图片的
	 * @param src	需要缩放的图片
	 * @return		缩放后的图片
	 */
	public Bitmap scaleImageByScreen(Bitmap src){
		AffineTransform trans = new AffineTransform();
	    trans.scale(scaleNum, scaleNum);
	    BufferedImage img = new BufferedImage((int)(src.getWidth()*scaleNum), (int)(src.getHeight()*scaleNum), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) img.getGraphics();

		g.drawImage(src.getImage(), trans, null);
		return new Bitmap(img);
	}
	/**
	 * 根据给定尺寸缩放图片
	 * @param src
	 * @param width
	 * @param height
	 * @return
	 */
	public Bitmap sacleImageByWidthAndHeight(Bitmap src,int width,int height){
		AffineTransform trans = new AffineTransform();
	    trans.scale(width*1f/src.getWidth(), height*1f/src.getHeight());
	    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) img.getGraphics();

		g.drawImage(src.getImage(), trans, null);
		return new Bitmap(img);
	}
	/**
	 * 从资产中获取图片
	 * @param imageName	相对资产assets跟目录下的带路径的图片名称
	 * @return			返回指定的图片
	 * @throws Exception
	 */
	public Bitmap getBitmapByAssets(String imageName)throws Exception{
		try {
			return new Bitmap(ImageIO.read(new File(imageName)));
		} catch (IOException e) {
			LogTools.doLogForException(e);
			throw e;
		}
	}
	/**
	 * 从资产中获取根据屏幕尺寸缩放后的图片
	 * @param imageName
	 * @return
	 * @throws Exception
	 */
	public Bitmap getscaleImageByScreenFromAssets(String imageName)throws Exception{
		try {
			return scaleImageByScreen(getBitmapByAssets(imageName));
		} catch (IOException e) {
			throw e;
		}
	}
	
}

