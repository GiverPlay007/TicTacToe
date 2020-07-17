package me.giverplay.tictactoe;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, MouseListener
{
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 307;
	private static final int HEIGHT = 330;
	private static final int TILE = 100;
	private static final int PLAYER = 1;
	private static final int ENEMY = -1;
	private static final int NONE = 0;
	private static final int EMPATE = -10;
	
	private int[][] tabuleiro = new int[3][3];
	
	private BufferedImage cross;
	private BufferedImage circle;
	private Thread thread;
	private JFrame frame;
	
	private boolean isRunning = false;
	private boolean press = false;
	
	private int current = ENEMY;
	private int mx = 0;
	private int my = 0;
	
	public static void main(String[] args)
	{
		new Game().start();
	}
	
	public Game()
	{
		setupAssets();
		setupWindow();
		resetTabuleiro();
	}
	
	public void setupWindow()
	{
		frame = new JFrame("Game 12 - TicTacToe");
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.add(this);
		frame.setVisible(true);
		
		addMouseListener(this);
	}
	
	private void setupAssets()
	{
		try
		{
			cross = ImageIO.read(getClass().getResource("/Cross.png"));
			circle = ImageIO.read(getClass().getResource("/Circle.png"));
		} catch (IOException e)
		{
			System.out.println("Erro ao carregar as imagens");
		}
	}
	
	public void resetTabuleiro()
	{
		for (int xx = 0; xx < tabuleiro.length; xx++)
		{
			for (int yy = 0; yy < tabuleiro.length; yy++)
			{
				tabuleiro[xx][yy] = 0;
			}
		}
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
		if (current == PLAYER)
		{
			if(press)
			{
				press = false;
				mx /= 100;
				my /= 100;
				
				if(tabuleiro[mx][my] == 0)
				{
					tabuleiro[mx][my] = PLAYER;
					current = ENEMY;
				}
			}
		} 
		else if (current == ENEMY)
		{
			for(int xx = 0; xx < tabuleiro.length; xx++) 
			{
				for(int yy = 0; yy < tabuleiro.length; yy++) 
				{
					if(tabuleiro[xx][yy] == 0) 
					{
						Node bestMove = getBestMove(xx, yy, 0, ENEMY);
						
						tabuleiro[bestMove.x][bestMove.y] = ENEMY;
						current = PLAYER;
						
						return;
					}
				}
			}
		}
		
		int result = validateVictory();
		
		if (result != EMPATE)
		{
			System.out.println((result == PLAYER ? "Player" : result == ENEMY ? "Oponente" : "Velha") + " venceu!");
		}
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
		for (int xx = 0; xx < tabuleiro.length; xx++)
		{
			for (int yy = 0; yy < tabuleiro.length; yy++)
			{
				g.setColor(Color.BLACK);
				g.drawRect(TILE * xx, TILE * yy, TILE, TILE);
				
				if (tabuleiro[xx][yy] == PLAYER)
				{
					g.drawImage(cross, xx * 100 + 25, yy * 100 + 25, null);
				} else if (tabuleiro[xx][yy] == ENEMY)
				{
					g.drawImage(circle, xx * 100 + 25, yy * 100 + 25, null);
				}
			}
		}
	}
	
	private int validateVictory()
	{
		if (tabuleiro[0][0] != 0 && tabuleiro[0][0] == tabuleiro[0][1] && tabuleiro[0][1] == tabuleiro[0][2])
			return tabuleiro[0][0];
		
		if (tabuleiro[1][0] != 0 && tabuleiro[1][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[1][2])
			return tabuleiro[1][0];
		
		if (tabuleiro[2][0] != 0 && tabuleiro[2][0] == tabuleiro[2][1] && tabuleiro[2][1] == tabuleiro[2][2])
			return tabuleiro[2][0];
		
		if (tabuleiro[0][0] != 0 && tabuleiro[0][0] == tabuleiro[1][0] && tabuleiro[1][0] == tabuleiro[2][0])
			return tabuleiro[0][0];
		
		if (tabuleiro[0][1] != 0 && tabuleiro[0][1] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][1])
			return tabuleiro[0][1];
		
		if (tabuleiro[0][2] != 0 && tabuleiro[0][2] == tabuleiro[1][2] && tabuleiro[1][2] == tabuleiro[2][2])
			return tabuleiro[0][2];
		
		if (tabuleiro[0][0] != 0 && tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2])
			return tabuleiro[0][0];
		
		if (tabuleiro[2][0] != 0 && tabuleiro[2][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[0][2])
			return tabuleiro[2][0];
		
		for (int xx = 0; xx < tabuleiro.length; xx++)
		{
			for (int yy = 0; yy < tabuleiro.length; yy++)
			{
				if (tabuleiro[xx][yy] == 0)
					return EMPATE;
			}
		}
		
		return NONE;
	}
	
	
	public Node getBestMove(int x, int y, int depth, int turn)
	{
		if (validateVictory() == PLAYER)
		{
			return new Node(x, y, depth - 10, depth);
		} 
		else if (validateVictory() == ENEMY)
		{
			return new Node(x, y, 10 - depth, depth);
		} 
		else if (validateVictory() == NONE)
		{
			return new Node(x, y, 0, depth);
		}
		
		List<Node> nodes = new ArrayList<>();
		
		for (int xx = 0; xx < tabuleiro.length; xx++)
		{
			for (int yy = 0; yy < tabuleiro.length; yy++)
			{
				if (tabuleiro[xx][yy] == 0)
				{
					Node node;
					
					if (turn == PLAYER)
					{
						tabuleiro[xx][yy] = PLAYER;
						node = getBestMove(xx, yy, depth + 1, ENEMY);
						tabuleiro[xx][yy] = 0;
					} 
					else
					{
						tabuleiro[xx][yy] = ENEMY;
						node = getBestMove(xx, yy, depth + 1, PLAYER);
						tabuleiro[xx][yy] = 0;
					}
					
					nodes.add(node);
				}
			}
		}
		
		Node finalNode = nodes.get(0);
		
		for (int i = 0; i < nodes.size(); i++)
		{
			Node n = nodes.get(i);
			
			if (turn == PLAYER)
			{
				if (n.score > finalNode.score)
				{
					finalNode = n;
				}
			} 
			else
			{
				if (n.score < finalNode.score)
				{
					finalNode = n;
				}
			}
		}
		
		return finalNode;
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		mx = e.getX();
		my = e.getY();
		press = true;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0)
	{
	}
	
	@Override
	public void mouseExited(MouseEvent arg0)
	{
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0)
	{
	}
}
