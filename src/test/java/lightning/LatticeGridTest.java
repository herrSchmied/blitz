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
		lg.setAllLatticesOnTile(3, 4);
		lg.setOneLatticeOnTile(5, 5, indexLatticeBitLeft);
		
		int affectedTilesCounter =0;
		int hasLatticeAnywhere = 0;
		for(int x=0;x<widthInTiles;x++)
		{
			for(int y=0;y<heightInTiles;y++)
			{
				
				if(lg.hasLatticeSomeWhere(x, y)) hasLatticeAnywhere++;
				
				if(lg.hasLatticeOnTheBottom(x, y))
				{
					affectedTilesCounter++;
					continue;
				}
				
				if(lg.hasLatticeOnTheTop(x, y))
				{
					affectedTilesCounter++;
					continue;
					
				}
				if(lg.hasLatticeOnTheLeft(x, y))
				{
					affectedTilesCounter++;
					continue;
				}
				if(lg.hasLatticeOnTheRight(x, y))
				{
					affectedTilesCounter++;
					continue;
				}
			}
		}
		
		System.out.println("AffectedTiles: " + affectedTilesCounter);
		assert(affectedTilesCounter==13);
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

	@Test
	public void testSetAndCheckLatticesMethods()
	{
		
	}

}