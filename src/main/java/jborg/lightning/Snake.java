package jborg.lightning;


import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


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
		
		if(!statie.contains(status))throw new SnakeException(excepMsgUnknownStatus);
		if(status.equals(deadStatus))statusChanged = true;
		this.startPoint = startPoint;
		this.status = status;
		
		consecutiveParts.add(startPoint);
	}
	
	public Snake(int xPos, int yPos, String status) throws SnakeException
	{
		
		if(!statie.contains(status))throw new SnakeException(excepMsgUnknownStatus);
		Point startPoint = new Point(xPos, yPos);
		this.startPoint = startPoint;
		this.status = status;
		if(status.equals(deadStatus))statusChanged = true;
		
		consecutiveParts.add(startPoint);
	}
	
	public Snake(List<Point> parts, String status) throws SnakeException
	{
		
		if(!statie.contains(status))throw new SnakeException(excepMsgUnknownStatus);
		if(parts==null)throw new SnakeException(constructorExcepMsgNullArgument);
		if(parts.isEmpty())throw new SnakeException(constructorExcepMsgEmptyArgument);
		if(parts.contains(null))throw new SnakeException(constructorExcepMsgNullGap);

		int length = parts.size();
		this.startPoint = parts.get(0);
		this.status = status;
		if(status.equals(deadStatus))statusChanged = true;
		for(int n=0;n<length;n++)
		{
			Point p = parts.get(n);
			
			if(n>0&&(!isNearBy(parts.get(n-1), p)))throw new SnakeException(constructorExcepMsgDistanceGap);
			if(n>0&&consecutiveParts.contains(p))throw new SnakeException(constructorExcepMsgDoublePoint);
			if(n>1&&isSelfCrossing(parts.get(n-1), p))throw new SnakeException(constructorExcepMsgSelfCrossing);
			consecutiveParts.add(new Point(p.x, p.y));
		}
	}

	public Snake growSnake(int xPos, int yPos, String status) throws SnakeException
	{

		if(!statie.contains(status))throw new SnakeException(excepMsgUnknownStatus);

		Point head = getHead();
		Point successorPoint = new Point(xPos, yPos);
		
		if(!isNearBy(head, successorPoint))throw new SnakeException(growExcepMsgNewHeadNotNearBy);
		if(consecutiveParts.contains(successorPoint))throw new SnakeException(growExcepMsgNewHeadAlreadyContained);
		if(isSelfCrossing(head, successorPoint))throw new SnakeException(growExcepMsgSelfCrossing);

		Snake newSnake = new Snake(consecutiveParts, status);
		newSnake.consecutiveParts.add(new Point(xPos, yPos));

		return newSnake;
	}
	
	
	/** Only makes Sense if p and successorPoint are near to
	 * 	each other.
	 */
	public boolean isSelfCrossing(Point p, Point successorPoint)
	{
		
		int xDiff = p.x-successorPoint.x;
		int yDiff = p.y-successorPoint.y;
		
		if(Math.abs(xDiff)>1||xDiff==0)return false;//Diagonal Test
		if(Math.abs(yDiff)>1||yDiff==0)return false;//Diagonal Test

		boolean xFlanke = 
				(consecutiveParts.contains(new Point(p.x-xDiff, p.y)));
		
		boolean yFlanke =
				(consecutiveParts.contains(new Point(p.x, p.y-yDiff)));
		
		return yFlanke&&xFlanke;
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
	
	public boolean isNearBy(Point point, Point successorPoint)
	{
		int absoluteXDiff = Math.abs(point.x-successorPoint.x);
		int absoluteYDiff = Math.abs(point.y-successorPoint.y);
		
		if(absoluteXDiff>1) return false;
		if(absoluteYDiff>1) return false;
		
		return true;
	}
	
	public Snake changeStatus(String newStatus) throws SnakeException
	{
		
		if(!statie.contains(newStatus))throw new SnakeException(excepMsgUnknownStatus);
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
	
	public boolean containsPart(Point p)
	{
		
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
		return Objects.hash(consecutiveParts, status);
	}
	
	public String toString()
	{
		
		String z = "";

		for(int n=0;n<consecutiveParts.size();n++)
		{
			Point part = consecutiveParts.get(n);
			z = z + pointToString(part) + "\n";
		}
		
		return z;
	}
	
	public static String pointToString(Point p)
	{
		return "("+p.x+", "+p.y+")";
	}
}