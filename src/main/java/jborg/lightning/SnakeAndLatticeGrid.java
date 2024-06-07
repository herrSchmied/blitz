package jborg.lightning;

import java.awt.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static consoleTools.TerminalXDisplay.*;

public class SnakeAndLatticeGrid
{
	
	private Set<Snake> snakeSet;
	private final LatticeGrid lg;
	private final Point finalPoint;
	
	public SnakeAndLatticeGrid(Snake snake, LatticeGrid lg, Point finalPoint)
    {
		
		this.snakeSet = new HashSet<>();
		snakeSet.add(snake);
		this.lg = lg;
		this.finalPoint = finalPoint;
    }
	
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
    		
    		boolean left = (hasLeft&&!lg.hasLatticeOnTheLeft(head));
    		boolean right = (hasRight&&!lg.hasLatticeOnTheRight(head));
    		boolean top = (hasTop&&!lg.hasLatticeOnTheTop(head));    		
    		boolean bottom = (hasBottom&&!lg.hasLatticeOnTheBottom(head));

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
    		
    		if(left)
    		{
    			Point newHead = addPoints(snake.getHead(), relativeLeft);
    			
    			if(checkOption(snake, newHead))growthOptions.add(relativeLeft);
    		}
    		
    		if(right)
    		{
    			Point newHead = addPoints(snake.getHead(), relativeRight);

    			if(checkOption(snake, newHead))growthOptions.add(relativeRight);
    		}
    		
    		if(top)
       		{
    			Point newHead = addPoints(snake.getHead(), relativeTop);
    			
    			if(checkOption(snake, newHead))growthOptions.add(relativeTop);
    		}
    		
    		if(bottom)
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

    private boolean checkOption(Snake snake, Point newHead)
    {
    	if(snake.isSelfCrossing(snake.getHead(), newHead))return false;
    	if(snake.getParts().contains(newHead))return false;
    	if(!snake.isNearBy(snake.getHead(), newHead))return false;

    	return true;
    }
    
    public static Point addPoints(Point p1, Point p2)
    {
    	return new Point(p1.x+p2.x, p1.y+p2.y);
    }
    
    public Set<Snake> getSnakeSet()
    {
    	return snakeSet;
    }
    
    public Set<Snake> theDivergence(Snake snake) throws LTGCException, SnakeException, InterruptedException
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
    
    public void setFinalSnakes() throws LTGCException, SnakeException, InterruptedException
    {
    	this.snakeSet = untilTheyAreAllDeadLoop(this.snakeSet);
    }
    
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
