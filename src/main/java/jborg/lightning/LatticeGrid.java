package jborg.lightning;

import java.awt.Point;

import jborg.lightning.exceptions.LTGCException;

/**
 * It holds the Data where the 'Lattices' are which are
 * Barriers to the Snake.
 */
public class LatticeGrid
{

	/**
	 * Holds the max X-Coordinate for Lattices.
	 */
	private final int width;
	/**
	 * Holds the max Y-Coordinate for Lattices.
	 */
	private final int height;
	/**
	 * The value of latticeCodes of a Tile
	 * describes on which sides are Lattices
	 * on the Tile. Also the first index is 
	 * the x-Coordinate of the Tile. The 
	 * second one ist the y-Coordinate. If
	 * a Tile has LatticeCode z, than this 
	 * means the Tiles surrounding that Tile
	 * can't Contradict that. Meaning if Tile
	 * A has Lattice on Top than the Tile that
	 * is directly above A must have a Lattice
	 * on the Bottom. This fact is handled. 
	 */
	private final int [][] latticeCodes;
	
	private final static int maxLatticeCode=15;

	/**
	 * How many Lattice Bits are there?
	 */
	public static final int nrOfLatticeBits = 4;

	/** index of Bits in an boolean area meant to
	 * determine where Lattices appear. In this 
	 * case Left.
	 */
	public static final int indexLatticeBitLeft = 0;
	/** index of Bits in an boolean area meant to
	 * determine where Lattices appear. In this
	 * case Right.
	 */
	public static final int indexLatticeBitBottom = 1;//Bottom means up !!!!!!!!!!!
	/** index of Bits in an boolean area meant to
	 * determine where Lattices appear. In this
	 * case Bottom.
	 */
	public static final int indexLatticeBitRight = 2;
	/** index of Bits in an boolean area meant to
	 * determine where Lattices appear. In this
	 * case Right.
	 */
	public static final int indexLatticeBitTop = 3; //Top means down!!!!!!!!!!

	/**
	 * @param width is the max. of how far x-Coordinate of a Lattice goes. 
	 * How far right a Lattice can be. The min.=0 always.
	 * @param height is the max of how far y-Coordiante of a Lattice goes.
	 * How far down a Lattice can be. The min.=0 always.
	 */
	public LatticeGrid(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		latticeCodes = new int[width][height];
	}
	
	/**
	 * Can potentially set or un-set multiple Lattices around the Tile
	 * specified by p.
	 * @param p contains the Tile Coordinates the Lattices is bordering on.
	 * @param latticeBits determines where are the Lattices are.
	 * bit 0: left.
	 * bit 1: bottom.
	 * bit 2: right.
	 * bit 3: top. 
	 * @throws LTGCException
	 */
	public void setLatticesOnTile(Point p, boolean [] latticeBits) throws LTGCException
	{
		throwsExceptionIfOutOfBounds(p.x, p.y);
		setLatticesOnTile(p.x, p.y, latticeBits);
	}
	
	/**
	 * Can potentially set or un-set multiple Lattices. If u set a
	 * Lattice-bit true that means a Lattice appears in specific Position
	 * relative to x and y.
	 * @param x contains the Tile x-Coordinate the Lattices is bordering on.
	 * @param y contains the Tile y-Coordinate the Lattices is bordering on.
	 * @param latticeBits determines where are the Lattices are.
	 * bit 0: left.
	 * bit 1: bottom.
	 * bit 2: right.
	 * bit 3: top. 
	 * @throws LTGCException
	 */
	public void setLatticesOnTile(int x, int y, boolean [] latticeBits) throws LTGCException
	{
		throwsExceptionIfOutOfBounds(x, y);
		if(latticeBits.length<nrOfLatticeBits)throw new LTGCException("Not enough Lattice Bits!");

		int latticeCode = translateLatticeBitsToLatticeCode(latticeBits);		
		
		setLatticesOnTile(x, y, latticeCode);

	}
	
	/**
	 * Sets one Lattice relative on a Tile.
	 * @param p Tile Coordinate.
	 * @param bitNr from 0 to 3. Similar to latticeCodes.
	 * 0 means: left of the Tile a lattice will be.
	 * 1 means: bottom of the Tile a lattice will be.
	 * 2 means: right of the Tile a lattice will be.
	 * 3 means: top of the Tile a lattice will be.
	 * Other values will make a LTGCException happen to be thrown.
	 * @throws LTGCException if p is beyond the width and/or height
	 * of the LatticeGrid. Also when p is null.
	 */
	public void setOneLatticeOnTile(Point p, int bitNr) throws LTGCException
	{
		if(p==null)throw new LTGCException("Point is null!");
		throwsExceptionIfOutOfBounds(p.x, p.y);
		setOneLatticeOnTile(p.x, p.y, bitNr);
	}

	public void setOneLatticeOnTile(int x, int y, int bitNr) throws LTGCException
	{
		throwsExceptionIfOutOfBounds(x, y);
		if(bitNr<0||bitNr>=nrOfLatticeBits)throw new LTGCException("Lattice bit not valide.");
		
		boolean[]latticeBits = translateLatticeCodeToLatticeBits(latticeCodes[x][y]);
		latticeBits[bitNr]= true;
		
		setLatticesOnTile(x, y, latticeBits);

	}
	
	/**
	 * Can potentially set more than one Lattice or 'un-set'
	 * One needs to know the corresponding latticeCode.
	 * @param p Tile Coordinates.
	 * @param latticeCode determines which lattices will appear.
	 * @throws LTGCException if p is beyond the width or height
	 * or when p is null.
	 */
	public void setLatticesOnTile(Point p, int latticeCode) throws LTGCException
	{
		throwsExceptionIfOutOfBounds(p.x, p.y);
		if(p==null)throw new LTGCException("Point is null!");
		setLatticesOnTile(p.x, p.y, latticeCode);
	}

	/**
	 * This is the workhorse. The cashCow. The chosen one.
	 * It handles the fact that: one Tiles right lattice is
	 * another Tiles left Lattice. All setLattic(es)blabla
	 * Methods rely on this one Method.
	 * @param x x-Coordinate of the Tile in question.
	 * @param y y-Coordinate of the Tile in question.
	 * @param latticeCode determines which Lattices are set or
	 * un-set.
	 * @throws LTGCException if x and/or y are out of Bounds.(max./min.)(width/height).
	 */
	public void setLatticesOnTile(int x, int y, int latticeCode) throws LTGCException
	{

		throwsExceptionIfOutOfBounds(x, y);
		throwsExceptionIfLatticeCodeAintValide(latticeCode);

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

	/**
	 * If u for some reason have a valid latticeBit Array and
	 * u want to know the corresponding latticeCode.
	 * @param latticeBits Array which contains min. nrOfLatticeBits.
	 * Which is four.
	 * @return latticeCode.
	 * @throws LTGCException if the Array is too small. Or if the hole Array is null.
	 */
	public static int translateLatticeBitsToLatticeCode(boolean[] latticeBits) throws LTGCException
	{
		if(latticeBits==null)throw new LTGCException("Array can't be null!");
		if(latticeBits.length<4)throw new LTGCException("Not enough bits in that Array!");
		int latticeCode = 0;
		
		for(int n=0;n<nrOfLatticeBits;n++)
			if(latticeBits[n])latticeCode += Math.pow(2, n);

		return latticeCode;
	}
	
	/**
	 * If u for some reason have a valid latticeCode and
	 * u want to know the corresponding Lattice-bit Array.
	 * @param latticeCode Array which contains min. nrOfLatticeBits.
	 * Which is four.
	 * @return latticeBits.
	 * @throws LTGCException if the Array is too small. Or if the hole Array is null.
	 */	
	public static boolean[] translateLatticeCodeToLatticeBits(int latticeCode) throws LTGCException
	{
		throwsExceptionIfLatticeCodeAintValide(latticeCode);
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
	
	/**
	 * @param x x-Coordinate of Tile in question.
	 * @param y y-Coordinate of Tile in question.
	 * @return Does the Tile in Question have a Lattice
	 * on the Right?
	 * @throws LTGCException
	 */
	public boolean hasLatticeOnTheRight(int x, int y) throws LTGCException
	{
		throwsExceptionIfOutOfBounds(x, y);
		//TODO: Figure this out!!if(x==width-1)return true;
		
		int latticeCode = latticeCodes[x][y];
		boolean []latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		return latticeBits[indexLatticeBitRight];
	}
	
	/**
	 * @param x x-Coordinate of Tile in question.
	 * @param y y-Coordinate of Tile in question.
	 * @return Does the Tile in Question have a Lattice
	 * on the Left?
	 * @throws LTGCException
	 */
	public boolean hasLatticeOnTheLeft(int x, int y) throws LTGCException
	{
		
		throwsExceptionIfOutOfBounds(x, y);
		//TODO: Figure this out!!if(x==0)return true;

		int latticeCode = latticeCodes[x][y];
		boolean []latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		return latticeBits[indexLatticeBitLeft];
	}
	
	/**
	 * @param x x-Coordinate of Tile in question.
	 * @param y y-Coordinate of Tile in question.
	 * @return Does the Tile in Question have a Lattice
	 * on the Bottom?
	 * @throws LTGCException
	 */
	public boolean hasLatticeOnTheBottom(int x, int y) throws LTGCException
	{
		throwsExceptionIfOutOfBounds(x, y);
		//TODO: Figure this out!!if(y==height-1)return true;

		int latticeCode = latticeCodes[x][y];
		boolean []latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		return latticeBits[indexLatticeBitBottom];
	}
	
	/**
	 * @param x x-Coordinate of Tile in question.
	 * @param y y-Coordinate of Tile in question.
	 * @return Does the Tile in Question have a Lattice
	 * on the Top?
	 * @throws LTGCException
	 */
	public boolean hasLatticeOnTheTop(int x, int y) throws LTGCException
	{
		
		throwsExceptionIfOutOfBounds(x, y);
		//TODO: Figure this out!!if(y==0)return true;
		
		int latticeCode = latticeCodes[x][y];
		boolean []latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		return latticeBits[indexLatticeBitTop];
	}

	public boolean hasLatticeOnTheRight(Point p) throws LTGCException
	{
		return hasLatticeOnTheRight(p.x, p.y);
	}
	
	public boolean hasLatticeOnTheLeft(Point p) throws LTGCException
	{		
		return hasLatticeOnTheLeft(p.x, p.y);
	}
	
	public boolean hasLatticeOnTheBottom(Point p) throws LTGCException
	{		
		return hasLatticeOnTheBottom(p.x, p.y);
	}
	
	public boolean hasLatticeOnTheTop(Point p) throws LTGCException
	{
		return hasLatticeOnTheTop(p.x, p.y);
	}
	
	/**
	 * If Tile on the given position (x, y) has latticeBit(bitNr) set
	 * to true. What would be the latticeCode of that Tile be?
	 * @param bitNr
	 * @param x x-Coordinate of Tile in question.
	 * @param y y-Coordinate of Tile in question.
	 * @return What if latticeCode.
	 * @throws LTGCException
	 */
	private int getLatticeCode(int bitNr, int x, int y) throws LTGCException
	{
		throwsExceptionIfOutOfBounds(x, y);
		int oldLatticeCode = latticeCodes[x][y];
		boolean [] latticeBits = translateLatticeCodeToLatticeBits(oldLatticeCode);
		latticeBits[bitNr]= true;
		int newLatticeCode = translateLatticeBitsToLatticeCode(latticeBits);
		
		return newLatticeCode;
	}

	public int getLatticeCode(int x, int y) throws LTGCException
	{
		throwsExceptionIfOutOfBounds(x, y);
		return latticeCodes[x][y];
	}
	
	private int getLatticeCode(int bitNr, Point p) throws LTGCException
	{
		return getLatticeCode(bitNr, p.x, p.y);
	}

	public int getLatticeCode(Point p) throws LTGCException
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
	
	public void throwsExceptionIfOutOfBounds(int x, int y) throws LTGCException
	{
		if(x>width-1||x<0) throw new LTGCException("x-Coordinate out of Bounds.");
		if(y>height-1||y<0)throw new LTGCException("y-Coordinate out of Bounds.");
	}
	
	public static void throwsExceptionIfLatticeCodeAintValide(int latticeCode) throws LTGCException
	{
		if(latticeCode<0||latticeCode>maxLatticeCode)throw new LTGCException("Lattice Code ain't valide");
	}
}
