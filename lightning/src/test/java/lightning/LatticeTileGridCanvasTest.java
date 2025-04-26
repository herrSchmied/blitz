package lightning;


import java.awt.Point;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import consoleTools.BashSigns;

import jborg.lightning.exceptions.LTGCException;
import jborg.lightning.LatticeGrid;
import jborg.lightning.LTGCS;
import jborg.lightning.Snake;
import jborg.lightning.SnakeAndLatticeGrid;
import jborg.lightning.exceptions.SnakeException;

import someMath.exceptions.CollectionException;
import someMath.CollectionManipulation;
import someMath.SequenzInListSearch;
import consoleTools.TerminalTableDisplay;
import static consoleTools.TerminalXDisplay.*;

import static jborg.lightning.LatticeGrid.*;
import static jborg.lightning.SnakeAndLatticeGrid.*;

import static org.junit.jupiter.api.Assertions.fail;


public class LatticeTileGridCanvasTest
{
	
	final static int stndrtWidth = 3, stndrtHeight = 3;
	final static Point stndrtStartPoint = new Point(0, 0);
	final static Point stndrtEndPoint = new Point(2, 2);

	static int width, height;
	
	static LTGCS canvas;
	static Point startPoint;
	static Point finalPoint;
	static Snake snake;

	
	public static void frameIt(Point startP, Point endP, int w, int h) throws SnakeException, LTGCException
	{
		startPoint = startP;
		finalPoint = endP;
		snake = new Snake(startPoint, Snake.readyStatus);
		
		width = w;
		height = h;
		canvas = new LTGCS(width, height, finalPoint, snake);
	}

	public static void initStndrt() throws SnakeException, LTGCException
	{
		frameIt(stndrtStartPoint, stndrtEndPoint, stndrtWidth, stndrtHeight);
	}
 
	@Test
	public void optionsTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{
		
		System.out.println(formatBashStringGreen("\nOptions Test."));
		
		initStndrt();

		canvas.setOneLattice(0, 0, indexLatticeBitTop);
		
		List<Point> options = canvas.getOptions(snake);

		String s = "";
		for(int n=0;n<options.size();n++)s=s + pointAsString("P", options.get(n)) + "\n";
		System.out.println(s);  
		
		assert(options.contains(new Point(1,1)));
		assert(options.contains(new Point(1,0)));
		assert(options.size()==2);
	}

	@Test
	public void anotherOptionsTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{

		
		System.out.println(formatBashStringGreen("\nAnother Options Test."));

		initStndrt();

		canvas.setOneLattice(0, 0, indexLatticeBitRight);
		
		List<Point> options = canvas.getOptions(snake);

		String s = "";
		for(int n=0;n<options.size();n++)s=s + pointAsString("P", options.get(n)) + "\n";
		System.out.println(s);  
		
		assert(options.contains(new Point(1,1)));
		assert(options.contains(new Point(0,1)));
		assert(options.size()==2);
	}

	@Test
	public void againOptionsTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{

		System.out.println(formatBashStringGreen("\nAgain Options Test."));
		
		initStndrt();

		List<Point> options = canvas.getOptions(snake);

		String s = "";
		for(int n=0;n<options.size();n++)s=s + pointAsString("P", options.get(n)) + "\n";
		System.out.println(s); 
		
		
		assert(options.contains(new Point(1,1)));
		assert(options.contains(new Point(0,1)));
		assert(options.contains(new Point(1,0)));
		assert(options.size()==3);
	}

	@Test
	public void divergenceTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{
	
		System.out.println(formatBashStringGreen("\nDivergence Test."));
		
		initStndrt();

		canvas.setOneLattice(0, 0, indexLatticeBitRight);
		canvas.setOneLattice(0, 1, indexLatticeBitRight);
		
		Set<Snake> theNewGrownOnes = canvas.theDivergence(snake);
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
			evenNewer.addAll(canvas.theDivergence(s));
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
	
		System.out.println(formatBashStringGreen("\nAnother Divergence Test."));
		
		initStndrt();

		Point rightPoint = new Point(1, 0);
		//Point upPoint = new Point(0, 1);
		//Point upRightPoint = SnakeAndLatticeGrid.addPoints(upPoint, rightPoint);
		
		canvas.setOneLattice(startPoint, indexLatticeBitTop);
		canvas.setOneLattice(rightPoint, indexLatticeBitTop);
		
		Set<Snake> theNewGrownOnes = canvas.theDivergence(snake);
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
			
			Set<Snake> newOnes = canvas.theDivergence(s);
			evenNewer.addAll(newOnes);
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

		System.out.println(formatBashStringGreen("\nUntil they Dead Test!"));

		initStndrt();

		Point isolatedPoint = new Point(1,1);
		
		//isolation
		canvas.setAllLatticesOnTile(isolatedPoint);

		canvas.setFinalSnakes();;
		Set<Snake> finalSnakes = canvas.getSnakeSet();
		
		for(Snake s: finalSnakes)assert(!s.containsPart(isolatedPoint));
		
		Set<Snake> successes = canvas.filterSuccesses();
				
		System.out.println("Final Snakes: " + canvas.getSnakeSet().size());
		System.out.println("Successful Snakes: " + successes.size());
		
		for(Snake snake: successes)
		{
			assert(snake.getHead().equals(finalPoint));
		}
	}

	@Test
	public void swappedUntilTheyDeadTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{
    
		System.out.println(formatBashStringGreen("Until they Dead Test! Swap start/end"));
		
		initStndrt();

		Set<Point> ankerPoints = new HashSet<>();
		ankerPoints.add(startPoint);
		ankerPoints.add(finalPoint);
		
		Point isolatedPoint = new Point(1, 0);

		//isolation
		canvas.setAllLatticesOnTile(isolatedPoint);
		assert(canvas.isSurounded(isolatedPoint.x, isolatedPoint.y));

		canvas.setFinalSnakes();
		Set<Snake> finalSnakes = canvas.getSnakeSet();
		
		for(Snake s: finalSnakes)assert(!s.containsPart(isolatedPoint));
		
		Set<Snake> successes = canvas.filterSuccesses();
		
		System.out.println("Final Snakes: " + canvas.getSnakeSet().size());
		System.out.println("Successful Snakes: " + successes.size());
	}

	@Test
	public void anotherUntilTheyDeadTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{

		Point pointA = new Point(0, 1);
		Point pointB = new Point(1, 1);
		Point pointC = new Point(2, 1);
		Point pointD = new Point(3, 1);

		frameIt(pointA, pointD, 4, 4);
		
		System.out.println(formatBashStringGreen("\nAnother until they Dead Test!"));

		//Snake snake = new Snake(pointA, Snake.readyStatus);
		

		canvas.setOneLattice(pointA, indexLatticeBitTop);
		canvas.setOneLattice(pointA, indexLatticeBitBottom);
		canvas.setOneLattice(pointB, indexLatticeBitTop);
		canvas.setOneLattice(pointB, indexLatticeBitBottom);
		canvas.setOneLattice(pointC, indexLatticeBitTop);
		canvas.setOneLattice(pointC, indexLatticeBitBottom);
		canvas.setOneLattice(pointD, indexLatticeBitTop);
		canvas.setOneLattice(pointD, indexLatticeBitBottom);
		
		canvas.setFinalSnakes();
		Set<Snake> finalSnakes = canvas.getSnakeSet();
		
		Set<Snake> successes = canvas.filterSuccesses();
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

		assert(successes.size()==1);
		assert(finalSnakes.size()==1);
	}

	@Test
	public void justNoLatticesTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{
		
		System.out.println(formatBashStringGreen("\nJust no Lattices Test!"));

		initStndrt();
		
		canvas.setFinalSnakes();
		Set<Snake> finalSnakes = canvas.getSnakeSet();
		
		Set<Snake> successes = canvas.filterSuccesses();
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
	
		System.out.println("\nOne of the long a ways");
		System.out.println(longSuccess);
		System.out.println("Length: " + longSuccess.getLength());
		
		System.out.println("Successful Snakes: " + successes.size());

		assert(shortSuccess.getLength()==3);
		assert(longSuccess.getLength()==9);
		
		System.out.println("Final Snakes: " + finalSnakes.size() + "\n");
	}

	@Test
	public void simpleSNLGridTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{

		System.out.println(formatBashStringGreen("\nSimple canvas Test."));

		frameIt(new Point(0,0), new Point(1,1),2,2);
		canvas.setFinalSnakes();
		
		for(Snake snake: canvas.getSnakeSet())System.out.println(snake);
		System.out.println("canvas size: " + canvas.getSnakeSet().size());

		Point f = canvas.getFinalPoint();
		System.out.println("FinalPoint: f(" + f.x + ", " + f.y + ")");

		assert(canvas.getSnakeSet().size()==5);
		
		Set<Snake> successSnakes = canvas.filterSuccesses();
		
		assert(successSnakes.size()==5);
	}
	
	@Test
	public void oneLatticeSmallGridTest() throws SnakeException, LTGCException, InterruptedException, IOException
	{

		System.out.println(formatBashStringGreen("\nOne Lattice Small Grid Test."));

		Point startPoint = new Point(0, 0);
		Point endPoint = new Point(1, 1);
		frameIt(startPoint, endPoint, 2, 2);
		
		canvas.setOneLatticeOnTile(startPoint, LatticeGrid.indexLatticeBitRight);
		assert(canvas.hasLatticeOnTheRight(startPoint));
		canvas.setFinalSnakes();
		
		for(Snake snake: canvas.getSnakeSet())System.out.println(snake);
		System.out.println("Snake Set Size: " + canvas.getSnakeSet().size());

		Point f = canvas.getFinalPoint();
		System.out.println("FinalPoint: f(" + f.x + ", " + f.y + ")");

		assert(canvas.getSnakeSet().size()==3);

		Set<Snake> successSnakes = canvas.filterSuccesses();
		
		assert(successSnakes.size()==3);
	}

	@Test
	public void oneLatticeSmallGridTest_Again() throws SnakeException, LTGCException, InterruptedException, IOException
	{

		System.out.println(formatBashStringGreen("\nLattice small Grid Test Again."));
		Point zero = new Point(0, 0);
		Point aHalfTimesSquareRootOf2AwayPoint = new Point(1, 1);
		Point rightFromZero = new Point(1, 0);
		
		frameIt(zero, aHalfTimesSquareRootOf2AwayPoint,2,2);
		
		canvas.setOneLatticeOnTile(rightFromZero, LatticeGrid.indexLatticeBitTop);
		canvas.setFinalSnakes();
		
		for(Snake snake: canvas.getSnakeSet())System.out.println(snake);
		System.out.println("canvas size: " + canvas.getSnakeSet().size());

		Point f = canvas.getFinalPoint();
		System.out.println("FinalPoint: f(" + f.x + ", " + f.y + ")");

		assert(canvas.getSnakeSet().size()==4);

		Set<Snake> successSnakes = canvas.filterSuccesses();
		
		assert(successSnakes.size()==3);
	}
	
	@Test
	public void latticesWorkCorrectAsBarrier_I_Test() throws SnakeException, LTGCException
	{
		Point startPoint = new Point(0, 0);
		Point destPoint = new Point(2, 2);
		Point centerPoint = new Point(1, 1);
		Point leftCenter = new Point(0, 1);

		frameIt(startPoint, destPoint, 3, 3);
		
		System.out.println(formatBashStringGreen("\nLattices as Barrier Test I!"));

		//Snake snake = new Snake(pointA, Snake.readyStatus);
		
		canvas.setAllLatticesOnTile(centerPoint);
		
		
		canvas.setFinalSnakes();
		Set<Snake> finalSnakes = canvas.getSnakeSet();
		List<Point> sequenz = new ArrayList<>(Arrays.asList(startPoint, centerPoint));
		List<Point> sequenzII = new ArrayList<>(Arrays.asList(leftCenter, centerPoint));
		List<Point> sequenzSearchedFor = snake.getParts();
		
		for(Snake snake: finalSnakes)
		{
			
			assert(!snake.containsPart(centerPoint));
			assert(!SequenzInListSearch.containingThisSequenz(sequenz, sequenzSearchedFor));
			assert(!SequenzInListSearch.containingThisSequenz(sequenzII, sequenzSearchedFor));
		}

		System.out.println("Final Snakes: " + finalSnakes.size());

	}

	@Test
	public void latticesWorkCorrectAsBarrier_II_Test() throws SnakeException, LTGCException
	{
		Point startPoint = new Point(0, 0);
		Point destPoint = new Point(2, 2);
		Point centerPoint = new Point(1, 1);
		Point rightCenter = new Point(2, 1);

		initStndrt();
		
		System.out.println(formatBashStringGreen("\nLattices as Barrier Test II!"));

		canvas.setOneLattice(centerPoint, indexLatticeBitRight);
		canvas.setOneLattice(centerPoint, indexLatticeBitTop);
		
		canvas.setFinalSnakes();
		Set<Snake> finalSnakes = canvas.getSnakeSet();
		List<Point> sequenz = new ArrayList<>(Arrays.asList(centerPoint, destPoint));
		List<Point> sequenzII = new ArrayList<>(Arrays.asList(centerPoint, rightCenter));;
		List<Point> sequenzIII = new ArrayList<>(Arrays.asList(rightCenter, centerPoint));
		List<Point> sequenzSearchedFor = snake.getParts();
		
		for(Snake snake: finalSnakes)
		{
			assert(!SequenzInListSearch.containingThisSequenz(sequenz, sequenzSearchedFor));
			assert(!SequenzInListSearch.containingThisSequenz(sequenzII, sequenzSearchedFor));
			assert(!SequenzInListSearch.containingThisSequenz(sequenzIII, sequenzSearchedFor));
			assert(snake.containsPart(startPoint));
		}

		System.out.println("Final Snakes: " + finalSnakes.size());

	}

	public Point getRandomPoint(Set<Point> excludedPoints, LTGCS canvas)
	{
		int w= canvas.getWidthInTiles();
		int h= canvas.getHeightInTiles();
		
		int x = (int)(Math.random()*w);
		int y = (int)(Math.random()*h);
		Point rndPoint = new Point(x,y);
		
		if(excludedPoints.contains(rndPoint))return getRandomPoint(excludedPoints, canvas);
		
		return rndPoint;
	}
	
}