package jborg.lightning;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import CollectionTools.CollectionManipulation;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import jborg.lightning.exceptions.ActOnCanvasException;

import someMath.*;
import someMath.exceptions.*;
import someMath.pathFinder.LatticeGrid;
import someMath.pathFinder.Snake;
import someMath.pathFinder.SnakeAndLatticeGrid;

import static consoleTools.TerminalXDisplay.*;

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
		setupLattices((int)width,(int)height,(int)(Math.sqrt(width*height)/2));
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
	
    private void setupLattices(int width, int height, int latticeNr) throws LTGCException, CollectionException
    {

    	int nrOfInternPossibleLattices = 2*width*height-width-height;
    	if(latticeNr>=nrOfInternPossibleLattices)throw new IllegalArgumentException("Much to many Lattices!");

    	System.out.println(formatBashStringBoldAndBlue("Nr. of Possible Intern Lattices: " + nrOfInternPossibleLattices));
    	System.out.println(formatBashStringBoldAndBlue("Nr. of Factual Intern Lattices: " + latticeNr));
    	System.out.println(formatBashStringBoldAndBlue("Percentage: " + ((double)(latticeNr)/(nrOfInternPossibleLattices))));

    	List<Integer> possibleLatticeNrs = new ArrayList<>();
    	for(int n=0;n<nrOfInternPossibleLattices;n++)possibleLatticeNrs.add(n);

    	List<Integer> actualLatticeNrs = new ArrayList<>();
    	for(int n=0;n<latticeNr;n++)
    	{
    		int k = CollectionManipulation.catchRandomElementOfList(possibleLatticeNrs);
    		int i = possibleLatticeNrs.indexOf(k);
    		possibleLatticeNrs.remove(i);
    		actualLatticeNrs.add(k);
    	}

    	System.out.println(formatBashStringBoldAndBlue("ActualLattices: "+ actualLatticeNrs.size()));
    	for(int n=0;n<actualLatticeNrs.size();n++)System.out.print(", " + actualLatticeNrs.get(n));
    	System.out.println("");
    	
    	
    	Set<Pair<Point, Integer>> allPositions = poolOfPossibleLatticePositions(lg);
    	
    	Set<Pair<Point, Integer>> chosenPositions = new HashSet<>();
    	
    	while(chosenPositions.size()<latticeNr)
    	{
    		Pair<Point, Integer> position = CollectionManipulation.catchRandomElementOfCollection(allPositions);
    		chosenPositions.add(position);
    	}
    	
    	for(Pair<Point, Integer> position: chosenPositions)
    	{
    		Point p = position.getKey();
    		int bitNr= position.getValue();
    		
    		setOneLattice(p, bitNr);
    	}

    	int cnt = 0;
    	
	
    	System.out.println(formatBashStringBoldAndBlue("Count: " + cnt));
    }
    
	public void setOneLattice(Point p, int bitNr) throws LTGCException
	{
		lg.setOneLatticeOnTile(p, bitNr);
	}


    public Set<Pair<Point, Integer>> poolOfPossibleLatticePositions(LatticeGrid lg)
    {
    	
    	Set<Pair<Point, Integer>> pool = new HashSet<>();
    	int leftBitNr = 0;
    	int bottomBitNr = 1;
    	lg.walkThruTiles((p)->
    	{
    		
    		Pair<Point, Integer> position;
    		if(p.x>0)
    		{
   				position = new Pair<>(p, leftBitNr);
   				pool.add(position);
   			}
      				
    		if(p.y>0)
    		{
   				position = new Pair<>(p, bottomBitNr);
   				pool.add(position);
   			}
    	});

    	return pool;
    }
}
