package ppkg;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Potion extends Seed{
	public Potion(int x, int y) {
		super(x, y);
	}
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(x+12, y+12, width, height);
	}

}


