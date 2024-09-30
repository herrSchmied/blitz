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
	
	private final static int minLatticeCode=0;
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
	 * @throws LTGCException if width and/or height is smaller than 1.
	 */
	public LatticeGrid(int width, int height) throws LTGCException
	{
		if(width<=0)throw new LTGCException("Width must at least be 1.");
		if(height<=0)throw new LTGCException("Height must be at least be 1.");
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
	 * @throws LTGCException if p is out of Bounds. If p is null or 
	 * there is something wrong with the latticeBits.
	 */
	public void setLatticesOnTile(Point p, boolean [] latticeBits) throws LTGCException
	{
		throwsExceptionIfOutOfBounds(p.x, p.y);
		throwsExceptionIfArrayIsNotValid(latticeBits);
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
	 * @throws LTGCException if x and/or y is out of Bounds. Or
	 * if there is something wrong with the latticeBits.
	 */
	public void setLatticesOnTile(int x, int y, boolean [] latticeBits) throws LTGCException
	{
		
		throwsExceptionIfArrayIsNotValid(latticeBits);
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
	 * of the LatticeGrid. Also when p is null. And when bitNr is
	 * out of Range.
	 */
	public void setOneLatticeOnTile(Point p, int bitNr) throws LTGCException
	{
		throwsExceptionIfPIsNull(p);
		throwsExceptionIfBitNrAintValide(bitNr);
		throwsExceptionIfOutOfBounds(p.x, p.y);
		setOneLatticeOnTile(p.x, p.y, bitNr);
	}

	/**
	 * Sets one Lattice relative on a Tile.
	 * @param x x-Coordinate of Tile in question.
	 * @param y y-Coordiante of Tile in question.
	 * @param bitNr from 0 to 3. Similar to latticeCodes.
	 * 0 means: left of the Tile a lattice will be.
	 * 1 means: bottom of the Tile a lattice will be.
	 * 2 means: right of the Tile a lattice will be.
	 * 3 means: top of the Tile a lattice will be.
	 * Other values will make a LTGCException happen to be thrown.
	 * @throws LTGCException if x and/or y is beyond the width and/or height
	 * of the LatticeGrid. And when bitNr is out of Range.
	 */
	public void setOneLatticeOnTile(int x, int y, int bitNr) throws LTGCException
	{
		
		throwsExceptionIfBitNrAintValide(bitNr);
		throwsExceptionIfOutOfBounds(x, y);
		
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
	 * or when p is null. Also if latticeCode ain't valid.
	 */
	public void setLatticesOnTile(Point p, int latticeCode) throws LTGCException
	{
		
		throwsExceptionIfPIsNull(p);
		throwsExceptionIfLatticeCodeAintValide(latticeCode);
		throwsExceptionIfOutOfBounds(p.x, p.y);
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
	public int translateLatticeBitsToLatticeCode(boolean[] latticeBits) throws LTGCException
	{
		throwsExceptionIfArrayIsNotValid(latticeBits);
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
	public boolean[] translateLatticeCodeToLatticeBits(int latticeCode) throws LTGCException
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
	 * @throws LTGCException if x or/and y is out of Bounds.
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
	 * @throws LTGCException if x or/and y is out of Bounds.
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
	 * @throws LTGCException if x or/and y is out of Bounds.
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
	 * @throws LTGCExceptionLTGCException if x or/and y is out of Bounds.
	 */
	public boolean hasLatticeOnTheTop(int x, int y) throws LTGCException
	{
		
		throwsExceptionIfOutOfBounds(x, y);
		//TODO: Figure this out!!if(y==0)return true;
		
		int latticeCode = latticeCodes[x][y];
		boolean []latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		return latticeBits[indexLatticeBitTop];
	}

	/**
	 * @param x x-Coordinate of Tile in question.
	 * @param y y-Coordinate of Tile in question.
	 * @return Does the Tile in Question have a Lattice
	 * anywhere?
	 * @throws LTGCExceptionLTGCException if x or/and y is out of Bounds.
	 */
	public boolean hasLatticeSomeWhere(int x, int y)throws LTGCException
	{

		boolean l = hasLatticeOnTheLeft(x,y);
		boolean r = hasLatticeOnTheRight(x,y);
		boolean b = hasLatticeOnTheBottom(x,y);
		boolean t = hasLatticeOnTheTop(x,y);
		
		return(l||r||b||t);		
	}

	/**
	 * @param p contains the Coordinates of Tile in question.
	 * @return Does the Tile in Question have a Lattice
	 * on the Right?
	 * @throws LTGCException if p is out of Bounds.
	 */
	public boolean hasLatticeOnTheRight(Point p) throws LTGCException
	{
		return hasLatticeOnTheRight(p.x, p.y);
	}
	
	/**
	 * @param p contains the Coordinates of Tile in question.
	 * @return Does the Tile in Question have a Lattice
	 * on the Left?
	 * @throws LTGCException if p is out of Bounds.
	 */
	public boolean hasLatticeOnTheLeft(Point p) throws LTGCException
	{		
		return hasLatticeOnTheLeft(p.x, p.y);
	}
	
	/**
	 * @param p contains the Coordinates of Tile in question.
	 * @return Does the Tile in Question have a Lattice
	 * on the Bottom?
	 * @throws LTGCException if p is out of Bounds.
	 */
	public boolean hasLatticeOnTheBottom(Point p) throws LTGCException
	{		
		return hasLatticeOnTheBottom(p.x, p.y);
	}
	
	/**
	 * @param p contains the Coordinates of Tile in question.
	 * @return Does the Tile in Question have a Lattice
	 * on the Top?
	 * @throws LTGCException if p is out of Bounds.
	 */
	public boolean hasLatticeOnTheTop(Point p) throws LTGCException
	{
		return hasLatticeOnTheTop(p.x, p.y);
	}
	
	/**
	 * @param p contains the Coordinates of Tile in question.
	 * @return Does the Tile in Question have a Lattice
	 * anywhere?
	 * @throws LTGCException if p is out of Bounds.
	 */
	public boolean hasLatticeSomeWhere(Point p)throws LTGCException
	{
		return hasLatticeSomeWhere(p.x, p.y);
	}
	
	/**
	 * If Tile on the given position (x, y) has latticeBit(bitNr)
	 * changed to true. What would be the latticeCode of that Tile
	 * be?
	 * @param bitNr
	 * @param x x-Coordinate of Tile in question.
	 * @param y y-Coordinate of Tile in question.
	 * @return What if latticeCode.
	 * @throws LTGCException
	 */
	private int getLatticeCode(int bitNr, int x, int y) throws LTGCException
	{
		
		throwsExceptionIfBitNrAintValide(bitNr);
		throwsExceptionIfOutOfBounds(x, y);
		int oldLatticeCode = latticeCodes[x][y];
		boolean [] latticeBits = translateLatticeCodeToLatticeBits(oldLatticeCode);
		latticeBits[bitNr]= true;
		int newLatticeCode = translateLatticeBitsToLatticeCode(latticeBits);
		
		return newLatticeCode;
	}

	/**
	 * What is the latticeCode at The Tile on the given position(x, y)?
	 * @param x x-Coordinate of Tile in question.
	 * @param y y-Coordinate of Tile in question.
	 * @return latticeCode.
	 * @throws LTGCException if x and/or y is out of Bounds.
	 */
	public int getLatticeCode(int x, int y) throws LTGCException
	{
		throwsExceptionIfOutOfBounds(x, y);
		return latticeCodes[x][y];
	}
	
	/**
	 * If Tile on the given position p has latticeBit(bitNr)
	 * changed to true. What would be the latticeCode of that Tile
	 * be?
	 * @param bitNr
	 * @param p contains the Coordinates of Tile in question.
	 * @return What if latticeCode.
	 * @throws LTGCException if p is out of Bounds.
	 */
	private int getLatticeCode(int bitNr, Point p) throws LTGCException
	{
		return getLatticeCode(bitNr, p.x, p.y);
	}

	/**
	 * What is the latticeCode at The Tile on the given position p?
	 * @param p contains the Coordinates of Tile in question.
	 * @return latticeCode.
	 * @throws LTGCException if x and/or y is out of Bounds.
	 */
	public int getLatticeCode(Point p) throws LTGCException
	{
		return getLatticeCode(p.x, p.y);
	}

	/**
	 * 
	 * @return max. x Coordinate for lattices.
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * 
	 * @return max. y Coordinate for lattices.
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Throws LTGCException if x and/or y are out of Bounds.
	 * And thats its only purpose.
	 * @param x x-Coordinate
	 * @param y y-Coordinate.
	 * @throws LTGCException if x and/or y is out of Bounds.
	 */
	public void throwsExceptionIfOutOfBounds(int x, int y) throws LTGCException
	{
		if(x>width-1||x<0) throw new LTGCException("x-Coordinate out of Bounds.");
		if(y>height-1||y<0)throw new LTGCException("y-Coordinate out of Bounds.");
	}
	
	/**
	 * Throws LTGCException if given latticeCode is out of Range.
	 * And thats its only purpose.
	 * @param latticeCode input that's been tested.
	 * @throws LTGCException if input is smaller than 
	 * minLatticeCode(0) or if its larger than maxLatticeCode(15).
	 */
	public void throwsExceptionIfLatticeCodeAintValide(int latticeCode) throws LTGCException
	{
		if(latticeCode<0||latticeCode>maxLatticeCode)throw new LTGCException("Lattice Code ain't valide");
	}

	/**
	 * Throws LTGCException if the given input(bitNr) is out of Range.
	 * And thats its only purpose.
	 * @param bitNr input to be Tested.
	 * @throws LTGCException if the input is smaller than 0 or if the
	 * input is larger than: (Nr. of Lattice Bit's there are)-1.
	 */
	public void throwsExceptionIfBitNrAintValide(int bitNr) throws LTGCException
	{
		if(bitNr<0||bitNr>(nrOfLatticeBits-1))throw new LTGCException("Bit Nr. ain't valide");
	}
	
	public void throwsExceptionIfPIsNull(Point p) throws LTGCException
	{
		if(p==null)throw new LTGCException("Point is null!");

	}
	
	public void throwsExceptionIfArrayIsNotValid(boolean [] latticeBits) throws LTGCException
	{

		if(latticeBits==null)throw new LTGCException("Array can't be null!");
		if(latticeBits.length<4)throw new LTGCException("Not enough bits in that Array!");
		
		for(Boolean b: latticeBits)
		{
			if(b==null)throw new LTGCException("Lattice Bit Array contains at least one null!");
		}
	}

}