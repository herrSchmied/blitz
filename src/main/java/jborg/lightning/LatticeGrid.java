package jborg.lightning;

import java.awt.Point;

public class LatticeGrid
{

	private final int width;
	private final int height;
	private final int [][] latticeCodes;
	
	public static final int nrOfLatticeBits = 4;

	public static final int indexLatticeBitLeft = 0;
	public static final int indexLatticeBitBottom = 1;
	public static final int indexLatticeBitRight = 2;
	public static final int indexLatticeBitTop = 3;

	public LatticeGrid(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		latticeCodes = new int[width][height];
	}
	
	public void setLatticesOnTile(Point p, boolean [] latticeBits) throws LTGCException
	{
		setLatticesOnTile(p.x, p.y, latticeBits);
	}
	
	public void setLatticesOnTile(int x, int y, boolean [] latticeBits) throws LTGCException
	{

		if(x>width-1||x<0)throw new LTGCException("X-Position out of Bounds.");
		if(y>height-1||y<0)throw new LTGCException("Y-Position out of Bounds.");
		if(latticeBits.length<nrOfLatticeBits)throw new LTGCException("Not enough Lattice Bits!");

		int latticeCode = translateLatticeBitsToLatticeCode(latticeBits);		
		
		setLatticesOnTile(x, y, latticeCode);

	}
	
	public void setOneLatticeOnTile(Point p, int bitNr) throws LTGCException
	{
		setOneLatticeOnTile(p.x, p.y, bitNr);
	}

	public void setOneLatticeOnTile(int x, int y, int bitNr) throws LTGCException
	{
		if(x>width-1||x<0)throw new LTGCException("X-Position out of Bounds.");
		if(y>height-1||y<0)throw new LTGCException("Y-Position out of Bounds.");
		if(bitNr<0||bitNr>=nrOfLatticeBits)throw new LTGCException("Lattice bit not valide.");
		
		boolean[]latticeBits = translateLatticeCodeToLatticeBits(latticeCodes[x][y]);
		latticeBits[bitNr]= true;
		
		setLatticesOnTile(x, y, latticeBits);

	}
	
	public void setLatticesOnTile(Point p, int latticeCode) throws LTGCException
	{
		setLatticesOnTile(p.x, p.y, latticeCode);
	}
	
	public void setLatticesOnTile(int x, int y, int latticeCode) throws LTGCException
	{

		if(x>width-1||x<0)throw new LTGCException("X-Position out of Bounds.");
		if(y>height-1||y<0)throw new LTGCException("Y-Position out of Bounds.");
		if(latticeCode<0||latticeCode>15)throw new LTGCException("Lattice Code not valide.");
		
		boolean[]latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		for(int n=0;n<nrOfLatticeBits;n++)
			if(latticeBits[n])latticeCodes[x][y]= getLatticeCode(n, x, y);

		boolean thereIsATileOnTheRight = x < width - 1;
		boolean thereIsATileOnTheLeft = x > 0;
		boolean thereIsATileOnTheTop = y < height-1;
		boolean thereIsATileOnTheBottom = y > 0;

		if(latticeBits[indexLatticeBitRight]&&thereIsATileOnTheRight)
		{
			latticeCodes[x+1][y] = getLatticeCode(indexLatticeBitLeft, x+1, y);
		}

		if(latticeBits[indexLatticeBitLeft]&&thereIsATileOnTheLeft)
		{
			latticeCodes[x-1][y] = getLatticeCode(indexLatticeBitRight, x-1, y);
		}

		if(latticeBits[indexLatticeBitTop]&&thereIsATileOnTheTop)
		{
			latticeCodes[x][y+1] = getLatticeCode(indexLatticeBitBottom, x, y+1);
		}

		if(latticeBits[indexLatticeBitBottom]&&thereIsATileOnTheBottom)
		{
			latticeCodes[x][y-1] = getLatticeCode(indexLatticeBitTop, x, y-1);
		}
	}

	public static int translateLatticeBitsToLatticeCode(boolean[] latticeBits)
	{
		
		int latticeCode = 0;
		
		for(int n=0;n<nrOfLatticeBits;n++)
			if(latticeBits[n])latticeCode += Math.pow(2, n);

		return latticeCode;
	}
	
	public static boolean[] translateLatticeCodeToLatticeBits(int latticeCode)
	{
		
		boolean[]latticeBits = new boolean[nrOfLatticeBits];
		
		int tempLatticeCode = latticeCode;

		if(tempLatticeCode%2==0)latticeBits[0]=false;
		else latticeBits[0]= true;
		
		tempLatticeCode = tempLatticeCode/2;
		if(tempLatticeCode%2==0)latticeBits[1]=false;
		else latticeBits[1]= true;

		tempLatticeCode = tempLatticeCode/2;
		if(tempLatticeCode%2==0)latticeBits[2]=false;
		else latticeBits[2]= true;

		tempLatticeCode = tempLatticeCode/2;
		if(tempLatticeCode%2==0)latticeBits[3]=false;
		else latticeBits[3]= true;

		return latticeBits;
	}
	
	public boolean hasLatticeOnTheRight(int x, int y)
	{
		
		int latticeCode = latticeCodes[x][y];
		boolean []latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		return latticeBits[indexLatticeBitRight];
	}
	
	public boolean hasLatticeOnTheLeft(int x, int y)
	{
		
		int latticeCode = latticeCodes[x][y];
		boolean []latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		return latticeBits[indexLatticeBitLeft];
	}
	
	public boolean hasLatticeOnTheBottom(int x, int y)
	{
		
		int latticeCode = latticeCodes[x][y];
		boolean []latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		return latticeBits[indexLatticeBitBottom];
	}
	
	public boolean hasLatticeOnTheTop(int x, int y)
	{
		
		int latticeCode = latticeCodes[x][y];
		boolean []latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		return latticeBits[indexLatticeBitTop];
	}

	public boolean hasLatticeOnTheRight(Point p)
	{
		return hasLatticeOnTheRight(p.x, p.y);
	}
	
	public boolean hasLatticeOnTheLeft(Point p)
	{		
		return hasLatticeOnTheLeft(p.x, p.y);
	}
	
	public boolean hasLatticeOnTheBottom(Point p)
	{		
		return hasLatticeOnTheBottom(p.x, p.y);
	}
	
	public boolean hasLatticeOnTheTop(Point p)
	{
		return hasLatticeOnTheTop(p.x, p.y);
	}
	
	private int getLatticeCode(int bitNr, int x, int y)
	{
		int oldLatticeCode = latticeCodes[x][y];
		boolean [] latticeBits = translateLatticeCodeToLatticeBits(oldLatticeCode);
		latticeBits[bitNr]= true;
		int newLatticeCode = translateLatticeBitsToLatticeCode(latticeBits);
		
		return newLatticeCode;
	}

	public int getLatticeCode(int x, int y)
	{
		return latticeCodes[x][y];
	}
	
	private int getLatticeCode(int bitNr, Point p)
	{
		return getLatticeCode(bitNr, p.x, p.y);
	}

	public int getLatticeCode(Point p)
	{
		return getLatticeCode(p.x, p.y);
	}

	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
}
