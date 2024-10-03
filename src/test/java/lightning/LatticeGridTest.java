package lightning;


import static jborg.lightning.LatticeGrid.*;

import org.junit.jupiter.api.Test;

import jborg.lightning.LatticeGrid;
import jborg.lightning.exceptions.LTGCException;


public class LatticeGridTest 
{

	@Test
	public void testLatticeGrid() throws LTGCException, InterruptedException
	{
		
		int widthInTiles = 10;
		int heightInTiles = 10;

		LatticeGrid lg = new LatticeGrid(widthInTiles, heightInTiles);
		
		boolean [] latticeBits = new boolean[4];
		latticeBits[indexLatticeBitBottom] = true;
		latticeBits[indexLatticeBitLeft] = false;
		latticeBits[indexLatticeBitRight] = false;
		latticeBits[indexLatticeBitTop] = true;
		
		lg.setLatticesOnTile(2, 2, latticeBits);
		
		latticeBits[indexLatticeBitBottom] = false;
		latticeBits[indexLatticeBitLeft] = true;
		latticeBits[indexLatticeBitRight] = true;
		latticeBits[indexLatticeBitTop] = false;
		
		lg.setLatticesOnTile(7, 7, latticeBits);
		
		lg.setOneLatticeOnTile(5, 5, indexLatticeBitLeft);
		
		int affectedTilesCounter =0;
		int hasLatticeAnywhere = 0;
		for(int n=0;n<widthInTiles;n++)
		{
			for(int m=0;m<heightInTiles;m++)
			{
				
				if(lg.hasLatticeSomeWhere(n,m)) hasLatticeAnywhere++;
				
				if(lg.hasLatticeOnTheBottom(n, m))
				{
					affectedTilesCounter++;
					continue;
				}
				if(lg.hasLatticeOnTheTop(n, m))
				{
					affectedTilesCounter++;
					continue;
					
				}
				if(lg.hasLatticeOnTheLeft(n, m))
				{
					affectedTilesCounter++;
					continue;
				}
				if(lg.hasLatticeOnTheRight(n, m))
				{
					affectedTilesCounter++;
					continue;
				}
			}
		}
		
		System.out.println("AffectedTiles: " + affectedTilesCounter);
		assert(affectedTilesCounter==8);
		assert(hasLatticeAnywhere==affectedTilesCounter);
		assert(lg.hasLatticeOnTheBottom(2,2));
		assert(lg.hasLatticeOnTheTop(2,2));
		assert(!lg.hasLatticeOnTheRight(2,2));
		assert(!lg.hasLatticeOnTheLeft(2,2));
		assert(!lg.hasLatticeOnTheBottom(7,7));
		assert(!lg.hasLatticeOnTheTop(7,7));
		assert(lg.hasLatticeOnTheRight(7,7));
		assert(lg.hasLatticeOnTheLeft(7,7));
	}

}