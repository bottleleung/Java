package com.mypro.mainsurface;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import com.mypro.base.graphics.Bitmap;
import com.mypro.base.graphics.Canvas;
import com.mypro.base.graphics.Matrix;
import com.mypro.base.graphics.Paint;
import com.mypro.basecomponet.JMatrix;
import com.mypro.constant.Constant;
import com.mypro.model.GamingInfo;
import com.mypro.model.interfaces.Drawable;

public class MainSurface extends JPanel{
	/**
	 * 修改图层的操作定义
	 */
	//更新图层
	private final static int CHANGE_MODE_UPDATE = 0;
	//添加元素到图层
	private final static int CHANGE_MODE_ADD = 1;
	//删除元素从图层
	private final static int CHANGE_MODE_REMOVE = 2;
	// 图片的图层分布
	private HashMap<Integer, ArrayList<Drawable>> picLayer =new HashMap<Integer, ArrayList<Drawable>>();
	// 修改后的图片的图层分布,这里根据操作分为了两个图层，分别是添加的元素，和删除的元素
	private HashMap<Integer, ArrayList<Drawable>> addPicLayer = new HashMap<Integer, ArrayList<Drawable>>(),removePicLayer = new HashMap<Integer, ArrayList<Drawable>>();
	// 是否修改过图层
	private boolean changeLayer = false;
	private int picLayerId[] = new int[0]; // 定义一个图层ID，加速获取图层绘制（省去了从map中获取各个图层排序问题）
	private Paint paint; // 画笔
	private OnDrawThread odt; // 屏幕绘制线程，用于控制绘制帧数，周期性调用onDraw方法
	public MainSurface() {
		setSize(GamingInfo.getGamingInfo().getScreenWidth(), GamingInfo.getGamingInfo().getScreenHeight());
		paint = canvas.getPaint();
		paint.setAntiAlias(true);//设置抗锯齿
		paint.setDither(true);
		odt = new OnDrawThread(this);
		
	}
	public void action() throws Exception {
		odt.start();
	}
	/**
	 * 绘图方法，这个方法是由线程控制，周期性调用的
	 */
	public void onDraw(Canvas canvas) {
		//更新图层内容
		updatePicLayer(CHANGE_MODE_UPDATE,0,null);
		
		// 遍历所有图层，按图层先后顺序绘制
		for (int id : picLayerId) {
				for (Drawable drawable : picLayer.get(id)) {
					drawable.onDraw(canvas, paint);
				}
		}
	}
	/**
	 * 更新图层，这里分为三种操作，分别是更新临时图层中的内容到绘制图层中，删除绘制图层中的元素，添加绘制图层中的元素
	 * 这里加了个线程锁，保证多线程下操作图层的安全性
	 * @param mode	对绘制图层的操作类型，对应当前类的CHANGE_MODE常量
	 * @param layerId	操作的图层ID
	 * @param draw		操作的图层元素
	 */
	private synchronized void updatePicLayer(int mode,int layerId,Drawable draw){
		switch(mode){
		//将临时图层中的内容更新至绘制图层中
		case CHANGE_MODE_UPDATE:
			//如果有修改
			if(changeLayer){
				//向图层添加新的元素
				for(Integer id:addPicLayer.keySet()){
					for(Drawable d:addPicLayer.get(id)){
						//如果要添加的元素所处图层不存在，则创建这个图层，并更新图层ID数组
						if(this.picLayer.get(id)==null){
							this.picLayer.put(id, new ArrayList<Drawable>());
							updateLayerIds(id);
						}
						this.picLayer.get(id).add(d);
					}
				}
				addPicLayer.clear();
				//删除图层中的元素
				for(Integer id:removePicLayer.keySet()){
					for(Drawable d:removePicLayer.get(id)){
						try {
							this.picLayer.get(id).remove(d);
						} catch (Exception e) {
							System.out.println("图层内容不存在:"+id);
						}
						
					}
				}
				removePicLayer.clear();
				changeLayer = false;			
			}
			break;
		/**
		 * 无论是向绘图图层中添加还是删除元素，都不是直接操作绘制图层，都是存放在对应的临时图层中，等待绘制方法绘制周期中将变化的内容更新到绘制图层中
		 * 保证多线程操作情况下的安全性
		 */
		//添加一个元素
		case CHANGE_MODE_ADD:
			ArrayList<Drawable> al = addPicLayer.get(layerId);
			if(al==null){
				al = new ArrayList<Drawable>();
				addPicLayer.put(layerId, al);
			}
			al.add(draw);
			changeLayer = true;	
			break;
		//删除一个元素
		case CHANGE_MODE_REMOVE:
			ArrayList<Drawable> al1 = removePicLayer.get(layerId);
			if(al1==null){
				al1 = new ArrayList<Drawable>();
				removePicLayer.put(layerId, al1);
			}
			al1.add(draw);
			changeLayer = true;	
			break;
		}
		
	}
	
	/**
	 * 将一个可绘制的图放入图层中
	 * 
	 * @param layer
	 *            图层号 图层号虽然是int，但是实际上只支持到byte，原因是图层没有必要那么多
	 * @param pic
	 *            可绘制的图
	 */
	public void putDrawablePic(int layer, Drawable pic) {
		if(pic==null){
			System.out.println("图层内容不能为空:对应图层:"+layer);
			return;
		}
		updatePicLayer(CHANGE_MODE_ADD,layer,pic);
	}

	/**
	 * 将一个可绘制的图从图层中移除
	 * 
	 * @param layer
	 * @param pic
	 */
	public void removeDrawablePic(int layer, Drawable pic) {
		if(pic==null){
			System.out.println("图层内容不能为空:对应图层:"+layer);
			return;
		}
		updatePicLayer(CHANGE_MODE_REMOVE,layer,pic);
	}

	/**
	 * 更新图层Id
	 * 
	 * @param newLayerId
	 */
	private void updateLayerIds(int newLayerId) {
		// 初始化图层
		if (picLayerId.length == 0) {
			picLayerId = new int[1];
			picLayerId[0] = newLayerId; // 将新的图层ID添加到初始化的图层ID数组中
		} else {
			// 创建一个新的图层数组，长度比原来的大1位
			int picLayerIdFlag[] = new int[picLayerId.length + 1];
			for (int i = 0; i < picLayerId.length; i++) {
				// 排序操作，如果新的图层ID小于当前图层ID，讲新的图层ID插入其中
				if (picLayerId[i] > newLayerId) {
					for (int f = picLayerIdFlag.length - 1; f > i; f--) {
						picLayerIdFlag[f] = picLayerId[f - 1];
					}
					picLayerIdFlag[i] = newLayerId;
					break;
				} else {
					picLayerIdFlag[i] = picLayerId[i];
				}
				// 如果到了最后，都没有比新图层ID大的，就将新的图层ID存入最后
				if (i == picLayerId.length - 1) {
					picLayerIdFlag[picLayerIdFlag.length - 1] = newLayerId;
				}
			}
			// 将新的图层ID数组覆盖原有的
			this.picLayerId = picLayerIdFlag;
		}
	}
	//画板
	private JCanvas canvas = new JCanvas();
	@Override
	public synchronized void paint(Graphics g) {
			g.drawImage(canvas.getCanvas(), 0, 0, null);		
	}
	
	public synchronized Canvas lockCanvas() {
			return canvas;		
	}

	
	public void unlockCanvasAndPost(Canvas canvas) {
		repaint();		
	}
	
	/**
	 * 画板类
	 * @author Xiloer
	 *
	 */
	private class JCanvas implements Canvas{		
		/*
		 * 绘制画板
		 */
		private BufferedImage canvas = new BufferedImage(GamingInfo.getGamingInfo().getScreenWidth(), GamingInfo.getGamingInfo().getScreenHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		private JPaint paint = new JPaint(canvas);
		
		public Paint getPaint() {
			return paint;
		}
		
		
		public BufferedImage getCanvas() {
			return canvas;
		}


		@Override
		public void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint) {	
			this.paint.getGraphics().drawImage(bitmap.getImage(),((JMatrix)matrix).trans, null);
		}
		@Override
		public void drawBitmap(Bitmap bitmap, float x, float y, Paint paint) {
			this.paint.getGraphics().drawImage(bitmap.getImage(),(int)x,(int)y, null);			
		}	
		
		public class JPaint implements Paint{
			Graphics2D graphics;
			public JPaint(BufferedImage canvas){
				graphics = (Graphics2D)canvas.getGraphics();
			}
			
			@Override
			public void setTypeface(Object obj) {
			}

			public Graphics2D getGraphics() {
				return graphics;
			}

			@Override
			public void setAntiAlias(boolean tf) {
				if(tf){
					RenderingHints qualityHints = graphics.getRenderingHints();
					if(qualityHints==null){
						qualityHints = new  RenderingHints(RenderingHints.KEY_ANTIALIASING,              
						  		RenderingHints.VALUE_ANTIALIAS_ON);
						graphics.setRenderingHints(qualityHints);
					}else{
						qualityHints.put(RenderingHints.KEY_ANTIALIASING,               
						  		RenderingHints.VALUE_ANTIALIAS_ON); 
					}				
					qualityHints.put(RenderingHints.KEY_RENDERING,               
							RenderingHints.VALUE_RENDER_QUALITY); 
					 
					qualityHints.put(RenderingHints.KEY_INTERPOLATION,               
							RenderingHints.VALUE_INTERPOLATION_BICUBIC);
					qualityHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,               
							RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
					qualityHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,               
							RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
				}
			}

			@Override
			public void setFilterBitmap(boolean tf) {
			}

			@Override
			public void setDither(boolean tf) {
				if(tf){
					RenderingHints qualityHints = graphics.getRenderingHints();
					if(qualityHints==null){
						qualityHints = new  RenderingHints(RenderingHints.KEY_DITHERING,              
						  		RenderingHints.VALUE_DITHER_ENABLE);
						graphics.setRenderingHints(qualityHints);
					}else{
						qualityHints.put(RenderingHints.KEY_DITHERING,               
						  		RenderingHints.VALUE_DITHER_ENABLE); 
					}	
					qualityHints.put(RenderingHints.KEY_RENDERING,               
							RenderingHints.VALUE_RENDER_QUALITY);
				}
			}

			@Override
			public void setTextSize(int size) {
			}

			@Override
			public void setColor(int color) {
			}
			
		}
	}
	
	public class OnDrawThread extends Thread{
		private MainSurface surface;
		private int drawSpeed;//每次绘制后的休息毫秒数，这个值是根据常量中的绘制帧数决定的
		public OnDrawThread(MainSurface surface){
			super();
			this.surface = surface;
			drawSpeed = 1000/Constant.ON_DRAW_SLEEP;
		}
		
		public void run(){
			super.run();
			Canvas canvas = null;
			while(GamingInfo.getGamingInfo().isGaming()){
				try{
					canvas = lockCanvas();
//					synchronized (this.sh) {
						if(canvas!=null){
							surface.onDraw(canvas);
						}
//					}
				}catch(Exception e){
//					Log.e(this.getName(), e.toString());
					e.printStackTrace();
				}finally{
					try{
						unlockCanvasAndPost(canvas);
					}catch(Exception e){
//						Log.e(this.getName(), e.toString());
					}
				}
				try{
					Thread.sleep(drawSpeed);
				}catch(Exception e){
					
				}
			}
		}
	}
}
