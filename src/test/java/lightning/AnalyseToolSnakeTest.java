package lightning;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jborg.lightning.AnalysisToolSnake;
import jborg.lightning.Snake;
import jborg.lightning.exceptions.SnakeException;

public class AnalyseToolSnakeTest
{

	@Test
	public void toolTest() throws SnakeException
	{
		Point startPoint = new Point(0, 0);
		Point centerPoint = new Point(1, 1);
		Point leftCenter = new Point(0, 1);
		Point centerUp = new Point(1, 2);
		Point destPoint = new Point(2, 2);
		
		
		LatticeTileGridCanvasTest.printlnGreen("\ntool Test!");
		
		List<Point> sequenz = new ArrayList<>(Arrays.asList(startPoint, centerPoint, leftCenter, centerUp, destPoint));
		List<Point> sequenz_II = sequenz.reversed();
		
		Snake snake = new Snake(sequenz);
		
		assert(!AnalysisToolSnake.containingThisSequenz(sequenz_II, snake));
		
		for(int n=0;n<sequenz.size();n++)
		{
			
			for(int k=sequenz.size();k>=n;k--)
			{
				List<Point> subSequenz = sequenz.subList(n, k);
				assert(AnalysisToolSnake.containingThisSequenz(subSequenz, snake));
			}
		}
	}
}
