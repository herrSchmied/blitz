package jborg.lightning;


import java.util.List;
import java.util.Set;
import java.util.function.Function;

import jborg.lightning.exceptions.SnakeException;

import static consoleTools.TerminalXDisplay.*;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class Snake implements Cloneable, Serializable
{

	private static final long serialVersionUID = 4520949554290687793L;
	public static final String excepMsgUnknownStatus = "Unknown Status!";
	public static final String excepMsgStatusChangeNotAllowedMoreThanOnce = "Status can only be changed once!";
	
	public static final String constructorExcepMsgNullArgument = "Constructor argument is null.";
	public static final String constructorExcepMsgEmptyArgument = "Constructor argument is empty.";
	public static final String constructorExcepMsgNullGap = "Constructor argument contains null.";
	public static final String constructorExcepMsgDistanceGap ="At least two consecutive Points are not near each other.";
	public static final String constructorExcepMsgDoublePoint = "Argument has at least two identical Points";
	public static final String constructorExcepMsgSelfCrossing = "In Argument is a self crossing contained.";

	public static final String cantTestExcepMsgNullArgument = "Can't test if it is nearby because one or both Points are null!";
	public static final String growExcepMsgNewHeadNotNearBy = "New Head not near by!";
	public static final String growExcepMsgNewHeadAlreadyContained = "New Head already contained.";
	public static final String growExcepMsgSelfCrossing = "Selfcrossing not allowed.";

	private final List<Point> consecutiveParts = new ArrayList<>();
	private final Point startPoint;
	
	public static final String readyStatus = "Ready!";
	public static final String deadStatus = "Dead!";
	private static final List<String> statie = new ArrayList<>(Arrays.asList(readyStatus, deadStatus));
	private String status = "";
	private boolean statusChanged = false;

	public Snake(Point startPoint, String status) throws SnakeException
	{

		throwsExceptionIfPointIsNull(startPoint);
		throwsExceptionIfStatusIsUnknown(status);
		if(status.equals(deadStatus))statusChanged = true;
		this.startPoint = startPoint;
		this.status = status;
		
		consecutiveParts.add(startPoint);
	}
	
	public Snake(int xPos, int yPos, String status) throws SnakeException
	{
		this(new Point(xPos, yPos), status);
	}
	
	public Snake(List<Point> parts, String status) throws SnakeException
	{
		
		throwsExceptionIfStatusIsUnknown(status);
		throwsExceptionIfPartsNotValid(parts);
		
		this.startPoint = parts.get(0);
		this.status = status;
		if(status.equals(deadStatus))statusChanged = true;
		
		consecutiveParts.addAll(parts);
	}

	public Snake growSnake(Point p, String status) throws SnakeException
	{
		return growSnake(p.x, p.y, status);
	}

	public Snake growSnake(int xPos, int yPos, String status) throws SnakeException
	{

		throwsExceptionIfStatusIsUnknown(status);

		Point head = getHead();
		Point successorPoint = new Point(xPos, yPos);

		throwsExceptionIfIsAlreadyInSnake(successorPoint);
		throwsExceptionIfIsIllegalSuccessor(head, successorPoint);
		

		List<Point> consecutiveParts_ii = new ArrayList<>();
		consecutiveParts_ii.addAll(this.consecutiveParts);
		consecutiveParts_ii.add(successorPoint);
		
		return new Snake(consecutiveParts_ii, status);

	}
	
	
	/** Only makes Sense if p and successorPoint are near to
	 * 	each other.
	 * @throws SnakeException 
	 */
	public boolean isSelfCrossing(Point p, Point successorPoint, List<Point> parts) throws SnakeException
	{
		
		throwsExceptionIfPointIsNull(p);
		throwsExceptionIfPointIsNull(successorPoint);
		//Couldn't use throwsExceptionIfIsIllegalSuccessor because infinite Loop.
		if(!isNearBy(p, successorPoint))throw new SnakeException(growExcepMsgNewHeadNotNearBy);

		int xDiff = successorPoint.x-p.x;
		int yDiff = successorPoint.y-p.y;
		
		
		if(!(Math.abs(xDiff)==1&&Math.abs(yDiff)==1))return false;//Diagonal Test

		boolean xFlanke = 
				(parts.contains(new Point(p.x+xDiff, p.y)));
		
		boolean yFlanke =
				(parts.contains(new Point(p.x, p.y+yDiff)));
		
		return yFlanke&&xFlanke;
	}
	
	public boolean isSelfCrossing(Point p, Point successorPoint) throws SnakeException
	{
		return isSelfCrossing(p, successorPoint, consecutiveParts);
	}

	@SuppressWarnings("unchecked")
	public Snake clone()//Deep copy?!?!
	{
		
		ArrayList<Point> newParts = new ArrayList<>();
		ArrayList<Point> cast = (ArrayList<Point>)consecutiveParts;
		newParts = (ArrayList<Point>) cast.clone();
		
		try
		{
			return new Snake(newParts, this.status);
		}
		catch (SnakeException e)//Shouldn't ever happen!!!
		{

			e.printStackTrace();
			System.out.println("Couldn't clone.");

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
	
	@SuppressWarnings("unchecked")
	public List<Point> getParts()//Im not returning the original 
	{							 //because of immutability.
		
		ArrayList<Point> newParts = new ArrayList<>();
		ArrayList<Point> cast = (ArrayList<Point>)consecutiveParts;
		newParts = (ArrayList<Point>) cast.clone();
		
		return newParts;
		
	}
	
	public boolean isNearBy(Point point, Point successorPoint) throws SnakeException
	{
		
		throwsExceptionIfPointIsNull(point);
		throwsExceptionIfPointIsNull(successorPoint);
		int absoluteXDiff = Math.abs(point.x-successorPoint.x);
		int absoluteYDiff = Math.abs(point.y-successorPoint.y);
		
		if(absoluteXDiff>1) return false;
		if(absoluteYDiff>1) return false;
		
		return true;
	}
	
	public Snake changeStatus(String newStatus) throws SnakeException
	{
		
		throwsExceptionIfStatusIsUnknown(newStatus);
		if(statusChanged)throw new SnakeException(excepMsgStatusChangeNotAllowedMoreThanOnce);
		else return new Snake(consecutiveParts, newStatus);
	}
	
	public <E> List<E> doFuncPartByPart(Function<Point, E> func)
	{
		
		List<E> ergebnisse = new ArrayList<>();
		int l = getLength();
		
		for(int n=0;n<l;n++)ergebnisse.add(func.apply(consecutiveParts.get(n)));
		
		return ergebnisse;
		
	}
	
	public boolean containsPart(Point p) throws SnakeException
	{

		throwsExceptionIfPointIsNull(p);
		for(Point point: consecutiveParts)
		{
			if(point.equals(p))return true;
		}
		
		return false;
	}
	public String getStatus()
	{
		return new String(status);//Immutable?
	}
	

	
	public void throwsExceptionIfStatusIsUnknown(String status) throws SnakeException
	{
		if(!statie.contains(status))throw new SnakeException(excepMsgUnknownStatus);	
	}
		
	public void throwsExceptionIfIsAlreadyInSnake(Point p) throws SnakeException
	{
		if(this.consecutiveParts.contains(p))throw new SnakeException(growExcepMsgNewHeadAlreadyContained);
	}

	public void throwsExceptionIfPartsNotValid(List<Point> parts) throws SnakeException
	{
		if(parts==null)throw new SnakeException(constructorExcepMsgNullArgument);
		if(parts.isEmpty())throw new SnakeException(constructorExcepMsgEmptyArgument);
		if(parts.contains(null))throw new SnakeException(constructorExcepMsgNullGap);

		
		Set<Point> uniqueness = new HashSet<>(parts);
		if(uniqueness.size()<parts.size())throw new SnakeException(constructorExcepMsgDoublePoint);
		
		
		for(int n=0;n<parts.size();n++)
		{
			Point p2 = parts.get(n);
			Point p1 = null;
			if(n>0)
			{
				p1 = parts.get(n-1);
				if(!isNearBy(p1, p2))throw new SnakeException(constructorExcepMsgDistanceGap);			
				if(isSelfCrossing(p1, p2, parts))throw new SnakeException(constructorExcepMsgSelfCrossing);
			}
		}
}
	public void throwsExceptionIfIsIllegalSuccessor(Point A, Point successorOfA) throws SnakeException
	{
		if(isSelfCrossing(A, successorOfA))throw new SnakeException(growExcepMsgSelfCrossing);
		if(!isNearBy(A, successorOfA))throw new SnakeException(growExcepMsgNewHeadNotNearBy);
	}

	public void throwsExceptionIfPointIsNull(Point p) throws SnakeException
	{
		if(p==null)throw new SnakeException("Point is null!");
	}

	public int hashCode()
	{
	       int result = 17;
	       
	       result = 31 * result + status.hashCode();
	       
	       for(int n=0;n<getLength();n++)
	       {
	    	   result = 31 * result + consecutiveParts.get(n).hashCode();
	       }

	       return result;
	}
	
	public boolean equals(Object obj)
	{
		
		if (obj == this) return true;
		
	    if (!(obj instanceof Snake)) return false;
	    
	    Snake other = (Snake)obj;//TODO: Must be raw?

	    if(other.getLength()!=this.getLength())return false;
	    
	    int length = other.getLength();
	    int l2 = this.getLength();
	    if(l2!=length)return false;
	    
	    for(int n=0;n<length;n++)
	    {

	    	Point thisPoint = this.consecutiveParts.get(n);
	    	Point otherPoint = other.consecutiveParts.get(n);
	    	
	    	if(!(thisPoint.x==otherPoint.x&&thisPoint.y==otherPoint.y))
	    	{
	    		System.out.println("Expected: P("+thisPoint.x+", "+thisPoint.y+")");
	    		System.out.println("Actual: P("+otherPoint.x+", "+otherPoint.y+")");
	    		return false;
	    	}
	    }

	    return true;
	}

	public String toString()
	{
		
		String z = "";

		for(int n=0;n<consecutiveParts.size();n++)
		{
			Point part = consecutiveParts.get(n);
			z = z + pointToString("P", part) + "\n";
		}
		
		return z;
	}
	
}