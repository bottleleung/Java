package com.mypro.manager;

import java.util.ArrayList;

import com.mypro.constant.Constant;
import com.mypro.model.GamingInfo;
import com.mypro.model.componets.Bottom;
import com.mypro.model.componets.BottomGold;
import com.mypro.model.componets.BottomTime;
import com.mypro.model.componets.ButtonAdapter;
import com.mypro.model.componets.Cannon;
import com.mypro.model.componets.DownCannonButtonListener;
import com.mypro.model.componets.UpCannonButtonListener;
import com.mypro.model.interfaces.Button;
import com.mypro.tools.LogTools;

/**
 * 布局管理器
 * @author Leslie Leung
 *
 */
public class LayoutManager {
	private static LayoutManager manager;
	/**
	 *	所有按钮
	 */
	private ArrayList<LayoutInfo> allButton = new ArrayList<LayoutInfo>();
	/**
	 * 计分板，这个是给ScoreManager使用的
	 */
	private BottomGold bottomGold;
	/**
	 * 计时板，这个是给增加金币使用的
	 */
	private BottomTime bottomTime;
	/**
	 * 当前使用的大炮
	 */
	private Cannon cannon;
	public void initCannon(Cannon cannon){
		try{
			this.cannon = cannon;
			if(GamingInfo.getGamingInfo().getSurface()!=null){
				GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.GUN_LAYER, cannon);
			}		
		}catch(Exception e){
			LogTools.doLogForException(e);
		}
	}
	public void updateCannon(final Cannon newCannon){
		new Thread(new Runnable() {
			public void run() {
				try {
					float scale;
					float pos;
					for(int i=5;i>=1;i--){
						scale = i*0.2f;
						pos = (5-i)*0.2f;
						cannon.getPicMatrix().setTranslate(cannon.getX()+cannon.getPicWidth()/2*pos, cannon.getY()+cannon.getPicHeight()/2*pos);
						cannon.getPicMatrix().preScale(scale, scale);
						Thread.sleep(60);
					}
					GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.GUN_LAYER, cannon);
					cannon = newCannon;
					GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.GUN_LAYER, cannon);
					for(int i=1;i<=5;i++){
						scale = i*0.2f;
						pos = (5-i)*0.2f;
						cannon.getPicMatrix().setTranslate(cannon.getX()+cannon.getPicWidth()/2*pos, cannon.getY()+cannon.getPicHeight()/2*pos);
						cannon.getPicMatrix().preScale(scale, scale);
						Thread.sleep(60);
					}
				} catch (Exception e) {
					LogTools.doLogForException(e);
				}
			}
		}).start();
		
	}
	/**
	 * 获取计分板
	 * @return
	 */
	public BottomGold getBottomGold() {
		return bottomGold;
	}
	/**
	 * 获取计时板
	 * @return
	 */
	public BottomTime getBottomTime() {
		return bottomTime;
	}
	/**
	 * 添加一个按钮
	 * @param button
	 */
	public void addButton(ButtonAdapter button,float x,float y){
		button.getPicMatrix().setTranslate(x, y);
		button.setLayout_x(x);
		button.setLayout_y(y);
		allButton.add(new LayoutInfo(button,x,y));
		
	}
	/**
	 *  响应单击事件
	 * @param x	触发的x点
	 * @param y 触发的y点
	 * @return
	 */
	public boolean onClick(float x,float y){
		for(LayoutInfo button:allButton){
			if(x>=button.getLayout_x()&&x<=button.getLayout_x()+button.getDrawable().getPicWidth()
					&&y>=button.getLayout_y()&&y<=button.getLayout_y()+button.getDrawable().getPicHeight()){
				/**
				 * 若点击的是按钮的透明位置，则忽略。否则为点击了按钮
				 */
				if(button.getDrawable().getCurrentPic().getPixel((int)(x-button.getLayout_x()), (int)(y-button.getLayout_y()))!=0x00000000){
					((Button)button.getDrawable()).onClick(); 
					return true;
				}				
			}
		}
		return false;
	}
	
	private LayoutManager(){}
	/**
	 * 获取一个布局管理器,单例
	 * @return
	 */
	public static LayoutManager getLayoutManager(){
		if(manager == null){
			manager = new LayoutManager();
		}
		return manager;
	}
	/**
	 * 初始化布局
	 * 这里会初始化改变大炮的按钮，记分板，暂停按钮等
	 */
	public void init(){
		try {			
			//初始化大炮底座
			Bottom bottom = new Bottom();
			GamingInfo.getGamingInfo().setCannonLayoutX(bottom.getLayout_x()+bottom.getPicWidth()/2);
			GamingInfo.getGamingInfo().setCannonLayoutY(bottom.getLayout_y()+bottom.getPicHeight()/2);
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, bottom);		
			//初始化提高大炮质量的按钮
			ButtonAdapter upCannon = new ButtonAdapter(ImageManager.getImageMnagaer().getscaleImageByScreenFromAssets("cannon/add.png"),new UpCannonButtonListener());
			addButton(upCannon,bottom.getLayout_x()+bottom.getPicWidth(),GamingInfo.getGamingInfo().getScreenHeight()-upCannon.getPicHeight());
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, upCannon);	
			//初始化降低大炮质量的按钮
			ButtonAdapter downCannon = new ButtonAdapter(ImageManager.getImageMnagaer().getscaleImageByScreenFromAssets("cannon/sub.png"),new DownCannonButtonListener());
			addButton(downCannon,bottom.getLayout_x()-downCannon.getPicWidth(),GamingInfo.getGamingInfo().getScreenHeight()-downCannon.getPicHeight());
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, downCannon);	
			//初始化计分板
			//计分板在提升大炮质量按钮右边屏幕宽度1/30,1/3按钮高度的位置
			bottomGold = new BottomGold((int)(upCannon.getLayout_x()+upCannon.getPicWidth()+GamingInfo.getGamingInfo().getScreenWidth()/30),(int)upCannon.getLayout_y()+upCannon.getPicHeight()/3);
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, bottomGold);	
			//初始化计分板
			//计分板在降低大炮质量按钮左边边屏幕宽度1/30加组件宽度,1/3按钮高度的位置
			bottomTime = new BottomTime();
			bottomTime.setPosition((int)(downCannon.getLayout_x()-GamingInfo.getGamingInfo().getScreenWidth()/30-bottomTime.getPicWidth()),(int)downCannon.getLayout_y()+downCannon.getPicHeight()/3);
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, bottomTime);	
		} catch (Exception e) {
			LogTools.doLogForException(e);
		}
	}
}
