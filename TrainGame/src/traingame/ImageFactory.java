package traingame;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * 工厂类
 * @author Leslie Leung
 */
public class ImageFactory {
	private static ClassLoader loader;		//定义ClassLoader变量

	static {	
		loader = ImageFactory.class.getClassLoader();	//初始化loader
	}

	/* 返回Image资源对象 */
	public static Image createImage(String ImageName) {
		ImageIcon icon = new ImageIcon(loader.getResource("resource/"+ImageName + ".png"));
		return icon.getImage();
	}
}
