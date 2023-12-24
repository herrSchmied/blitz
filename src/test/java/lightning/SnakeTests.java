package lightning;


import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import jborg.lightning.Snake;
import jborg.lightning.SnakeException;

class SnakeTests
{

	@Test
	void testSnakeGrowExceptionSelfCrossing()
	{

		List<Point> points = new ArrayList<>();
		points.add(new Point(1, 1));
		points.add(new Point(0, 2));
		points.add(new Point(0, 1));
		
		Exception exception = assertThrows(SnakeException.class, ()->
		{
			Snake snake = new Snake(points);
			snake = snake.growSnake(1, 2);
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
			Snake snake = new Snake(otherPoints);
			snake = snake.growSnake(0, 1);
		});
			
		expectedMessage = Snake.growExcepMsgSelfCrossing;
		actualMessage = exception.getMessage();

		assert(actualMessage.equals(expectedMessage));

	}

	@Test
	void testSnakeGrowExceptionDoublePoint()
	{
				
		List<Point> points = new ArrayList<>();
		points.add(new Point(1, 1));
		points.add(new Point(0, 2));
		points.add(new Point(0, 3));
		
		Exception exception = assertThrows(SnakeException.class, ()->
		{
			Snake snake = new Snake(points);
			snake = snake.growSnake(0, 2);
		});
		
		 String expectedMessage = Snake.growExcepMsgNewHeadAlreadyContained;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}
	
	@Test
	void testSnakeGrowException()
	{
				
		List<Point> points = new ArrayList<>();
		points.add(new Point(1, 1));
		points.add(new Point(0, 2));
		points.add(new Point(0, 3));
		
		Exception exception = assertThrows(SnakeException.class, ()->
		{
			Snake snake = new Snake(points);
			snake = snake.growSnake(0, 0);
		});
		
		 String expectedMessage = Snake.growExcepMsgNewHeadNotNearBy;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}

	@Test
	void testSnakeConstructorSelfCrossing()
	{
				
		List<Point> points = new ArrayList<>();
		points.add(new Point(1, 1));
		points.add(new Point(0, 2));
		points.add(new Point(0, 1));
		points.add(new Point(1, 2));
		
		Exception exception = assertThrows(SnakeException.class, ()->
		{
			Snake snake = new Snake(points);
		});
		
		 String expectedMessage = Snake.constructorExcepMsgSelfCrossing;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}
	
	@Test
	void testSnakeConstructorExceptionNullArgument()
	{
				
		List<Point> points = null;
		
		Exception exception = assertThrows(SnakeException.class, ()->
		{
			Snake snake = new Snake(points);
		});
		
		 String expectedMessage = Snake.constructorExcepMsgNullArgument;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}

	@Test
	void testSnakeConstructorDoublePoint()
	{
				
		List<Point> points = new ArrayList<>();
		points.add(new Point(1, 1));
		points.add(new Point(0, 2));
		points.add(new Point(0, 3));
		points.add(new Point(0, 2));

		
		Exception exception = assertThrows(SnakeException.class, ()->
		{
			Snake snake = new Snake(points);
		});
		
		 String expectedMessage = Snake.constructorExcepMsgDoublePoint;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}
	
	@Test
	void testSnakeConstructorExceptionEmptyArgument()
	{
				
		List<Point> points = new ArrayList<>();
		
		Exception exception = assertThrows(SnakeException.class, ()->
		{
			Snake snake = new Snake(points);
		});
		
		 String expectedMessage = Snake.constructorExcepMsgEmptyArgument;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}

	@Test
	void testSnakeConstructorExceptionNullGap()
	{
				
		List<Point> points = new ArrayList<>();
		points.add(new Point(1, 1));
		points.add(null);//Gap
		points.add(new Point(0, 3));
		
		Exception exception = assertThrows(SnakeException.class, ()->
		{
			Snake snake = new Snake(points);
		});
		
		 String expectedMessage = Snake.constructorExcepMsgNullGap;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}

	@Test
	void testSnakeConstructorExceptionDistanceGap()
	{
				
		List<Point> points = new ArrayList<>();
		points.add(new Point(1, 1));
		points.add(new Point(0, 1));//Gap
		points.add(new Point(0, 3));//Gap
		
		Exception exception = assertThrows(SnakeException.class, ()->
		{
			Snake snake = new Snake(points);
		});
		
		 String expectedMessage = Snake.constructorExcepMsgDistanceGap;
		 String actualMessage = exception.getMessage();

		 assert(actualMessage.equals(expectedMessage));
	}

	@Test
	void testSnakeConstructor() throws SnakeException
	{
		List<Point> points = new ArrayList<>();
		//Point startPoint = new Point(1, 1);
		points.add(new Point(1, 1));
		points.add(new Point(0, 1));
		points.add(new Point(0, 2));
		
		Snake snake = new Snake(points);
		assert(snake.getLength()==3);
	}
	
	@Test
	void testSnakeClone() throws SnakeException
	{
		Snake snake = new Snake(0, 0);
		snake = snake.growSnake(1, 1);
		snake = snake.growSnake(2, 0);
		
		Snake snake2 = snake.clone();
		
		assert(snake2!=snake);
		assert(snake2.equals(snake));
		
		assert(snake2.getHead().equals(snake.getHead()));
		assert(snake2.getStart().equals(snake.getStart()));
	}

	@Test
	void testSnakeGrowth() throws SnakeException
	{
		Snake snake = new Snake(0, 0);
		snake = snake.growSnake(1, 0);
		
		assert(snake.getLength()==2);
		
		snake = snake.growSnake(2, 0);
		
		assert(snake.getLength()==3);
	}
}
