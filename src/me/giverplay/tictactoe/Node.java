package me.giverplay.tictactoe;

public class Node
{
	public int x;
	public int y;
	public int depth;
	public int score;
	
	public Node(int x, int y, int score, int depth)
	{
		this.x = x;
		this.y = y;
		this.score = score;
		this.depth = depth;
	}
}
