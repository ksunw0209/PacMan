package ppkg;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener {

	private boolean isRunning = false;

	public static final int WIDTH = 1340;
	public static final int HEIGHT = 960;
	public static final String TITLE = "Pacman";

	private Thread thread;

	public static Pacman pacman;
	public static Map map;
	public static Appearance appearance;
	public static Appearance_inversion appearance_inversion;
	public static Score score;
	// 모드
	public static final int START = 0;
	public static final int GAME = 1;
	public static final int SELECT_MODE = 2;
	public static boolean DEAD = false;
	public static boolean WIN = false;

	public static int STATE = -1;
	public static int MODE = -1;

	public boolean isEnter = false;

	private int time = 0;
	private int targetFrames = 35;
	private boolean showText = true;

	/********************************************************************************************************/

	public Game() {
		Dimension dimension = new Dimension(Game.WIDTH+150, Game.HEIGHT);
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		addKeyListener(this);

		STATE = SELECT_MODE;
		WIN = false;
		DEAD = false;

		score = new Score("res\\scoreboard\\scoreboard.txt");
		pacman = new Pacman(Game.WIDTH / 2, Game.HEIGHT / 2); // 모니터 정 중앙에 배치
		map = new Map("/map/map.png");
		appearance = new Appearance("/appearance/appearance.png");
		appearance_inversion = new Appearance_inversion("/appearance/appearance_inversion.png");

		new Character();
	}

	/********************************************************************************************************/

	public synchronized void start() {
		if (isRunning)
			return;
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	/********************************************************************************************************/

	public synchronized void stop() {
		if (!isRunning)
			return;
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/********************************************************************************************************/

	private void render() { // tick에서 변화시킨 클래스를 실제로 보여줌
		BufferStrategy bs = getBufferStrategy(); // 화면이 찢어지거나 깜빡거리는 현상을 방지하기 위함

		if (bs == null) {
			createBufferStrategy(3); // 버퍼를 3개 활용 -> 그림을 그리는 것 보다 화면에 표출하는 것이 빠른 현상을 방지함
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

		if (STATE == GAME) {
			pacman.render(g);
			map.render(g);
			
			g.setColor(new Color(0, 0, 150));
			g.fillRect(WIDTH, 0, 150, HEIGHT);

			g.setColor(Color.white);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
			g.drawString("SCORE", WIDTH + 15, 50);
			
			score.render(g);
		}

		else if (STATE == SELECT_MODE) {
			int menu_width = 600;
			int menu_height = 400;
			int xx = Game.WIDTH / 2 - menu_width / 2;
			int yy = Game.HEIGHT / 2 - menu_height / 2;
			g.setColor(new Color(0, 0, 150));
			g.fillRect(xx, yy, menu_width, menu_height);

			g.setColor(Color.white);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
			g.drawString("Select Mode", xx + 200, yy + 100);
			g.drawString("Press 0: Easy", xx + 200, yy + 170);
			g.drawString("Press 1: Medium ", xx + 200, yy + 210);
			g.drawString("Press 2: Hard", xx + 200, yy + 250);

		}

		else if (STATE == START) { // 다시 시작해야 하거나 시작하는 상황
			int menu_width = 600;
			int menu_height = 400;
			int xx = Game.WIDTH / 2 - menu_width / 2;
			int yy = Game.HEIGHT / 2 - menu_height / 2;
			g.setColor(new Color(0, 0, 150));
			g.fillRect(xx, yy, menu_width, menu_height);

			g.setColor(Color.white);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 25));

			if (showText) { // 시작화면
				if (DEAD) { // 죽었을때
					g.drawString("YOU ARE DEAD", xx + 200, yy + 100);
					g.drawString("Your Score is " + score.score, xx + 190, yy + 140);
					g.drawString("Your Rank is " + score.rank, xx + 200, yy + 180);
					g.drawString("Press enter to restart the game", xx + 122, yy + 250);
				} else if (WIN) {
					g.drawString("YOU WIN!!", xx + 200, yy + 100);
					g.drawString("Your Score is " + score.score, xx + 190, yy + 140);
					g.drawString("Your Rank is " + score.rank, xx + 200, yy + 180);
					g.drawString("Press enter to start the game", xx + 125, yy + 180);
				} else // 이겼을때
					g.drawString("Press enter to start the game", xx + 125, yy + 200);
			}
		}

		g.dispose(); // 화면에 있던 그래픽을 새걸로 바꾸기 위해 예전 그래픽 지움
		bs.show();
	}

	/********************************************************************************************************/

	private void tick() { // 거의 모든 클래스에 있는 tick 함수는 각 클래스의 상태를 변화시킴
		if (STATE == GAME) {
			pacman.tick();
			map.tick();
		} else if (STATE == SELECT_MODE) {
			time++;
			if (time == targetFrames) {
				time = 0;
				if (showText) {
					showText = false;
				} else
					showText = true;
			} // 시작하거나 죽었을 때 문구를 깜빡거리도록 함
			if (MODE != -1) {
				STATE = START;
			}

		} else if (STATE == START) {
			time++;
			if (time == targetFrames) {
				time = 0;
				if (showText) {
					showText = false;
				} else
					showText = true;
			} // 시작하거나 죽었을 때 문구를 깜빡거리도록 함
			
			if(DEAD) {
				if(isEnter) {
					isEnter=false;
					STATE = SELECT_MODE;
					MODE=-1;
					DEAD = false;
				}
			}

			else if (isEnter) { // 엔터를 눌렀으면 게임 새로 시작함
				isEnter = false;
				if (MODE != -1) {
					pacman = new Pacman(Game.WIDTH / 2, Game.HEIGHT / 2);
					map = new Map("/map/map.png", MODE);
					appearance = new Appearance("/appearance/appearance.png");
					appearance_inversion = new Appearance_inversion("/appearance/appearance_inversion.png");
					time=0;
				}
				STATE = GAME;				
			}
		}
	}

	/********************************************************************************************************/

	public static void main(String[] args) {
		Game game = new Game();
		JFrame frame = new JFrame();
		frame.setTitle(Game.TITLE);
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
		game.start();
	}

	/*****************************************
	 * 스레드 실행 함수 / fps관리
	 ************************************************/

	public void run() {

		requestFocus();
		int fps = 0; // fps가 항상 100이 되도록 즉 1/100초에 한번씩 화면에 새로운 그래픽을 띄우도록 함
		double timer = System.currentTimeMillis();
		long lastTime = System.nanoTime();
		double targetTick = 100.0;
		double delta = 0;
		double ns = 1000000000 / targetTick;

		while (isRunning) {
			long now = System.nanoTime();
			delta = delta + ((now - lastTime) / ns);
			lastTime = now;

			while (delta >= 1) { // render를 tick보다 더 많이 부르지 않으면 synchronized가 되지 않음
				tick();
				render();
				fps++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) { // 1초보다 크면
				fps = 0;
				timer = timer + 1000;
			}

		}
		stop();
	}

	/******************************************************
	 * 키보드 입력 관리
	 *******************************************************/

	public void keyPressed(KeyEvent e) {
		if (STATE == GAME) {

			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				pacman.move_queue = Movement.RIGHT;
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				pacman.move_queue = Movement.LEFT;
			}

			if (e.getKeyCode() == KeyEvent.VK_UP) {
				pacman.move_queue = Movement.UP;
			}

			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				pacman.move_queue = Movement.DOWN;
			}
		}

		else if (STATE == START) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				isEnter = true;
			}
		}

		else if (STATE == SELECT_MODE) {
			if (e.getKeyCode() == KeyEvent.VK_0)
				MODE = 0;
			if (e.getKeyCode() == KeyEvent.VK_1)
				MODE = 1;
			if (e.getKeyCode() == KeyEvent.VK_2)
				MODE = 2;
		}
	}

	public void keyReleased(KeyEvent e) {
		/*
		 * if (e.getKeyCode() == KeyEvent.VK_RIGHT) pacman.right = false; if
		 * (e.getKeyCode() == KeyEvent.VK_LEFT) pacman.left = false; if (e.getKeyCode()
		 * == KeyEvent.VK_UP) pacman.up = false; if (e.getKeyCode() == KeyEvent.VK_DOWN)
		 * pacman.down = false;
		 */
	}

	public void keyTyped(KeyEvent e) {

	}
}
