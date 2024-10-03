package lightning;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import jborg.lightning.Snake;
import jborg.lightning.exceptions.SnakeException;


public class SnakeTest
{

	@Test
	public void testSnakeGrowExceptionSelfCrossing() throws InterruptedException
	{

		List<Point> points = new ArrayList<>();
		points.add(new Point(1, 1));
		points.add(new Point(0, 2));
		points.add(new Point(0, 1));
		
		SnakeException exception = assertThrows(SnakeException.class, ()->
		{
			Snake snake = new Snake(points, Snake.readyStatus);
			snake = snake.growSnake(1, 2, Snake.readyStatus);
		});
		
		String expectedMessage = Snake.growExcepMsgSelfCrossing;
		String actualMessage = exception.getMessage();

		assert(actualMessage.equals(expectedMessage));

		List<Point> otherPoints = new ArrayList<>();
		otherPoints.add(new Point(1, 1));
		otherPoints.add(new Point(0, 2));
		otherPoints.add(new Point(1, 2));
			
		exception = assertThrows(SnakeException.class, ()->
		{
			Snake snake = new Snake(otherPoints, Snake.readyStatus);
			snake = snake.growSnake(0, 1, Snake.readyStatus);
		});
			
		expectedMessage = Snake.growExcepMsgSelfCrossing;
		actualMessage = exception.getMessage();

		assert(actualMessage.equals(expectedMessage));
	}

	@Test
	public void testSnakeGrowExceptionDoublePoint() throws InterruptedException
	{
				
		List<Point> points = new ArrayList<>();
		points.add(new Point(0, 1));
		points.add(new Point(0, 2));
		points.add(new Point(0, 3));
		
		SnakeException exception = assertThrows(SnakeException.class, ()->
		{
			Snake snake = new Snake(points, Snake.readyStatus);
			snake = snake.growSnake(0, 2, Snake.readyStatus);
		});
		
		 String expectedMessage = Snake.growExcepMsgNewHeadAlreadyContained;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}
	
	@Test
	public void testSnakeGrowException() throws InterruptedException
	{
				
		List<Point> points = new ArrayList<>();
		points.add(new Point(1, 1));
		points.add(new Point(0, 2));
		points.add(new Point(0, 3));
		
		SnakeException exception = assertThrows(SnakeException.class, ()->
		{
			Snake snake = new Snake(points, Snake.readyStatus);
			snake = snake.growSnake(0, 0, Snake.readyStatus);
		});
		
		 String expectedMessage = Snake.growExcepMsgNewHeadNotNearBy;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}

	@Test
	public void testSnakeConstructorSelfCrossing() throws InterruptedException
	{
				
		List<Point> points = new ArrayList<>();
		points.add(new Point(1, 1));
		points.add(new Point(0, 2));
		points.add(new Point(0, 1));
		points.add(new Point(1, 2));
		
		SnakeException exception = assertThrows(SnakeException.class, ()->
		{
			new Snake(points, Snake.readyStatus);
		});
		
		 String expectedMessage = Snake.constructorExcepMsgSelfCrossing;
		 String actualMessage = exception.getMessage();
		 
		 assert(actualMessage.equals(expectedMessage));
	}
	
	@Test
	public void testSnakeConstructorExceptionNullArgument() throws InterruptedException
	{
				
		List<Point> points = null;
		
		SnakeException exception = assertThrows(SnakeException.class, ()->
		{
			new Snake(points, Snake.readyStatus);
		});
		
		 String expectedMessage = Snake.constructorExcepMsgNullArgument;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}

	@Test
	public void testSnakeConstructorDoublePoint() throws InterruptedException
	{
				
		List<Point> points = new ArrayList<>();
		points.add(new Point(1, 1));
		points.add(new Point(0, 2));
		points.add(new Point(0, 3));
		points.add(new Point(0, 2));

		
		SnakeException exception = assertThrows(SnakeException.class, ()->
		{
			new Snake(points, Snake.readyStatus);
		});
		
		 String expectedMessage = Snake.constructorExcepMsgDoublePoint;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}
	
	@Test
	public void testSnakeConstructorExceptionEmptyArgument() throws InterruptedException
	{
				
		List<Point> points = new ArrayList<>();
		
		SnakeException exception = assertThrows(SnakeException.class, ()->
		{
			new Snake(points, Snake.readyStatus);
		});
		
		 String expectedMessage = Snake.constructorExcepMsgEmptyArgument;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}

	@Test
	public void testSnakeConstructorExceptionNullGap() throws InterruptedException
	{
				
		List<Point> points = new ArrayList<>();
		points.add(new Point(1, 1));
		points.add(null);//Gap
		points.add(new Point(0, 3));
		
		SnakeException exception = assertThrows(SnakeException.class, ()->
		{
			new Snake(points, Snake.readyStatus);
		});
		
		 String expectedMessage = Snake.constructorExcepMsgNullGap;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}

	@Test
	public void testSnakeConstructorExceptionDistanceGap() throws InterruptedException
	{
				
		List<Point> points = new ArrayList<>();
		points.add(new Point(1, 1));
		points.add(new Point(0, 1));//Gap
		points.add(new Point(0, 3));//Gap
		
		SnakeException exception = assertThrows(SnakeException.class, ()->
		{
			new Snake(points, Snake.readyStatus);
		});
		
		 String expectedMessage = Snake.constructorExcepMsgDistanceGap;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}

	@Test
	public void testSnakeConstructor() throws SnakeException, InterruptedException
	{
		List<Point> points = new ArrayList<>();
		//Point startPoint = new Point(1, 1);
		points.add(new Point(1, 1));
		points.add(new Point(0, 1));
		points.add(new Point(0, 2));
		
		Snake snake = new Snake(points, Snake.readyStatus);
		assert(snake.getLength()==3);
	}
	
	@Test
	public void testSnakeClone() throws SnakeException, InterruptedException
	{
		Snake snake = new Snake(0, 0, Snake.readyStatus);
		snake = snake.growSnake(1, 1, Snake.readyStatus);
		snake = snake.growSnake(2, 0, Snake.readyStatus);
		
		Snake snake2 = snake.clone();
		
		assert(snake2!=snake);
		assert(snake2.equals(snake));
		
		assert(snake2.getHead().equals(snake.getHead()));
		assert(snake2.getStart().equals(snake.getStart()));
	}

	@Test
	public void testSnakeGrowth() throws SnakeException, InterruptedException
	{
		Snake snake = new Snake(0, 0, Snake.readyStatus);
		snake = snake.growSnake(1, 0, Snake.readyStatus);
		
		assert(snake.getLength()==2);
		
		snake = snake.growSnake(2, 0, Snake.readyStatus);
		
		assert(snake.getLength()==3);
	}
	
	@Test
	public void testImmutabilityUnderGrowth() throws SnakeException
	{

		Point p0 = new Point(0,0);
		Point p1 = new Point(1,0);
		List<Point> parts = new ArrayList<>();
		parts.add(p0);
		parts.add(p1);

		Snake snake0A = new Snake(parts, Snake.readyStatus);
		Snake snake1 = new Snake(p0, Snake.readyStatus);
		Snake snake0B = snake1.growSnake(p1, Snake.readyStatus);
		
		assert(snake1!=snake0A);
		
		assert(!snake0B.equals(snake1));
		assert(snake0B!=snake0A);

		if(!snake0B.equals(snake0A))System.out.println(consoleTools.BashSigns.boldBBCPX+"hmm"+consoleTools.BashSigns.boldBBCSX);
		assertTrue(snake0B.equals(snake0A));
	}
}