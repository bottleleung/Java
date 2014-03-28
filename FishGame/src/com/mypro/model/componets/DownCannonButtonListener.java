package com.mypro.model.componets;

import com.mypro.manager.CannonManager;
import com.mypro.model.interfaces.OnClickListener;

/**
 * 降低大炮质量的按钮逻辑
 * @author Leslie Leung
 *
 */
public class DownCannonButtonListener implements OnClickListener{

	@Override
	public void onClick() {
		// TODO Auto-generated method stub
		CannonManager.getCannonManager().downCannon();
	}

}
