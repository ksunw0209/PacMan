package ppkg;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Potion extends Seed{
	
	public long startTime = 0;
	private double switchTime = 7;
	private boolean showText = true;
	
	public Potion(int x, int y) {
		super(x, y);
	}
	public void render(Graphics g) {
		if(showText == true) {
			g.setColor(Color.RED);
			g.fillRect(x+6, y+6, 18, 18);
		}		
	}
	
	public void tick() {
		if ((double)((System.nanoTime()) / 100000000 - startTime)>switchTime) {
			startTime = (System.nanoTime()) / 100000000;
			if (showText) {
				showText = false;
			} else
				showText = true;
			System.out.println((System.nanoTime()) / 100000000);
			System.out.println(startTime);
		} // ±ôºý°Å¸²
	}
}


