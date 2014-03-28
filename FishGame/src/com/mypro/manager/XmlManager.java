package com.mypro.manager;

import java.io.FileInputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Xml管理器
 * @author Leslie Leung
 *
 */
public class XmlManager {
	/**
	 * 获取XML解析器
	 * @param fileName	需要解析的xml文件路径加文件名（不含后缀，这里后缀统一用plist,有必要在修改）
	 * @param encode	字符集编码
	 * @return
	 */
	public static XmlPullParser getXmlParser(String fileName,String encode){
		try{
			// 获得处理 xml 文件的 XmlResourceParser 对象
//			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance(
					 
                    System.getProperty(XmlPullParserFactory.PROPERTY_NAME), 

           Thread.currentThread().getContextClassLoader().getClass() );

			factory.setNamespaceAware(true);
			XmlPullParser xml = factory.newPullParser();
			xml.setInput(new FileInputStream(fileName + ".plist"), encode);
			return xml;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取当前标签的值
	 * 
	 * @return
	 */
	public static String getValueByCurrentTag(XmlPullParser xml) {
		try {
			int eventType = xml.next();
			while (true) {
				// 读取标签内容状态
				if (eventType == XmlPullParser.TEXT) {
					return xml.getText().trim();
				}
				// 文档结束状态
				else if (eventType == XmlPullParser.END_DOCUMENT) {
					// 文档分析结束后，退出 while 循环
					break;
				}
				eventType = xml.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取指定名称的标签
	 * 
	 * @return	true:到达这个标签  false没有这个标签
	 */
	public static boolean gotoTagByTagName(XmlPullParser xml, String tagName) {
		try {
			int eventType = xml.next();
			String key = null;
			while (true) {
				// 标签开始状态
				if (eventType == XmlPullParser.START_TAG) {
					key = xml.getName();
					if (key.trim().equals(tagName)) {
						return true;
					}
				}
				// 文档结束状态
				else if (eventType == XmlPullParser.END_DOCUMENT) {
					// 文档分析结束后，退出 while 循环
					return false;
				}
				// 切换到下一个状态，并获得当前状态的类型
				eventType = xml.next();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
