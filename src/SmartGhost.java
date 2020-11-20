import java.awt.*;
import java.util.Random;

public class SmartGhost extends Rectangle{
	
	// ������� <-> ����Ʈ���
	private int random = 0;
	private int smart = 1;
	private int find_path = 2;	// ���� ã�� ���
	
	private int state = smart;
	
	private int right = 0;
	private int left = 1;
	private int up = 2;
	private int down = 3;
	
	private int direction = -1;
	
	public Random randomMaker;
	
	private int time = 0;
	
	private int changingTime = 60 * 5;
	
	private int speed = 1;
	
	private int lastDirection = -1;
	
	private int imageIndex = 0;
	
	public SmartGhost(int x, int y) {
		randomMaker = new Random();
		setBounds(x, y, 32, 32);
		direction = randomMaker.nextInt(4);
	}
	
	public void tick() {
		// �������� ���� �����ϱ� -> �Ϲ� ���ɰ� ���� ����
		imageIndex = 0;
		// �������
		if(state == random) {
			if(direction == right) {
				if(canMove(x + speed, y)) {
					x=x+speed;
					if(Pacman.hasPotion==false)
						imageIndex = 0;
					else
						imageIndex = 8;
					lastDirection=right;
				}
				else {
					direction = randomMaker.nextInt(4);
				}
			}
			else if(direction == left) {
				if(canMove(x - speed, y)) {
					x=x-speed;
					if(Pacman.hasPotion==false)
						imageIndex = 1;
					else
						imageIndex = 9;
					lastDirection=left;
				}
				else {
					direction = randomMaker.nextInt(4);
				}
			}
			else if(direction == up) {
				if(canMove(x, y - speed)) {
					y=y-speed;
					if(Pacman.hasPotion==false)
						imageIndex = 2;
					else
						imageIndex = 10;
					lastDirection=up;
				}
				else {
					direction = randomMaker.nextInt(4);
				}
			}
			else if(direction == down) {
				if(canMove(x, y + speed)) {
					y=y+speed;
					if(Pacman.hasPotion==false)
						imageIndex = 3;
					else
						imageIndex = 11;
					lastDirection=down;
				}
				else {
					direction = randomMaker.nextInt(4);
				}
			}
			
			time++;
			
			if(time == changingTime) {
				state = smart;
				time = 0;
			}
		}
		
		// ����Ʈ���
		else if(state == smart) {
			// follow the player!
			if(Pacman.hasPotion==false)
				imageIndex = 1;
			else
				imageIndex = 9;
			boolean move = false;
			
			if(x < Game.pacman.x) {
				if(canMove(x + speed, y)) {
					x=x+speed;
					if(Pacman.hasPotion==false)
						imageIndex = 4;
					else
						imageIndex = 12;
					move = true;
					lastDirection = right;
				}
			}
			
			if(x > Game.pacman.x) {
				if(canMove(x - speed, y)) {
					x=x-speed;
					if(Pacman.hasPotion==false)
						imageIndex = 5;
					else
						imageIndex = 13;
					move = true;
					lastDirection = left;
				}
			}
			
			if(y < Game.pacman.y) {
				if(canMove(x, y + speed)) {
					y=y+speed;
					if(Pacman.hasPotion==false)
						imageIndex = 7;
					else
						imageIndex = 15;
					move = true;
					lastDirection = down;
				}
			}
			
			if(y > Game.pacman.y) {
				if(canMove(x, y - speed)) {
					y=y-speed;
					if(Pacman.hasPotion==false)
						imageIndex = 6;
					else
						imageIndex = 14;
					move = true;
					lastDirection = up;
				}
			}
			
			if(x == Game.pacman.x && y == Game.pacman.y) { // �Ѹ��� ã��
				move = true;
			}
			
			if(!move) {
				state = find_path;
			}
			
			time++;
			
			// 5�ʸ��� ��� ����
			if(time == changingTime) {
				state = random;
				time = 0;
			}
		}
		
		// ���� ã��
		else if(state == find_path) {
			imageIndex = 4;
			if(lastDirection == right) {
				if(y < Game.pacman.y) {
					if(canMove(x, y + speed)) {
						y=y+speed;
						if(Pacman.hasPotion==false)
							imageIndex = 7;
						else
							imageIndex = 15;
						state = smart;
						lastDirection = up;
					}							
				}
				else {
					if(canMove(x, y - speed)) {
						y=y-speed;
						if(Pacman.hasPotion==false)
							imageIndex = 6;
						else
							imageIndex = 14;
						state = smart;
						lastDirection = down;
					}
				}
				if(canMove(x + speed, y)) {
					x=x+speed;
					if(Pacman.hasPotion==false)
						imageIndex = 4;
					else
						imageIndex = 12;
					lastDirection = right;
				}
				else {
					lastDirection=randomMaker.nextInt(4);
				}
			}
			else if(lastDirection == left) {
				if(y < Game.pacman.y) {
					if(canMove(x, y + speed)) {
						y=y+speed;
						if(Pacman.hasPotion==false)
							imageIndex = 7;
						else
							imageIndex = 15;
						state = smart;
						lastDirection = down;
					}							
				}
				else {
					if(canMove(x, y - speed)) {
						y=y-speed;
						if(Pacman.hasPotion==false)
							imageIndex = 6;
						else
							imageIndex = 14;
						state = smart;
						lastDirection = up;
						
					}
				}
				if(canMove(x - speed, y)) {
					x=x-speed;
					if(Pacman.hasPotion==false)
						imageIndex = 5;
					else
						imageIndex = 13;
					lastDirection = left;
				}
				else {
					lastDirection=randomMaker.nextInt(4);
				}
			}
			else if(lastDirection == up) {
				if(x < Game.pacman.x) {
					if(canMove(x + speed, y)) {
						x=x+speed;
						if(Pacman.hasPotion==false)
							imageIndex = 4;
						else
							imageIndex = 12;
						state = smart;
						lastDirection = right;
					}							
				}
				else {
					if(canMove(x - speed, y)) {
						x=x-speed;
						if(Pacman.hasPotion==false)
							imageIndex = 5;
						else
							imageIndex = 13;
						state = smart;
						lastDirection = left;
					}
				}
				if(canMove(x, y - speed)) {
					y=y-speed;
					if(Pacman.hasPotion==false)
						imageIndex = 6;
					else
						imageIndex = 14;
					lastDirection = up;
				}
				else {
					lastDirection=randomMaker.nextInt(4);
				}
			}
			else if(lastDirection == down) {
				if(x < Game.pacman.x) {
					if(canMove(x + speed, y)) {
						x=x+speed;
						if(Pacman.hasPotion==false)
							imageIndex = 4;
						else
							imageIndex = 12;
						state = smart;
						lastDirection = right;
					}							
				}
				else {
					if(canMove(x - speed, y)) {
						x=x-speed;
						if(Pacman.hasPotion==false)
							imageIndex = 5;
						else
							imageIndex = 13;
						state = smart;
						lastDirection = left;
					}
				}
				if(canMove(x, y + speed)) {
					y=y+speed;
					if(Pacman.hasPotion==false)
						imageIndex = 7;
					else
						imageIndex = 15;
					lastDirection = down;
				}	
				else {
					lastDirection=randomMaker.nextInt(4);
				}
			}
			
			time++;
			
			// 5�ʸ��� ��� ����
			if(time == changingTime) {
				state = random;
				time = 0;
			}
		}
	}
	
	// �̵����ɿ����Ǵ�
	private boolean canMove(int next_x, int next_y) {
		
		Rectangle bounds = new Rectangle(next_x, next_y, width, height);
		Map map = Game.map;
		
		for(int i=0;i<map.tiles.length;i++) {
			for(int j=0;j<map.tiles[0].length;j++) {
				if(map.tiles[i][j]!=null) {
					if(bounds.intersects(map.tiles[i][j])) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public void render(Graphics g) {
		g.drawImage(Character.smartGhost[imageIndex], x, y, width, height, null);
	}
}
