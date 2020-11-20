import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Pacman extends Rectangle { // ����� ������ ������ ���� �ϱ� ���� extends Rectangle

	public boolean right;
	public boolean left;
	public boolean up;
	public boolean down;
	private int speed = 3;
	private int imageIndex = 0;
	public long startTime = 0;
	public static boolean hasPotion = false;
	Score curScore;

	public Pacman(int x, int y) {
		curScore = Game.score;
		curScore.score = 0;
		setBounds(x, y, 30, 30);
	}

	private boolean canMove(int next_x, int next_y) { // Ghost�� ���� �Լ� ���� ������ �������� ����

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

	public void tick() { // �� �������� ������ ������ �̹����� �ٲ�

		if (right && canMove(x + speed, y)) {

			hasPotion = (System.nanoTime()) / 1000000000 - startTime <= 4;
			if (hasPotion == false) {
				x = x + speed;
				imageIndex = 0;
			}

			else {
				imageIndex = 4;
				x = x + speed;
			}
		}
		if (left && canMove(x - speed, y)) {
			x = x - speed;
			hasPotion = (System.nanoTime()) / 1000000000 - startTime <= 4;
			if (hasPotion == false) {
				imageIndex = 1;
				x = x-speed;
			}
			else
				imageIndex = 5;
		}
		if (up && canMove(x, y - speed)) {
			y = y - speed;
			hasPotion = (System.nanoTime()) / 1000000000 - startTime <= 4;
			if (hasPotion == false)
				imageIndex = 2;
			else
				imageIndex = 6;
		}
		if (down && canMove(x, y + speed)) {
			y = y + speed;
			hasPotion = (System.nanoTime()) / 1000000000 - startTime <= 4;
			if (hasPotion == false)
				imageIndex = 3;
			else
				imageIndex = 7;
		}

		Map map = Game.map;

		for (int i = 0; i < map.seeds.size(); i++) { // �Ѹǰ� seed�� ��ġ�� seed�� �����
			if (this.intersects(map.seeds.get(i))) {
				curScore.score += 10;
				map.seeds.remove(i);
				break;
			}
		}

		for (int i = 0; i < map.potions.size(); i++) { // �Ѹǰ� potion�� ��ġ�� potion�� �����
			if (this.intersects(map.potions.get(i))) {
				curScore.score += 50;
				map.potions.remove(i);
				startTime = (System.nanoTime()) / 1000000000;
				hasPotion = true;
				break;
			}
		}

		if (map.seeds.size() == 0) {
			// win
			// seeds�� �� ����
			Game.STATE = Game.START;
			return;
		}

		for (int i = 0; i < Game.map.Ghosts.size(); i++) {
			// lose
			// �Ϲ� ���ɿ��� ����
			Ghost temp = Game.map.Ghosts.get(i);

			if (temp.intersects(this)) {
				hasPotion = (System.nanoTime()) / 1000000000 - startTime <= 4;

				if (hasPotion == false) {
					Game.STATE = Game.START;
					Game.DEAD = true;
					curScore.insertScore();
					return;
				} else {
					curScore.score += 200;
					map.Ghosts.remove(i);
					break;
				}

			}
		}
		for (int i = 0; i < Game.map.SmartGhosts.size(); i++) {
			// lose
			// �ȶ��� ���ɿ��� ����
			SmartGhost temp = Game.map.SmartGhosts.get(i);
			if (temp.intersects(this)) {
				hasPotion = (System.nanoTime()) / 1000000000 - startTime <= 4;

				if (hasPotion == false) {
					Game.STATE = Game.START;
					Game.DEAD = true;
					curScore.insertScore();
					return;
				} else {
					curScore.score += 200;
					map.SmartGhosts.remove(i);
				}

			}
		}
	}

	public void render(Graphics g) {
		g.drawImage(Character.pacman[imageIndex], x, y, width, height, null);
	}
}
