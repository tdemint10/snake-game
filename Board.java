//package com.zetcode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	private final int WIDTH = 600;
	private final int HEIGHT = 600;
	private final int DOT_SIZE = 20;
	private final int ALL_DOTS = 900;
	private final int RAND_POS = 29;
	private final int DELAY = 140;

	private final int x[] = new int[ALL_DOTS];
	private final int y[] = new int[ALL_DOTS];

	private int dots;
	private int apple_x;
	private int apple_y;

	private boolean left = false;
	private boolean right = true;
	private boolean up = false;
	private boolean down = false;
	private boolean inGame = true;

	private Timer timer;
	private Image ball;
	private Image apple;
	private Image head;

	public Board() {
		addKeyListener(new TAdapter());
		setBackground(Color.black);
		setFocusable(true);

		setPreferredSize(new Dimension(WIDTH + (WIDTH / 2), HEIGHT));
		loadImages();
		initGame();
	}

	private void loadImages() {
		ImageIcon iid = new ImageIcon("dot_20x20.png");
		ball = iid.getImage();

		ImageIcon iia = new ImageIcon("apple_20x20.png");
		apple = iia.getImage();

		ImageIcon iih = new ImageIcon("dot_20x20.png");
		head = iih.getImage();
	}

	private void initGame() {
		dots = 3;

		for (int i = 0; i < dots; i++) {
			x[i] = 60 - i * 10;
			y[i] = 60;
		}

		locateApple();

		timer = new Timer(DELAY, this);
		timer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);
	}

	private void doDrawing(Graphics g) {
		if (inGame) {
			g.drawImage(apple, apple_x, apple_y, this);

			for (int i = 0; i < dots; i++) {
				if (i==0) {
					g.drawImage(head, x[i], y[i], this);
				}
				else {
					g.drawImage(ball, x[i], y[i], this);
				}
			}

			Toolkit.getDefaultToolkit().sync();
		}
		else {
			gameOver(g);
		}
	}

	private void gameOver(Graphics g) {
		String message = "GAME OVER";
		Font small = new Font("Helvetica", Font.BOLD, 40);
		FontMetrics metric = getFontMetrics(small);

		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(message, (WIDTH - metric.stringWidth(message)) / 2, HEIGHT / 2);
	}

	private void checkApple() {
		if ((x[0] == apple_x) && (y[0] == apple_y)) {
			dots++;
			locateApple();
		}
	}

	private void move() {
		for (int i = dots; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}

		if (left) {
			x[0] -= DOT_SIZE;
		}
		else if (right) {
			x[0] += DOT_SIZE;
		}
		else if (up) {
			y[0] -= DOT_SIZE;
		}
		else if (down) {
			y[0] += DOT_SIZE;
		}
	}

	private void checkCollision() {
		for (int i = dots; i > 0; i--) {
			if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
				inGame = false;
			}
		}

		if (y[0] >= HEIGHT) {
			inGame = false;
		}
		else if (y[0] < 0) {
			inGame = false;
		}
		else if (x[0] >= WIDTH) {
			inGame = false;
		}
		else if (x[0] < 0) {
			inGame = false;
		}

		if (!inGame) {
			timer.stop();
		}
	}

	private void locateApple() {
		int r = (int) (Math.random() * RAND_POS);
		apple_x = r * DOT_SIZE;

		r = (int) (Math.random() * RAND_POS);
		apple_y = r * DOT_SIZE;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (inGame) {
			checkApple();
			checkCollision();
			move();
		}

		repaint();
	}

	private class TAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if ((key == KeyEvent.VK_LEFT) && (!right)) {
				left = true;
				up = false;
				down = false;
			}
			else if ((key == KeyEvent.VK_RIGHT) && (!left)) {
				right = true;
				up = false;
				down = false;
			}
			else if ((key == KeyEvent.VK_UP) && (!down)) {
				up = true;
				left = false;
				right = false;
			}
			else if ((key == KeyEvent.VK_DOWN) && (!up)) {
				down = true;
				left = false;
				right = false;
			}
		}
	}

}