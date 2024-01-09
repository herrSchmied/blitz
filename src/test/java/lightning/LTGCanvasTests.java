package lightning;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

import jborg.lightning.LTGCException;

import jborg.lightning.LatticeTileGridCanvas;
import static jborg.lightning.LatticeTileGridCanvas.*;


public class LTGCanvasTests 
{

	@Test
	public void test() throws LTGCException
	{
		
		int widthInTiles = 10;
		int heightInTiles = 10;
		int tileSize = 10;
		double strokeWidthLattice = 3.5;
		
		boolean [] latticeBits = new boolean[4];
		latticeBits[indexLatticeBitBottom] = true;
		latticeBits[indexLatticeBitLeft] = false;
		latticeBits[indexLatticeBitRight] = false;
		latticeBits[indexLatticeBitTop] = true;
		
		LatticeTileGridCanvas ltgCnvs = new LatticeTileGridCanvas(widthInTiles, heightInTiles, tileSize, strokeWidthLattice);
		ltgCnvs.setLatticesOnTile(2, 2, latticeBits, Color.GREEN);
		
		latticeBits[indexLatticeBitBottom] = false;
		latticeBits[indexLatticeBitLeft] = true;
		latticeBits[indexLatticeBitRight] = true;
		latticeBits[indexLatticeBitTop] = false;
		
		ltgCnvs.setLatticesOnTile(7, 7, latticeBits, Color.RED);
		
		int affectedTilesCounter =0;
		for(int n=0;n<widthInTiles;n++)
		{
			for(int m=0;m<heightInTiles;m++)
			{
				boolean bottom = ltgCnvs.hasLatticeOnTheBottom(n, m);
				boolean top = ltgCnvs.hasLatticeOnTheTop(n, m);
				boolean right = ltgCnvs.hasLatticeOnTheRight(n, m);
				boolean left = ltgCnvs.hasLatticeOnTheLeft(n, m);
				
				if((left||right||top||bottom))
				{
					if(bottom)
					{
						System.out.println("LatticeCode: " + ltgCnvs.getLatticeCode(n, m));
						System.out.println("Tile(" + n + ", " + m + ") has Lattice on Bottom: " + bottom);
					}
					if(top)
					{
						System.out.println("LatticeCode: " + ltgCnvs.getLatticeCode(n, m));
						System.out.println("Tile(" + n + ", " + m + ") has Lattice on Top: " + top);
					}
					if(right)
					{
						System.out.println("LatticeCode: " + ltgCnvs.getLatticeCode(n, m));
						System.out.println("Tile(" + n + ", " + m + ") has Lattice on Right: " + right);
					}
					if(left)
					{
						System.out.println("LatticeCode: " + ltgCnvs.getLatticeCode(n, m));
						System.out.println("Tile(" + n + ", " + m + ") has Lattice on Left: " + left);
					}
					affectedTilesCounter++;
				}
			}
		}
		
		System.out.println("AffectedTiles: " + affectedTilesCounter);
		assert(affectedTilesCounter==6);
	}

}