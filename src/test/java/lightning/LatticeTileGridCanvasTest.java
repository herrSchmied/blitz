package lightning;

import static jborg.lightning.LatticeGrid.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jborg.lightning.LTGCException;
import jborg.lightning.LatticeGrid;
import jborg.lightning.LatticeTileGridCanvas;
import jborg.lightning.Snake;
import jborg.lightning.SnakeAndLatticeGrid;
import jborg.lightning.SnakeException;
import someMath.CollectionManipulation;

class LatticeTileGridCanvasTest
{

	@Test
	public void optionsTest() throws SnakeException, LTGCException
	{

		
		System.out.println("\nOptions Test.");
		Snake snake = new Snake(new Point(0,0), Snake.readyStatus);
		Point finalPoint = new Point(1,1);
		
		LatticeTileGridCanvas canvas = new LatticeTileGridCanvas(2, 2, 10, 3.5);
		LatticeGrid lg = canvas.getLatticeGrid();
		canvas.setOneLattice(0, 0, indexLatticeBitTop);
		
		SnakeAndLatticeGrid snlGrid = new SnakeAndLatticeGrid(snake, lg, finalPoint);
		List<Point> options = snlGrid.getOptions(snake);
		
		assert(options.contains(new Point(1,1)));
		assert(options.contains(new Point(1,0)));
		assert(options.size()==2);
	}
	
	@Test
	public void divergenceTest() throws SnakeException, LTGCException
	{
	
		System.out.println("\nDivergence Test.");
		Snake snake = new Snake(new Point(0,0), Snake.readyStatus);
		Point finalPoint = new Point(2,2);
		
		LatticeTileGridCanvas canvas = new LatticeTileGridCanvas(3, 3, 10, 3.5);
		
		canvas.setOneLattice(0, 0, indexLatticeBitRight);
		canvas.setOneLattice(0, 1, indexLatticeBitRight);
		LatticeGrid lg = canvas.getLatticeGrid();
		
		assert(lg.hasLatticeOnTheRight(0, 0));
		assert(lg.hasLatticeOnTheLeft(1, 0));
		assert(lg.hasLatticeOnTheRight(0, 1));
		assert(lg.hasLatticeOnTheLeft(1, 1));
		
		SnakeAndLatticeGrid snlGrid = new SnakeAndLatticeGrid(snake, lg, finalPoint);
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

		int i = 0;
		for(Snake s: evenNewer)
		{
			Point head = s.getHead();
			System.out.println("Head of Snake(" + i + "): " + "P(" + head.x + ", " +head.y + ")");
			i++;
		}
	}
	
	@Test
	public void anotherDivergenceTest() throws SnakeException, LTGCException
	{
	
		System.out.println("\nAnother Divergence Test.");
		Point startPoint = new Point(0, 0);
		Point rightPoint = new Point(1, 0);
		Point upPoint = new Point(0, 1);
		Point upRightPoint = SnakeAndLatticeGrid.addPoints(upPoint, rightPoint);
		Snake snake = new Snake(startPoint, Snake.readyStatus);
		Point finalPoint = new Point(2,2);
		
		LatticeTileGridCanvas canvas = new LatticeTileGridCanvas(3, 3, 10, 3.5);
		LatticeGrid lg = canvas.getLatticeGrid();
		canvas.setOneLattice(startPoint, indexLatticeBitTop);
		canvas.setOneLattice(rightPoint, indexLatticeBitTop);
		
		assert(lg.hasLatticeOnTheTop(startPoint));
		assert(lg.hasLatticeOnTheBottom(upPoint));
		assert(lg.hasLatticeOnTheTop(rightPoint));
		assert(lg.hasLatticeOnTheBottom(upRightPoint));
		
		SnakeAndLatticeGrid snlGrid = new SnakeAndLatticeGrid(snake, lg, finalPoint);
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
	public void untilTheyDeadTest() throws SnakeException, LTGCException
	{
		
		int width = 3;
		int height = 3;
		System.out.println("\nUntil they Dead Test!");
		LatticeTileGridCanvas canvas = new LatticeTileGridCanvas(width, height, 10, 3.5);

		Point isolatedPoint = new Point(1,1);
		
		//isolation
		canvas.setOneLattice(isolatedPoint, indexLatticeBitRight);
		canvas.setOneLattice(isolatedPoint, indexLatticeBitLeft);
		canvas.setOneLattice(isolatedPoint, indexLatticeBitTop);
		canvas.setOneLattice(isolatedPoint, indexLatticeBitBottom);
		LatticeGrid lg = canvas.getLatticeGrid();
		
		Snake snake = new Snake(0,0, Snake.readyStatus);

		Point finalPoint = new Point(2,2);
		SnakeAndLatticeGrid snlGrid = new SnakeAndLatticeGrid(snake, lg, finalPoint);
		snlGrid.setFinalSnakes();;
		Set<Snake> finalSnakes = snlGrid.getSnakeSet();
		
		for(Snake s: finalSnakes)assert(!s.containsPart(isolatedPoint));
		
		Set<Snake> successes = snlGrid.filterSuccesses();
		
		for(Snake s: successes)
		{
			System.out.println("\nFound a way");
			System.out.println(s);
		}
		
		System.out.println("Final Snakes: " + snlGrid.getSnakeSet().size());
		System.out.println("Successful Snakes: " + successes.size());
	}
	
	@Test
	public void reversedEndAndStartUntilTheyDeadTest() throws SnakeException, LTGCException
	{
		
		int width = 3;
		int height = 3;
		System.out.println("Until they Dead Test! Swap start/end");
		LatticeTileGridCanvas canvas = new LatticeTileGridCanvas(width, height, 10, 3.5);

		Point isolatedPoint = new Point(1,1);
		
		//isolation
		canvas.setOneLattice(isolatedPoint, indexLatticeBitRight);
		canvas.setOneLattice(isolatedPoint, indexLatticeBitLeft);
		canvas.setOneLattice(isolatedPoint, indexLatticeBitTop);
		canvas.setOneLattice(isolatedPoint, indexLatticeBitBottom);
		LatticeGrid lg = canvas.getLatticeGrid();
		
		Snake snake = new Snake(2,2, Snake.readyStatus);

		Point finalPoint = new Point(0,0);
		SnakeAndLatticeGrid snlGrid = new SnakeAndLatticeGrid(snake, lg, finalPoint);
		snlGrid.setFinalSnakes();;
		Set<Snake> finalSnakes = snlGrid.getSnakeSet();
		
		for(Snake s: finalSnakes)assert(!s.containsPart(isolatedPoint));
		
		Set<Snake> successes = snlGrid.filterSuccesses();
		
		for(Snake s: successes)
		{
			System.out.println("\nFound a way");
			System.out.println(s);
		}
		
		System.out.println("Final Snakes: " + snlGrid.getSnakeSet().size());
		System.out.println("Successful Snakes: " + successes.size());
	}
	
	@Test
	public void anotherUntilTheyDeadTest() throws SnakeException, LTGCException
	{
		
		int width = 4;
		int height = 4;
		System.out.println("\nUntil they Dead Test!");
		LatticeTileGridCanvas canvas = new LatticeTileGridCanvas(width, height, 10, 3.5);

		Point pointA = new Point(0,0);
		Point pointB = new Point(1,0);
		Point pointC = new Point(2,0);
		Point pointD = new Point(2,1);
		Point pointE = new Point(2, 2);
		Point pointF = new Point(2, 3);

		//isolation
		canvas.setOneLattice(pointA, indexLatticeBitTop);
		canvas.setOneLattice(pointB, indexLatticeBitTop);
		canvas.setOneLattice(pointC, indexLatticeBitTop);
		canvas.setOneLattice(pointD, indexLatticeBitRight);
		canvas.setOneLattice(pointE, indexLatticeBitRight);
		canvas.setOneLattice(pointF, indexLatticeBitBottom);
		canvas.setOneLattice(pointF, indexLatticeBitLeft);
		
		LatticeGrid lg = canvas.getLatticeGrid();
		
		Snake snake = new Snake(0,0, Snake.readyStatus);

		Point finalPoint = new Point(3, 3);
		
		SnakeAndLatticeGrid snlGrid = new SnakeAndLatticeGrid(snake, lg, finalPoint);
		snlGrid.setFinalSnakes();
		Set<Snake> finalSnakes = snlGrid.getSnakeSet();
		
		Set<Snake> successes = snlGrid.filterSuccesses();
		Snake success = CollectionManipulation.catchRandomElementOfSet(successes);

		System.out.println("\nFound a way");
		System.out.println(success);

		
		System.out.println("Final Snakes: " + snlGrid.getSnakeSet().size());
		System.out.println("Successful Snakes: " + successes.size());
		
		/*
		Snake s = null;
		boolean foundFail = false;
		while(!foundFail)
		{
			s = CollectionManipulation.catchRandomElementOfSet(finalSnakes);
			if(!successes.contains(s))
			{
				System.out.println("Fail example:\n" + s);
				foundFail = true;
			}
		}
		*/
		
	}
}