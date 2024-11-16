package jborg.lightning;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import consoleTools.BashSigns;
import jborg.lightning.exceptions.LTGCException;
import jborg.lightning.exceptions.SnakeException;


/**
 * Put's a Snake, Lattice-Grid and a final-Point together
 * to explore all ways a snake with just one Part(Point) 
 * can grow. The Lattices in the Lattice-Grid and the Parts of
 * the Snake narrow the Options. There maybe no Options.
 * In that case the snake will change status to
 * dead-Status. Once a snake reached the finalPoint it is
 * also Dead(dead-status). A Dead Snake will not grow further.
*/
public class SnakeAndLatticeGrid
{
	
	private Point leftPos = new Point(-1, 0);
	private Point rightPos = new Point(+1, 0);
	private Point topPos = new Point(0,+1);
	private Point bottomPos = new Point(0,-1);
	private Point leftTopPos = addPoints(leftPos, topPos);
	private Point leftBottomPos = addPoints(leftPos, bottomPos);
	private Point rightTopPos = addPoints(rightPos, topPos);
	private Point rightBottomPos = addPoints(rightPos, bottomPos);
	
	private Set<Point> posSet = new HashSet<>(Arrays.asList(leftPos, rightPos, topPos, bottomPos, leftTopPos, leftBottomPos, rightTopPos, rightBottomPos));

	/**
	 * Starting Snake-Set which will be just one 
	 * And May grow after executing certain Methods.
	 */
	private Set<Snake> snakeSet;
	
	/**
	 * Contains the Positions of the Lattices.
	 */
	private final LatticeGrid lg;
	
	/**
	 * Is the Point, when reached, will Kill the
	 * Snake and the Snake is then a success.
	 */
	private final Point finalPoint;
	
	/**
	 * Constructor
	 * 
	 * @param snake Start Snake.
	 * @param lg contains the Postion of the Lattices.
	 * @param finalPoint Destination for Successful Snakes.
	 */
	public SnakeAndLatticeGrid(Snake snake, LatticeGrid lg, Point finalPoint)
    {
		
		this.snakeSet = new HashSet<>();
		snakeSet.add(snake);
		this.lg = lg;
		this.finalPoint = finalPoint;
    }

	/**
	 * Gives the Options the Argument(Snake) has for growth.
	 * The Options are based on the Lattice Grid and the
	 * Position of the given Snake.
	 * @param snake Input
	 * @return List of Points as the Options the Snake has to
	 * grow.
	 * @throws LTGCException Shouldn't.
	 * @throws SnakeException Shouldn't.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public List<Point> getOptions(Snake snake) throws LTGCException, SnakeException, InterruptedException, IOException
    {

		Point head = snake.getHead();
   		
		List<Point> growthOptions = new ArrayList<>();
   		
		//hasLatticeOnTheXXXX treats frame borders like Lattices!!!
   		boolean hasLeft = !lg.hasLatticeOnTheLeft(head);
   		boolean hasRight = !lg.hasLatticeOnTheRight(head);
   		boolean hasTop = !lg.hasLatticeOnTheTop(head);
   		boolean hasBottom = !lg.hasLatticeOnTheBottom(head);

   		Set<Point> rPoints = new HashSet<>();
    		
   		if(hasLeft)rPoints.add(leftPos);
   		if(hasRight)rPoints.add(rightPos);
   		if(hasTop)rPoints.add(topPos);
   		if(hasBottom)rPoints.add(bottomPos);

		if(checkDiagonal(head, leftTopPos))rPoints.add(leftTopPos);
   		if(checkDiagonal(head, leftBottomPos))rPoints.add(leftBottomPos);
   		if(checkDiagonal(head, rightTopPos))rPoints.add(rightTopPos);
   		if(checkDiagonal(head, rightBottomPos))rPoints.add(rightBottomPos);

   		if(rPoints.contains(leftTopPos)||rPoints.contains(leftBottomPos)||rPoints.contains(rightTopPos)||rPoints.contains(rightBottomPos))
   		{
   			System.out.println(BashSigns.boldGBCPX+"Treffer"+BashSigns.boldGBCSX);
  		}
  
   		for(Point relativePoint: rPoints)
   		{
   			System.out.println(BashSigns.boldYBCPX+"P(" + relativePoint.x+", " + relativePoint.y + ")"+BashSigns.boldYBCSX);
   			Point newHead = addPoints(head, relativePoint);
   			if(checkOption(snake, newHead))growthOptions.add(newHead);
   		}
 
   		//TODO:Till here everything is fine. Then sometimes the final Point is not added!!!!
    	return growthOptions;
    }

	private boolean checkDiagonal(Point p, Point relative) throws LTGCException
	{

		if(!posSet.contains(relative))
		{
			System.out.println(BashSigns.rBCPX+"Warning: check Diagonal argument is not a relative Diagonal Point!"+BashSigns.rBCSX);
			return false;
		}

		Point dest = addPoints(p, relative);
		
		//Catches the cases where p or dest already out of Bounds
		boolean checkDest = checkPoint("dest", dest);
		boolean checkP = checkPoint("p", p);
		
		if(!(checkDest&&checkP))return false;
		
		int cnt=0;

		if(relative.equals(leftBottomPos))
		{
			if(lg.hasLatticeOnTheLeft(p))cnt++;
			if(lg.hasLatticeOnTheRight(dest))cnt++;
			if(lg.hasLatticeOnTheBottom(p))cnt++;
			if(lg.hasLatticeOnTheTop(dest))cnt++;

			return (cnt<2);
		}

		if(relative.equals(leftTopPos))
		{
				
			if(lg.hasLatticeOnTheLeft(p))cnt++;
			if(lg.hasLatticeOnTheRight(dest))cnt++;
			if(lg.hasLatticeOnTheTop(p))cnt++;
			if(lg.hasLatticeOnTheBottom(dest))cnt++;

			return (cnt<2);
		}

		if(relative.equals(rightBottomPos))
		{
			if(lg.hasLatticeOnTheRight(p))cnt++;
			if(lg.hasLatticeOnTheLeft(dest))cnt++;
			if(lg.hasLatticeOnTheBottom(p))cnt++;
			if(lg.hasLatticeOnTheTop(dest))cnt++;

			return (cnt<2);
		}

		if(relative.equals(rightTopPos))
		{	
			if(lg.hasLatticeOnTheRight(p))cnt++;
			if(lg.hasLatticeOnTheLeft(dest))cnt++;
			if(lg.hasLatticeOnTheTop(p))cnt++;
			if(lg.hasLatticeOnTheBottom(dest))cnt++;

			return (cnt<2);
		}

		System.out.println("This is normaly impossible to be shown!!.");
		
		return false;
	}

	/**
	 * Given that there is no Lattice blocking or 'Frame'-Border
	 * is a given point an Option? The only things that hinders
	 * that is SnakeExceptions. And that's Tested here.
	 * SnakeExceptions will be thrown if:
	 * 1.) SelfCrossing.
	 * 2.) Is already Part of the Snake.
	 * 3.) Other.
	 * Other will not happen in this API. This Method will apply
	 * the grow Method to the given Snake and given Point. If the
	 * Exception is thrown checkOption returns false. Otherwise
	 * true.
	 * @param snake To be grown.
	 * @param newHead new Part of the grown Snake.
	 * @return
	 */
    private boolean checkOption(Snake snake, Point newHead)
    {

    	try
    	{
			snake.growSnake(newHead, snake.getStatus());
		}
    	catch (SnakeException e) 
    	{
    		return false;
		}

    	return true;
    }
    
    /**
     * Just convenient Point(Vector) addition.
     * of p1 and p2.
     * @param p1 Summand.
     * @param p2 Summand.
     * @return Sum of those Points.
     */
    public static Point addPoints(Point p1, Point p2)
    {
    	return new Point(p1.x+p2.x, p1.y+p2.y);
    }
    
    /**
     * Set of all Snakes also the Dead ones.
     * @return Set of Snakes.
     */
    public Set<Snake> getSnakeSet()
    {
    	return snakeSet;
    }
    
    /**
     * Takes a Snake and makes Potential many Snakes out of it.
     * It may make only one out of it or zero. Each Snake in the
     * Result Set is just the old Snake plus one part. Every valid
     * Option will be taken to do that. This relies on the getOptions
     * Method.
     * @param snake Snake to grow in several Directions.
     * @return Snake Set.of the Snakes grown.
     * @throws LTGCException If Something goes wrong with getOptions. It is 
     * in this API not probable.
     * @throws SnakeException If something goes wrong with the Options Method.
     * Or the Snake can't grow or can't change status. It is in this API not
     * probable.
     * @throws InterruptedException If something goes wrong with Thread.sleep().
     * Not probable.
     * @throws IOException 
     */
    public Set<Snake> theDivergence(Snake snake) throws InterruptedException, SnakeException, LTGCException, IOException
    {
    	Set<Snake> snakeSet = new HashSet<>();
    	if(snake.getStatus().equals(Snake.deadStatus))
    	{
    		
    		System.out.println("U gave me a dead Snake.");
    		Thread.sleep(1000);
    		return snakeSet;
    	}
    
    	List<Point> options = getOptions(snake);

    	Point head = snake.getHead();
    	
    	if(options.isEmpty()||head.equals(finalPoint))
    	{
    		snake = snake.changeStatus(Snake.deadStatus);
    		snakeSet.add(snake);
    		return snakeSet;
    	}
    	
    	for(Point p: options)
    	{
    		
    		Snake spawn = snake.growSnake(p, snake.getStatus());
    		snakeSet.add(spawn);
    	}
    	
    	return snakeSet;
    }
    
    /**
     * Over-arching Method one of the only Methods that will be exposed
     * in the final Version.
     * @throws LTGCException Shouldn't.
     * @throws SnakeException Shouldn't.
     * @throws InterruptedException Shouldn't.
     * @throws IOException 
     */
    public void setFinalSnakes() throws LTGCException, SnakeException, InterruptedException, IOException
    {
    	this.snakeSet = untilTheyAreAllDeadLoop(this.snakeSet);
    }
    
    /**
     * Takes a Set of Snakes and does the work. The Set can contain
     * just one Snake.(Or zero).
     * @param snakeSet Input may just be one Snake in the Set.
     * @return Set of 'Dead' Snakes.
     * @throws LTGCException Shouldn't.
     * @throws SnakeException Shouldn't.
     * @throws InterruptedException Shouldn't.
     * @throws IOException 
     */
    public Set<Snake> untilTheyAreAllDeadLoop(Set<Snake> snakeSet) throws LTGCException, SnakeException, InterruptedException, IOException
    {
    	
    	Set<Snake> copy = new HashSet<>(snakeSet);
    	Set<Snake> newSnakes = new HashSet<>();
    		
    	if(copy.isEmpty()) return copy;
    	
    	int deadCount = 0;
    	for(Snake s: copy)
    	{
    		
    		if(s.getStatus().equals(Snake.deadStatus))
    		{
    			newSnakes.add(s);
    			deadCount++;
    			continue;
    		}
    		Set<Snake> spawns = theDivergence(s);
    		newSnakes.addAll(spawns);
    	}

    	if(deadCount==copy.size())return copy;
    	return untilTheyAreAllDeadLoop(newSnakes);
    }

    /**
     * Filters the Snake-Set for successes. Makes most sense
     * when called after setFinalSnakes Method. A Successful
     * Snake is one that made it to the Final-Point. Will be 
     * exposed at the final Version.
     * @return Snake Set
     */
    public Set<Snake> filterSuccesses()
    {
    
    	Set<Snake> successes = new HashSet<>();
    	
		for(Snake s: snakeSet)
		{
			Point head = s.getHead();
			if(head.equals(finalPoint))successes.add(s);
		}

    	return successes;
    }
    
    public LatticeGrid getLatticeGrid()
    {
    	return lg;
    }
    
    public Point getFinalPoint()
    {
    	return finalPoint;
    }
    
    public static String pointAsString(String name, Point p)
    {
    	return name+"(" + p.x +  ", " + p.y + ")";
    }
    
    public boolean checkPoint(String name, Point p)
    {
    	
		try
		{
			lg.throwsExceptionIfOutOfBounds(p.x, p.y);
		}
		catch(LTGCException ltgcException)
		{

			System.out.println(pointAsString(name, p) + "is out of Bounds!");
			return false;
		}

    	return true;
    }
}