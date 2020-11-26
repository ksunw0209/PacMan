package ppkg;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Ghost extends Movable{
	
	private int right = 0;
	private int left = 1;
	private int up = 2;
	private int down = 3;
	private int direction = -1;
	
	private int imageIndex=0;
	
	public Random randomDirection;
	
	protected int speed = 1;
	
	public Ghost(int x, int y, int speed) {
		this.speed = speed;
		randomDirection = new Random();
		setBounds(x, y, 32, 32);
		direction = randomDirection.nextInt(4);
	}
	
	public void tick() { // 랜덤으로 방향을 생성해서 만약 움직일 수 있다면 이동, 움직일 수 없다면 다시 방향을 생성함
		if(direction==right) {
			if(canMove(x+speed, y)) {
				x=x+speed;
				if(Pacman.hasPotion==false)
					imageIndex = 0;
				else
					imageIndex = 4;
			}
			else
				direction = randomDirection.nextInt(4);
		}
		else if(direction==left) {
			if(canMove(x-speed, y)) {
				x=x-speed;
				if(Pacman.hasPotion==false)
					imageIndex = 1;
				else
					imageIndex = 5;
			}
			else
				direction = randomDirection.nextInt(4);
		}
		else if(direction==up) {
			if(canMove(x, y-speed)) {
				y=y-speed;
				if(Pacman.hasPotion==false)
					imageIndex = 2;
				else
					imageIndex = 6;
			}
			else
				direction = randomDirection.nextInt(4);
		}
		else if(direction==down) {
			if(canMove(x, y+speed)) {
				y=y+speed;
				if(Pacman.hasPotion==false)
					imageIndex = 3;
				else
					imageIndex = 7;
			}
			else
				direction = randomDirection.nextInt(4);
		}
		
		// Portal
		super.tick();
	}
	
	public void render(Graphics g) {
		g.drawImage(Character.ghost[imageIndex], x, y, null);
	}
}
