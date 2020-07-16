package me.giverplay.tictactoe;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, MouseListener
{
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 307;
	private static final int HEIGHT = 330;
	private static final int TILE = 100;
	
	private int[][] tabuleiro = new int[3][3];
	
	private Thread thread;
	private JFrame frame;
	
	private boolean isRunning = false;
	
	public static void main(String[] args)
	{
		new Game().start();
	}
	
	public Game()
	{
		setupWindow();
	}
	
	public void setupWindow()
	{
		frame = new JFrame("Game 11 - TicTacToe");
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.add(this);
		frame.setVisible(true);
	}
	
	public synchronized void start()
	{
		isRunning = true;
		thread = new Thread(this, "Main Thread");
		thread.start();
	}
	
	@Override
	public void run()
	{
		requestFocus();
		
		long lastTime = System.nanoTime();
		long now;
		
		double updates = 60.0D;
		double delta = 0.0D;
		double update = 1000000000 / updates;
		
		while (isRunning)
		{
			now = System.nanoTime();
			delta += (now - lastTime) / update;
			lastTime = now;
			
			if (delta >= 1)
			{
				update();
				render();
				
				delta--;
			}
		}
	}
	
	private void update()
	{
		
	}
	
	private void render()
	{
		BufferStrategy bs = getBufferStrategy();
		
		if (bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		draw(g);
	
		bs.show();	
	}
	
	private void draw(Graphics g)
	{
		for(int xx = 0; xx < tabuleiro.length; xx++)
		{
			for(int yy = 0; yy < tabuleiro.length; yy++)
			{
				g.setColor(Color.BLACK);
				g.drawRect(TILE * xx, TILE * yy, TILE, TILE);
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent arg0)
	{
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0){}

	@Override
	public void mouseEntered(MouseEvent arg0){}

	@Override
	public void mouseExited(MouseEvent arg0){}

	@Override
	public void mouseReleased(MouseEvent arg0){}
}
