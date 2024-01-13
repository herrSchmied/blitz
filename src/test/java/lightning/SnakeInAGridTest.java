package lightning;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jborg.lightning.LTGCException;
import jborg.lightning.LatticeGrid;
import jborg.lightning.Snake;
import jborg.lightning.SnakeAndLatticeGrid;
import jborg.lightning.SnakeException;
import static jborg.lightning.LatticeGrid.*;


public class SnakeInAGridTest
{

	@Test
	public void optionsTest() throws SnakeException, LTGCException
	{

		System.out.println("\nOptions Test.");
		Snake snake = new Snake(new Point(0,0), Snake.readyStatus);
		Set<Snake> snakeSet = new HashSet<>();
		snakeSet.add(snake);
		Point finalPoint = new Point(1,1);
		LatticeGrid lg = new LatticeGrid(2, 2);
		lg.setOneLatticeOnTile(0, 0, indexLatticeBitTop);
		
		SnakeAndLatticeGrid snlGrid = new SnakeAndLatticeGrid(snakeSet, lg, finalPoint);
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
		Set<Snake> snakeSet = new HashSet<>();
		snakeSet.add(snake);
		Point finalPoint = new Point(2,2);
		
		LatticeGrid lg = new LatticeGrid(3, 3);
		lg.setOneLatticeOnTile(0, 0, indexLatticeBitRight);
		lg.setOneLatticeOnTile(0, 1, indexLatticeBitRight);
		
		assert(lg.hasLatticeOnTheRight(0, 0));
		assert(lg.hasLatticeOnTheLeft(1, 0));
		assert(lg.hasLatticeOnTheRight(0, 1));
		assert(lg.hasLatticeOnTheLeft(1, 1));
		
		SnakeAndLatticeGrid snlGrid = new SnakeAndLatticeGrid(snakeSet, lg, finalPoint);
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

		for(Snake s: evenNewer)
		{
			Point head = s.getHead();
			System.out.println("Even newer Head: " + "P(" + head.x + ", " +head.y + ")");
		}
	}
	
	@Test
	public void untilTheyDeadTest() throws SnakeException, LTGCException
	{
		
		System.out.println("\nUntil they Dead Test!");
		LatticeGrid lg = new LatticeGrid(3, 3);
		lg.setOneLatticeOnTile(1, 1, indexLatticeBitBottom);
		lg.setOneLatticeOnTile(1, 1, indexLatticeBitTop);
		lg.setOneLatticeOnTile(1, 1, indexLatticeBitRight);
		lg.setOneLatticeOnTile(1, 1, indexLatticeBitLeft);
		
		Snake snake = new Snake(0,0, Snake.readyStatus);
		Set<Snake> snakeSet = new HashSet<>();
		snakeSet.add(snake);

		Point finalPoint = new Point(2,2);
		SnakeAndLatticeGrid snlGrid = new SnakeAndLatticeGrid(snakeSet, lg, finalPoint);
		Set<Snake> finalSnakes = snlGrid.untilTheyAreAllDeadLoop(snakeSet);
		
		Set<Snake> successes = snlGrid.filterSuccesses();
		
		for(Snake s: successes)printSnake(s);
		
		System.out.println("Final Snakes: " + finalSnakes.size());
	}
	
	public void printSnake(Snake s)
	{
		
		List<Point> parts = s.getParts();
		
		System.out.println("Found a way: ");
		for(int n=0;n<parts.size();n++)
		{
			Point part = parts.get(n);
			System.out.println("(" + part.x + ", " + part.y + ")");
		}
	}
}