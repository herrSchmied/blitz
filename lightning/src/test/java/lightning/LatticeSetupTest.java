package lightning;

import java.awt.Point;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import jborg.lightning.LatticeSetup;
import someMath.exceptions.LTGCException;
import someMath.pathFinder.LatticeGrid;


import static consoleTools.TerminalXDisplay.*;


public class LatticeSetupTest
{

	@Test
	public void LatticeHorizontalTest() throws LTGCException
	{

		LatticeGrid lg = new LatticeGrid(6, 6);
		LatticeSetup ls = new LatticeSetup(lg);
		int height = lg.getHeight();
		
		ls.setHorizontalLatticesLokStep();
		
		Consumer<Point> wttConsumer = (p)->
		{
			int x = p.x;
			int y = p.y;
			
			try
			{
				
				System.out.println(pointToString("P", p));

				if((y>0)&&(x%2==0))
				{
					assert(lg.hasLatticeOnTheBottom(p));
					System.out.println("It has like it should be.");
				}
				//else assert(!lg.hasLatticeOnTheBottom(p));
				
				
			}
			catch(LTGCException e)
			{
				e.printStackTrace();
			}
		};
		
		lg.walkThruTiles(wttConsumer);
	}
}
