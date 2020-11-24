package ppkg;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Potion extends Seed{
	
	public long startTime = 0;
	private double visible_time = 7;
	private double invisible_time = 3;
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
		long time = (System.nanoTime()) / 100000000;
		if(time-startTime>invisible_time) {
			if(!showText) {
				startTime = time;
				showText = true;
			}else if(time-startTime>visible_time) {
				startTime = time;
				showText = false;
			}
		}
	}
}


