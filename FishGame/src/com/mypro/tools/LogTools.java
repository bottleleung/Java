package com.mypro.tools;

import com.mypro.model.GamingInfo;

/**
 * 日志记录类
 * @author Leslie Leung
 *
 */
public class LogTools {
	/**
	 * 把异常消息记录日志
	 * @param e
	 */
	public static void doLogForException(Exception e){
		e.printStackTrace();
		//如果游戏结束了，那么不算是异常
		if(!GamingInfo.getGamingInfo().isGaming()){
			return;
		}
		for(StackTraceElement ste:e.getStackTrace()){
//			Log.e(ste.getClassName()+":", e.toString());
//			Log.e("line:", ste.getLineNumber()+"");
//			Log.e("method:", ste.getMethodName());
//			Log.e("file:", ste.getFileName());
		}
	}
}
