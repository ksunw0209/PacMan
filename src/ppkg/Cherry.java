package ppkg;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Cherry extends Seed{

	private static Appearance appearance = new Appearance("/appearance/cherry.png");
	private static BufferedImage image = appearance.getAppearance(0, 0, 64, 64);
	
	public Cherry(int x, int y) {
		super(x, y);
		
	}
	public void render(Graphics g) {		
			g.drawImage(image, x, y, 32, 32, null);	
	}
	
}


