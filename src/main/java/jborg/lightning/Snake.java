package jborg.lightning;


import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.awt.Point;
import java.util.ArrayList;


public class Snake implements Cloneable
{
	
	public static final String constructorExcepMsgNullArgument = "Constructor argument is null.";
	public static final String constructorExcepMsgEmptyArgument = "Constructor argument is empty.";
	public static final String constructorExcepMsgNullGap = "Constructor argument contains null.";
	public static final String constructorExcepMsgDistanceGap ="At least two consecutive Points are not near each other.";
	public static final String constructorExcepMsgDoublePoint = "Argument has at least two identical Points";
	
	public static final String growExcepMsgNewHeadNotNearBy = "New Head not near by!";
	public static final String growExcepMsgNewHeadAlreadyContained = "New Head already contained.";

	private final List<Point> consecutiveParts = new ArrayList<>();
	private final Point startPoint;
	
	
	
	public Snake(Point startPoint)
	{
		
		this.startPoint = startPoint;
		
		consecutiveParts.add(startPoint);
	}
	
	public Snake(int xPos, int yPos)
	{
		
		Point startPoint = new Point(xPos, yPos);
		this.startPoint = startPoint;
		
		consecutiveParts.add(startPoint);
	}
	
	public Snake(List<Point> parts) throws SnakeException
	{
		
		if(parts==null)throw new SnakeException(constructorExcepMsgNullArgument);
		if(parts.isEmpty())throw new SnakeException(constructorExcepMsgEmptyArgument);
		if(parts.contains(null))throw new SnakeException(constructorExcepMsgNullGap);

		int length = parts.size();
		this.startPoint = parts.get(0);
		
		for(int n=0;n<length;n++)
		{
			Point p = parts.get(n);
			if(n>0&&(!isNearBy(parts.get(n-1), p)))throw new SnakeException(constructorExcepMsgDistanceGap);
			if(n>0&&consecutiveParts.contains(p))throw new SnakeException(constructorExcepMsgDoublePoint);
			consecutiveParts.add(new Point(p.x, p.y));
		}
	}

	public Snake growSnake(int xPos, int yPos) throws SnakeException
	{
		
		Point head = getHead();
		Point successorPoint = new Point(xPos, yPos);
		
		if(!isNearBy(head, successorPoint))throw new SnakeException(growExcepMsgNewHeadNotNearBy);
		if(consecutiveParts.contains(successorPoint))throw new SnakeException(growExcepMsgNewHeadAlreadyContained);
		Snake newSnake = this.clone();
		newSnake.consecutiveParts.add(new Point(xPos, yPos));
		
		return newSnake;
	}
	
	public Snake clone()//Deep copy?!?!
	{
		
		List<Point> newParts = new ArrayList<>();
		int length = consecutiveParts.size();
		
		for(int n=0;n<length;n++)
		{
			Point p = consecutiveParts.get(n);
			newParts.add(new Point(p.x, p.y));
		}

		try
		{
			return new Snake(newParts);
		}
		catch(SnakeException snkExce)
		{
			System.out.println("Somehow parts aint valide.");
			return null;
		}
	}
	
	public Point getStart()
	{
		return (Point) startPoint.clone();
	}
	
	public int getLength()
	{
		return consecutiveParts.size();
	}
	
	public Point getHead()//Immutability just returning a Deep Copy
	{
		Point head = consecutiveParts.get(getLength()-1);
		return new Point(head.x, head.y);
	}
	
	public List<Point> getParts()//Im not returning the original 
	{							 //because of immutability.
		
		List<Point> newParts = new ArrayList<>();
		int length = consecutiveParts.size();
		
		for(int n=0;n<length;n++)
		{
			Point p = consecutiveParts.get(n);
			newParts.add(new Point(p.x, p.y));
		}

		return newParts;
	}
	
	public boolean isNearBy(Point point, Point successorPoint)
	{
		int absoluteXDiff = Math.abs(point.x-successorPoint.x);
		int absoluteYDiff = Math.abs(point.y-successorPoint.y);
		
		if(absoluteXDiff>1) return false;
		if(absoluteYDiff>1) return false;
		
		return true;
	}
	
	public <E> List<E> doFuncPartByPart(Function<Point, E> func)
	{
		
		List<E> ergebnisse = new ArrayList<>();
		int l = getLength();
		
		for(int n=0;n<l;n++)ergebnisse.add(func.apply(consecutiveParts.get(n)));
		
		return ergebnisse;
		
	}
	
	public boolean equals(Object obj)
	{
		
		if (obj == this) return true;
		
	    if (!(obj instanceof Snake)) return false;
	    
	    Snake other = (Snake)obj;//TODO: Must be raw?

	    if(other.getLength()!=this.getLength())return false;
	    
	    int length = other.getLength();
	    for(int n=0;n<length;n++)
	    {
	    	Point otherP = other.consecutiveParts.get(n);
	    	Point thisP = this.consecutiveParts.get(n);
	    	if(!otherP.equals(thisP))return false;
	    }
	    	
	    return true;
	}
	
	public int hashCode()
	{
		return Objects.hash(consecutiveParts);
	}
}
