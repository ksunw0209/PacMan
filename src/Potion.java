import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Potion extends Rectangle{
	public Potion(int x, int y) {
		setBounds(x+12, y+12, 8, 8);
	}
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(x, y, width, height);
	}

}


