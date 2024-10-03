package lightning;


import static jborg.lightning.LatticeGrid.*;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.junit.jupiter.api.Test;

import jborg.lightning.LatticeTileGridCanvas;
import jborg.lightning.Snake;
import jborg.lightning.SnakeAndLatticeGrid;
import jborg.lightning.exceptions.LTGCException;
import jborg.lightning.exceptions.SnakeException;
import someMath.CollectionException;
import someMath.CollectionManipulation;

import static consoleTools.TerminalXDisplay.*;

public class LTGCTest
{
	
	final static int stndrtWidth = 3, stndrtHeight = 3;
	final static Point stndrtStartPoint = new Point(0, 0);
	final static Point stndrtEndPoint = new Point(2, 2);

	static int width, height;
	
	static LatticeTileGridCanvas canvas;
	static Point startPoint;
	static Point finalPoint;
	static Snake snake;
	static SnakeAndLatticeGrid snlGrid;

	public static void frameIt(Point startP, Point endP, int w, int h) throws SnakeException, LTGCException
	{
		startPoint = startP;
		finalPoint = endP;
		snake = new Snake(startPoint, Snake.readyStatus);
		
		width = w;
		height = h;
		canvas = new LatticeTileGridCanvas(width, height, finalPoint, snake);
		snlGrid = canvas.getSNLGrid();

	}
	
	@Test
	public void testOptions() throws SnakeException, LTGCException
	{

		frameIt(stndrtStartPoint, stndrtEndPoint, stndrtWidth, stndrtHeight);

		System.out.println("\nOptions Test.");
		
		canvas.setOneLattice(0, 0, indexLatticeBitTop);
		
		List<Point> options = snlGrid.getOptions(snake);

		String s = "";
		for(int n=0;n<options.size();n++)s=s + pointToString("P", options.get(n)) + "\n";
		System.out.println(s);  
		
		assert(options.contains(new Point(1,1)));
		assert(options.contains(new Point(1,0)));
		assert(options.size()==2);
	}
	
	@Test
	public void testOtherOptions() throws SnakeException, LTGCException
	{

		frameIt(stndrtStartPoint, stndrtEndPoint, stndrtWidth, stndrtHeight);

		System.out.println("\nAnother Options Test.");
		
		canvas.setOneLattice(0, 0, indexLatticeBitRight);
		
		List<Point> options = snlGrid.getOptions(snake);

		String s = "";
		for(int n=0;n<options.size();n++)s=s + pointToString("P", options.get(n)) + "\n";
		System.out.println(s);  
		
		assert(options.contains(new Point(1,1)));
		assert(options.contains(new Point(0,1)));
		assert(options.size()==2);
	}

	@Test
	public void testAgainOptions() throws SnakeException, LTGCException
	{

		frameIt(stndrtStartPoint, stndrtEndPoint, stndrtWidth, stndrtHeight);

		System.out.println("\nAgain Options Test.");

		List<Point> options = snlGrid.getOptions(snake);

		String s = "";
		for(int n=0;n<options.size();n++)s=s + pointToString("P", options.get(n)) + "\n";
		System.out.println(s);  
		
		assert(options.contains(new Point(1,1)));
		assert(options.contains(new Point(0,1)));
		assert(options.contains(new Point(1,0)));
		assert(options.size()==3);
	}

	@Test
	public void testDivergence() throws SnakeException, LTGCException, InterruptedException
	{
	
		frameIt(stndrtStartPoint, stndrtEndPoint, stndrtWidth, stndrtHeight);

		System.out.println("\nDivergence Test.");
		
		canvas.setOneLattice(0, 0, indexLatticeBitRight);
		canvas.setOneLattice(0, 1, indexLatticeBitRight);
		
		Set<Snake> theNewGrownOnes = snlGrid.theDivergence(snake);
		System.out.println("The new Snake Set Size: " + theNewGrownOnes.size());
		assert(theNewGrownOnes.size()==1);
		
		for(Snake s: theNewGrownOnes)
		{
			Point head = s.getHead();
			System.out.println("Head: " + "P(" + head.x + ", " +head.y + ")");
		}

		Set<Snake> evenNewer = new HashSet<>();
		for(Snake s: theNewGrownOnes)
		{
			evenNewer.addAll(snlGrid.theDivergence(s));
		}
		
		System.out.println("Even Newer Size:" + evenNewer.size());
		assert(evenNewer.size()==2);

		int i = 0;
		for(Snake s: evenNewer)
		{
			Point head = s.getHead();
			System.out.println("Head of Snake(" + i + "): " + "P(" + head.x + ", " +head.y + ")");
			i++;
			assert(s.getStart().equals(startPoint));
		}
	}
	
	@Test
	public void testOtherDivergence() throws SnakeException, LTGCException, InterruptedException
	{
	
		frameIt(stndrtStartPoint, stndrtEndPoint, stndrtWidth, stndrtHeight);

		System.out.println("\nAnother Divergence Test.");
		
		Point rightPoint = new Point(1, 0);
		//Point upPoint = new Point(0, 1);
		//Point upRightPoint = SnakeAndLatticeGrid.addPoints(upPoint, rightPoint);
		
		canvas.setOneLattice(startPoint, indexLatticeBitTop);
		canvas.setOneLattice(rightPoint, indexLatticeBitTop);
		
		Set<Snake> theNewGrownOnes = snlGrid.theDivergence(snake);
		System.out.println("The new Snake Set Size: " + theNewGrownOnes.size());
		assert(theNewGrownOnes.size()==1);
		
		for(Snake s: theNewGrownOnes)
		{
			Point head = s.getHead();
			System.out.println("Head: " + "P(" + head.x + ", " +head.y + ")");
		}

		Set<Snake> evenNewer = new HashSet<>();
		for(Snake s: theNewGrownOnes)
		{
			evenNewer.addAll(snlGrid.theDivergence(s));
		}
		
		assert(evenNewer.size()==2);

		int i =0;
		for(Snake s: evenNewer)
		{
			Point head = s.getHead();
			System.out.println("Head(" + i + "):" + "P(" + head.x + ", " +head.y + ")");
			i++;
		}
	}

	@Test
	public void testUntilTheyDead() throws SnakeException, LTGCException, InterruptedException
	{

		frameIt(stndrtStartPoint, stndrtEndPoint, stndrtWidth, stndrtHeight);

		System.out.println("\nUntil they Dead Test!");

		Point isolatedPoint = new Point(1,1);
		
		//isolation
		isolate(isolatedPoint, canvas);

		snlGrid.setFinalSnakes();;
		Set<Snake> finalSnakes = snlGrid.getSnakeSet();
		
		for(Snake s: finalSnakes)assert(!s.containsPart(isolatedPoint));
		
		Set<Snake> successes = snlGrid.filterSuccesses();
				
		System.out.println("Final Snakes: " + snlGrid.getSnakeSet().size());
		System.out.println("Successful Snakes: " + successes.size());
	}
	
	@Test
	public void testSwappedUntilTheyDead() throws SnakeException, LTGCException, InterruptedException
	{
    
		frameIt(stndrtStartPoint, stndrtEndPoint, stndrtWidth, stndrtHeight);

		System.out.println("Until they Dead Test! Swap start/end");
		

		Set<Point> ankerPoints = new HashSet<>();
		ankerPoints.add(startPoint);
		ankerPoints.add(finalPoint);
		
		Point isolatedPoint = getRandomIsolatedPoint(ankerPoints, canvas);

		//isolation
		isolate(isolatedPoint, canvas);

		snlGrid.setFinalSnakes();
		Set<Snake> finalSnakes = snlGrid.getSnakeSet();
		
		for(Snake s: finalSnakes)assert(!s.containsPart(isolatedPoint));
		
		Set<Snake> successes = snlGrid.filterSuccesses();
		
		System.out.println("Final Snakes: " + snlGrid.getSnakeSet().size());
		System.out.println("Successful Snakes: " + successes.size());
	}


	@Test
	public void testAnotherUntilTheyDead() throws SnakeException, LTGCException, InterruptedException
	{
		
		frameIt(new Point(0, 0), new Point(3,3), 4, 4);
		
		System.out.println("\nAnother until they Dead Test!");

		
		Point pointA = new Point(0,0);
		Point pointB = new Point(1,0);
		Point pointC = new Point(2,0);
		Point pointD = new Point(2,1);
		Point pointE = new Point(2, 2);
		Point pointF = new Point(2, 3);

		canvas.setOneLattice(pointA, indexLatticeBitTop);
		canvas.setOneLattice(pointB, indexLatticeBitTop);
		canvas.setOneLattice(pointC, indexLatticeBitTop);
		canvas.setOneLattice(pointD, indexLatticeBitRight);
		canvas.setOneLattice(pointE, indexLatticeBitRight);
		canvas.setOneLattice(pointF, indexLatticeBitBottom);
		canvas.setOneLattice(pointF, indexLatticeBitLeft);
		
		snlGrid.setFinalSnakes();
		Set<Snake> finalSnakes = snlGrid.getSnakeSet();
		
		Set<Snake> successes = snlGrid.filterSuccesses();
		Snake success;
		
		try
		{
			success = CollectionManipulation.catchRandomElementOfSet(successes);
			System.out.println("\nFound a way");
			System.out.println(success);
			System.out.println("Successful Snakes: " + successes.size());
		}
		catch(CollectionException ce)
		{
			fail("Didn't find any Success.\n" + ce);
		}

		System.out.println("Final Snakes: " + finalSnakes.size());

		assert(successes.size()==4);
		assert(finalSnakes.size()==5);
	}

	@Test
	public void testHalfIsolated() throws SnakeException, LTGCException, InterruptedException
	{
		
		frameIt(stndrtStartPoint, stndrtEndPoint, stndrtWidth, stndrtHeight);

		System.out.println("Half isolated Test.");
				
		Point halfIsolatedPoint = new Point(1,1);

		canvas.setOneLattice(halfIsolatedPoint, indexLatticeBitBottom);
		canvas.setOneLattice(halfIsolatedPoint, indexLatticeBitLeft);
		
		snlGrid.setFinalSnakes();
		Set<Snake> finalSnakes = snlGrid.getSnakeSet();
		
		Set<Snake> successes = snlGrid.filterSuccesses();
		List<Snake> orderedSuccesses = new ArrayList<>();
		orderedSuccesses.addAll(successes);
		
		Comparator<Snake> c = (s1, s2)->
		{
			if(s1.getLength()>s2.getLength())return 1;
			if(s2.getLength()>s1.getLength())return -1;
			
			return 0;
		};
		orderedSuccesses.sort(c);

		Snake success = orderedSuccesses.get(0);
		System.out.println("\nOne of the short a ways");
		System.out.println(success);
		
		success = orderedSuccesses.get(orderedSuccesses.size()-1);
		System.out.println("\nOne of the long a ways");
		System.out.println(success);
		System.out.println("Successful Snakes: " + successes.size());
		//TODO: assert(success.getLength()==9); //All nine Fields.
		
		System.out.println("Final Snakes: " + finalSnakes.size() + "\n");
	}

	public Point getRandomIsolatedPoint(Set<Point> excludedPoints, LatticeTileGridCanvas ltgCanvas)
	{
		
		int width = ltgCanvas.getWidthInTiles();
		int height = ltgCanvas.getHeightInTiles();
		
		int x = (int)(Math.random()*width);
		int y = (int)(Math.random()*height);
		Point p = new Point(x, y);
		
		if(excludedPoints.contains(p))return getRandomIsolatedPoint(excludedPoints, ltgCanvas);

		return p;
	}
	
	public void isolate(Point p, LatticeTileGridCanvas ltgCanvas) throws LTGCException
	{
		ltgCanvas.setOneLattice(p, indexLatticeBitTop);
		ltgCanvas.setOneLattice(p, indexLatticeBitBottom);
		ltgCanvas.setOneLattice(p, indexLatticeBitLeft);
		ltgCanvas.setOneLattice(p, indexLatticeBitRight);
	}
}