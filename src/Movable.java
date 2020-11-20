import java.awt.Rectangle;

public abstract class Movable extends Rectangle {
	
	public boolean right;
	public boolean left;
	public boolean up;
	public boolean down;
	protected int speed;
	protected int imageIndex = 0;
	
	protected boolean canMove(int next_x, int next_y) { 
		
		Rectangle bounds = new Rectangle(next_x, next_y, width, height);
		Map map = Game.map;
		
		for(int i=0;i<map.tiles.length;i++){
			for(int j=0;j<map.tiles[0].length;j++){
				if(map.tiles[i][j]!=null){
					if(bounds.intersects(map.tiles[i][j])) {
						return false;
					}
				}
			}
		}
		
		return true;
    }
    
    public abstract void direction();
	
	public void tick() { 
		if(right&&canMove(x+speed, y)){
			x=x+speed;
			imageIndex = 0;
		}
		if(left&&canMove(x-speed, y)) {
			x=x-speed;
			imageIndex = 1;
		}
		if(up&&canMove(x, y-speed)) {
			y=y-speed;
			imageIndex = 2;
		}
		if(down&&canMove(x, y+speed)) {
			y=y+speed;
			imageIndex = 3;
		}
	}
	
	public void render() {
		
	}
}
