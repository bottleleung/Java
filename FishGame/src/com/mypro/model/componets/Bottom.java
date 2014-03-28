package com.mypro.model.componets;

import com.mypro.base.graphics.Bitmap;
import com.mypro.base.tools.Log;
import com.mypro.manager.ImageManager;
import com.mypro.model.GamingInfo;
/**
 * ´óÅÚµ××ù
 * @author Leslie Leung
 *
 */
public class Bottom extends Componet{
	private Bitmap pic;
	public Bottom(){
		try{
		pic = ImageManager.getImageMnagaer().getscaleImageByScreenFromAssets("componet/bottom.png");
		this.setLayout_x(GamingInfo.getGamingInfo().getScreenWidth()/2-getPicWidth()/2);
		this.setLayout_y(GamingInfo.getGamingInfo().getScreenHeight()-getPicHeight());
		this.getPicMatrix().setTranslate(this.getLayout_x(),this.getLayout_y());
		}catch(Exception e){
			Log.e("Bottom", e.toString());
		}
	}

	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return pic;
	}

	public int getPicWidth() {
		// TODO Auto-generated method stub
		return pic.getWidth();
	}

	public int getPicHeight() {
		// TODO Auto-generated method stub
		return pic.getHeight();
	}

}
