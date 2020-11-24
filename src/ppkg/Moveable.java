package ppkg;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Moveable extends Rectangle {

	// 이동가능여부판단
	protected boolean canMove(int next_x, int next_y) {

		Rectangle bounds = new Rectangle(next_x, next_y, width, height);
		Map map = Game.map;

		for (int i = 0; i < map.tiles.length; i++) {
			for (int j = 0; j < map.tiles[0].length; j++) {
				if (map.tiles[i][j] != null) {
					if (bounds.intersects(map.tiles[i][j])) {
						return false;
					}
				}
			}
		}

		return true;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
}
