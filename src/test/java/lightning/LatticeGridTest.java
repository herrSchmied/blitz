package lightning;


import static jborg.lightning.LatticeGrid.*;

import org.junit.jupiter.api.Test;

import jborg.lightning.LTGCException;
import jborg.lightning.LatticeGrid;


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
		for(int n=0;n<widthInTiles;n++)
		{
			for(int m=0;m<heightInTiles;m++)
			{
				boolean bottom = lg.hasLatticeOnTheBottom(n, m);
				boolean top = lg.hasLatticeOnTheTop(n, m);
				boolean right = lg.hasLatticeOnTheRight(n, m);
				boolean left = lg.hasLatticeOnTheLeft(n, m);
				
				if((left||right||top||bottom))
				{
					if(bottom)
					{
						System.out.println("LatticeCode: " + lg.getLatticeCode(n, m));
						System.out.println("Tile(" + n + ", " + m + ") has Lattice on Bottom: " + bottom);
					}
					if(top)
					{
						System.out.println("LatticeCode: " + lg.getLatticeCode(n, m));
						System.out.println("Tile(" + n + ", " + m + ") has Lattice on Top: " + top);
					}
					if(right)
					{
						System.out.println("LatticeCode: " + lg.getLatticeCode(n, m));
						System.out.println("Tile(" + n + ", " + m + ") has Lattice on Right: " + right);
					}
					if(left)
					{
						System.out.println("LatticeCode: " + lg.getLatticeCode(n, m));
						System.out.println("Tile(" + n + ", " + m + ") has Lattice on Left: " + left);
					}
					affectedTilesCounter++;
				}
			}
		}
		
		System.out.println("AffectedTiles: " + affectedTilesCounter);
		assert(affectedTilesCounter==8);
		
		Thread.sleep(750);;
	}

}