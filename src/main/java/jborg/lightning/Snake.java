package jborg.lightning;


import java.util.List;
import java.util.Set;

import jborg.lightning.exceptions.SnakeException;

import static consoleTools.TerminalXDisplay.*;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * A Snake contains at least one "Point", "Coordinate Pair" or 
 * what ever u want to call it. Consecutive "Points" are always
 * neighbors on an "Grid". In other words is a Part of the Snake
 * (aka Point) is following another Part than u can imagine them
 * as two neighboring Tile coordinates. A Snake can grow as long
 * as it follows the mentioned neighbor rule and the Status is not
 * the dead status.
*/
public class Snake implements Cloneable, Serializable
{

	private static final long serialVersionUID = 4520949554290687793L;
	
	/**
	 * Exception Message String.
	 */
	public static final String excepMsgUnknownStatus = "Unknown Status!";

	/**
	 * Exception Message String.
	 */
	public static final String excepMsgStatusChangeNotAllowedMoreThanOnce = "Status can only be changed once!";

	/**
	 * Exception Message String.
	 */
	public static final String constructorExcepMsgNullArgument = "Constructor argument is null.";

	/**
	 * Exception Message String.
	 */
	public static final String constructorExcepMsgEmptyArgument = "Constructor argument is empty.";

	/**
	 * Exception Message String.
	 */
	public static final String constructorExcepMsgNullGap = "Constructor argument contains null.";

	/**
	 * Exception Message String.
	 */
	public static final String constructorExcepMsgDistanceGap ="At least two consecutive Points are not near each other.";

	/**
	 * Exception Message String.
	 */
	public static final String constructorExcepMsgDoublePoint = "Argument has at least two identical Points";

	/**
	 * Exception Message String.
	 */
	public static final String constructorExcepMsgSelfCrossing = "In Argument is a self crossing contained.";

	/**
	 * Exception Message String.
	 */
	public static final String cantTestExcepMsgNullArgument = "Can't test if it is nearby because one or both Points are null!";

	/**
	 * Exception Message String.
	 */
	public static final String growExcepMsgNewHeadNotNearBy = "New Head not near by!";

	/**
	 * Exception Message String.
	 */
	public static final String growExcepMsgNewHeadAlreadyContained = "New Head already contained.";

	/**
	 * Exception Message String.
	 */
	public static final String growExcepMsgSelfCrossing = "Selfcrossing not allowed.";

	/**
	 * Alle the Parts of the Snake. Ordering matters.
	 */
	private final List<Point> consecutiveParts = new ArrayList<>();
	
	/**
	 * The First and oldest Part of the Snake.
	 */
	private final Point startPoint;
	
	/**
	 * Possible Status of the Snake.
	 */
	public static final String readyStatus = "Ready!";

	/**
	 * Possible Status of the Snake.
	 */
	public static final String deadStatus = "Dead!";
	
	/**
	 * List of possible States of  a Snake.
	 */
	private static final List<String> statie = new ArrayList<>(Arrays.asList(readyStatus, deadStatus));
	
	/**
	 * Current status of this Snake.
	 */
	private String status = readyStatus;
	
	/**
	 * Did the Status get changed?
	 */
	private boolean statusChanged = false;

	/**
	 * Creates a Snake with readyStatus.
	 * @param startPoint the first and oldest part of the Snake.
	 * @throws SnakeException if the startPoint is null.
	 */
	public Snake(Point startPoint) throws SnakeException
	{
		this(startPoint, readyStatus);
	}

	/**
	 * Constructor
	 * 
	 * @param startPoint the first and oldest part of the Snake.
	 * @param status The dead status will block any further change
	 * that means no Growing.
	 * @throws SnakeException if the startPoint is null or if 
	 * the status is unknown(ex. null).
	 */
	public Snake(Point startPoint, String status) throws SnakeException
	{

		throwsExceptionIfPointIsNull(startPoint);
		throwsExceptionIfStatusIsUnknown(status);
		if(status.equals(deadStatus))statusChanged = true;
		this.startPoint = startPoint;
		this.status = status;
		
		consecutiveParts.add(startPoint);
	}
	
	/**
	 * A Snake contains at least one "Point", "Coordinate Pair" or 
	 * what ever u want to call it. Consecutive "Points" are always
	 * neighbors on an "Grid". In other words is a Part of the Snake
	 * (aka Point) is following another Part than u can imagine them
	 * as two neighboring Tile coordinates. A Snake can grow as long
	 * as it follows the mentioned neighbor rule and the Status is not
	 * the dead status.
	 * @param xPos startPoint x-Coordinate.
	 * @param yPos startPoint y-Coordinate.
	 * @param status The dead status will block any further change
	 * that means no more Growing.
	 * @throws SnakeException if the status is unknown.
	 */
	public Snake(int xPos, int yPos, String status) throws SnakeException
	{
		this(new Point(xPos, yPos), status);
	}
	
	/**
	 * This constructor is for a predefined longer Snake.
	 * @param parts are the Points of the Snake.
	 * @param status of the Snake.
	 * @throws SnakeException if the parts are not valid or the
	 * Status is not valid.
	 */
	public Snake(List<Point> parts, String status) throws SnakeException
	{
		
		throwsExceptionIfStatusIsUnknown(status);
		throwsExceptionIfPartsNotValid(parts);
		
		this.startPoint = parts.get(0);
		this.status = status;
		if(status.equals(deadStatus))statusChanged = true;
		
		consecutiveParts.addAll(parts);
	}

	/**
	 * Creates Snake with given parts and ready Status.
	 * 
	 * @param parts predefined parts of the Snake.
	 * @throws SnakeException if parts are invalid.
	 */
	public Snake(List<Point> parts) throws SnakeException
	{
		this(parts, readyStatus);
	}

	/** See below.
	 * 
	 * @param p new Head.
	 * @param status new Status.
	 * @return new bigger Snake.
	 * @throws SnakeException if status is unknown or if the new
	 * part is not a neighbor of the last part of the old snake.
	 */
	public Snake growSnake(Point p, String status) throws SnakeException
	{
		return growSnake(p.x, p.y, status);
	}

	/**
	 * Because of immutability the Snake actually doesn't grow.
	 * Instead u get a "Fresh" snake one Part bigger if u
	 * execute this Method and follow the "neighbor rule".
	 * @param xPos x-Coordinate of the new Part.
	 * @param yPos y-Coordinate of the new Part.
	 * @param status if it is the deadStatus it is not possible
	 * for the snake to grow after execution.
	 * @return the "Fresh" Snake one Part bigger.
	 * @throws SnakeException if status is unknown or if the new
	 * part is not a neighbor of the last part of the old snake.
	 */
	public Snake growSnake(int xPos, int yPos, String status) throws SnakeException
	{

		throwsExceptionIfStatusIsUnknown(status);

		Point head = getHead();
		Point successorPoint = new Point(xPos, yPos);

		throwsExceptionIfIsAlreadyInSnake(successorPoint);
		throwsExceptionIfIsIllegalSuccessor(head, successorPoint);
		

		List<Point> consecutiveParts_ii = new ArrayList<>();
		
		for(int n=0;n<consecutiveParts.size();n++)
		{
			Point p = consecutiveParts.get(n);
			consecutiveParts_ii.add(p);
		}
		
		consecutiveParts_ii.add(successorPoint);
		
		return new Snake(consecutiveParts_ii, status);

	}
	
	
	/**
	 * checks if u self-cross diagonally if successorPoint follows p given
	 * a list of Points as Snake-parts. Theoretically u could inject 
	 * invalid snake parts. Probably other stuff is possible too. But if 
	 * used in the sense is meant to be it gives the correct answer.
	 * @param p predecessor
	 * @param successorPoint successor
	 * @param parts parts of the snake in question.
	 * @return true if there is a diagonally crossing. Else false.
	 * @throws SnakeException if the Points are null.
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
	
	/**
	 * checks if u self-cross diagonally if successorPoint follows p given
	 * the list of Points of "This" Snake.
	 * @param p predecessor
	 * @param successorPoint successor
	 * @return true if there is a diagonally crossing. Else false.
	 * @throws SnakeException if the Points are null.
	 */
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
	
	/**
	 * Self Explanatory.
	 * @return Oldest and First Part of the Snake.
	 */
	public Point getStart()
	{
		return (Point) startPoint.clone();
	}
	
	/**
	 * Self Explanatory.
	 * @return Number of Parts of this Snake.
	 */
	public int getLength()
	{
		return consecutiveParts.size();
	}
	
	/**
	 * Self Explanatory.
	 * @return the last and youngest part of the Snake.
	 */
	public Point getHead()//Immutability just returning a Deep Copy
	{
		Point head = consecutiveParts.get(getLength()-1);
		return new Point(head.x, head.y);
	}
	
	/**
	 * Self Explanatory.
	 * @return List of Parts of the Snake. Order Matters.
	 * Because of Immutability it's a clone.
	 */
	@SuppressWarnings("unchecked")
	public List<Point> getParts() 
	{

		ArrayList<Point> newParts = new ArrayList<>();
		ArrayList<Point> cast = (ArrayList<Point>)consecutiveParts;
		newParts = (ArrayList<Point>) cast.clone();
		
		return newParts;
		
	}
	
	/**
	 * Checks if a and b are neighbors.
	 * @param a Point.
	 * @param b Point.
	 * @return true if a and b are neighbors.
	 * @throws SnakeException if a and/or b is null.
	 */
	public boolean isNearBy(Point a, Point b) throws SnakeException
	{
		
		throwsExceptionIfPointIsNull(a);
		throwsExceptionIfPointIsNull(b);
		int absoluteXDiff = Math.abs(a.x-b.x);
		int absoluteYDiff = Math.abs(a.y-b.y);
		
		if(absoluteXDiff>1) return false;
		if(absoluteYDiff>1) return false;
		
		return true;
	}
	
	/**
	 * Changes the Status of a snake. this can happen only once per Snake.
	 * @param newStatus self Explanatory.
	 * @return new Snake and new Status.
	 * @throws SnakeException if newStatus is 
	 */
	public Snake changeStatus(String newStatus) throws SnakeException
	{
		throwsExceptionIfStatusIsUnknown(newStatus);
		if(statusChanged)throw new SnakeException(excepMsgStatusChangeNotAllowedMoreThanOnce);
		else 
		{
			statusChanged = true;
			return new Snake(consecutiveParts, newStatus);
		}
	}

	/**
	 * Self Explanatory.
	 * @param p Potential part of this Snake?
	 * @return Is p Part of this Snake?
	 * @throws SnakeException If p is null.
	 */
	public boolean containsPart(Point p) throws SnakeException
	{

		throwsExceptionIfPointIsNull(p);
		for(Point point: consecutiveParts)
		{
			if(point.equals(p))return true;
		}
		
		return false;
	}
	
	/**
	 * Status of this Snake.
	 * @return Status.
	 */
	public String getStatus()
	{
		return new String(status);//Immutable?
	}

	/**
	 * Self Explanatory.
	 * @param status To be checked.
	 * @throws SnakeException If Status is null or not known.
	 */
	public void throwsExceptionIfStatusIsUnknown(String status) throws SnakeException
	{
		if(!statie.contains(status))throw new SnakeException(excepMsgUnknownStatus);	
	}
	
	/**
	 * Self Explanatory.
	 * @param p Point.
	 * @throws SnakeException if p is already part of this Snake.
	 */
	public void throwsExceptionIfIsAlreadyInSnake(Point p) throws SnakeException
	{
		if(this.consecutiveParts.contains(p))throw new SnakeException(growExcepMsgNewHeadAlreadyContained);
	}

	/**
	 * Exeception thrower
	 * @param parts List of Parts of a Snake.
	 * @throws SnakeException 
	 * If the Parts contain Null or are Null.
	 * If it is a empty List.
	 * If a Part is more than one time in the List.
	 * If the order of the Parts don't follow the Neighbor rule.
	 */
	public void throwsExceptionIfPartsNotValid(List<Point> parts) throws SnakeException
	{
		if(parts==null)throw new SnakeException(constructorExcepMsgNullArgument);
		if(parts.isEmpty())throw new SnakeException(constructorExcepMsgEmptyArgument);
		if(parts.contains(null))throw new SnakeException(constructorExcepMsgNullGap);

		
		Set<Point> uniqueness = new HashSet<>(parts);
		if(uniqueness.size()<parts.size())throw new SnakeException(constructorExcepMsgDoublePoint);
		
		
		List<Point> notIncludingLaterParts = new ArrayList<>();
		for(int n=0;n<parts.size();n++)
		{
			Point p2 = parts.get(n);
			notIncludingLaterParts.add(p2);
			Point p1 = null;
			if(n>0)
			{
				p1 = parts.get(n-1);
				if(!isNearBy(p1, p2))throw new SnakeException(constructorExcepMsgDistanceGap);			
				if(isSelfCrossing(p1, p2, notIncludingLaterParts))throw new SnakeException(constructorExcepMsgSelfCrossing);
			}
		}
	}
	
	/**
	 * SelfExplanatory
	 * 
	 * @param A To be checked.
	 * @param successorOfA To be checked.
	 * @throws SnakeException If successorOfA can't follow Point A.
	 */
	public void throwsExceptionIfIsIllegalSuccessor(Point A, Point successorOfA) throws SnakeException
	{
		if(isSelfCrossing(A, successorOfA))throw new SnakeException(growExcepMsgSelfCrossing);
		if(!isNearBy(A, successorOfA))throw new SnakeException(growExcepMsgNewHeadNotNearBy);
	}

	/**
	 * Simple Nullcheck.
	 * @param p Point to be checked.
	 * @throws SnakeException if p is null.
	 */
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
	    
	    Snake other = (Snake)obj;

	    if(other.getLength()!=this.getLength())return false;
	    
	    int length = other.getLength();
	    int l2 = this.getLength();
	    if(l2!=length)return false;
	    
	    for(int n=0;n<length;n++)
	    {

	    	Point thisPoint = this.consecutiveParts.get(n);
	    	Point otherPoint = other.consecutiveParts.get(n);
	    	
	    	if(!(thisPoint.x==otherPoint.x&&thisPoint.y==otherPoint.y))return false;

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