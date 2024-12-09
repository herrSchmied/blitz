package jborg.lightning;

import java.awt.Point;
import java.util.List;

import jborg.lightning.exceptions.SnakeException;

public class AnalysisToolSnake
{

	public static boolean containingThisPart(Snake snake, Point p)
	{		
		return snake.getParts().contains(p);
	}
	
	public static boolean containingThisSequenz(List<Point> sequenz, Snake snake) throws SnakeException
	{

		if(sequenz.size()>snake.getLength())return false;
	
		int diff = snake.getLength()-sequenz.size();
		
		for(int i=0;i<diff+1;i++)
		{
			if(containingThisSequenzAtPosition(sequenz, snake, i))return true;
		}
		
		return false;
	}

	public static boolean containingThisSequenzAtPosition(List<Point> sequenz, Snake snake, int n) throws SnakeException
	{
		
		if(sequenz.size()>snake.getLength())return false;

		Point p = sequenz.get(0);
		int indexOfP = snake.indexOfP(p);
		if(indexOfP==-1)return false;
		if(sequenz.size()==1)return true;
		
		List<Point> cutOff = sequenz.subList(1, sequenz.size()-1);
		
		return containingThisSequenzAtPosition(cutOff, snake, n+1);
	}	
}