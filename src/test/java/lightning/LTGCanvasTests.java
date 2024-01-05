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
		
		boolean [] latticeBits = new boolean[4];
		latticeBits[indexLatticeBitBottom] = true;
		latticeBits[indexLatticeBitLeft] = false;
		latticeBits[indexLatticeBitRight] = false;
		latticeBits[indexLatticeBitTop] = true;
		
		LatticeTileGridCanvas ltgCnvs = new LatticeTileGridCanvas(5, 5, 10, 3.5);
		ltgCnvs.setLatticesOnTile(2, 2, latticeBits, Color.GREEN);
		int affectedTilesCounter =0;
		for(int n=0;n<5;n++)
		{
			for(int m=0;m<5;m++)
			{
				boolean bottom = ltgCnvs.hasLatticeOnTheBottom(n, m);
				boolean top = ltgCnvs.hasLatticeOnTheTop(n, m);
				boolean right = ltgCnvs.hasLatticeOnTheRight(n, m);
				boolean left = ltgCnvs.hasLatticeOnTheLeft(n, m);
				
				if((left||right||top||bottom))
				{
					System.out.println("Tile(" + n + ", " + m + ") has Lattice on Bottom: " + bottom);
					System.out.println("Tile(" + n + ", " + m + ") has Lattice on Top: " + top);
					System.out.println("Tile(" + n + ", " + m + ") has Lattice on Right: " + right);
					System.out.println("Tile(" + n + ", " + m + ") has Lattice on Left: " + left);
					affectedTilesCounter++;
				}
			}
		}
		
		System.out.println("AffectedTiles: " + affectedTilesCounter);
		assert(affectedTilesCounter==3);
	}

}