package ppkg;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class Map {
	public int width; // 40
	public int height; //25
	
	public Tile[][] tiles;
	
	public List<Seed> seeds; //contains seeds and potions
	//public List<Potion> potions;
	public List<Ghost> Ghosts; //contains ghost and smartghosts
	//public List<SmartGhost> SmartGhosts;
	
	private HashSet<Integer> randset = new HashSet<>();
	
	private int location=30;
	private static int cherry_num = 10;
	private int potion_num;
	private int smart_num;
	private int speed;
	
	public Map(String path, int mode) {
		this(path);
		if(mode==0) {//easy
			potion_num = 10;	
			smart_num = 2;
			speed = 1;
		}else if(mode==1) {//medium			
			potion_num = 6;
			smart_num = 4;
			speed = 1;
		}else if(mode==2) {//hard
			potion_num = 4;
			smart_num = 6;
			speed = 2;
		}
		
		//random generate potion & cherry
		Random rd = new Random();
		while(randset.size()<potion_num+cherry_num) {
			int nextint = rd.nextInt(seeds.size());
			randset.add(nextint);
		}
		ArrayList<Integer> randlist = new ArrayList<>(randset);
		for(int i=0; i<potion_num; i++) {
			Seed seed = seeds.get(randlist.get(i));
			seed  = new Potion(seed.x, seed.y);
			seeds.set(randlist.get(i), seed);
		}
		
		for(int i=potion_num; i<potion_num+cherry_num; i++) {
			Seed seed = seeds.get(randlist.get(i));
			seed  = new Cherry(seed.x, seed.y);
			seeds.set(randlist.get(i), seed);
		}
		
		for(int i=0; i<Ghosts.size(); i++) {
			if(i<smart_num) Ghosts.set(i, new SmartGhost(Ghosts.get(i).x, Ghosts.get(i).y, speed));
			else Ghosts.set(i, new Ghost(Ghosts.get(i).x, Ghosts.get(i).y, speed));			
		}
	}
	
	public Map(String path) { // map(png)이 저장되어 있는 경로를 생성자로 받음
		try {
			seeds = new ArrayList<>();
			//potions = new ArrayList<>();
			Ghosts = new ArrayList<>();
			//SmartGhosts = new ArrayList<>();
			
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			this.width=map.getWidth();
			this.height=map.getHeight();
			
			int[] pixels = new int[width*height]; // 포토샵으로 그려놓은 맵
			// pixels 배열에 map.png의 픽셀의 색을 일렬화(세로방향으로) 해서 저장함
			map.getRGB(0, 0, width, height, pixels, 0, width);
			
			tiles =new Tile[width][height]; // GUI에 구현될 맵
			for(int i=0;i<width;i++) {
				for(int j=0;j<height;j++) {
					int val = pixels[i+(j*width)];
					if(val==0xFF000A7C) {
						// 벽
						tiles[i][j]=new Tile(i*32+location, j*32+location);
					}
					else if(val==0xFFFFD800) {
						// 팩맨
						Game.pacman.x=i*32+location;
						Game.pacman.y=j*32+location;
					}
					else if(val==0xFFFF0000||val==0xFF00FFFF) {
						// 일반 유령
						Ghosts.add(new Ghost(i*32+location, j*32+location, speed));
					}
					else{
						// 씨앗
						seeds.add(new Seed(i*32+location, j*32+location));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void tick() { // map에서 유령들의 상태를 바꾸고 랜더링 시킴
		for(int i=0;i<Ghosts.size();i++) {
			Ghosts.get(i).tick();
		}
		for(int i=0;i<seeds.size();i++) {
			seeds.get(i).tick();
		}
	}
	
	public void render(Graphics g) { // seeds와 tile은 상태가 바뀔 필요가 없으므로 tick함수가 존재하지 않음
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				if(tiles[i][j]!=null)
					tiles[i][j].render(g);
			}
		}
		
		
		for(int i=0;i<seeds.size();i++) 
			seeds.get(i).render(g);
		for(int i=0;i<Ghosts.size();i++)
			Ghosts.get(i).render(g);
		
	}
}
