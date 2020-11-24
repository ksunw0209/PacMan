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
	// ���
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
		pacman = new Pacman(Game.WIDTH / 2, Game.HEIGHT / 2); // ����� �� �߾ӿ� ��ġ
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

	private void render() { // tick���� ��ȭ��Ų Ŭ������ ������ ������
		BufferStrategy bs = getBufferStrategy(); // ȭ���� �������ų� �����Ÿ��� ������ �����ϱ� ����

		if (bs == null) {
			createBufferStrategy(3); // ���۸� 3�� Ȱ�� -> �׸��� �׸��� �� ���� ȭ�鿡 ǥ���ϴ� ���� ���� ������ ������
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

		else if (STATE == START) { // �ٽ� �����ؾ� �ϰų� �����ϴ� ��Ȳ
			int menu_width = 600;
			int menu_height = 400;
			int xx = Game.WIDTH / 2 - menu_width / 2;
			int yy = Game.HEIGHT / 2 - menu_height / 2;
			g.setColor(new Color(0, 0, 150));
			g.fillRect(xx, yy, menu_width, menu_height);

			g.setColor(Color.white);
			g.setFont(new Font(Font.DIALOG, Font.BOLD, 25));

			if (showText) { // ����ȭ��
				if (DEAD) { // �׾�����
					g.drawString("YOU ARE DEAD", xx + 200, yy + 100);
					g.drawString("Your Score is " + score.score, xx + 190, yy + 140);
					g.drawString("Your Rank is " + score.rank, xx + 200, yy + 180);
					g.drawString("Press enter to restart the game", xx + 122, yy + 250);
				} else if (WIN) {
					g.drawString("YOU WIN!!", xx + 200, yy + 100);
					g.drawString("Your Score is " + score.score, xx + 190, yy + 140);
					g.drawString("Your Rank is " + score.rank, xx + 200, yy + 180);
					g.drawString("Press enter to start the game", xx + 125, yy + 180);
				} else // �̰�����
					g.drawString("Press enter to start the game", xx + 125, yy + 200);
			}
		}

		g.dispose(); // ȭ�鿡 �ִ� �׷����� ���ɷ� �ٲٱ� ���� ���� �׷��� ����
		bs.show();
	}

	/********************************************************************************************************/

	private void tick() { // ���� ��� Ŭ������ �ִ� tick �Լ��� �� Ŭ������ ���¸� ��ȭ��Ŵ
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
			} // �����ϰų� �׾��� �� ������ �����Ÿ����� ��
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
			} // �����ϰų� �׾��� �� ������ �����Ÿ����� ��
			
			if(DEAD) {
				if(isEnter) {
					isEnter=false;
					STATE = SELECT_MODE;
					MODE=-1;
					DEAD = false;
				}
			}

			else if (isEnter) { // ���͸� �������� ���� ���� ������
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
	 * ������ ���� �Լ� / fps����
	 ************************************************/

	public void run() {

		requestFocus();
		int fps = 0; // fps�� �׻� 100�� �ǵ��� �� 1/100�ʿ� �ѹ��� ȭ�鿡 ���ο� �׷����� ��쵵�� ��
		double timer = System.currentTimeMillis();
		long lastTime = System.nanoTime();
		double targetTick = 100.0;
		double delta = 0;
		double ns = 1000000000 / targetTick;

		while (isRunning) {
			long now = System.nanoTime();
			delta = delta + ((now - lastTime) / ns);
			lastTime = now;

			while (delta >= 1) { // render�� tick���� �� ���� �θ��� ������ synchronized�� ���� ����
				tick();
				render();
				fps++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) { // 1�ʺ��� ũ��
				fps = 0;
				timer = timer + 1000;
			}

		}
		stop();
	}

	/******************************************************
	 * Ű���� �Է� ����
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
