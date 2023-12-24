package jborg.lightning;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LatticeTileGridCanvas extends Canvas
{

	private final int widthInTiles;
	private final int heightInTiles;
	
	private final int absolutWidthInPixels;
	private final int absolutHeightInPixels;
	
	private final int tileSize;
	private final double strokeWidthLattice;
	
	private Color[][] colorOfTile;
	private int[][] latticeCodeOfTile;
	
	public static final int nrOfLatticeBits = 4;

	public static final int indexLatticeBitLeft = 0;
	public static final int indexLatticeBitBottom = 1;
	public static final int indexLatticeBitRight = 2;
	public static final int indexLatticeBitTop = 3;

	private GraphicsContext gc2D;
	
	//TODO: Testing next!!!!!!!!!!!!!!!!!!!!!!
	
	public LatticeTileGridCanvas(int xTileWidth, int yTileHeight, int tileSize, double strokeWidthLattice)
	{
		super(xTileWidth*tileSize, yTileHeight*tileSize);
		
		this.widthInTiles = xTileWidth;
		this.heightInTiles = yTileHeight;
		
		this.absolutWidthInPixels = xTileWidth*tileSize;
		this.absolutHeightInPixels = yTileHeight*tileSize;
		
		this.tileSize = tileSize;
		
		gc2D = this.getGraphicsContext2D();
		
		this.strokeWidthLattice = strokeWidthLattice;
		
		colorOfTile = new Color[xTileWidth][yTileHeight];
		latticeCodeOfTile = new int[xTileWidth][yTileHeight];//Default entry is 0!
	}
	
	public void setColorOnTile(Color c, int xPos, int yPos) throws LTGCException
	{
		
		if(xPos>widthInTiles-1||xPos<0)throw new LTGCException("X-Position out of Bounds.");
		if(yPos>heightInTiles-1||yPos<0)throw new LTGCException("Y-Position out of Bounds.");

		gc2D.setFill(c);
		gc2D.fillRect(xPos*tileSize, yPos*tileSize, tileSize, tileSize);
	
		colorOfTile[xPos][yPos] = c;
	}
	
	public void setLatticesOnTile(int xPosOfTile, int yPosOfTile, boolean [] latticeBits, Color latticeColor) throws LTGCException
	{

		if(xPosOfTile>widthInTiles-1||xPosOfTile<0)throw new LTGCException("X-Position out of Bounds.");
		if(yPosOfTile>heightInTiles-1||yPosOfTile<0)throw new LTGCException("Y-Position out of Bounds.");
		if(latticeBits.length<nrOfLatticeBits)throw new LTGCException("Not enough Lattice Bits!");

		int latticeCode = translateLatticeBitsToLatticeCode(latticeBits);		
		
		setLatticesOnTile(xPosOfTile, yPosOfTile, latticeCode, latticeColor);

	}
	
	public void setLatticesOnTile(int xPosOfTile, int yPosOfTile, int latticeCode, Color latticeColor) throws LTGCException
	{

		if(xPosOfTile>widthInTiles-1||xPosOfTile<0)throw new LTGCException("X-Position out of Bounds.");
		if(yPosOfTile>heightInTiles-1||yPosOfTile<0)throw new LTGCException("Y-Position out of Bounds.");
		if(latticeCode<0||latticeCode>15)throw new LTGCException("Lattice Code not valide.");
		
		boolean[]latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		for(int n=0;n<nrOfLatticeBits;n++)
			if(latticeBits[n])setBitToLatticeCode(n, xPosOfTile, yPosOfTile);

		boolean thereIsATileOnTheRight = xPosOfTile < heightInTiles - 1;
		boolean thereIsATileOnTheLeft = xPosOfTile > 0;
		boolean thereIsATileOnTheTop = yPosOfTile > 0;
		boolean thereIsATileOnTheBottom = yPosOfTile < widthInTiles - 1;

		if(latticeBits[indexLatticeBitRight]&&thereIsATileOnTheRight)
		{
			setBitToLatticeCode(indexLatticeBitLeft, xPosOfTile+1, yPosOfTile);
		}

		if(latticeBits[indexLatticeBitLeft]&&thereIsATileOnTheLeft)
		{
			setBitToLatticeCode(indexLatticeBitRight, xPosOfTile-1, yPosOfTile);
		}

		if(latticeBits[indexLatticeBitTop]&&thereIsATileOnTheTop)
		{
			setBitToLatticeCode(indexLatticeBitBottom, xPosOfTile, yPosOfTile-1);
		}

		if(latticeBits[indexLatticeBitBottom]&&thereIsATileOnTheBottom)
		{
			setBitToLatticeCode(indexLatticeBitTop, xPosOfTile, yPosOfTile+1);
		}

		for(int n=0;n<nrOfLatticeBits;n++)if(latticeBits[n])
			drawLattice(xPosOfTile, yPosOfTile, n, latticeColor);
	}
	
	private void setBitToLatticeCode(int bitNr, int xPos, int yPos)
	{
		int oldLatticeCode = latticeCodeOfTile[xPos][yPos];
		boolean [] latticeBits = translateLatticeCodeToLatticeBits(oldLatticeCode);
		latticeBits[bitNr]= true;
		int newLatticeCode = translateLatticeBitsToLatticeCode(latticeBits);
		latticeCodeOfTile[xPos][yPos] = newLatticeCode;
	}

	private void drawLattice(int xPosTile, int yPosTile, int latticeNr, Color latticeColor)
	{
		
		Double xStart = 0d;
		Double xEnd = 0d;
		Double yStart = 0d;
		Double yEnd = 0d;
		
		if(latticeNr==indexLatticeBitLeft)//Left of Tile
		{
			xStart= (double)(xPosTile*tileSize);
			xEnd = xStart;
			yStart = (double)(yPosTile*tileSize);
			yEnd = yStart + tileSize;
		}
		
		if(latticeNr==indexLatticeBitBottom)//downOfTile
		{
			xStart= (double)(xPosTile*tileSize);
			xEnd = xStart + tileSize;
			yStart = (double)(yPosTile+1)*tileSize;
			yEnd = yStart;
		}
		
		if(latticeNr==indexLatticeBitRight)//rightOfTile
		{
			xStart= (double)(xPosTile+1)*tileSize;
			xEnd = xStart;
			yStart = (double)(yPosTile*tileSize);
			yEnd = yStart + tileSize;
		}
		
		if(latticeNr==indexLatticeBitTop)//TopOfTile
		{
			xStart= (double)(xPosTile*tileSize);
			xEnd = xStart + tileSize;
			yStart = (double)(yPosTile*tileSize);
			yEnd = yStart;
		}
		
		gc2D.setStroke(latticeColor);
		gc2D.setLineWidth(strokeWidthLattice);
		gc2D.strokeLine(xStart, yStart, xEnd, yEnd);
	}

	public int translateLatticeBitsToLatticeCode(boolean[] latticeBits)
	{
		
		int latticeCode = 0;
		
		for(int n=0;n<nrOfLatticeBits;n++)
			if(latticeBits[n])latticeCode += Math.pow(2, n);

		return latticeCode;
	}
	
	public boolean[] translateLatticeCodeToLatticeBits(int latticeCode)
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
	
	public boolean hasLatticeOnTheRight(int xPos, int yPos)
	{
		
		int latticeCode = latticeCodeOfTile[xPos][yPos];
		boolean []latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		return latticeBits[indexLatticeBitRight];
	}
	
	public boolean hasLatticeOnTheLeft(int xPos, int yPos)
	{
		
		int latticeCode = latticeCodeOfTile[xPos][yPos];
		boolean []latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		return latticeBits[indexLatticeBitLeft];
	}
	
	public boolean hasLatticeOnTheBottom(int xPos, int yPos)
	{
		
		int latticeCode = latticeCodeOfTile[xPos][yPos];
		boolean []latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		return latticeBits[indexLatticeBitBottom];
	}
	
	public boolean hasLatticeOnTheTop(int xPos, int yPos)
	{
		
		int latticeCode = latticeCodeOfTile[xPos][yPos];
		boolean []latticeBits = translateLatticeCodeToLatticeBits(latticeCode);
		
		return latticeBits[indexLatticeBitTop];
	}
	
	public Color getColorOfTile(int xPos, int yPos)
	{
		return colorOfTile[xPos][yPos];
	}
	
	public int getWidthInTiles()
	{
		return widthInTiles;
	}

	public int getHeightInTiles()
	{
		return heightInTiles;
	}

	public int getAbsolutWidthInPixels()
	{
		return absolutWidthInPixels;
	}

	public int getAbsolutHeightInPixels()
	{
		return absolutHeightInPixels;
	}

	public int getTileSize()
	{
		return tileSize;
	}
}