package lightning;


import static jborg.lightning.LatticeGrid.*;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import jborg.lightning.exceptions.LTGCException;
import jborg.lightning.LatticeGrid;


public class LatticeGridTest 
{

	@Test
	public void testLatticeGrid() throws LTGCException, InterruptedException
	{
		
		int widthInTiles = 10;
		int heightInTiles = 10;

		LatticeGrid lg = new LatticeGrid(widthInTiles, heightInTiles);
		Point pointA = new Point(2, 2);
		Point pointB = new Point(7, 7);
		Point pointC = new Point(5, 5);
		
		boolean [] latticeBits = new boolean[4];
		latticeBits[indexLatticeBitBottom] = true;
		latticeBits[indexLatticeBitLeft] = false;
		latticeBits[indexLatticeBitRight] = false;
		latticeBits[indexLatticeBitTop] = true;
		
		lg.setLatticesOnTile(pointA, latticeBits);
		
		latticeBits[indexLatticeBitBottom] = false;
		latticeBits[indexLatticeBitLeft] = true;
		latticeBits[indexLatticeBitRight] = true;
		latticeBits[indexLatticeBitTop] = false;
		
		lg.setLatticesOnTile(pointB, latticeBits);
		
		lg.setOneLatticeOnTile(pointC, indexLatticeBitLeft);
		
		int affectedTiles =0;
		for(int n=0;n<widthInTiles;n++)
		{
			for(int m=0;m<heightInTiles;m++)
			{
				if(lg.hasLatticeSomeWhere(new Point(n,m)))affectedTiles++;
			}
		}
		
		System.out.println("AffectedTiles: " + affectedTiles);
		int borderLattices = 2*widthInTiles+2*heightInTiles-4;
		
		assert(borderLattices==36);//The Lattices on Point A to C are not near those.
		assert(affectedTiles==44);//Because Frameborders are considered to have 36 Lattices!
	}

}
