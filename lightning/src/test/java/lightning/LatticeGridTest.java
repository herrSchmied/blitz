package lightning;


import static jborg.lightning.LatticeGrid.*;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import static consoleTools.TerminalXDisplay.*;
import jborg.lightning.exceptions.LTGCException;
import jborg.lightning.LatticeGrid;


public class LatticeGridTest 
{

	Integer[] affectedTiles;

	@Test
	public void testLatticeGrid() throws LTGCException, InterruptedException
	{
		
		affectedTiles = new Integer[1];
		affectedTiles[0] = 0;
		
		int widthInTiles = 2;
		int heightInTiles = 2;

		LatticeGrid lg = new LatticeGrid(widthInTiles, heightInTiles);
		Point pointA = new Point(1, 1);
		
		lg.setAllLatticesOnTile(pointA);
		
		lg.walkThruTiles((p)->
		{
			
			try
			{
				if(lg.hasLatticeSomeWhere(p))
				{
					if(lg.hasLatticeOnTheBottom(p))System.out.println("Has Lattice On The Bottom");
					if(lg.hasLatticeOnTheTop(p))System.out.println("Has Lattice On The Top");
					if(lg.hasLatticeOnTheLeft(p))System.out.println("Has Lattice On The Left");
					if(lg.hasLatticeOnTheRight(p))System.out.println("Has Lattice On The Right");
	
					System.out.println(pointToString("P", p));
					System.out.println("\n");

					plusPlus();
				}
			}
			catch(LTGCException e)
			{
				e.printStackTrace();
			}
		});
	
			
		
		System.out.println("AffectedTiles: " + affectedTiles);
		int borderLattices = 2*widthInTiles+2*heightInTiles-4;
		
		assert(borderLattices==4);//The Lattices on Point A to C are not near those.
		assert(affectedTiles[0]==4);//Because Frameborders are considered to have 36 Lattices!
	}
	
	@Test
	public void borderTest() throws LTGCException
	{

		int widthInTiles = 5;
		int heightInTiles = 5;

		LatticeGrid lg = new LatticeGrid(widthInTiles, heightInTiles);
	
		lg.walkThruTiles((p)->
		{
			
				try
				{
					if(p.x==0)assert(lg.hasLatticeOnTheLeft(p));
					if(p.x==widthInTiles-1)assert(lg.hasLatticeOnTheRight(p));
					if(p.y==0)assert(lg.hasLatticeOnTheBottom(p));
					if(p.y==heightInTiles-1)assert(lg.hasLatticeOnTheTop(p));
			
					if(!((p.x==0)||(p.x==widthInTiles-1)||(p.y==0)||(p.y==heightInTiles-1)))
						assert(!lg.hasLatticeSomeWhere(p));
				}
				catch(LTGCException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		});
	}

	private void plusPlus()
	{
		affectedTiles[0] = affectedTiles[0]+1;

	}
}