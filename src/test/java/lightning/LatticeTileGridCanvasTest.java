package lightning;


import static jborg.lightning.LatticeGrid.*;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import consoleTools.InputStreamSession;
import javafx.application.Platform;
import jborg.lightning.exceptions.LTGCException;
import jborg.lightning.LatticeTileGridCanvas;
import jborg.lightning.Snake;
import jborg.lightning.SnakeAndLatticeGrid;
import jborg.lightning.exceptions.SnakeException;

import someMath.CollectionException;
import someMath.CollectionManipulation;

import static consoleTools.TerminalXDisplay.*;

public class LatticeTileGridCanvasTest
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

	@BeforeEach
	public void initStndrt() throws SnakeException, LTGCException
	{
		frameIt(stndrtStartPoint, stndrtEndPoint, stndrtWidth, stndrtHeight);
	}
	
	/*
	@Test
	public void optionsTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{
		
		System.out.println("\nOptions Test.");
		
		initStndrt();

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
	public void anotherOptionsTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{

		
		System.out.println("\nAnother Options Test.");

		initStndrt();

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
	public void againOptionsTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{

		
		System.out.println("\nAgain Options Test.");
		
		initStndrt();

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
	public void divergenceTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{
	
		System.out.println("\nDivergence Test.");
		
		initStndrt();

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
	public void anotherDivergenceTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{
	
		System.out.println("\nAnother Divergence Test.");
		
		initStndrt();

		Point rightPoint = new Point(1, 0);
		Point upPoint = new Point(0, 1);
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
	public void untilTheyDeadTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{

		System.out.println("\nUntil they Dead Test!");

		initStndrt();

		Point isolatedPoint = new Point(1,1);
		
		//isolation
		snlGrid.getLatticeGrid().setAllLatticesOnTile(isolatedPoint);

		snlGrid.setFinalSnakes();;
		Set<Snake> finalSnakes = snlGrid.getSnakeSet();
		
		for(Snake s: finalSnakes)assert(!s.containsPart(isolatedPoint));
		
		Set<Snake> successes = snlGrid.filterSuccesses();
				
		System.out.println("Final Snakes: " + snlGrid.getSnakeSet().size());
		System.out.println("Successful Snakes: " + successes.size());
	}
	
	@Test
	public void swappedUntilTheyDeadTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{
    
		System.out.println("Until they Dead Test! Swap start/end");
		
		initStndrt();

		Set<Point> ankerPoints = new HashSet<>();
		ankerPoints.add(startPoint);
		ankerPoints.add(finalPoint);
		
		Point isolatedPoint = getRandomIsolatedPoint(ankerPoints, canvas);

		//isolation
		snlGrid.getLatticeGrid().setAllLatticesOnTile(isolatedPoint);

		snlGrid.setFinalSnakes();
		Set<Snake> finalSnakes = snlGrid.getSnakeSet();
		
		for(Snake s: finalSnakes)assert(!s.containsPart(isolatedPoint));
		
		Set<Snake> successes = snlGrid.filterSuccesses();
		
		System.out.println("Final Snakes: " + snlGrid.getSnakeSet().size());
		System.out.println("Successful Snakes: " + successes.size());
	}


	@Test
	public void anotherUntilTheyDeadTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{
		
		frameIt(new Point(0, 0), new Point(3,3), 4, 4);
		System.out.println("\nAnother until they Dead Test!");

		
		Snake snake = new Snake(startPoint, Snake.readyStatus);
		
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
	public void justNoLatticesTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{
		
		System.out.println("Just no Lattices Test!");
		Thread.sleep(1000);

		initStndrt();

		//Point halfIsolatedPoint = new Point(1,1);

		//canvas.setOneLattice(halfIsolatedPoint, indexLatticeBitBottom);
		//canvas.setOneLattice(halfIsolatedPoint, indexLatticeBitLeft);
		
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

		Snake shortSuccess = orderedSuccesses.get(0);
		Snake longSuccess = orderedSuccesses.get(orderedSuccesses.size()-1);
		System.out.println("\nOne of the short a ways");
		System.out.println(shortSuccess);
		System.out.println("Length: "+shortSuccess.getLength());
		Thread.sleep(2000);
	
		System.out.println("\nOne of the long a ways");
		System.out.println(longSuccess);
		System.out.println("Length: " + longSuccess.getLength());
		Thread.sleep(2000);
		
		System.out.println("Successful Snakes: " + successes.size());
		
		InputStreamSession iss = new InputStreamSession(System.in);
		boolean questionAnswer = iss.forcedYesOrNo("Continue?");
		if(!questionAnswer)System.exit(0);

		assert(shortSuccess.getLength()==3);
		assert(longSuccess.getLength()==9);
		
		System.out.println("Final Snakes: " + finalSnakes.size() + "\n");
	}	
	*/

	@Test
	public void simpleSNLGridTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{

		frameIt(new Point(0,0), new Point(1,1),2,2);
		snlGrid.setFinalSnakes();
		
		for(Snake snake: snlGrid.getSnakeSet())
		{
			System.out.println(snake);
		}
		
		Point f = snlGrid.getFinalPoint();
		System.out.println("FinalPoint: f(" + f.x + ", " + f.y + ")");
		assert(snlGrid.getSnakeSet().size()==3);
		
	}
	
	public Point getRandomIsolatedPoint(Set<Point> excludedPoints, LatticeTileGridCanvas canvas)
	{
		int w= canvas.getWidthInTiles();
		int h= canvas.getHeightInTiles();
		
		int x = (int)(Math.random()*w);
		int y = (int)(Math.random()*h);
		Point rndPoint = new Point(x,y);
		
		if(excludedPoints.contains(rndPoint))return getRandomIsolatedPoint(excludedPoints, canvas);
		
		return rndPoint;
	}
}