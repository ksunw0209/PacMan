package ppkg;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Pacman extends Movable { // 사이즈나 포지션 관리를 쉽게 하기 위해 extends Rectangle

	private Movement move = Movement.NOOP;
	private Movement move_queue = Movement.NOOP;

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
	
	public void setMoveQueue(Movement move) {
		move_queue = move;
	}

	public boolean canMove(Movement move) {
		switch (move) {
		case RIGHT:
			return canMove(x + speed, y);
		case LEFT:
			return canMove(x - speed, y);
		case UP:
			return canMove(x, y - speed);
		case DOWN:
			return canMove(x, y + speed);
		default:
			return true;
		}
	}

	public void tick() { // 각 방향으로 움직일 때마다 이미지를 바꿈

		// 더 이상 이동중인 방향으로 갈 수 없을 때
		if (!canMove(move)) {
			move = move_queue;
		}

		// 전환할 방향이 대기중이고, 전환 가능할 때
		if (move != move_queue && canMove(move_queue)) {
			move = move_queue;
		}

		if (move == Movement.RIGHT && canMove(Movement.RIGHT)) {
			x = x + speed;
			hasPotion = (System.nanoTime()) / 1000000000 - startTime <= 4;
			if (hasPotion == false) {
				imageIndex = 0;
			} else {
				imageIndex = 4;
			}
		}
		if (move == Movement.LEFT && canMove(Movement.LEFT)) {
			x = x - speed;
			hasPotion = (System.nanoTime()) / 1000000000 - startTime <= 4;
			if (hasPotion == false) {
				imageIndex = 1;
			} else
				imageIndex = 5;
		}
		if (move == Movement.UP && canMove(Movement.UP)) {
			y = y - speed;
			hasPotion = (System.nanoTime()) / 1000000000 - startTime <= 4;
			if (hasPotion == false)
				imageIndex = 2;
			else
				imageIndex = 6;
		}
		if (move == Movement.DOWN && canMove(Movement.DOWN)) {
			y = y + speed;
			hasPotion = (System.nanoTime()) / 1000000000 - startTime <= 4;
			if (hasPotion == false)
				imageIndex = 3;
			else
				imageIndex = 7;
		}

		Map map = Game.map;

		for (int i = 0; i < map.seeds.size(); i++) { // 팩맨과 seed가 겹치면 seed는 사라짐
			Seed seed = map.seeds.get(i);
			if (this.intersects(seed)) {
				if (seed instanceof Potion) {
					curScore.score += 50;
					map.seeds.remove(i);
					startTime = (System.nanoTime()) / 1000000000;
					hasPotion = true;
					break;
				} else if (seed instanceof Cherry) {
					curScore.score += 100;
					map.seeds.remove(i);
					break;
				} else {
					curScore.score += 10;
					map.seeds.remove(i);
					break;
				}
			}
		}

		if (map.seeds.size() == 0) {
			// win
			// seeds를 다 먹음
			Game.STATE = Game.START;
			Game.WIN = true;
			curScore.insertScore();
			return;
		}

		for (int i = 0; i < Game.map.Ghosts.size(); i++) {
			// lose
			// 일반 유령에게 잡힘
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
		
		// Portal
		super.tick();

	}

	public void render(Graphics g) {
		g.drawImage(Character.pacman[imageIndex], x, y, width, height, null);
	}
}
