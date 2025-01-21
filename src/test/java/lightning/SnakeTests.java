package lightning;


import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import jborg.lightning.AnalysisToolSnake;
import jborg.lightning.Snake;
import jborg.lightning.exceptions.SnakeException;


public class SnakeTests
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
		points.add(new Point(1, 1));
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
		assert(AnalysisToolSnake.containingThisSequenz(points, snake));
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
		
		int startX = 0;
		int startY = 0;
		Point startP = new Point(startX, startY);

		int nextX =1;
		int nextY =0;
		Point nextP = new Point(nextX, nextY);
	
		Snake snake = new Snake(startX, startY, Snake.readyStatus);
		snake = snake.growSnake(nextX, nextY, Snake.readyStatus);
		
		assert(snake.getLength()==2);
		List<Point> sequenz = new ArrayList<>();
		sequenz.add(startP);
		sequenz.add(nextP);
		
		assert(AnalysisToolSnake.containingThisSequenz(sequenz, snake));
		
		snake = snake.growSnake(2, 0, Snake.readyStatus);
		
		assert(snake.getLength()==3);
		
	}

	@Test
	public void testImmutabilityUnderGrowth() throws SnakeException
	{
		
		Snake snake1 = new Snake(0, 0, Snake.readyStatus);
		Snake snake2 = snake1.growSnake(1, 0, Snake.readyStatus);
		
		assert(snake1!=snake2);
		assert(!snake2.equals(snake1));
	}
}