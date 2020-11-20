import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Appearance_inversion { // res폴더에 있는 appearance_inversion 파일을 읽어옴
	
	private BufferedImage appearance_inversion;
	
	public Appearance_inversion(String path) {
		try {
			appearance_inversion = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public BufferedImage getAppearance(int x, int y) {
		return appearance_inversion.getSubimage(x, y, 32, 32);
	}
	
}
