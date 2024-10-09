package jborg.lightning;

import java.awt.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	 */
	public List<Point> getOptions(Snake snake) throws LTGCException, SnakeException
    {
    
    	
    		Point relativeLeft = new Point(-1, 0);
    		Point relativeRight = new Point(+1, 0);
    		Point relativeTop = new Point(0,+1);
    		Point relativeBottom = new Point(0,-1);
    		Point relativeLeftTop = addPoints(relativeLeft, relativeTop);
    		Point relativeLeftBottom = addPoints(relativeLeft, relativeBottom);
    		Point relativeRightTop = addPoints(relativeRight, relativeTop);
    		Point relativeRightBottom = addPoints(relativeRight, relativeBottom);
    		
    		Point head = snake.getHead();
    		List<Point> growthOptions = new ArrayList<>();
    		int width = lg.getWidth();
    		int height = lg.getHeight();
    		
    		boolean hasLeft = head.x>0;
    		boolean hasRight = head.x<width-1;
    		boolean hasTop = head.y<height-1;
    		boolean hasBottom = head.y>0;
    		
    		boolean leftIsAnOption = (hasLeft&&!lg.hasLatticeOnTheLeft(head));
    		boolean rightIsAnOption = (hasRight&&!lg.hasLatticeOnTheRight(head));
    		boolean topIsAnOption = (hasTop&&!lg.hasLatticeOnTheTop(head));    		
    		boolean bottomIsAnOption = (hasBottom&&!lg.hasLatticeOnTheBottom(head));

    		boolean hasLeftTop = head.x>0&&head.y<height-1;
    		boolean hasLeftBottom = head.x>0&&head.y>0;
    		boolean hasRightTop = head.x<width-1&&head.y<height-1;
    		boolean hasRightBottom = head.x<width-1&&head.y>0;
    		
    		boolean leftTop = false;
    		if(!hasLeftTop)leftTop= false;
    		else
    		{
    			
    			Point dest = addPoints(head, relativeLeftTop);
    			boolean destinationRight = lg.hasLatticeOnTheRight(dest);
    			boolean destinationBottom = lg.hasLatticeOnTheBottom(dest);
    			boolean headLeft = lg.hasLatticeOnTheLeft(head);
    			boolean headTop = lg.hasLatticeOnTheTop(head);
    			
    			boolean verticalLineIsolation = headLeft&&destinationRight;
    			boolean horizontalLineIsolation = headTop&&destinationBottom;
    			boolean lLineIsolationNearBy = headLeft&&headTop;
    			boolean lLineIsolationOnTheOtherSide = destinationRight
    													&&destinationBottom;
    	
    			leftTop = !(verticalLineIsolation||horizontalLineIsolation
    				||lLineIsolationNearBy||lLineIsolationOnTheOtherSide);
    		}
    	
    		boolean leftBottom = false;
    		if(!hasLeftBottom)leftBottom= false;
    		else
    		{
    			
    			Point dest = addPoints(head, relativeLeftBottom);
    			boolean destinationRight = lg.hasLatticeOnTheRight(dest);
    			boolean destinationTop = lg.hasLatticeOnTheTop(dest);
    			boolean headLeft = lg.hasLatticeOnTheLeft(head);
    			boolean headBottom = lg.hasLatticeOnTheBottom(head);
    			
    			boolean verticalLineIsolation = headLeft&&destinationRight;
    			boolean horizontalLineIsolation = headBottom&&destinationTop;
    			boolean lLineIsolationNearBy = headLeft&&headBottom;
    			boolean lLineIsolationOnTheOtherSide = destinationRight&&destinationTop;
    			
    			leftBottom = !(verticalLineIsolation||horizontalLineIsolation
    				||lLineIsolationNearBy||lLineIsolationOnTheOtherSide);
    		}

    		boolean rightBottom = false;
    		if(!hasRightBottom)rightBottom= false;
    		else
    		{
    			
    			Point dest = addPoints(head, relativeRightBottom);
    			boolean destinationLeft = lg.hasLatticeOnTheLeft(dest);
    			boolean destinationTop = lg.hasLatticeOnTheTop(dest);
    			boolean headRight = lg.hasLatticeOnTheRight(head);
    			boolean headBottom = lg.hasLatticeOnTheBottom(head);
    			
    			boolean verticalLineIsolation = headRight&&destinationLeft;
    			boolean horizontalLineIsolation = headBottom&&destinationTop;
    			boolean lLineIsolationNearBy = headRight&&headBottom;
    			boolean lLineIsolationOnTheOtherSide = destinationLeft&&destinationTop;
    	
    			rightBottom = !(verticalLineIsolation||horizontalLineIsolation
    				||lLineIsolationNearBy||lLineIsolationOnTheOtherSide);
    		}
    		
    		boolean rightTop = false;
    		if(!hasRightTop)rightTop= false;
    		else
    		{
    			
    			Point dest = addPoints(head, relativeRightTop);
    			boolean destinationLeft = lg.hasLatticeOnTheLeft(dest);
        		boolean headRight = lg.hasLatticeOnTheRight(head);
        		boolean headTop = lg.hasLatticeOnTheTop(head);
    			boolean destinationBottom = lg.hasLatticeOnTheBottom(dest);
    			
    			boolean verticalLineIsolation = headRight&&destinationLeft;
    			boolean horizontalLineIsolation = headTop&&destinationBottom;
    			boolean lLineIsolationNearBy = headRight&&headTop;
    			boolean lLineIsolationOnTheOtherSide = destinationBottom&&destinationLeft;
    	
    			rightTop = !(verticalLineIsolation||horizontalLineIsolation
    				||lLineIsolationNearBy|lLineIsolationOnTheOtherSide);
    		}
    		
    		if(leftIsAnOption)
    		{
    			Point newHead = addPoints(snake.getHead(), relativeLeft);
    			
    			if(checkOption(snake, newHead))growthOptions.add(relativeLeft);
    		}
    		
    		if(rightIsAnOption)
    		{
    			Point newHead = addPoints(snake.getHead(), relativeRight);

    			if(checkOption(snake, newHead))growthOptions.add(relativeRight);
    		}
    		
    		if(topIsAnOption)
       		{
    			Point newHead = addPoints(snake.getHead(), relativeTop);
    			
    			if(checkOption(snake, newHead))growthOptions.add(relativeTop);
    		}
    		
    		if(bottomIsAnOption)
       		{
    			Point newHead = addPoints(snake.getHead(), relativeBottom);
    			
    			if(checkOption(snake, newHead))growthOptions.add(relativeBottom);
    		}
    		
    		if(leftTop)
       		{
    			Point newHead = addPoints(snake.getHead(), relativeLeftTop);
    			
    			if(checkOption(snake, newHead))growthOptions.add(relativeLeftTop);
    		}
       		
    		if(leftBottom)
       		{
    			Point newHead = addPoints(snake.getHead(), relativeLeftBottom);
    			
    			if(checkOption(snake, newHead))growthOptions.add(relativeLeftBottom);
    		}
    		
    		if(rightTop)
       		{
    			Point newHead = addPoints(snake.getHead(), relativeRightTop);
    			
    			if(checkOption(snake, newHead))growthOptions.add(relativeRightTop);
    		}
    		
    		if(rightBottom)
       		{
    			Point newHead = addPoints(snake.getHead(), relativeRightBottom);
    			
    			if(checkOption(snake, newHead))growthOptions.add(relativeRightBottom);
    		}
    		
    		return growthOptions;
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
     */
    public Set<Snake> theDivergence(Snake snake) throws InterruptedException, SnakeException, LTGCException
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
    		
    		Point newHead = addPoints(snake.getHead(), p);
    		Snake spawn = snake.growSnake(newHead.x, newHead.y, Snake.readyStatus);
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
     */
    public void setFinalSnakes() throws LTGCException, SnakeException, InterruptedException
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
     */
    public Set<Snake> untilTheyAreAllDeadLoop(Set<Snake> snakeSet) throws LTGCException, SnakeException, InterruptedException
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
}