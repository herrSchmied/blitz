package jborg.lightning;


import javafx.application.Platform;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.Point;

import java.util.List;
import java.util.Set;


import CollectionTools.CollectionManipulation;


import jborg.lightning.exceptions.ActOnCanvasException;


import someMath.exceptions.*;

import someMath.pathFinder.LatticeGrid;
import someMath.pathFinder.Snake;
import someMath.pathFinder.SnakeAndLatticeGrid;


public class ActOnCanvas
{

	private SnakeAndLatticeGrid snlGrid;
	private LatticeGrid lg;
	
	private Point startPoint;
	private Point finalPoint;
	
	private Snake startSnake;
	
	private GraphicsContext gc2D;
	
	double width, height;

	public ActOnCanvas(Canvas canvas, Point start, Point end) throws ActOnCanvasException, SnakeException, LTGCException, CollectionException, InterruptedException
	{

		width = canvas.getWidth();
		height = canvas.getHeight();
		
		if(start.x<0||start.y<0)throw new ActOnCanvasException("Start Point out of Bounds!");
		if(start.x>width||start.y>height)throw new ActOnCanvasException("Start Point out of Bounds!");
		if(end.x<0||end.y<0)throw new ActOnCanvasException("End Point out of Bounds!");
		if(end.x>width||end.y>height)throw new ActOnCanvasException("End Point out of Bounds!");
	
		if(width<2||height<2)throw new ActOnCanvasException("Canvas to narrow.");
	
		startSnake = new Snake(start, Snake.readyStatus);
		
		this.startPoint = start;//snake.getHead();
		this.finalPoint = end;

		
		gc2D = canvas.getGraphicsContext2D();
		gc2D.setStroke(Color.BLUE);

		lg = new LatticeGrid((int)width, (int)height);
		
		snlGrid = new SnakeAndLatticeGrid(startSnake, lg, finalPoint);
		LatticeSetup ls = new LatticeSetup(lg);
		ls.setRandomLattices((int)(1.5*Math.sqrt(width*height)/2));
        setFinalSnakes();
        Set<Snake> snakes = filterSuccesses();
        
        Snake winner = CollectionManipulation.catchRandomElementOfSet(snakes);
        if(!(winner==null))animateSnake(winner);
        else System.out.println("No winner Snake!");
	}
	
	public Set<Snake> filterSuccesses()
	{
		return snlGrid.filterSuccesses();
	}
	
	public List<Point> getOptions(Snake snake) throws LTGCException, SnakeException
	{
		return snlGrid.getOptions(snake);
	}
	
	public Set<Snake> getSnakeSet()
	{
		return snlGrid.getSnakeSet();
	}
	
	public void setFinalSnakes() throws LTGCException, SnakeException
	{
		snlGrid.setFinalSnakes();
	}

	public Set<Snake> theDivergence(Snake snake) throws SnakeException, LTGCException
	{
		return snlGrid.theDivergence(snake);
	}
	
	private void animateSnake(Snake snake) throws LTGCException, SnakeException, InterruptedException
	{

	   	Thread animationThread = new Thread(()->
	   	{
	       	for(int n=0;n<snake.getLength();n++)
	       	{
	        		
	        		
	       		try
	       		{
	        		Point p1 = snake.getPartAt(n);
	        		double x1 = (double)p1.x;
	        		double y1 = (double)p1.y;

	       			Point p2;
	       			if(n+1<snake.getLength())
	       			{
	       				p2 = snake.getPartAt(n+1);
	       			}
	       			else p2 = p1;
	       			
	       			double x2 = (double)p2.x;
	        		double y2 = (double)p2.y;
	        		
	        		double w = 5.0;
	        		double h = 5.0;
	        		
	       			Platform.runLater(()->
	       			{
								
							gc2D.strokeLine(x1, y1, x2, y2);
					});
	     			Thread.sleep(300);
	       		}
	       		catch(InterruptedException | SnakeException exce)
	       		{
	       			exce.printStackTrace();
	       		}
	       	}

	   	});
	    	
	   	animationThread.start();
	}
}
