package com.mypro.tools;


public class Tool {
	private static final double ARG = 180/Math.PI;
	/**
	 * 	获得目标与源之间的角度
	 * @param targetX		目标的X坐标
	 * @param targetY		目标的Y坐标
	 * @param sourceX		源的X坐标
	 * @param sourceY		源的Y坐标
	 * @return				目标与源之间的角度
	 */
	public static float getAngle(float targetX,float targetY,float sourceX,float sourceY){
		return -(float)(ARG*Math.atan2(targetY-sourceY,targetX-sourceX));

	}
}
