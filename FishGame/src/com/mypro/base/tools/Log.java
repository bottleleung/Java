package com.mypro.base.tools;


public class Log {
	public static void e(String name,String info){
		System.err.println("error:"+name+":"+info);
	}
	
	public static void w(String name,String info){
		System.out.println("warning:"+name+":"+info);
	}
}
