package ppkg;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Movable extends Rectangle {

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
	
	public void tick() {
		Map map = Game.map;
		
		// Portal
		int map_width = map.width;
		int map_height = map.height;
		
		int left_end = 32;
		int up_end = 32;
		int right_end = map_width * 32;
		int down_end = map_height * 32;
		
		if (x < left_end) {
			x = right_end;
		}
		else if (x > right_end) {
			x = left_end;
		}
		else if (y < up_end) {
			y = down_end;
		}
		else if (y > down_end) {
			y = up_end;
		}
	}
	
	public abstract void render(Graphics g);
}
