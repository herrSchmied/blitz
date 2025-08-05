package lightning;

import java.awt.Point;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jborg.lightning.LTGCS;
import jborg.lightning.LatticeSetup;
import someMath.exceptions.CollectionException;
import someMath.exceptions.LTGCException;
import someMath.exceptions.SnakeException;
import someMath.pathFinder.LatticeGrid;
import someMath.pathFinder.Snake;
import someMath.pathFinder.SnakeAndLatticeGrid;

import static consoleTools.TerminalXDisplay.*;

public class ALittleLargerTest
{


	private int width = 7, height = 7;
	private SnakeAndLatticeGrid snl;
	private LatticeGrid lg;
	private LatticeSetup ls;
	private Point startPoint = new Point(0,0);
	private Point finalPoint = new Point(width-1, height-1);
	private int latticeNr = (int)(1.5*Math.sqrt(width*height));
	private Snake snake;
	
	public void frameIt() throws SnakeException, LTGCException, InterruptedException, CollectionException
	{

		snake = new Snake(startPoint, Snake.readyStatus);
		lg = new LatticeGrid(width, height);
		snl = new SnakeAndLatticeGrid(snake, lg, finalPoint);
		ls = new LatticeSetup(lg);
		ls.setVerticalBarsWithOneHole();
	}
	
	@Test
	public void test() throws SnakeException, LTGCException, CollectionException, InterruptedException
	{
		
		frameIt();
		snl.setFinalSnakes();
		Set<Snake> winners = snl.filterSuccesses();
		Set<Snake> snakeSet = snl.getSnakeSet();
		
		System.out.println(formatBashStringBoldAndGreen("We have " + winners.size() + " Winner."));
		System.out.println(formatBashStringBoldAndYellow("We have " + snakeSet.size() + " Snakes."));
		System.out.println(formatBashStringBoldAndBlue("Width: " + width));
		System.out.println(formatBashStringBoldAndBlue("Height" + height));
		System.out.println(formatBashStringBoldAndBlue("Lattices: " + latticeNr));
	}
}
